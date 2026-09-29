package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ServicioRediseñoTest {

  private final ServicioRediseño servicioRediseño = new ServicioRediseñoImpl();

  @Test
  public void dadoCadaEstilo_cuandoPidoUnaRecomendacion_enternoUnaPaletaYUnaIluminacion() {
    for (Estilo estilo : Estilo.values()) {
      Recomendacion recomendacion = this.servicioRediseño.recomendar(estilo);

      assertThat(
        estilo + ": la recomendacion deberia ser del mismo estilo",
        recomendacion.getEstilo(),
        is(estilo)
      );
      assertThat(
        estilo + ": deberia traer una paleta de 3 colores",
        recomendacion.getColores(),
        hasSize(3)
      );
      assertThat(
        estilo + ": deberia traer una iluminacion",
        recomendacion.getIluminacion(),
        is(notNullValue())
      );
    }
  }

  @Test
  public void dadoDosEstilosDistintos_cuandoPidoSusRecomendaciones_entornoPaletasDistintas() {
    Recomendacion retro = this.servicioRediseño.recomendar(Estilo.Retro);
    Recomendacion industrial = this.servicioRediseño.recomendar(Estilo.Industrial);

    assertThat(retro.getColores(), is(not(industrial.getColores())));
    assertThat(retro.getIluminacion(), is(Iluminacion.CALIDA));
    assertThat(industrial.getIluminacion(), is(Iluminacion.FRIA));
  }

  @Test
  public void dadoUnEstilo_cuandoPidoUnaRecomendacion_entornoTodosLosColoresConNombreYHexValidos() {
    Recomendacion japandi = this.servicioRediseño.recomendar(Estilo.Japandi);

    for (Color color : japandi.getColores()) {
      assertThat(color.getNombre(), is(notNullValue()));
      assertThat(color.getHex().matches("^#[0-9A-Fa-f]{6}$"), is(true));
    }
  }

  @Test
  public void dadoUnaRecomendacion_cuandoIntentoAgregarUnColor_entornoLaPaletaQuedaIntacta() {
    Recomendacion escandinavo = this.servicioRediseño.recomendar(Estilo.Escandinavo);

    assertThrows(
      UnsupportedOperationException.class,
      () -> escandinavo.getColores().add(new Color("Rosa", "#FF00FF"))
    );
    assertThat(escandinavo.getColores(), hasSize(3));
  }

  @Test
  public void dadoUnEstilo_cuandoPidoUnaRecomendacion_entornoLaIluminacionInformaNombreYTemperatura() {
    Recomendacion minimalista = this.servicioRediseño.recomendar(Estilo.Minimalista);
    Recomendacion japandi = this.servicioRediseño.recomendar(Estilo.Japandi);

    assertThat(minimalista.getIluminacion().getDescripcion(), is("Intermedia"));
    assertThat(minimalista.getIluminacion().getKelvin(), is(4000));
    assertThat(japandi.getIluminacion().getDescripcion(), is("Cálida"));
    assertThat(japandi.getIluminacion().getKelvin(), is(2700));
  }

  @Test
  public void dadoLosEstilosQueCompartenColores_cuandoPidoSusPaletas_entornoNoSonLaMismaLista() {
    List<Color> retro = this.servicioRediseño.recomendar(Estilo.Retro).getColores();
    List<Color> boho = this.servicioRediseño.recomendar(Estilo.Boho).getColores();

    assertThat(retro, is(not(boho)));
  }
}
