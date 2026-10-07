package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Datos que el plano expone para resaltar los conflictos con obstáculos fijos. */
public class PlanoTest {

  private final Ambiente ambiente = new Ambiente(4.0, 3.0);
  private final Mueble rack = new Mueble("Rack", 1.0, 0.5);

  @Test
  public void deberiaListarSoloLosMueblesEnConflicto() {
    // preparacion
    MuebleUbicado enConflicto = new MuebleUbicado(rack, 0.0, 0.0, true);
    MuebleUbicado libre = new MuebleUbicado(rack, 1.0, 0.0, false);
    Plano plano = new Plano(ambiente, List.of(enConflicto, libre), List.of(), List.of());

    // ejecucion
    List<MuebleUbicado> conflictos = plano.getMueblesEnConflicto();

    // validacion
    assertEquals(List.of(enConflicto), conflictos);
  }

  @Test
  public void deberiaDescribirLosObstaculosConLosQueChocaElMueble() {
    // preparacion
    ambiente.agregarObstaculo(
      new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, 0.0, 0.8, 0.15)
    );
    ambiente.agregarObstaculo(
      new Obstaculo(TipoDeObstaculo.TOMA_DE_CORRIENTE, Muro.SUPERIOR, 0.9, 0.15, 0.1)
    );
    ambiente.agregarObstaculo(
      new Obstaculo(TipoDeObstaculo.TOMA_DE_CORRIENTE, Muro.INFERIOR, 2.0, 0.15, 0.1)
    );
    MuebleUbicado enConflicto = new MuebleUbicado(rack, 0.0, 0.0, true);
    Plano plano = new Plano(ambiente, List.of(enConflicto), List.of(), List.of());

    // ejecucion
    String descripcion = plano.descripcionDelConflicto(enConflicto);

    // validacion
    assertEquals(
      "Rack choca con Radiador (muro superior), Toma de corriente (muro superior)",
      descripcion
    );
  }
}
