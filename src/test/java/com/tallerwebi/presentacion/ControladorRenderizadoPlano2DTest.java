package com.tallerwebi.presentacion;

import org.junit.jupiter.api.Test;

/**
 * Criterios de aceptación:
 *
 * Dado que cargué ancho y largo del ambiente, cuando confirmo los datos,
 * entonces el sistema muestra un plano 2D con los muebles principales ubicados sobre los muros perimetrales.
 *
 * Dado que el ambiente no tiene muebles cargados, cuando se genera la distribución,
 * entonces el sistema muestra un mensaje indicando que no hay mobiliario para distribuir.
 *
 * Dado un mueble cuyas dimensiones exceden el espacio disponible en el ambiente, cuando se genera la distribución,
 * entonces el sistema muestra un mensaje indicando que ese mueble no entra y lo excluye de la propuesta.
 *
 * Dado más de un mueble del mismo tipo (ej. dos sillones), cuando se calcula la ubicación,
 * entonces el sistema define un criterio de orden de colocación (ej. por tamaño o por orden de carga) y lo aplica de forma consistente.
 */

public class ControladorRenderizadoPlano2DTest {

  @Test
  public void ejemplo() {}
}
