package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class ControladorCatalogoDeMueblesTest {

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
  public void dadoQueNoAplicoFiltros_cuandoNavegoAlCatalogo_entoncesVeoTodosLosMueblesDelCatalogo()
    throws Exception {
    Mueble retro = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    Mueble japandi = this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);

    MvcResult result =
      this.mockMvc.perform(get("/catalogo")).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assertThat(modelAndView != null ? modelAndView.getViewName() : null, is("catalogo"));
    assertThat(this.obtenerMueblesDelModel(modelAndView), containsInAnyOrder(retro, japandi));
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlCatalogo_cuandoLoHago_entoncesReciboLosEstilosParaPoderFiltrar()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/catalogo")).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assertThat(
      modelAndView != null ? modelAndView.getModel().get("estilos") : null,
      is(Estilo.values())
    );
  }

  @Test
  @Transactional
  public void dadoQueFiltroPorEstilo_cuandoNavegoAlCatalogo_entoncesVeoSoloLosMueblesDeEseEstilo()
    throws Exception {
    Mueble retro = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);

    MvcResult result =
      this.mockMvc.perform(get("/catalogo").param("estilo", "Retro"))
        .andExpect(status().isOk())
        .andReturn();

    assertThat(this.obtenerMueblesDelModel(result.getModelAndView()), contains(retro));
  }

  @Test
  @Transactional
  public void dadoQueFiltroPorPrecioMaximo_cuandoNavegoAlCatalogo_entoncesVeoSoloLosMueblesQueNoLoSuperan()
    throws Exception {
    Mueble barato =
      this.dadoQueElCatalogoTieneUnMueble("Lámpara de Pie Retro", 25000.0, Estilo.Retro);
    this.dadoQueElCatalogoTieneUnMueble("Cama Boho", 180000.0, Estilo.Boho);

    MvcResult result =
      this.mockMvc.perform(get("/catalogo").param("precioMax", "50000"))
        .andExpect(status().isOk())
        .andReturn();

    assertThat(this.obtenerMueblesDelModel(result.getModelAndView()), contains(barato));
  }

  @Test
  @Transactional
  public void dadoQueFiltroPorEstiloYPrecioMaximo_cuandoNavegoAlCatalogo_entoncesVeoSoloLosQueCumplenAmbos()
    throws Exception {
    Mueble retroBarato =
      this.dadoQueElCatalogoTieneUnMueble("Lámpara de Pie Retro", 25000.0, Estilo.Retro);
    this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);

    MvcResult result =
      this.mockMvc.perform(get("/catalogo").param("estilo", "Retro").param("precioMax", "50000"))
        .andExpect(status().isOk())
        .andReturn();

    assertThat(this.obtenerMueblesDelModel(result.getModelAndView()), contains(retroBarato));
  }

  @Test
  @Transactional
  public void dadoQueElijoTodosLosEstilosYUnPrecioMaximo_cuandoFiltroPorElCatalogo_entoncesVeoLosMueblesDeTodosLosEstilosQueNoSuperanElPrecio()
    throws Exception {
    Mueble barato =
      this.dadoQueElCatalogoTieneUnMueble("Lámpara de Pie Retro", 25000.0, Estilo.Retro);
    this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    this.dadoQueElCatalogoTieneUnMueble("Sillón Japandi", 60000.0, Estilo.Japandi);

    MvcResult result =
      this.mockMvc.perform(get("/catalogo").param("estilo", "").param("precioMax", "50000"))
        .andExpect(status().isOk())
        .andReturn();

    assertThat(this.obtenerMueblesDelModel(result.getModelAndView()), contains(barato));
  }

  @Test
  @Transactional
  public void dadoQueAplicoFiltros_cuandoNavegoAlCatalogo_entoncesElModeloDevuelveLosFiltrosAplicados()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/catalogo").param("estilo", "Boho").param("precioMax", "50000"))
        .andExpect(status().isOk())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assertThat(
      modelAndView != null ? modelAndView.getModel().get("estiloSeleccionado") : null,
      is(Estilo.Boho)
    );
    assertThat(
      modelAndView != null ? modelAndView.getModel().get("precioMaximo") : null,
      is(50000.0)
    );
  }

  @Test
  @Transactional
  public void dadoQueNoAplicoFiltros_cuandoNavegoAlCatalogo_entoncesElModeloNoTraeFiltroDeEstiloNiDePrecio()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/catalogo")).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assertThat(
      modelAndView != null ? modelAndView.getModel().get("estiloSeleccionado") : null,
      is(nullValue())
    );
    assertThat(
      modelAndView != null ? modelAndView.getModel().get("precioMaximo") : null,
      is(nullValue())
    );
  }

  @Test
  @Transactional
  public void dadoQueElCatalogoEstaVacio_cuandoNavegoAlCatalogo_entoncesVeoLaVistaSinMuebles()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/catalogo")).andExpect(status().isOk()).andReturn();

    assertThat(this.obtenerMueblesDelModel(result.getModelAndView()), hasSize(0));
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlCatalogo_conUnEstiloQueNoExiste_entoncesVuelvoAlCatalogoSinFiltros()
    throws Exception {
    this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);

    MvcResult result =
      this.mockMvc.perform(get("/catalogo").param("estilo", "NoExiste"))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(result.getResponse().getRedirectedUrl(), is("/catalogo"));
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlCatalogo_conUnPrecioMaximoNegativo_entoncesVuelvoAlCatalogoSinFiltros()
    throws Exception {
    this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);

    MvcResult result =
      this.mockMvc.perform(get("/catalogo").param("precioMax", "-1"))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(result.getResponse().getRedirectedUrl(), is("/catalogo"));
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlCatalogo_conUnPrecioMaximoQueNoEsNumero_entoncesVuelvoAlCatalogoSinFiltros()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/catalogo").param("precioMax", "caro"))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(result.getResponse().getRedirectedUrl(), is("/catalogo"));
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlDetalleDeUnMueble_cuandoLoHago_entoncesVeoLosDatosDeEseMueble()
    throws Exception {
    Mueble retro = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    retro.setDescripcion("Sillón de voluteadas tapizado en terciopelo.");

    MvcResult result =
      this.mockMvc.perform(get("/mueble/{id}", retro.getId()))
        .andExpect(status().isOk())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assertThat(modelAndView != null ? modelAndView.getViewName() : null, is("mueble"));
    assertThat(modelAndView != null ? modelAndView.getModel().get("mueble") : null, is(retro));
  }

  @Test
  @Transactional
  public void dadoQueNavegoAlDetalleDeUnMuebleQueNoExiste_cuandoLoHago_entoncesVuelvoAlCatalogo()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/mueble/{id}", 999))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    assertThat(result.getResponse().getRedirectedUrl(), is("/catalogo"));
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
