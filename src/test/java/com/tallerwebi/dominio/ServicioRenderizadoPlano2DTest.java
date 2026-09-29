package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    private final ServicioPlano2D servicioPlano2D = new ServicioPlano2DImpl();
    private final Ambiente ambiente = new Ambiente(4.0, 3.0);
    private final Mueble silla = new Mueble("Silla", 0.5, 0.5);
    private final Mueble mesa = new Mueble("Mesa", 1.0, 1.0);
    private final List<Mueble> muebles = List.of(silla, mesa);

    @Test
    public void deberiaConservarElAmbienteDelPlanoCuandoLasDimensionesYMueblesSonValidos() {
        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, muebles);

        // validacion
        thenElAmbienteDelPlanoEs(planoGenerado, ambiente);
    }

    @Test
    public void deberiaUbicarTodosLosMueblesValidosEnElPlanoCuandoLasDimensionesYMueblesSonValidos() {
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, muebles);

        thenLaCantidadDeMueblesDelPlanoEs(planoGenerado, muebles.size());
    }

    @Test
    public void deberiaConsiderarQueUnMuebleEstaDentroDelPerimetroCuandoElMuebleCabeEnElPerimetro() {
        // preparacion
        Mueble mueble = new Mueble("Mesa", 1.0, 1.0);
        MuebleUbicado muebleUbicado = new MuebleUbicado(mueble, 3.0, 2.0);

        // ejecucion
        Boolean muebleEstaDentroDelPerimetro = whenVerificoSiElMuebleEstaEnElPerimetro(
                muebleUbicado,
                ambiente
        );

        // validacion
        thenElResultadoDeEstarDentroDelPerimetroEs(true, muebleEstaDentroDelPerimetro);
    }

    @Test
    public void deberiaConsiderarQueNoEstaDentroDelPerimetroCuandoLaCoordenadaXEsNegativa() {
        // preparacion
        MuebleUbicado muebleUbicado = new MuebleUbicado(mesa, -0.5, 0.0);

        // ejecucion
        Boolean muebleEstaDentroDelPerimetro = whenVerificoSiElMuebleEstaEnElPerimetro(
                muebleUbicado,
                ambiente
        );

        // validacion
        thenElResultadoDeEstarDentroDelPerimetroEs(false, muebleEstaDentroDelPerimetro);
    }

    @Test
    public void deberiaConsiderarQueUnMuebleNoEstaDentroDelPerimetroCuandoElMuebleSeSaleDelPerimetro() {
        // preparacion
        MuebleUbicado muebleUbicado = new MuebleUbicado(mesa, 3.5, 0.0);

        // ejecucion
        Boolean muebleEstaDentroDelPerimetro = whenVerificoSiElMuebleEstaEnElPerimetro(
                muebleUbicado,
                ambiente
        );

        // validacion
        thenElResultadoDeEstarDentroDelPerimetroEs(false, muebleEstaDentroDelPerimetro);
    }

    @Test
    public void deberiaConsiderarQueNoEstaDentroDelPerimetroCuandoElMuebleSeSaleDelPerimetroEnElEjeY() {
        // preparacion
        MuebleUbicado muebleUbicado = new MuebleUbicado(mesa, 0.0, 2.5);
        // 2.5 + 1.0 = 3.5 > 3.0 (largo)
        // ejecucion
        Boolean muebleEstaDentroDelPerimetro = whenVerificoSiElMuebleEstaEnElPerimetro(
                muebleUbicado,
                ambiente
        );

        // validacion
        thenElResultadoDeEstarDentroDelPerimetroEs(false, muebleEstaDentroDelPerimetro);
    }

    @Test
    public void deberiaRetornarMismoResultadoCuandoSeEjecutaDosVecesConMismoInput() {
        // ejecucion
        Plano primerPlano = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, muebles);
        Plano segundoPlano = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, muebles);

        // validacion
        thenLasPosicionesDeAmbosPlanosSonIguales(primerPlano, segundoPlano);
    }

    @Test
    public void deberiaUbicarLosMueblesDentroDelPerimetroCuandoDimensionesYMueblesSonValidos() {
        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, muebles);
        List<Boolean> resultadosDePerimetro = whenVerificoSiCadaMuebleEstaEnElPerimetro(planoGenerado);

        // validacion
        thenTodosLosMueblesEstanDentroDelPerimetro(resultadosDePerimetro);
    }

    @Test
    public void deberiaUbicarElMuebleEnElMuroDerechoCuandoElMuroSuperiorEstaOcupado() {
        // preparacion
        // el muro superior del ambiente mide 4.0, asi que los primeros 4 sillones de 1.0 lo llenan
        List<Mueble> sillones = List.of(
                new Mueble("Sillon", 1.0, 0.5),
                new Mueble("Sillon", 1.0, 0.5),
                new Mueble("Sillon", 1.0, 0.5),
                new Mueble("Sillon", 1.0, 0.5),
                new Mueble("Sillon", 1.0, 0.5)
        );

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, sillones);

        // validacion
        thenElUltimoMuebleEstaEnLaPosicion(List.of(3.0, 0.5), planoGenerado);
    }

    @Test
    public void deberiaUbicarElMuebleEnElMuroInferiorCuandoElMuroDerechoEstaOcupado() {
        // preparacion
        // el muro superior consume 4 sillones y el derecho 4 mas, porque cada sillon
        // avanza 0.5 de largo entre Y 0.5 y Y 2.5 con la esquina superior libre, asi que
        // el noveno sillon es el primero que toca el muro inferior
        List<Mueble> sillones = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            sillones.add(new Mueble("Sillon", 1.0, 0.5));
        }

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, sillones);

        // validacion
        thenElUltimoMuebleEstaEnLaPosicion(List.of(2.0, 2.5), planoGenerado);
    }

    @Test
    public void deberiaUbicarElMuebleEnElMuroIzquierdoCuandoLosMurosPreviosEstanOcupados() {
        // preparacion
        // el muro superior consume 4 sillones, el derecho 4 mas y el inferior 3, asi que
        // el duodecimo es el primero que toca el muro izquierdo, que se recorre de abajo
        // hacia arriba arrancando con la esquina inferior libre
        List<Mueble> sillones = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            sillones.add(new Mueble("Sillon", 1.0, 0.5));
        }

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, sillones);

        // validacion
        thenElUltimoMuebleEstaEnLaPosicion(List.of(0.0, 2.0), planoGenerado);
    }

    @Test
    public void deberiaAsignarPosicionesDistintasCuandoHayMueblesDelMismoTipo() {
        // preparacion
        Mueble sillonChico = new Mueble("Sillon", 0.5, 0.5);
        Mueble sillonGrande = new Mueble("Sillon", 1.0, 1.0);
        List<Mueble> sillones = List.of(sillonChico, sillonGrande);

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, sillones);

        // validacion
        thenLosMueblesTienenPosicionesDistintas(planoGenerado);
    }

    @Test
    public void deberiaUbicarElMuebleMasGrandePrimeroCuandoHayMueblesDelMismoTipo() {
        // preparacion
        Mueble sillonChico = new Mueble("Sillon", 0.5, 0.5);
        Mueble sillonGrande = new Mueble("Sillon", 1.0, 1.0);
        // el chico se carga primero, a proposito, para que el orden de entrada no alcance
        List<Mueble> sillones = List.of(sillonChico, sillonGrande);

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(ambiente, sillones);

        // validacion
        thenElPrimerMuebleDelPlanoEs(sillonGrande, planoGenerado);
    }

    @Test
    public void deberiaRetornarMensajeSinMobiliarioCuandoNoHayMueblesCargadosEnElPlano() {
        // preparacion
        List<Mueble> mueblesVacios = List.of();

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(
                ambiente,
                mueblesVacios
        );

        // validacion
        thenDevuelveMensajeSinMobiliario(planoGenerado);
    }

    @Test
    public void deberiaRetornarListaVaciaCuandoNoHayMueblesCargados() {
        // preparacion
        List<Mueble> mueblesVacios = List.of();

        // ejecucion
        Plano planoGenerado = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(
                ambiente,
                mueblesVacios
        );

        //validacion
        thenDevuelveListaVaciaDeMuebles(planoGenerado);
    }

    @Test
    public void deberiaExcluirMueblesDelResultadoCuandoElMuebleExcedeElEspacioDisponible() {
        // preparacion
        Ambiente ambiente = new Ambiente(2.0, 2.0);
        Mueble muebleGrande = new Mueble("Sofa", 3.0, 2.0);

        //ejecucion
        Plano plano = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(
                ambiente,
                List.of(muebleGrande)
        );

        // validacion
        thenExcluyeMuebleDelResultado(plano);
    }

    @Test
    public void deberiaRegistrarMuebleComoExcluidoCuandoElMuebleExcedeElEspacioDisponible() {
        // preparacion
        Ambiente ambientePequeno = new Ambiente(2.0, 2.0);
        Mueble muebleGrande = new Mueble("Sofa", 3.0, 2.0);

        // ejecucion
        Plano plano = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(
                ambientePequeno,
                List.of(muebleGrande)
        );

        // validacion
        thenElMuebleEstaEnLaListaDeExcluidos(muebleGrande, plano);
    }

    @Test
    public void deberiaIncluirMotivoDeExclusionCuandoElMuebleExcedeElEspacioDisponible() {
        // preparacion
        Ambiente ambientePequeno = new Ambiente(2.0, 2.0);
        Mueble muebleGrande = new Mueble("Sofa", 3.0, 2.0);

        // ejecucion
        Plano plano = whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(
                ambientePequeno,
                List.of(muebleGrande)
        );

        // validacion
        thenElMotivoDeExclusionDiceQueElMuebleNoEntra(plano);
    }

    private Plano whenCargoDimensionesYMueblesDelAmbienteGeneraUnPlano(
            Ambiente ambiente,
            List<Mueble> muebles
    ) {
        return servicioPlano2D.generarPlano(ambiente, muebles);
    }

    private Boolean whenVerificoSiElMuebleEstaEnElPerimetro(
            MuebleUbicado muebleUbicado,
            Ambiente ambiente
    ) {
        return servicioPlano2D.estaDentroDelPerimetro(muebleUbicado, ambiente);
    }

    private List<Boolean> whenVerificoSiCadaMuebleEstaEnElPerimetro(Plano plano) {
        List<Boolean> resultados = new ArrayList<>();
        for (MuebleUbicado muebleUbicado : plano.getMuebles()) {
            resultados.add(servicioPlano2D.estaDentroDelPerimetro(muebleUbicado, plano.getAmbiente()));
        }
        return resultados;
    }

    private void thenElAmbienteDelPlanoEs(Plano plano, Ambiente ambienteEsperado) {
        assertEquals(ambienteEsperado, plano.getAmbiente());
    }

    private void thenLaCantidadDeMueblesDelPlanoEs(Plano plano, int cantidadEsperada) {
        assertEquals(cantidadEsperada, plano.getMuebles().size());
    }

    private void thenElResultadoDeEstarDentroDelPerimetroEs(
            Boolean resultadoEsperado,
            Boolean elMuebleEstaDentroDelPerimetro
    ) {
        assertEquals(resultadoEsperado, elMuebleEstaDentroDelPerimetro);
    }

    private void thenLasPosicionesDeAmbosPlanosSonIguales(Plano primerPlano, Plano segundoPlano) {
        assertEquals(posicionesDe(primerPlano), posicionesDe(segundoPlano));
    }

    private List<List<Double>> posicionesDe(Plano plano) {
        return plano
                .getMuebles()
                .stream()
                .map(muebleUbicado -> List.of(muebleUbicado.getPosicionX(), muebleUbicado.getPosicionY()))
                .toList();
    }

    private void thenTodosLosMueblesEstanDentroDelPerimetro(List<Boolean> resultadosDePerimetro) {
        assertEquals(List.of(true, true), resultadosDePerimetro);
    }

    private void thenElUltimoMuebleEstaEnLaPosicion(List<Double> posicionEsperada, Plano plano) {
        List<MuebleUbicado> muebles = plano.getMuebles();
        MuebleUbicado ultimo = muebles.get(muebles.size() - 1);
        assertEquals(posicionEsperada, List.of(ultimo.getPosicionX(), ultimo.getPosicionY()));
    }

    private void thenElPrimerMuebleDelPlanoEs(Mueble muebleEsperado, Plano plano) {
        assertEquals(muebleEsperado, plano.getMuebles().get(0).getMueble());
    }

    private void thenLosMueblesTienenPosicionesDistintas(Plano plano) {
        MuebleUbicado primero = plano.getMuebles().get(0);
        MuebleUbicado segundo = plano.getMuebles().get(1);
        assertNotEquals(
                List.of(primero.getPosicionX(), primero.getPosicionY()),
                List.of(segundo.getPosicionX(), segundo.getPosicionY())
        );
    }

    private void thenDevuelveMensajeSinMobiliario(Plano planoGenerado) {
        assertEquals("No hay mobiliario para distribuir", planoGenerado.getMensaje());
    }

    private void thenDevuelveListaVaciaDeMuebles(Plano planoGenerado) {
        assertTrue(planoGenerado.getMuebles().isEmpty());
    }

    private void thenElMotivoDeExclusionDiceQueElMuebleNoEntra(Plano plano) {
        assertEquals(List.of("El mueble Sofa no entra en el ambiente"), plano.getMotivosDeExclusion());
    }

    private void thenElMuebleEstaEnLaListaDeExcluidos(Mueble mueble, Plano plano) {
        assertEquals(List.of(mueble), plano.getMueblesExcluidos());
    }

    private void thenExcluyeMuebleDelResultado(Plano plano) {
        assertTrue(plano.getMuebles().isEmpty());
    }
}
