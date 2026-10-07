package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorPlano2DObstaculosTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void dadoQueAbroElFormularioDelPlano_entoncesPuedoCargarObstaculosConSusMedidasSugeridas()
    throws Exception {
    String html =
      this.mockMvc.perform(get("/plano/ambiente"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertThat(html, containsString("id=\"filas-obstaculos\""));
    assertThat(html, containsString("value=\"RADIADOR\""));
    assertThat(html, containsString("data-ancho=\"0.8\""));
    assertThat(html, containsString("data-profundidad=\"0.15\""));
    assertThat(html, containsString(">Toma de corriente</option>"));
    assertThat(html, containsString(">Otro</option>"));
    assertThat(html, containsString("value=\"IZQUIERDO\""));
  }

  @Test
  public void dadoQueRegistreUnObstaculo_cuandoGeneroElPlano_entoncesSeDibujaSuZonaNoDisponible()
    throws Exception {
    String html =
      this.cuandoGeneroElPlanoCon(
          "obstaculos[0].tipo",
          "RADIADOR",
          "obstaculos[0].muro",
          "SUPERIOR",
          "obstaculos[0].posicion",
          "0",
          "obstaculos[0].ancho",
          "0.8",
          "obstaculos[0].profundidad",
          "0.15"
        );

    assertThat(html, containsString("class=\"plano-obstaculo\""));
    assertThat(html, containsString("Radiador (muro superior)"));
  }

  @Test
  public void dadoQueUnMuebleQuedaSobreUnObstaculo_cuandoGeneroElPlano_entoncesSeResaltaElConflicto()
    throws Exception {
    String html =
      this.cuandoGeneroElPlanoCon(
          "muebles[0].nombre",
          "Rack",
          "muebles[0].ancho",
          "1.0",
          "muebles[0].profundidad",
          "0.5",
          "obstaculos[0].tipo",
          "RADIADOR",
          "obstaculos[0].muro",
          "SUPERIOR",
          "obstaculos[0].posicion",
          "0",
          "obstaculos[0].ancho",
          "4",
          "obstaculos[0].profundidad",
          "0.15",
          "obstaculos[1].tipo",
          "RADIADOR",
          "obstaculos[1].muro",
          "DERECHO",
          "obstaculos[1].posicion",
          "0",
          "obstaculos[1].ancho",
          "3",
          "obstaculos[1].profundidad",
          "0.15",
          "obstaculos[2].tipo",
          "RADIADOR",
          "obstaculos[2].muro",
          "INFERIOR",
          "obstaculos[2].posicion",
          "0",
          "obstaculos[2].ancho",
          "4",
          "obstaculos[2].profundidad",
          "0.15",
          "obstaculos[3].tipo",
          "RADIADOR",
          "obstaculos[3].muro",
          "IZQUIERDO",
          "obstaculos[3].posicion",
          "0",
          "obstaculos[3].ancho",
          "3",
          "obstaculos[3].profundidad",
          "0.15"
        );

    assertThat(html, containsString("plano-mueble-conflicto"));
    assertThat(html, containsString("Rack choca con Radiador (muro superior)"));
  }

  private String cuandoGeneroElPlanoCon(String... parametros) throws Exception {
    MockHttpServletRequestBuilder pedido = post("/plano/generar")
      .param("ancho", "4")
      .param("largo", "3");
    for (int indice = 0; indice < parametros.length; indice += 2) {
      pedido = pedido.param(parametros[indice], parametros[indice + 1]);
    }
    return this.mockMvc.perform(pedido)
      .andExpect(status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString();
  }
}
