package com.tallerwebi.dominio;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Criterios de aceptación:
 * Dado que cargué ancho y largo del ambiente, cuando confirmo los datos,
 * entonces el sistema muestra un plano 2D con los muebles principales ubicados sobre los muros perimetrales.
 * -
 * Dado que el ambiente no tiene muebles cargados, cuando se genera la distribución,
 * entonces el sistema muestra un mensaje indicando que no hay mobiliario para distribuir.
 * -
 * Dado un mueble cuyas dimensiones exceden el espacio disponible en el ambiente, cuando se genera la distribución,
 * entonces el sistema muestra un mensaje indicando que ese mueble no entra y lo excluye de la propuesta.
 * -
 * Dado más de un mueble del mismo tipo (ej. dos sillones), cuando se calcula la ubicación,
 * entonces el sistema define un criterio de orden de colocación (ej. por tamaño o por orden de carga) y lo aplica de forma consistente.
 */

public class ServicioRenderizadoPlano2DTest {

    ServicioPlano2D servicioPlano2D = new ServicioPlano2DImpl();

    @Test
    public void DeberiaGenerarUnPlanoCuandoLasDimensionesYMueblesSonValidos() {

        // preparacion
        Mueble silla = new Mueble("Silla");
        Mueble mesa = new Mueble("Mesa");

        // ejecucion
        Plano planoObtenido = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(4.0,3.0,silla,mesa); // tdd podria hacer que valide dimensiones y muebles

        // validacion
        thenExisteUnPlanoGenerado(planoObtenido);

    }

    private Plano whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(Double ancho, Double largo, Mueble mueble1, Mueble mueble2) {
        return servicioPlano2D.generarPlano(ancho,largo,mueble1,mueble2);
    }

    private void thenExisteUnPlanoGenerado(Plano planoObtenido) {
        assertFalse(planoObtenido.getMuebles().isEmpty());
    }


}
