package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tallerwebi.dominio.excepcion.ValidacionException;
import org.junit.jupiter.api.Test;

/**
 * Criterios de aceptación:
 * Dado que registré un obstáculo fijo en un muro, cuando se calcula la distribución,
 * entonces esa zona queda marcada como no disponible.
 * -
 * Dado que un mueble colisiona con un obstáculo fijo, cuando se muestra el plano,
 * entonces el sistema resalta visualmente el conflicto.
 */
public class ObstaculoTest {

  private static final double TOLERANCIA = 1e-9;

  private final Ambiente ambiente = new Ambiente(4.0, 3.0);

  @Test
  public void deberiaUbicarLaZonaNoDisponibleContraElMuroSuperiorDesdeSuPosicion() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 1.0, 0.8, 0.15);

    // ejecucion
    ZonaNoDisponible zona = radiador.zonaNoDisponible(ambiente);

    // validacion
    thenLaZonaEs(zona, 1.0, 0.0, 0.8, 0.15);
  }

  @Test
  public void deberiaUbicarLaZonaNoDisponibleContraElMuroDerechoDesdeArriba() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.DERECHO, 0.5, 0.8, 0.15);

    // ejecucion
    ZonaNoDisponible zona = radiador.zonaNoDisponible(ambiente);

    // validacion
    thenLaZonaEs(zona, 3.85, 0.5, 0.15, 0.8);
  }

  @Test
  public void deberiaUbicarLaZonaNoDisponibleContraElMuroInferiorDesdeLaIzquierda() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.INFERIOR, 2.0, 0.8, 0.15);

    // ejecucion
    ZonaNoDisponible zona = radiador.zonaNoDisponible(ambiente);

    // validacion
    thenLaZonaEs(zona, 2.0, 2.85, 0.8, 0.15);
  }

  @Test
  public void deberiaUbicarLaZonaNoDisponibleContraElMuroIzquierdoDesdeArriba() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.IZQUIERDO, 1.0, 0.8, 0.15);

    // ejecucion
    ZonaNoDisponible zona = radiador.zonaNoDisponible(ambiente);

    // validacion
    thenLaZonaEs(zona, 0.0, 1.0, 0.15, 0.8);
  }

  @Test
  public void deberiaColisionarCuandoElMuebleSeSuperponeConElObstaculo() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 1.0, 0.8, 0.15);
    MuebleUbicado rack = new MuebleUbicado(new Mueble("Rack", 1.0, 0.5), 0.5, 0.0);

    // ejecucion
    boolean colisiona = radiador.colisionaCon(rack, ambiente);

    // validacion
    assertTrue(colisiona);
  }

  @Test
  public void deberiaNoColisionarCuandoElMuebleTerminaDondeEmpiezaElObstaculo() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.INFERIOR, 0.7, 0.8, 0.15);
    // Asi calcula el recorrido la X en el muro inferior: da 0.20000000000000018, no 0.2.
    Double posicionX = 4.0 - (4.0 - 0.7) - 0.5;
    MuebleUbicado mesa = new MuebleUbicado(new Mueble("Mesa", 0.5, 0.5), posicionX, 2.5);

    // ejecucion
    boolean colisiona = radiador.colisionaCon(mesa, ambiente);

    // validacion
    assertFalse(colisiona);
  }

  @Test
  public void deberiaRechazarUnObstaculoQueSeSaleDelMuro() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.DERECHO, 2.5, 0.8, 0.15);

    // ejecucion y validacion
    thenElObstaculoSeRechazaCon(
      radiador,
      "Radiador se sale del muro derecho: va de 2,5 m a 3,3 m contando desde arriba y el muro " +
      "mide 3 m. Ubicalo como máximo en la posición 2,2 m."
    );
  }

  @Test
  public void deberiaRechazarUnObstaculoMasAnchoQueElMuro() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 0.0, 4.5, 0.15);

    // ejecucion y validacion
    thenElObstaculoSeRechazaCon(
      radiador,
      "Radiador mide 4,5 m de ancho y no entra en el muro superior, que mide 4 m."
    );
  }

  @Test
  public void deberiaAceptarUnObstaculoQueLlegaJustoAlFinalDelMuro() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.DERECHO, 2.2, 0.8, 0.15);

    // ejecucion y validacion
    assertDoesNotThrow(() -> radiador.validarEn(ambiente));
  }

  @Test
  public void deberiaRechazarUnObstaculoSinAncho() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 0.5, 0.0, 0.15);

    // ejecucion y validacion
    thenElObstaculoSeRechazaCon(radiador, "Las medidas de Radiador deben ser mayores a 0");
  }

  @Test
  public void deberiaRechazarUnObstaculoSinProfundidadCargada() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 0.5, 0.8, null);

    // ejecucion y validacion
    thenElObstaculoSeRechazaCon(radiador, "Las medidas de Radiador deben ser mayores a 0");
  }

  @Test
  public void deberiaRechazarUnObstaculoConPosicionNegativa() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, -0.1, 0.8, 0.15);

    // ejecucion y validacion
    thenElObstaculoSeRechazaCon(radiador, "La posición de Radiador debe ser 0 o mayor");
  }

  @Test
  public void deberiaRechazarUnObstaculoSinPosicionCargada() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, null, 0.8, 0.15);

    // ejecucion y validacion
    thenElObstaculoSeRechazaCon(radiador, "La posición de Radiador debe ser 0 o mayor");
  }

  @Test
  public void deberiaRechazarUnObstaculoSinMuro() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, null, 0.5, 0.8, 0.15);

    // ejecucion y validacion
    thenElObstaculoSeRechazaCon(radiador, "Elegí el tipo y el muro de cada obstáculo");
  }

  @Test
  public void deberiaRechazarUnObstaculoMasProfundoQueElAmbiente() {
    // preparacion
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 0.0, 0.8, 3.0);

    // ejecucion y validacion
    thenElObstaculoSeRechazaCon(radiador, "Radiador es más profundo que el ambiente");
  }

  @Test
  public void deberiaAceptarUnObstaculoQueLlegaAlFinalDelMuroAunqueHayaErrorDeRedondeo() {
    // preparacion
    Ambiente ambienteDeTresTreinta = new Ambiente(3.3, 3.0);
    // 2.2 + 1.1 da 3.3000000000000003, apenas mas que el muro de 3.3.
    Obstaculo radiador = new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 2.2, 1.1, 0.15);

    // ejecucion y validacion
    assertDoesNotThrow(() -> radiador.validarEn(ambienteDeTresTreinta));
  }

  private void thenElObstaculoSeRechazaCon(Obstaculo obstaculo, String mensajeEsperado) {
    ValidacionException error = assertThrows(
      ValidacionException.class,
      () -> obstaculo.validarEn(ambiente)
    );
    assertEquals(mensajeEsperado, error.getMessage());
  }

  private void thenLaZonaEs(
    ZonaNoDisponible zona,
    double posicionX,
    double posicionY,
    double ancho,
    double largo
  ) {
    assertEquals(posicionX, zona.getPosicionX(), TOLERANCIA);
    assertEquals(posicionY, zona.getPosicionY(), TOLERANCIA);
    assertEquals(ancho, zona.getAncho(), TOLERANCIA);
    assertEquals(largo, zona.getLargo(), TOLERANCIA);
  }
}
