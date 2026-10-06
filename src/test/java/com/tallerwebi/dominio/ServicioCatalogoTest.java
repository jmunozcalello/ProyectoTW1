package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.MuebleNoEncontrado;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.dominio.excepcion.PresupuestoNuloException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioCatalogoTest {

  private ServicioCatalogo servicioCatalogo;
  private RepositorioCatalogo repositorioCatalogoMock;
  private List<Mueble> mueblesSembrados;

  @BeforeEach
  public void init() {
    this.repositorioCatalogoMock = mock(RepositorioCatalogo.class);
    this.servicioCatalogo = new ServicioCatalogoImpl(this.repositorioCatalogoMock);
    this.mueblesSembrados = new ArrayList<>();
  }

  @Test
  public void dadoUnMuebleConPrecioMayorAlPresupuesto_cuandoSeFiltraPorPrecio_entoncesLaListaResultanteEstaVacia()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 10.0;

    Mueble mueble = new Mueble();
    mueble.setPrecio(20.0);

    List<Mueble> catalogo = new ArrayList<>();
    catalogo.add(mueble);
    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(catalogo);

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);

    assertTrue(muebles.isEmpty());
  }

  @Test
  public void dadoUnCatalogoVacio_cuandoSeFiltraPorPrecio_entoncesLaListaResultanteEstaVacia()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(new ArrayList<>());

    List<Mueble> catalogo = servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(1000.0);

    assertTrue(catalogo.isEmpty());
  }

  @Test
  public void dadoUnMuebleConPrecioIgualAlPresupuestoMaximo_cuandoSeFiltraPorPrecio_entoncesElMuebleSeIncluyeEnElResultado()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 500.0;

    Mueble mueble = new Mueble();
    mueble.setPrecio(500.0);

    List<Mueble> catalogo = new ArrayList<>();
    catalogo.add(mueble);
    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(catalogo);

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);

    assertTrue(muebles.contains(mueble));
    assertEquals(1, muebles.size());
    assertEquals(precioMaximo, muebles.get(0).getPrecio());
  }

  @Test
  public void dadoUnMuebleConPrecioSuperiorAlPresupuestoMaximo_cuandoSeFiltraPorPrecio_entoncesElMuebleNoSeIncluyeEnElResultado()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 499.99;

    Mueble mueble = new Mueble();
    mueble.setPrecio(500.0);

    List<Mueble> catalogo = new ArrayList<>();
    catalogo.add(mueble);
    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(catalogo);

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);

    assertFalse(muebles.contains(mueble));
  }

  @Test
  public void dadoUnCatalogoSinMueblesDelEstiloBuscado_cuandoSeFiltraPorEstilo_entoncesLaListaResultanteEstaVacia() {
    Estilo estilo = Estilo.Retro;
    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(new ArrayList<>());
    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesDeEstilo(estilo);
    assertTrue(muebles.isEmpty());
  }

  @Test
  public void dadoVariosMueblesDeLaMismaCategoria_cuandoSeBuscaElMasBaratoDeLaCategoria_entoncesDevuelveElDeMenorPrecio() {
    Estilo categoria = Estilo.Retro;

    Mueble muebleMasBarato = new Mueble();
    muebleMasBarato.setPrecio(100.0);
    muebleMasBarato.setEstilo(Estilo.Retro);

    Mueble muebleMasCaro = new Mueble();
    muebleMasCaro.setPrecio(500.0);
    muebleMasCaro.setEstilo(Estilo.Retro);

    List<Mueble> catalogo = new ArrayList<>();
    catalogo.add(muebleMasBarato);
    catalogo.add(muebleMasCaro);

    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(catalogo);

    Mueble resultado = servicioCatalogo.ObtenerMuebleMasBaratoDeCategoria(categoria);

    assertEquals(100.0, resultado.getPrecio());
  }

  @Test
  public void dadoMueblesDeDistintosEstilos_cuandoSeFiltraPorVariosEstilos_entoncesDevuelveSoloLosMueblesDeEsosEstilos() {
    List<Estilo> estilos = new ArrayList<>();
    estilos.add(Estilo.Retro);
    estilos.add(Estilo.Minimalista);

    Mueble muebleRetro = new Mueble();
    Mueble muebleMinimalista = new Mueble();
    Mueble muebleOtroEstilo = new Mueble();

    muebleRetro.setEstilo(Estilo.Retro);
    muebleMinimalista.setEstilo(Estilo.Minimalista);
    muebleOtroEstilo.setEstilo(Estilo.Japandi);

    List<Mueble> catalogo = new ArrayList<>();
    catalogo.add(muebleRetro);
    catalogo.add(muebleMinimalista);
    catalogo.add(muebleOtroEstilo);

    when(this.repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(catalogo);

    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesDeEstilos(estilos);

    assertEquals(2, muebles.size());

    assertTrue(muebles.contains(muebleRetro));

    assertTrue(muebles.contains(muebleMinimalista));
  }

  @Test
  public void dadoUnPresupuestoNegativo_cuandoSeFiltraPorPrecio_entoncesLanzaPresupuestoNegativoException() {
    Double precioMaximo = -100.0;

    PresupuestoNegativoException exception = assertThrows(
      PresupuestoNegativoException.class,
      () -> this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo)
    );

    assertNotNull(exception);
  }

  @Test
  public void dadoUnPresupuestoNulo_cuandoSeFiltraPorPrecio_entoncesLanzaPresupuestoNuloException() {
    Double precioMaximo = null;

    PresupuestoNuloException exception = assertThrows(
      PresupuestoNuloException.class,
      () -> this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo)
    );

    assertNotNull(exception);
  }

  @Test
  public void dadoMueblesConDistintosPrecios_cuandoSeOrdenanDescendentemente_entoncesQuedanDeMayorAMenorPrecio() {
    Mueble mueble1 = new Mueble();
    mueble1.setPrecio(300.0);

    Mueble mueble2 = new Mueble();
    mueble2.setPrecio(200.0);

    Mueble mueble3 = new Mueble();
    mueble3.setPrecio(400.0);

    List<Mueble> catalogo = new ArrayList<>();
    catalogo.add(mueble1);
    catalogo.add(mueble2);
    catalogo.add(mueble3);

    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(catalogo);

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesOrdenadosPorPrecioDesc();

    assertEquals(3, muebles.size());
    assertEquals(mueble3, muebles.get(0));
    assertEquals(mueble1, muebles.get(1));
    assertEquals(mueble2, muebles.get(2));
  }

  @Test
  public void dadoQueNoSeFiltraPorEstiloNiPorPrecio_cuandoBuscoMueblesQueCumplan_entoncesDevuelveTodoElCatalogo()
    throws PresupuestoNegativoException {
    Mueble retro = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    Mueble japandi = this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);

    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesQueCumplan(null, null, null);

    assertThat(muebles, containsInAnyOrder(retro, japandi));
  }

  @Test
  public void dadoSoloElFiltroDePrecio_cuandoBuscoMueblesQueCumplan_entoncesDevuelveLosQueNoSuperanElMaximo()
    throws PresupuestoNegativoException {
    Mueble barato = this.dadoQueElCatalogoTieneUnMueble("Lámpara", 25000.0, Estilo.Retro);
    Mueble caro = this.dadoQueElCatalogoTieneUnMueble("Cama", 180000.0, Estilo.Boho);

    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesQueCumplan(null, null, 50000.0);

    assertThat(muebles, contains(barato));
    assertEquals(muebles.size(), 1);
  }

  @Test
  public void dadoQueElPrecioDeUnMuebleEsIgualAlMaximo_cuandoBuscoMueblesQueCumplan_entoncesElMuebleSeIncluye()
    throws PresupuestoNegativoException {
    Mueble limite = this.dadoQueElCatalogoTieneUnMueble("Lámpara", 25000.0, Estilo.Retro);

    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesQueCumplan(null, null, 25000.0);

    assertThat(muebles, contains(limite));
  }

  @Test
  public void dadoSoloElFiltroDeEstilo_cuandoBuscoMueblesQueCumplan_entoncesDevuelveLosDeEseEstilo()
    throws PresupuestoNegativoException {
    Mueble retro = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    Mueble otroRetro = this.dadoQueElCatalogoTieneUnMueble("Lámpara", 25000.0, Estilo.Retro);
    Mueble japandi = this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);

    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesQueCumplan(null, Estilo.Retro, null);

    assertThat(muebles, containsInAnyOrder(retro, otroRetro));
    assertEquals(muebles.size(), 2);
  }

  @Test
  public void dadoElFiltroDeEstiloYPrecio_cuandoBuscoMueblesQueCumplan_entoncesDevuelveSoloLosQueCumplenAmbos()
    throws PresupuestoNegativoException {
    Mueble retroBarato = this.dadoQueElCatalogoTieneUnMueble("Lámpara", 25000.0, Estilo.Retro);
    this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 60000.0, Estilo.Japandi);

    List<Mueble> muebles =
      this.servicioCatalogo.ObtenerMueblesQueCumplan(null, Estilo.Retro, 50000.0);

    assertThat(muebles, contains(retroBarato));
  }

  @Test
  public void dadoQueNingunMuebleEsDelEstiloBuscado_cuandoBuscoMueblesQueCumplan_entoncesDevuelveUnaListaVacia()
    throws PresupuestoNegativoException {
    this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);

    List<Mueble> muebles =
      this.servicioCatalogo.ObtenerMueblesQueCumplan(null, Estilo.Escandinavo, null);

    assertTrue(muebles.isEmpty());
  }

  @Test
  public void dadoQueFiltroPorTipoDeAmbiente_cuandoBuscoMueblesQueCumplan_entoncesDevuelveSoloLosDeEseTipo()
    throws PresupuestoNegativoException {
    Mueble paraLiving = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    paraLiving.agregarTipoDeAmbiente(new TipoDeAmbiente("Living"));
    Mueble paraComedor =
      this.dadoQueElCatalogoTieneUnMueble("Mesa de Comedor", 60000.0, Estilo.Japandi);
    paraComedor.agregarTipoDeAmbiente(new TipoDeAmbiente("Comedor"));
    this.dadoQueElCatalogoTieneUnMueble("Lámpara", 25000.0, Estilo.Retro);

    List<Mueble> muebles =
      this.servicioCatalogo.ObtenerMueblesQueCumplan(new TipoDeAmbiente("Living"), null, null);

    assertThat(muebles, contains(paraLiving));
  }

  @Test
  public void dadoQueUnMuebleSirveParaVariosTipos_cuandoBuscoPorUnoDeEllos_entoncesElMuebleSeIncluye()
    throws PresupuestoNegativoException {
    Mueble multiservicio =
      this.dadoQueElCatalogoTieneUnMueble("Escritorio", 40000.0, Estilo.Minimalista);
    multiservicio.agregarTipoDeAmbiente(new TipoDeAmbiente("Pasillo"));
    multiservicio.agregarTipoDeAmbiente(new TipoDeAmbiente("Habitación"));

    List<Mueble> muebles =
      this.servicioCatalogo.ObtenerMueblesQueCumplan(new TipoDeAmbiente("Habitación"), null, null);

    assertThat(muebles, contains(multiservicio));
  }

  @Test
  public void dadoUnPrecioMinimo_cuandoBuscoMueblesFiltrados_entoncesExcluyeLosQueNoLoAlcanzan()
    throws PresupuestoNegativoException {
    this.dadoQueElCatalogoTieneUnMueble("Lámpara", 25000.0, Estilo.Retro);
    Mueble caro = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);

    List<Mueble> muebles =
      this.servicioCatalogo.ObtenerMueblesFiltrados(50000.0, null, null, null, null);

    assertThat(muebles, contains(caro));
  }

  @Test
  public void dadoVariosCriterios_cuandoBuscoMueblesFiltrados_entoncesDevuelveSoloLosQueCumplenTodos()
    throws PresupuestoNegativoException {
    Mueble cumple = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    cumple.agregarTipoDeAmbiente(new TipoDeAmbiente("Living"));
    this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);
    Mueble fueraDeRango = this.dadoQueElCatalogoTieneUnMueble("Lámpara", 25000.0, Estilo.Retro);
    fueraDeRango.agregarTipoDeAmbiente(new TipoDeAmbiente("Living"));

    List<Mueble> muebles =
      this.servicioCatalogo.ObtenerMueblesFiltrados(
          50000.0,
          100000.0,
          Estilo.Retro,
          new TipoDeAmbiente("Living"),
          null
        );

    assertThat(muebles, contains(cumple));
  }

  @Test
  public void dadoQueOrdenoPorPrecioAscendente_cuandoBuscoMueblesFiltrados_entoncesQuedanDeMenorAMayor()
    throws PresupuestoNegativoException {
    Mueble trescientos = this.dadoQueElCatalogoTieneUnMueble("A", 300.0, Estilo.Retro);
    Mueble doscientos = this.dadoQueElCatalogoTieneUnMueble("B", 200.0, Estilo.Retro);
    Mueble cuatrocientos = this.dadoQueElCatalogoTieneUnMueble("C", 400.0, Estilo.Retro);

    List<Mueble> muebles =
      this.servicioCatalogo.ObtenerMueblesFiltrados(
          null,
          null,
          null,
          null,
          OrdenDeMuebles.PRECIO_ASC
        );

    assertEquals(3, muebles.size());
    assertEquals(doscientos, muebles.get(0));
    assertEquals(trescientos, muebles.get(1));
    assertEquals(cuatrocientos, muebles.get(2));
  }

  @Test
  public void dadoQueOrdenoPorPrecioDescendente_cuandoBuscoMueblesFiltrados_entoncesQuedanDeMayorAMenor()
    throws PresupuestoNegativoException {
    Mueble doscientos = this.dadoQueElCatalogoTieneUnMueble("A", 200.0, Estilo.Retro);
    Mueble cuatrocientos = this.dadoQueElCatalogoTieneUnMueble("B", 400.0, Estilo.Retro);
    Mueble trescientos = this.dadoQueElCatalogoTieneUnMueble("C", 300.0, Estilo.Retro);

    List<Mueble> muebles =
      this.servicioCatalogo.ObtenerMueblesFiltrados(
          null,
          null,
          null,
          null,
          OrdenDeMuebles.PRECIO_DESC
        );

    assertEquals(3, muebles.size());
    assertEquals(cuatrocientos, muebles.get(0));
    assertEquals(trescientos, muebles.get(1));
    assertEquals(doscientos, muebles.get(2));
  }

  @Test
  public void dadoUnPrecioMinimoNegativo_cuandoBuscoMueblesFiltrados_entoncesLanzaPresupuestoNegativoException() {
    PresupuestoNegativoException exception = assertThrows(
      PresupuestoNegativoException.class,
      () -> this.servicioCatalogo.ObtenerMueblesFiltrados(-1.0, null, null, null, null)
    );

    assertNotNull(exception);
  }

  @Test
  public void dadoUnPrecioMaximoNegativo_cuandoBuscoMueblesQueCumplan_entoncesLanzaPresupuestoNegativoException() {
    Double precioMaximo = -1.0;

    PresupuestoNegativoException exception = assertThrows(
      PresupuestoNegativoException.class,
      () -> this.servicioCatalogo.ObtenerMueblesQueCumplan(null, Estilo.Retro, precioMaximo)
    );

    assertNotNull(exception);
  }

  @Test
  public void dadoUnMuebleEnElCatalogo_cuandoLoBuscoPorId_entoncesDevuelvoEseMueble()
    throws MuebleNoEncontrado {
    Mueble retro = this.dadoQueElCatalogoTieneUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    retro.setId(7);
    this.dadoQueElCatalogoTieneUnMueble("Mesa Japandi", 150000.0, Estilo.Japandi);
    when(this.repositorioCatalogoMock.ObtenerMueblePorId(7)).thenReturn(retro);

    Mueble mueble = this.servicioCatalogo.ObtenerMueblePorId(7);

    assertThat(mueble, is(notNullValue()));
    assertThat(mueble.getNombre(), is("Sillón Retró"));
  }

  @Test
  public void dadoQueNoHayNingunMuebleConEseId_cuandoLoBuscoPorId_entoncesLanzaUnaExceptionMuebleNoEncontrado() {
    MuebleNoEncontrado exception = assertThrows(
      MuebleNoEncontrado.class,
      () -> this.servicioCatalogo.ObtenerMueblePorId(999)
    );

    assertNotNull(exception);
  }

  private Mueble dadoQueElCatalogoTieneUnMueble(String nombre, double precio, Estilo estilo) {
    Mueble mueble = new Mueble();
    mueble.setNombre(nombre);
    mueble.setPrecio(precio);
    mueble.setEstilo(estilo);
    this.mueblesSembrados.add(mueble);
    when(this.repositorioCatalogoMock.ObtenerTodosLosMuebles())
      .thenReturn(new ArrayList<>(this.mueblesSembrados));
    return mueble;
  }
}
