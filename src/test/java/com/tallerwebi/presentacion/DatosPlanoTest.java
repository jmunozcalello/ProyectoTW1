package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Muro;
import com.tallerwebi.dominio.TipoDeObstaculo;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DatosPlanoTest {

  @Test
  public void deberiaArmarElAmbienteConSusMedidasYSusObstaculos() {
    // preparacion
    DatosPlano formulario = givenUnFormularioConLosObstaculos(
      new DatosObstaculo(TipoDeObstaculo.RADIADOR, Muro.DERECHO, 0.5, 0.8, 0.15)
    );

    // ejecucion
    Ambiente ambiente = formulario.aAmbiente();

    // validacion
    assertEquals(List.of(4.0, 3.0), List.of(ambiente.getAncho(), ambiente.getLargo()));
    assertEquals(Muro.DERECHO, ambiente.getObstaculos().get(0).getMuro());
  }

  @Test
  public void deberiaIgnorarLasFilasDeObstaculoSinMedidas() {
    // preparacion
    DatosPlano formulario = givenUnFormularioConLosObstaculos(
      new DatosObstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, null, null, null)
    );

    // ejecucion
    Ambiente ambiente = formulario.aAmbiente();

    // validacion
    assertEquals(List.of(), ambiente.getObstaculos());
  }

  private DatosPlano givenUnFormularioConLosObstaculos(DatosObstaculo... datosObstaculos) {
    DatosPlano formulario = new DatosPlano();
    formulario.setAncho(4.0);
    formulario.setLargo(3.0);
    formulario.setObstaculos(new ArrayList<>(List.of(datosObstaculos)));
    return formulario;
  }
}
