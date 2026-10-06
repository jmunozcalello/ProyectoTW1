package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.*;

import com.tallerwebi.dominio.excepcion.MuebleNoEncontrado;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
Criterios de aceptación:
-Dado un mueble en el inventario, cuando lo edito y guardo, entonces los cambios se reflejan en la tabla y en el plano si ya estaba ubicado.
-Dado un mueble que elimino, cuando confirmo la acción, entonces desaparece del inventario y de cualquier distribución guardada.

*/

public class ServicioEdicionInventarioMueblesTest {

  private static final int ID_INEXISTENTE = 999;
  private static final int ID_MUEBLE_A_ELIMINAR = 1;
  private static final int ID_MUEBLE_QUE_QUEDA = 2;

  private List<Mueble> inventario;
  private ServicioEdicionInventarioMuebles servicioEdicionInventarioMuebles;

  @BeforeEach
  public void init() {
    this.inventario = new ArrayList<>();
    this.servicioEdicionInventarioMuebles =
      new ServicioEdicionInventarioMueblesImpl(this.inventario);
  }

  @Test
  public void deberiaActualizarElNombreCuandoElMuebleExisteYElNuevoNombreEsValido()
    throws MuebleNoEncontrado {
    // Given
    String nombreNuevo = "Mesa de comedor";
    Mueble mueble = givenMuebleExistenteEnElInventario();
    Mueble datosNuevos = new Mueble(nombreNuevo, 1.0, 0.8);

    // When
    whenActualizoElMueble(mueble, datosNuevos);

    // Then
    thenElNombreSeActualiza(mueble, nombreNuevo);
  }

  @Test
  public void deberiaActualizarElAnchoCuandoElMuebleExisteYElNuevoAnchoEsValido()
    throws MuebleNoEncontrado {
    // Given
    Double anchoNuevo = 1.5;
    Mueble mueble = givenMuebleExistenteConNuevasDimensionesValidas(1.0, 0.8);
    Mueble datosNuevos = new Mueble(mueble.getNombre(), anchoNuevo, mueble.getLargo());

    // When
    whenActualizoElMueble(mueble, datosNuevos);

    // Then
    thenElAnchoSeActualiza(mueble, anchoNuevo);
  }

  @Test
  public void deberiaActualizarElLargoCuandoElMuebleExisteYElNuevoLargoEsValido()
    throws MuebleNoEncontrado {
    // Given
    Double largoNuevo = 1.2;
    Mueble mueble = givenMuebleExistenteConNuevasDimensionesValidas(1.0, 0.8);
    Mueble datosNuevos = new Mueble(mueble.getNombre(), mueble.getAncho(), largoNuevo);

    // When
    whenActualizoElMueble(mueble, datosNuevos);

    // Then
    thenElLargoSeActualiza(mueble, largoNuevo);
  }

  @Test
  public void deberiaActualizarLaDescripcionCuandoElMuebleExisteYLaNuevaDescripcionEsValida()
    throws MuebleNoEncontrado {
    // Given
    String descripcionNueva = "Mesa de comedor de madera maciza";
    Mueble mueble = givenMuebleExistenteConUnaDescripcion("Mesa de madera");
    // datosNuevos llega con el resto de los campos tal como estaban, como un formulario
    // de edicion donde el usuario solo toco la descripcion
    Mueble datosNuevos = new Mueble(mueble.getNombre(), mueble.getAncho(), mueble.getLargo());
    datosNuevos.setDescripcion(descripcionNueva);

    // When
    whenActualizoElMueble(mueble, datosNuevos);

    // Then
    thenLaDescripcionSeActualiza(mueble, descripcionNueva);
  }

  @Test
  public void deberiaActualizarTodosLosDatosCuandoElMuebleExisteYLosDatosSonValidos()
    throws MuebleNoEncontrado {
    // Given
    Mueble mueble = givenMuebleExistenteEnElInventario();
    Mueble datosNuevos = givenTodosLosDatosNuevosValidos();

    // When
    whenActualizoElMueble(mueble, datosNuevos);

    // Then
    thenTodosLosDatosSeActualizan(mueble, datosNuevos);
  }

  @Test
  public void deberiaLanzarMuebleNoEncontradoCuandoElMuebleNoExisteEnElInventario() {
    // Given
    Mueble datosNuevos = givenTodosLosDatosNuevosValidos();

    assertThrows(
      MuebleNoEncontrado.class,
      () -> this.servicioEdicionInventarioMuebles.actualizar(ID_INEXISTENTE, datosNuevos)
    );
  }

  @Test
  public void deberiaEliminarElMuebleCuandoElMuebleExisteEnElInventario()
    throws MuebleNoEncontrado {
    // Given
    Mueble muebleAEliminar = givenInventarioConDosMuebles();

    // When
    whenEliminoElMueble(muebleAEliminar);

    // Then
    thenElInventarioNoContieneElId(ID_MUEBLE_A_ELIMINAR);
  }

