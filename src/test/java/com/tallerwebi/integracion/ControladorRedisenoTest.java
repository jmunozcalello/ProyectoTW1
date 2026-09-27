package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.RepositorioCatalogo;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorRedisenoTest {

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private RepositorioCatalogo repositorioCatalogo;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlFormularioDeRediseno_cuandoLoHago_entoncesReciboLosEstilosParaElegir()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/muebles/redisenar")).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assertThat(modelAndView != null ? modelAndView.getViewName() : null, is("redisenar"));
    assertThat(
      modelAndView != null ? modelAndView.getModel().get("estilos") : null,
      is(Estilo.values())
    );
  }

  @Test
  @Transactional
  public void dadoQueCompletoElFormularioDeRediseno_cuandoLoEnvio_entoncesMeRedirigeAlResultadoConLoElegido()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(
          post("/muebles/redisenar").param("estilo", "Japandi").param("presupuesto", "60000")
        )
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(
      result.getResponse().getRedirectedUrl(),
      is("/muebles/redisenar/resultado?estilo=Japandi&presupuesto=60000.0")
    );
  }

  @Test
  @Transactional
  public void dadoUnMuebleQueCumpleConLoElegido_cuandoVeoElResultado_entoncesVeoEseMueble()
    throws Exception {
    Mueble affordable =
      this.dadoQueElCatalogoTieneUnMueble("Sillón Japandi", 60000.0, Estilo.Japandi);
    this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);
    this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);

    MvcResult result =
      this.mockMvc.perform(
          get("/muebles/redisenar/resultado")
            .param("estilo", "Japandi")
            .param("presupuesto", "60000")
        )
        .andExpect(status().isOk())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assertThat(modelAndView != null ? modelAndView.getViewName() : null, is("resultadosRediseno"));
    assertThat(this.obtenerMueblesDelModel(modelAndView), contains(affordable));
  }

  @Test
  @Transactional
  public void dadoQueNingunMuebleCumpleConLoElegido_cuandoVeoElResultado_entoncesVeoLaVistaSinMuebles()
    throws Exception {
    this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);

    MvcResult result =
      this.mockMvc.perform(
          get("/muebles/redisenar/resultado")
            .param("estilo", "Japandi")
            .param("presupuesto", "60000")
        )
        .andExpect(status().isOk())
        .andReturn();

    assertThat(this.obtenerMueblesDelModel(result.getModelAndView()), hasSize(0));
  }

  @Test
  @Transactional
  public void dadoQueVeoElResultado_cuandoLoHago_entoncesElModeloTraeElEstiloYElPresupuestoElegidos()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(
          get("/muebles/redisenar/resultado").param("estilo", "Boho").param("presupuesto", "180000")
        )
        .andExpect(status().isOk())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assertThat(
      modelAndView != null ? modelAndView.getModel().get("estilo") : null,
      is(Estilo.Boho)
    );
    assertThat(
      modelAndView != null ? modelAndView.getModel().get("presupuesto") : null,
      is(180000.0)
    );
  }

  @Test
  @Transactional
  public void dadoQueEnvioElFormularioDeRediseno_conUnEstiloQueNoExiste_entoncesVuelvoAlFormulario()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(
          post("/muebles/redisenar").param("estilo", "NoExiste").param("presupuesto", "60000")
        )
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(result.getResponse().getRedirectedUrl(), is("/muebles/redisenar"));
  }

  @Test
  @Transactional
  public void dadoQueEnvioElFormularioDeRediseno_sinPresupuesto_entoncesVuelvoAlFormulario()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(post("/muebles/redisenar").param("estilo", "Japandi"))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(result.getResponse().getRedirectedUrl(), is("/muebles/redisenar"));
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlResultado_conUnPresupuestoNegativo_entoncesVuelvoAlFormulario()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(
          get("/muebles/redisenar/resultado").param("estilo", "Japandi").param("presupuesto", "-1")
        )
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(result.getResponse().getRedirectedUrl(), is("/muebles/redisenar"));
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlResultado_sinParametros_entoncesVuelvoAlFormulario() throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/muebles/redisenar/resultado"))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(result.getResponse().getRedirectedUrl(), is("/muebles/redisenar"));
  }

  private Mueble dadoQueElCatalogoTieneUnMueble(String nombre, double precio, Estilo estilo) {
    Mueble mueble = new Mueble();
    mueble.setNombre(nombre);
    mueble.setPrecio(precio);
    mueble.SetEstilo(estilo);
    this.repositorioCatalogo.guardarMueble(mueble);
    return mueble;
  }

  @SuppressWarnings("unchecked")
  private List<Mueble> obtenerMueblesDelModel(ModelAndView modelAndView) {
    assertThat(
      "El controlador deberia devolver la vista con los muebles",
      modelAndView,
      is(notNullValue())
    );
    return (List<Mueble>) modelAndView.getModel().get("muebles");
  }
}
