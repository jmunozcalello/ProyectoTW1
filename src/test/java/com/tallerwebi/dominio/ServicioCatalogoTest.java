package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.dominio.excepcion.PresupuestoNuloException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioCatalogoTest {

  private ServicioCatalogo servicioCatalogo;

  @BeforeEach
  public void init() {
    List<Mueble> catalogo = new ArrayList<>();
    this.servicioCatalogo = new ServicioCatalogoImpl(catalogo);
  }

  @Test
  public void ObtenerListaVaciaCuandoNingunMuebleCumpleElPresupuesto()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 10.0;
    Mueble mueble = new Mueble();
    mueble.setPrecio(20.0);
    this.servicioCatalogo.RegistrarMueble(mueble);
    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);
    Assertions.assertTrue(
      muebles.isEmpty(),
      "La lista de muebles debería estar vacía cuando ningún mueble cumple con el presupuesto."
    );
  }

  @Test
  public void ObtenerListaVaciaCuandoElCatalogoEstaVacio()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    List<Mueble> catalogo = this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(1000.0);
    Assertions.assertTrue(
      catalogo.isEmpty(),
      "La lista de muebles debería estar vacía cuando no hay muebles que cumplan con el criterio."
    );
  }

  @Test
  public void ObtenerMuebleCuyoPrecioEsExactamenteIgualAlPresupuestoMaximo()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 500.0;
    Mueble mueble = new Mueble();
    mueble.setPrecio(500.0);
    this.servicioCatalogo.RegistrarMueble(mueble);
    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);
    Assertions.assertTrue(
      muebles.contains(mueble),
      "El mueble con precio igual al presupuesto máximo debería incluirse en la lista de resultados."
    );
    Assertions.assertEquals(
      1,
      muebles.size(),
      "La lista de muebles debería contener exactamente un mueble cuando solo uno cumple con el presupuesto."
    );
    Assertions.assertEquals(
      precioMaximo,
      muebles.get(0).getPrecio(),
      "El precio del mueble devuelto debería ser igual al presupuesto máximo."
    );
  }

  @Test
  public void NoObtenerMuebleCuyoPrecioSuperaPorElPresupuestoMaximo()
    throws PresupuestoNegativoException, PresupuestoNuloException {
    Double precioMaximo = 499.99;
    Mueble mueble = new Mueble();
    mueble.setPrecio(500.0);
    this.servicioCatalogo.RegistrarMueble(mueble);
    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo);
    Assertions.assertFalse(
      muebles.contains(mueble),
      "El mueble con precio superior al presupuesto máximo no debería incluirse en la lista de resultados."
    );
  }

  @Test
  public void ObtenerListaVaciaCuandoElCatalogoNoTieneMueblesDeLaCategoriaEspecificada() {
    Estilo estilo = Estilo.Retro;
    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesDeEstilo(estilo);
    Assertions.assertTrue(
      muebles.isEmpty(),
      "La lista de muebles debería estar vacía cuando no hay muebles de la categoría especificada."
    );
  }

  @Test
  public void SugerirMontoMinimoIgualAlPrecioDelMuebleMasBaratoDeLaCategoriaElegida() {
    Estilo categoria = Estilo.Retro;
    Mueble muebleMasBarato = new Mueble();
    muebleMasBarato.setPrecio(100.0);
    Mueble muebleMasCaro = new Mueble();
    muebleMasCaro.setPrecio(500.0);
    this.servicioCatalogo.RegistrarMueble(muebleMasBarato);
    this.servicioCatalogo.RegistrarMueble(muebleMasCaro);
    this.servicioCatalogo.ObtenerMuebleMasBaratoDeCategoria(categoria);
    Assertions.assertEquals(
      100.0,
      muebleMasBarato.getPrecio(),
      "El precio del mueble más barato de la categoría elegida debería ser el monto mínimo sugerido."
    );
  }

  @Test
  public void ObtenerMueblesDeVariasCategoriasCuandoSePasaMasDeUnaCategoria() {
    List<Estilo> estilos = List.of(Estilo.Retro, Estilo.Minimalista);
    Mueble muebleRetro = new Mueble();
    Mueble muebleMinimalista = new Mueble();
    Mueble muebleOtroEstilo = new Mueble();
    muebleRetro.SetEstilo(Estilo.Retro);
    muebleMinimalista.SetEstilo(Estilo.Minimalista);
    muebleOtroEstilo.SetEstilo(Estilo.Japandi);
    this.servicioCatalogo.RegistrarMueble(muebleRetro);
    this.servicioCatalogo.RegistrarMueble(muebleMinimalista);
    this.servicioCatalogo.RegistrarMueble(muebleOtroEstilo);
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
  public void LanzarExcepcionCuandoElPresupuestoMaximoEsNegativo() {
    Double precioMaximo = -100.0;

    PresupuestoNegativoException exception = assertThrows(
      PresupuestoNegativoException.class,
      () -> this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo)
    );

    Assertions.assertNotNull(exception);
  }

  @Test
  public void LanzarExcepcionCuandoElPresupuestoMaximoEsNulo() {
    Double precioMaximo = null;

    PresupuestoNuloException exception = assertThrows(
      PresupuestoNuloException.class,
      () -> this.servicioCatalogo.ObtenerMueblesConUnPrecioMenorAl(precioMaximo)
    );

    Assertions.assertNotNull(exception);
  }

  @Test
  public void ObtenerMueblesOrdenadosDeMenorAMayorPrecio() {
    Mueble mueble1 = new Mueble();
    mueble1.setPrecio(300.0);
    Mueble mueble2 = new Mueble();
    mueble2.setPrecio(200.0);
    Mueble mueble3 = new Mueble();
    mueble3.setPrecio(400.0);
    this.servicioCatalogo.RegistrarMueble(mueble1);
    this.servicioCatalogo.RegistrarMueble(mueble2);
    this.servicioCatalogo.RegistrarMueble(mueble3);

    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesOrdenadosPorPrecioAsc();

    Assertions.assertEquals(
      3,
      muebles.size(),
      "La lista debería contener todos los muebles registrados."
    );
    Assertions.assertEquals(
      mueble2,
      muebles.get(0),
      "El primer mueble en la lista debería ser el de menor precio."
    );
    Assertions.assertEquals(
      mueble1,
      muebles.get(1),
      "El segundo mueble en la lista debería ser el de precio intermedio."
    );
    Assertions.assertEquals(
      mueble3,
      muebles.get(2),
      "El tercer mueble en la lista debería ser el de mayor precio."
    );
  }

  @Test
  public void ObtenerMueblesOrdenadosDeMayorAMenorPrecio() {
    Mueble mueble1 = new Mueble();
    mueble1.setPrecio(300.0);
    Mueble mueble2 = new Mueble();
    mueble2.setPrecio(200.0);
    Mueble mueble3 = new Mueble();
    mueble3.setPrecio(400.0);
    this.servicioCatalogo.RegistrarMueble(mueble1);
    this.servicioCatalogo.RegistrarMueble(mueble2);
    this.servicioCatalogo.RegistrarMueble(mueble3);

    List<Mueble> muebles = this.servicioCatalogo.ObtenerMueblesOrdenadosPorPrecioDesc();

    Assertions.assertEquals(
      3,
      muebles.size(),
      "La lista debería contener todos los muebles registrados."
    );
    Assertions.assertEquals(
      mueble3,
      muebles.get(0),
      "El primer mueble en la lista debería ser el de mayor precio."
    );
    Assertions.assertEquals(
      mueble1,
      muebles.get(1),
      "El segundo mueble en la lista debería ser el de precio intermedio."
    );
    Assertions.assertEquals(
      mueble2,
      muebles.get(2),
      "El tercer mueble en la lista debería ser el de menor precio."
    );
  }
}
