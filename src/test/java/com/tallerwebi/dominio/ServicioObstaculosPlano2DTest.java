package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tallerwebi.dominio.excepcion.ValidacionException;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Criterios de aceptación:
 * Dado que registré un obstáculo fijo en un muro, cuando se calcula la distribución,
 * entonces esa zona queda marcada como no disponible.
 * -
 * Dado que un mueble colisiona con un obstáculo fijo, cuando se muestra el plano,
 * entonces el sistema resalta visualmente el conflicto.
 */
public class ServicioObstaculosPlano2DTest {

  private static final double TOLERANCIA = 1e-9;

  private final ServicioPlano2D servicioPlano2D = new ServicioPlano2DImpl();
  private final Ambiente ambiente = new Ambiente(4.0, 3.0);
  private final Mueble rack = new Mueble("Rack", 1.0, 0.5);

  @Test
  public void deberiaRechazarElPlanoCuandoUnObstaculoSeSaleDelMuro() {
    // preparacion
    ambiente.agregarObstaculo(radiadorEn(Muro.DERECHO, 2.5, 0.8));

    // ejecucion y validacion
    assertThrows(
      ValidacionException.class,
      () -> servicioPlano2D.generarPlano(ambiente, List.of(rack))
    );
  }

  @Test
  public void deberiaUbicarElMuebleDespuesDelObstaculoCuandoElObstaculoEstaEnElMuroSuperior() {
    // preparacion
    ambiente.agregarObstaculo(radiadorEn(Muro.SUPERIOR, 0.0, 0.8));

    // ejecucion
    Plano plano = servicioPlano2D.generarPlano(ambiente, List.of(rack));

    // validacion
    thenElMuebleQuedaEn(plano, 0.8, 0.0);
  }

  @Test
  public void deberiaPasarAlMuroDerechoCuandoUnObstaculoOcupaTodoElMuroSuperior() {
    // preparacion
    ambiente.agregarObstaculo(radiadorEn(Muro.SUPERIOR, 0.0, 4.0));

    // ejecucion
    Plano plano = servicioPlano2D.generarPlano(ambiente, List.of(rack));

    // validacion
    thenElMuebleQuedaEn(plano, 3.0, 0.5);
  }

  @Test
  public void deberiaUbicarElMuebleDebajoDelObstaculoCuandoElObstaculoEstaEnElMuroDerecho() {
    // preparacion
    ambiente.agregarObstaculo(radiadorEn(Muro.SUPERIOR, 0.0, 4.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.DERECHO, 0.5, 0.8));

    // ejecucion
    Plano plano = servicioPlano2D.generarPlano(ambiente, List.of(rack));

    // validacion
    thenElMuebleQuedaEn(plano, 3.0, 1.3);
  }

  @Test
  public void deberiaUbicarElMuebleALaIzquierdaDelObstaculoCuandoElObstaculoEstaEnElMuroInferior() {
    // preparacion
    ambiente.agregarObstaculo(radiadorEn(Muro.SUPERIOR, 0.0, 4.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.DERECHO, 0.0, 3.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.INFERIOR, 2.5, 0.8));

    // ejecucion
    Plano plano = servicioPlano2D.generarPlano(ambiente, List.of(rack));

    // validacion
    thenElMuebleQuedaEn(plano, 1.5, 2.5);
  }

  @Test
  public void deberiaUbicarElMuebleArribaDelObstaculoCuandoElObstaculoEstaEnElMuroIzquierdo() {
    // preparacion
    ambiente.agregarObstaculo(radiadorEn(Muro.SUPERIOR, 0.0, 4.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.DERECHO, 0.0, 3.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.INFERIOR, 0.0, 4.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.IZQUIERDO, 1.8, 0.8));

    // ejecucion
    Plano plano = servicioPlano2D.generarPlano(ambiente, List.of(rack));

    // validacion
    thenElMuebleQuedaEn(plano, 0.0, 1.3);
  }

  @Test
  public void deberiaUbicarElMuebleEnConflictoCuandoLosObstaculosNoDejanLugarLibre() {
    // preparacion
    dadoQueTodosLosMurosEstanCubiertos();

    // ejecucion
    Plano plano = servicioPlano2D.generarPlano(ambiente, List.of(rack));

    // validacion
    thenElMuebleQuedaEn(plano, 0.0, 0.0);
    assertTrue(plano.getMuebles().get(0).isEnConflicto());
  }

  @Test
  public void deberiaUbicarElMuebleSinConflictoCuandoNoPisaNingunObstaculo() {
    // preparacion
    ambiente.agregarObstaculo(radiadorEn(Muro.SUPERIOR, 0.0, 0.8));

    // ejecucion
    Plano plano = servicioPlano2D.generarPlano(ambiente, List.of(rack));

    // validacion
    assertFalse(plano.getMuebles().get(0).isEnConflicto());
  }

  @Test
  public void deberiaUbicarElMuebleJustoEntreElObstaculoYLaEsquinaAunqueHayaErrorDeRedondeo() {
    // preparacion
    Ambiente ambienteDeTresTreinta = new Ambiente(3.3, 3.0);
    ambienteDeTresTreinta.agregarObstaculo(radiadorEn(Muro.SUPERIOR, 0.0, 2.2));
    Mueble mesa = new Mueble("Mesa", 1.1, 0.5);

    // ejecucion
    Plano plano = servicioPlano2D.generarPlano(ambienteDeTresTreinta, List.of(mesa));

    // validacion
    thenElMuebleQuedaEn(plano, 2.2, 0.0);
  }

  private void dadoQueTodosLosMurosEstanCubiertos() {
    ambiente.agregarObstaculo(radiadorEn(Muro.SUPERIOR, 0.0, 4.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.DERECHO, 0.0, 3.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.INFERIOR, 0.0, 4.0));
    ambiente.agregarObstaculo(radiadorEn(Muro.IZQUIERDO, 0.0, 3.0));
  }

  private Obstaculo radiadorEn(Muro muro, Double posicion, Double ancho) {
    return new Obstaculo(TipoDeObstaculo.RADIADOR, muro, posicion, ancho, 0.15);
  }

  private void thenElMuebleQuedaEn(Plano plano, double posicionX, double posicionY) {
    MuebleUbicado ubicado = plano.getMuebles().get(0);
    assertEquals(posicionX, ubicado.getPosicionX(), TOLERANCIA);
    assertEquals(posicionY, ubicado.getPosicionY(), TOLERANCIA);
  }
}
