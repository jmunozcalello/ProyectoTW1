package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.dominio.excepcion.PresupuestoNuloException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioCatalogoTest {

  private ServicioCatalogo servicioCatalogo;
  private RepositorioCatalogo repositorioCatalogoMock;

  @BeforeEach
  public void init() {
    this.repositorioCatalogoMock = mock(RepositorioCatalogo.class);
    this.servicioCatalogo = new ServicioCatalogoImpl(this.repositorioCatalogoMock);
  }

  @Test
  public void dadoUnMuebleConPrecioMayorAlPresupuesto_cuandoSeFiltraPorPrecio_entoncesLaListaResultanteEstaVacia()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 10.0;

    Mueble mueble = new Mueble();
    mueble.setPrecio(20.0);

    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(List.of(mueble));

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);

    Assertions.assertTrue(muebles.isEmpty());
  }

  @Test
  public void dadoUnCatalogoVacio_cuandoSeFiltraPorPrecio_entoncesLaListaResultanteEstaVacia()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(List.of());

    List<Mueble> catalogo = servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(1000.0);

    Assertions.assertTrue(catalogo.isEmpty());
  }

  @Test
  public void dadoUnMuebleConPrecioIgualAlPresupuestoMaximo_cuandoSeFiltraPorPrecio_entoncesElMuebleSeIncluyeEnElResultado()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 500.0;

    Mueble mueble = new Mueble();
    mueble.setPrecio(500.0);

    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(List.of(mueble));

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);

    Assertions.assertTrue(muebles.contains(mueble));
    Assertions.assertEquals(1, muebles.size());
    Assertions.assertEquals(precioMaximo, muebles.get(0).getPrecio());
  }

  @Test
  public void dadoUnMuebleConPrecioSuperiorAlPresupuestoMaximo_cuandoSeFiltraPorPrecio_entoncesElMuebleNoSeIncluyeEnElResultado()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 499.99;

    Mueble mueble = new Mueble();
    mueble.setPrecio(500.0);

    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(List.of(mueble));

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);

    Assertions.assertFalse(muebles.contains(mueble));
  }

  @Test
  public void dadoUnCatalogoSinMueblesDelEstiloBuscado_cuandoSeFiltraPorEstilo_entoncesLaListaResultanteEstaVacia() {
    Estilo estilo = Estilo.Retro;
    when(repositorioCatalogoMock.ObtenerTodosLosMuebles()).thenReturn(List.of());
    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesDeEstilo(estilo);
    Assertions.assertTrue(muebles.isEmpty());
  }

  @Test
  public void dadoVariosMueblesDeLaMismaCategoria_cuandoSeBuscaElMasBaratoDeLaCategoria_entoncesDevuelveElDeMenorPrecio() {
    Estilo categoria = Estilo.Retro;

    Mueble muebleMasBarato = new Mueble();
    muebleMasBarato.setPrecio(100.0);
    muebleMasBarato.SetEstilo(Estilo.Retro);

    Mueble muebleMasCaro = new Mueble();
    muebleMasCaro.setPrecio(500.0);
    muebleMasCaro.SetEstilo(Estilo.Retro);

    when(repositorioCatalogoMock.ObtenerTodosLosMuebles())
      .thenReturn(List.of(muebleMasBarato, muebleMasCaro));

    Mueble resultado = servicioCatalogo.ObtenerMuebleMasBaratoDeCategoria(categoria);

    Assertions.assertEquals(100.0, resultado.getPrecio());
  }

  @Test
  public void dadoMueblesDeDistintosEstilos_cuandoSeFiltraPorVariosEstilos_entoncesDevuelveSoloLosMueblesDeEsosEstilos() {
    List<Estilo> estilos = List.of(Estilo.Retro, Estilo.Minimalista);

    Mueble muebleRetro = new Mueble();
    Mueble muebleMinimalista = new Mueble();
    Mueble muebleOtroEstilo = new Mueble();

    muebleRetro.SetEstilo(Estilo.Retro);
    muebleMinimalista.SetEstilo(Estilo.Minimalista);
    muebleOtroEstilo.SetEstilo(Estilo.Japandi);

    when(this.repositorioCatalogoMock.ObtenerTodosLosMuebles())
      .thenReturn(List.of(muebleRetro, muebleMinimalista, muebleOtroEstilo));

    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesDeEstilos(estilos);

    Assertions.assertEquals(
      2,
      muebles.size(),
      "La lista de muebles debería contener los muebles de las categorías especificadas."
    );

    Assertions.assertTrue(
      muebles.contains(muebleRetro),
      "La lista de muebles debería contener el mueble de estilo Retro."
    );

    Assertions.assertTrue(
      muebles.contains(muebleMinimalista),
      "La lista de muebles debería contener el mueble de estilo Minimalista."
    );
  }

  @Test
  public void dadoUnPresupuestoNegativo_cuandoSeFiltraPorPrecio_entoncesLanzaPresupuestoNegativoException() {
    Double precioMaximo = -100.0;

    PresupuestoNegativoException exception = assertThrows(
      PresupuestoNegativoException.class,
      () -> this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo)
    );

    Assertions.assertNotNull(exception);
  }

  @Test
  public void dadoUnPresupuestoNulo_cuandoSeFiltraPorPrecio_entoncesLanzaPresupuestoNuloException() {
    Double precioMaximo = null;

    PresupuestoNuloException exception = assertThrows(
      PresupuestoNuloException.class,
      () -> this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo)
    );

    Assertions.assertNotNull(exception);
  }

  @Test
  public void dadoMueblesConDistintosPrecios_cuandoSeOrdenanAscendentemente_entoncesQuedanDeMenorAMayorPrecio() {
    Mueble mueble1 = new Mueble();
    mueble1.setPrecio(300.0);

    Mueble mueble2 = new Mueble();
    mueble2.setPrecio(200.0);

    Mueble mueble3 = new Mueble();
    mueble3.setPrecio(400.0);

    when(repositorioCatalogoMock.ObtenerTodosLosMuebles())
      .thenReturn(List.of(mueble1, mueble2, mueble3));

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesOrdenadosPorPrecioAsc();

    Assertions.assertEquals(3, muebles.size());

    Assertions.assertEquals(mueble2, muebles.get(0));
    Assertions.assertEquals(mueble1, muebles.get(1));
    Assertions.assertEquals(mueble3, muebles.get(2));
  }

  @Test
  public void dadoMueblesConDistintosPrecios_cuandoSeOrdenanDescendentemente_entoncesQuedanDeMayorAMenorPrecio() {
    Mueble mueble1 = new Mueble();
    mueble1.setPrecio(300.0);

    Mueble mueble2 = new Mueble();
    mueble2.setPrecio(200.0);

    Mueble mueble3 = new Mueble();
    mueble3.setPrecio(400.0);

    when(repositorioCatalogoMock.ObtenerTodosLosMuebles())
      .thenReturn(List.of(mueble1, mueble2, mueble3));

    List<Mueble> muebles = servicioCatalogo.ObtenerMueblesOrdenadosPorPrecioDesc();

    Assertions.assertEquals(3, muebles.size());

    Assertions.assertEquals(mueble3, muebles.get(0));
    Assertions.assertEquals(mueble1, muebles.get(1));
    Assertions.assertEquals(mueble2, muebles.get(2));
  }
}