  @Test
  public void deberiaConservarLosDemasMueblesCuandoEliminoUnoDelInventario()
    throws MuebleNoEncontrado {
    // Given
    Mueble muebleAEliminar = givenInventarioConDosMuebles();

    // When
    whenEliminoElMueble(muebleAEliminar);

    // Then
    thenElInventarioContieneElId(ID_MUEBLE_QUE_QUEDA);
  }

  @Test
  public void deberiaLanzarMuebleNoEncontradoCuandoIntentoEliminarUnMuebleInexistente() {
    // Given
    // El inventario tiene dos muebles: elimino un id que no esta, con inventario
    // poblado, para que el test no dependa de que el caso sea un inventario vacio.
    givenInventarioConDosMuebles();

    assertThrows(
      MuebleNoEncontrado.class,
      () -> this.servicioEdicionInventarioMuebles.eliminar(ID_INEXISTENTE)
    );
  }

  private Mueble givenMuebleExistenteEnElInventario() {
    Mueble mueble = new Mueble("Mesa", 1.0, 0.8);
    mueble.setId(1);
    this.inventario.add(mueble);
    return mueble;
  }

  private Mueble givenMuebleExistenteConUnaDescripcion(String descripcion) {
    Mueble mueble = new Mueble("Mesa", 1.0, 0.8);
    mueble.setId(1);
    mueble.setDescripcion(descripcion);
    this.inventario.add(mueble);
    return mueble;
  }

  // Los seis campos editables llegan distintos de los que tiene el mueble ya sembrado
  // (Mesa, 1.0, 0.8, sin descripcion, sin precio y sin estilo), asi que si actualizar
  // deja de copiar alguno, el assert lo detecta.
  private Mueble givenTodosLosDatosNuevosValidos() {
    Mueble datosNuevos = new Mueble("Sillon de dos cuerpos", 2.4, 1.1);
    datosNuevos.setDescripcion("Tapizado de lino gris");
    datosNuevos.setPrecio(850.0);
    datosNuevos.setEstilo(Estilo.Japandi);
    return datosNuevos;
  }

  // Se siembran dos muebles para que una implementacion que vacie el inventario entero
  // en vez de borrar uno solo . Los ids salen de las constantes
  // de clase para que no puedan desincronizarse entre el Given y los asserts.
  private Mueble givenInventarioConDosMuebles() {
    Mueble muebleAEliminar = new Mueble("Mesa", 1.0, 0.8);
    muebleAEliminar.setId(ID_MUEBLE_A_ELIMINAR);
    this.inventario.add(muebleAEliminar);
    Mueble muebleQueQueda = new Mueble("Silla", 0.5, 0.5);
    muebleQueQueda.setId(ID_MUEBLE_QUE_QUEDA);
    this.inventario.add(muebleQueQueda);
    return muebleAEliminar;
  }

  private void whenEliminoElMueble(Mueble mueble) throws MuebleNoEncontrado {
    this.servicioEdicionInventarioMuebles.eliminar(mueble.getId());
  }

  private void thenElInventarioNoContieneElId(int id) {
    assertFalse(
      this.inventario.stream().anyMatch(mueble -> mueble.getId() == id),
      "el mueble con id " + id + " deberia haberse eliminado del inventario"
    );
  }

  private void thenElInventarioContieneElId(int id) {
    assertTrue(
      this.inventario.stream().anyMatch(mueble -> mueble.getId() == id),
      "el mueble con id " + id + " deberia seguir en el inventario"
    );
  }

  // El ancho y el largo del mueble ya sembrado son los datos de partida;
  // lo que cambia en cada test es el valor que se manda a actualizar.
  private Mueble givenMuebleExistenteConNuevasDimensionesValidas(Double ancho, Double largo) {
    Mueble mueble = new Mueble("Mesa", ancho, largo);
    mueble.setId(1);
    this.inventario.add(mueble);
    return mueble;
  }

  private void whenActualizoElMueble(Mueble mueble, Mueble datosNuevos) throws MuebleNoEncontrado {
    this.servicioEdicionInventarioMuebles.actualizar(mueble.getId(), datosNuevos);
  }

  private void thenElNombreSeActualiza(Mueble mueble, String nombreEsperado) {
    assertEquals(nombreEsperado, mueble.getNombre());
  }

  private void thenElAnchoSeActualiza(Mueble mueble, Double anchoEsperado) {
    assertEquals(anchoEsperado, mueble.getAncho());
  }

  private void thenElLargoSeActualiza(Mueble mueble, Double largoEsperado) {
    assertEquals(largoEsperado, mueble.getLargo());
  }

  private void thenLaDescripcionSeActualiza(Mueble mueble, String descripcionEsperada) {
    assertEquals(descripcionEsperada, mueble.getDescripcion());
  }

  private void thenTodosLosDatosSeActualizan(Mueble mueble, Mueble esperado) {
    assertEquals(esperado, mueble);
  }
}
