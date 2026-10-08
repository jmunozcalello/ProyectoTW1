package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tallerwebi.dominio.excepcion.ValidacionException;
import org.junit.jupiter.api.Test;

public class ServicioObstaculosTest {

  private final ServicioObstaculos servicioObstaculos = new ServicioObstaculosImpl();
  private final Ambiente ambiente = new Ambiente(4.0, 3.0);

  @Test
  public void deberiaAceptarLosObstaculosQueEntranEnSusMuros() {
    // preparacion
    ambiente.agregarObstaculo(
      new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 0.0, 0.8, 0.15)
    );

    // ejecucion y validacion
    assertDoesNotThrow(() -> servicioObstaculos.validarObstaculos(ambiente));
  }

  @Test
  public void deberiaRechazarElAmbienteCuandoUnObstaculoSeSaleDeSuMuro() {
    // preparacion
    ambiente.agregarObstaculo(
      new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 0.0, 0.8, 0.15)
    );
    ambiente.agregarObstaculo(
      new Obstaculo(TipoDeObstaculo.TOMA_DE_CORRIENTE, Muro.IZQUIERDO, 3.0, 0.15, 0.1)
    );

    // ejecucion
    ValidacionException error = assertThrows(
      ValidacionException.class,
      () -> servicioObstaculos.validarObstaculos(ambiente)
    );

    // validacion
    assertEquals(
      "Toma de corriente se sale del muro izquierdo: va de 3 m a 3,15 m contando desde arriba " +
      "y el muro mide 3 m. Ubicalo como máximo en la posición 2,85 m.",
      error.getMessage()
    );
  }
}
