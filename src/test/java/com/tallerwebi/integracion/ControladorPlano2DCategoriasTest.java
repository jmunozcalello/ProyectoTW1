package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tallerwebi.dominio.CategoriaDeMueble;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.Plano;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import java.util.List;
import org.hibernate.SessionFactory;
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

/** HU-08: el formulario del plano recibe las categorías con sus medidas promedio. */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorPlano2DCategoriasTest {

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private SessionFactory sessionFactory;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  @Transactional
  public void dadoQueHayCategorias_cuandoAbroElFormularioDelPlano_entoncesElModeloLasTraeOrdenadas()
    throws Exception {
    this.dadoQueExisteLaCategoria("Sofá 3 cuerpos", 2.1, 0.9);
    this.dadoQueExisteLaCategoria("Cama doble", 1.4, 1.9);

    MvcResult resultado = this.cuandoAbroElFormularioDelPlano();

    @SuppressWarnings("unchecked")
    List<CategoriaDeMueble> categorias = (List<CategoriaDeMueble>) resultado
      .getModelAndView()
      .getModel()
      .get("categorias");
    assertThat(
      categorias.stream().map(CategoriaDeMueble::getNombre).toList(),
      contains("Cama doble", "Sofá 3 cuerpos")
    );
  }

  @Test
  @Transactional
  public void dadoQueHayUnaCategoria_cuandoAbroElFormularioDelPlano_entoncesSuOpcionLlevaLasMedidas()
    throws Exception {
    this.dadoQueExisteLaCategoria("Cama doble", 1.4, 1.9);

    String html = this.cuandoAbroElFormularioDelPlano().getResponse().getContentAsString();

    assertThat(html, containsString("data-ancho=\"1.4\""));
    assertThat(html, containsString("data-profundidad=\"1.9\""));
    assertThat(html, containsString(">Cama doble</option>"));
  }

  @Test
  @Transactional
  public void dadoQueHayUnaCategoria_cuandoAbroElFormularioDelPlano_entoncesElSelectorEnviaSuId()
    throws Exception {
    CategoriaDeMueble cama = this.dadoQueExisteLaCategoria("Cama doble", 1.4, 1.9);

    String html = this.cuandoAbroElFormularioDelPlano().getResponse().getContentAsString();

    assertThat(html, containsString("name=\"muebles[0].categoria\""));
    assertThat(html, containsString("value=\"" + cama.getId() + "\""));
  }

  @Test
  @Transactional
  public void dadoQueEligoUnaCategoriaSinMedidas_cuandoConfirmoElFormulario_entoncesElPlanoUsaLosPromedios()
    throws Exception {
    CategoriaDeMueble cama = this.dadoQueExisteLaCategoria("Cama doble", 1.4, 1.9);

    MvcResult resultado =
      this.mockMvc.perform(
          post("/plano/generar")
            .param("ancho", "4")
            .param("largo", "3")
            .param("muebles[0].nombre", "Mi cama")
            .param("muebles[0].categoria", cama.getId().toString())
            .param("muebles[0].ancho", "")
            .param("muebles[0].profundidad", "")
        )
        .andExpect(status().isOk())
        .andReturn();

    Plano plano = (Plano) resultado.getModelAndView().getModel().get("plano");
    Mueble mueble = plano.getMuebles().get(0).getMueble();
    assertThat(List.of(mueble.getAncho(), mueble.getLargo()), contains(1.4, 1.9));
  }

  private CategoriaDeMueble dadoQueExisteLaCategoria(
    String nombre,
    Double ancho,
    Double profundidad
  ) {
    CategoriaDeMueble categoria = new CategoriaDeMueble(nombre, ancho, profundidad);
    this.sessionFactory.getCurrentSession().persist(categoria);
    return categoria;
  }

  private MvcResult cuandoAbroElFormularioDelPlano() throws Exception {
    return this.mockMvc.perform(get("/plano/ambiente")).andExpect(status().isOk()).andReturn();
  }
}
