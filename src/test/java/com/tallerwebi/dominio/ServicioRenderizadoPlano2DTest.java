package com.tallerwebi.dominio;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private ServicioPlano2D servicioPlano2D = new ServicioPlano2DImpl();
    private final Mueble SILLA = new Mueble("Silla");
    private final Mueble MESA = new Mueble("Mesa");
    private final List<Mueble> MUEBLES = List.of(SILLA, MESA);

    @Test
    public void deberiaGenerarUnPlanoCuandoLasDimensionesYMueblesSonValidos() {

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(4.0, 3.0, MUEBLES);

        // validacion
        thenExisteUnPlanoGenerado(planoGenerado);

    }

    @Test
    public void deberiaUbicarTodosLosMueblesEnElPlanoCuandoDimensionesYMueblesSonValidos() {

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(4.0, 3.0, MUEBLES);

        // validacion
        thenSeUbicanTodosLosMueblesEnElPlano(planoGenerado);

    }

    @Test
    public void deberiaConsiderarQueUnMuebleEstaDentroDelPerimetroCuandoElMuebleCabeEnElPerimetro() {

        // preparacion
        Mueble mueble = new Mueble("Mesa", 1.0, 1.0);
        MuebleUbicado muebleUbicado = new MuebleUbicado(mueble, 3.0, 2.0);
        Ambiente ambiente = new Ambiente(4.0, 3.0);


        // ejecucion
        Boolean muebleEstaDentroDelPerimetro = whenVerificoSiElMuebleEstaEnElPerimetro(muebleUbicado, ambiente);

        // validacion
        thenElResultadoDeEstarDentroDelPerimetroEs(true, muebleEstaDentroDelPerimetro);

    }

    @Test
    public void deberiaConsiderarQueUnMuebleNoEstaDentroDelPerimetroCuandoElMuebleSeSaleDelPerimetro() {

        // preparacion
        Mueble mueble = new Mueble("Mesa", 1.0, 1.0);
        MuebleUbicado muebleUbicado = new MuebleUbicado(mueble, 3.5, 0.0);
        Ambiente ambiente = new Ambiente(4.0, 3.0);

        // ejecucion
        Boolean muebleEstaDentroDelPerimetro = whenVerificoSiElMuebleEstaEnElPerimetro(muebleUbicado, ambiente);

        // validacion
        thenElResultadoDeEstarDentroDelPerimetroEs(false, muebleEstaDentroDelPerimetro);

    }

    @Test
    public void deberiaConsiderarQueNoEstaDentroDelPerimetroCuandoElMuebleSeSaleDelPerimetroEnElEjeY() {

        // preparacion
        Mueble mueble = new Mueble("Mesa", 1.0, 1.0);
        MuebleUbicado muebleUbicado = new MuebleUbicado(mueble, 0.0, 2.5);
        Ambiente ambiente = new Ambiente(4.0, 3.0);
        // 2.5 + 1.0 = 3.5 > 3.0 (largo)
        // ejecucion
        Boolean muebleEstaDentroDelPerimetro = whenVerificoSiElMuebleEstaEnElPerimetro(muebleUbicado, ambiente);

        // validacion
        thenElResultadoDeEstarDentroDelPerimetroEs(false, muebleEstaDentroDelPerimetro);
    }

    //falta test principal









    private Plano whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(Double ancho, Double largo, List<Mueble> muebles) {
        return servicioPlano2D.generarPlano(ancho, largo, muebles);
    }

    private void thenExisteUnPlanoGenerado(Plano planoGenerado) {
        assertFalse(planoGenerado.getMuebles().isEmpty());
    }

    private void thenSeUbicanTodosLosMueblesEnElPlano(Plano planoGenerado) {
        assertEquals(2, planoGenerado.getMuebles().size());
    }

    private Boolean whenVerificoSiElMuebleEstaEnElPerimetro(MuebleUbicado muebleUbicado, Ambiente ambiente) {
        return servicioPlano2D.estaDentroDelPerimetro(muebleUbicado, ambiente);
    }

    private void thenElResultadoDeEstarDentroDelPerimetroEs(Boolean resultadoEsperado, Boolean elMuebleEstaDentroDelPerimetro) {
        assertEquals(resultadoEsperado, elMuebleEstaDentroDelPerimetro);
    }


}
