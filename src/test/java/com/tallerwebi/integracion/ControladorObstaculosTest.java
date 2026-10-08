package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
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
public class ControladorObstaculosTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void dadoQueCargueElAmbienteYLosMuebles_cuandoSigoAObstaculos_entoncesPuedoCargarlosSinPerderLoAnterior()
    throws Exception {
    String html = this.htmlDe(this.conElAmbienteYUnRack(post("/plano/obstaculos")));

    assertThat(html, containsString("id=\"filas-obstaculos\""));
    assertThat(html, containsString("data-ancho=\"0.8\""));
    assertThat(html, containsString(">Toma de corriente</option>"));
    assertThat(html, containsString("value=\"IZQUIERDO\""));
    assertThat(html, containsString("action=\"/plano/obstaculos/confirmar\""));
    assertThat(html, containsString("name=\"largo\" value=\"3.0\""));
    assertThat(html, containsString("name=\"muebles[0].nombre\" value=\"Rack\""));
  }

  @Test
  public void dadoQueUnObstaculoSeSaleDelMuro_cuandoConfirmo_entoncesVeoElErrorSinPerderLoCargado()
    throws Exception {
    String html =
      this.htmlDe(
          this.conElAmbienteYUnRack(post("/plano/obstaculos/confirmar"))
            .param("obstaculos[0].tipo", "TOMA_DE_CORRIENTE")
            .param("obstaculos[0].muro", "IZQUIERDO")
            .param("obstaculos[0].posicion", "3")
            .param("obstaculos[0].ancho", "0.15")
            .param("obstaculos[0].profundidad", "0.1")
        );

    assertThat(html, containsString("Toma de corriente se sale del muro izquierdo"));
    assertThat(html, containsString("name=\"obstaculos[0].posicion\" value=\"3.0\""));
    assertThat(html, containsString("value=\"IZQUIERDO\" selected=\"selected\""));
    assertThat(html, containsString("name=\"muebles[0].nombre\" value=\"Rack\""));
  }

  @Test
  public void dadoQueLosObstaculosSonValidos_cuandoConfirmo_entoncesSigoAGenerarElPlano()
    throws Exception {
    this.mockMvc.perform(
        this.conElAmbienteYUnRack(post("/plano/obstaculos/confirmar"))
          .param("obstaculos[0].tipo", "RADIADOR")
          .param("obstaculos[0].muro", "SUPERIOR")
          .param("obstaculos[0].posicion", "0")
          .param("obstaculos[0].ancho", "0.8")
          .param("obstaculos[0].profundidad", "0.15")
      )
      .andExpect(status().isOk())
      .andExpect(forwardedUrl("/plano/generar"));
  }

  private MockHttpServletRequestBuilder conElAmbienteYUnRack(MockHttpServletRequestBuilder pedido) {
    return pedido
      .param("ancho", "4")
      .param("largo", "3")
      .param("muebles[0].nombre", "Rack")
      .param("muebles[0].ancho", "1.0")
      .param("muebles[0].profundidad", "0.5");
  }

  private String htmlDe(MockHttpServletRequestBuilder pedido) throws Exception {
    return this.mockMvc.perform(pedido)
      .andExpect(status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString();
  }
}
