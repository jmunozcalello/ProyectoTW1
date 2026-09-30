package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.MuebleUbicado;
import com.tallerwebi.dominio.Plano;
import com.tallerwebi.dominio.ServicioPlano2D;
import com.tallerwebi.dominio.excepcion.ValidacionException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Criterios de aceptación:
 * Dado que cargué ancho y largo del ambiente, cuando confirmo los datos,
 * entonces el sistema muestra un plano 2D con los muebles principales ubicados sobre los muros perimetrales.
 * Dado que el ambiente no tiene muebles cargados, cuando se genera la distribución,
 * entonces el sistema muestra un mensaje indicando que no hay mobiliario para distribuir.
 * Dado un mueble cuyas dimensiones exceden el espacio disponible en el ambiente, cuando se genera la distribución,
 * entonces el sistema muestra un mensaje indicando que ese mueble no entra y lo excluye de la propuesta.
 * Dado más de un mueble del mismo tipo (ej. dos sillones), cuando se calcula la ubicación,
 * entonces el sistema define un criterio de orden de colocación (ej. por tamaño o por orden de carga) y lo aplica de forma consistente.
 */

public class ControladorRenderizadoPlano2DTest {

  private final Ambiente ambiente = new Ambiente(4.0, 3.0);
  private final List<Mueble> muebles = List.of(
    new Mueble("Silla", 0.5, 0.5),
    new Mueble("Mesa", 1.0, 1.0)
  );

  private ServicioPlano2D servicioPlano2D;
  private ControladorPlano2D controladorPlano2D;

  @BeforeEach
  public void init() {
    servicioPlano2D = mock(ServicioPlano2D.class);
    controladorPlano2D = new ControladorPlano2D(servicioPlano2D);
  }

  @Test
  public void deberiaMostrarLaVistaPlanoInteractivoCuandoElServicioLanzaExcepcionDeValidacion() {
    // preparacion
    doThrow(new ValidacionException("Las dimensiones deben ser mayores a 0"))
      .when(servicioPlano2D)
      .generarPlano(ambiente, muebles);

    // ejecucion
    ModelAndView mav = whenGeneroElPlanoCon(ambiente, muebles);

    // validacion
    thenLaVistaEs("plano-interactivo", mav);
  }

  @Test
  public void deberiaUsarLaMismaVistaCuandoNoHayMueblesCargados() {
    // preparacion
    List<Mueble> sinMuebles = List.of();
    Plano planoVacio = planoConLosMueblesUbicados(sinMuebles);
    planoVacio.setMensaje("No hay mobiliario para distribuir");
    when(servicioPlano2D.generarPlano(ambiente, sinMuebles)).thenReturn(planoVacio);

    // ejecucion
    ModelAndView mav = whenGeneroElPlanoCon(ambiente, sinMuebles);

    // validacion
    thenLaVistaEs("plano-interactivo", mav);
  }

  @Test
  public void deberiaMostrarElMensajeDeErrorCuandoElServicioLanzaExcepcionDeValidacion() {
    // preparacion
    doThrow(new ValidacionException("Las dimensiones deben ser mayores a 0"))
      .when(servicioPlano2D)
      .generarPlano(ambiente, muebles);

    // ejecucion
    ModelAndView mav = whenGeneroElPlanoCon(ambiente, muebles);

    // validacion
    thenElModeloTieneElMensajeDeError("Las dimensiones deben ser mayores a 0", mav);
  }

  @Test
  public void deberiaDelegarLosDatosDelFormularioAlServicio() {
    // preparacion
    List<Mueble> tresMuebles = List.of(
      new Mueble("Silla", 0.5, 0.5),
      new Mueble("Mesa", 1.0, 1.0),
      new Mueble("Sillon", 0.8, 0.8)
    );
    when(servicioPlano2D.generarPlano(ambiente, tresMuebles))
      .thenReturn(planoConLosMueblesUbicados(tresMuebles));

    // ejecucion
    whenGeneroElPlanoCon(ambiente, tresMuebles);

    // validacion
    thenElServicioRecibeLosMismosDatos(ambiente, tresMuebles);
  }

  @Test
  public void deberiaMostrarElFormularioDeAmbienteCuandoSeAccedeAlPaso() {
    // preparacion
    // sin datos de entrada: cargar el formulario no necesita ambiente ni muebles

    // ejecucion
    ModelAndView mav = whenAccedoAlPasoDeConfiguracion();

    // validacion
    thenLaVistaEs("ambiente-config", mav);
  }

  @Test
  public void deberiaPasarElPlanoAlModeloCuandoDimensionesYMueblesSonValidos() {
    // preparacion
    Plano planoGenerado = planoConLosMueblesUbicados(muebles);
    when(servicioPlano2D.generarPlano(ambiente, muebles)).thenReturn(planoGenerado);

    // ejecucion
    ModelAndView mav = whenGeneroElPlanoCon(ambiente, muebles);

    // validacion
    thenElModeloTieneElPlano(planoGenerado, mav);
  }

  @Test
  public void deberiaMostrarLaVistaPlanoInteractivoCuandoDimensionesYMueblesSonValidos() {
    // preparacion
    Plano planoGenerado = planoConLosMueblesUbicados(muebles);
    when(servicioPlano2D.generarPlano(ambiente, muebles)).thenReturn(planoGenerado);

    // ejecucion
    ModelAndView mav = whenGeneroElPlanoCon(ambiente, muebles);

    // validacion
    thenLaVistaEs("plano-interactivo", mav);
  }

  @Test
  public void deberiaEntregarAlServicioElAmbienteDelFormulario() {
    // preparacion
    DatosPlano datosDelFormulario = new DatosPlano();
    datosDelFormulario.setAncho(4.0);
    datosDelFormulario.setLargo(3.0);
    when(servicioPlano2D.generarPlano(any(Ambiente.class), anyList()))
      .thenReturn(planoConLosMueblesUbicados(List.of()));

    // ejecucion
    whenConfirmoElFormulario(datosDelFormulario);

    // validacion
    thenElServicioRecibeLasDimensiones(4.0, 3.0);
  }

  @Test
  public void deberiaEntregarAlServicioLosMueblesDelFormulario() {
    // preparacion
    List<DatosMueble> mueblesCargados = new ArrayList<>();
    mueblesCargados.add(new DatosMueble("Silla", 0.5, 0.5));
    mueblesCargados.add(new DatosMueble("Mesa", 1.0, 1.0));
    DatosPlano datosDelFormulario = new DatosPlano();
    datosDelFormulario.setAncho(4.0);
    datosDelFormulario.setLargo(3.0);
    datosDelFormulario.setMuebles(mueblesCargados);
    when(servicioPlano2D.generarPlano(any(Ambiente.class), anyList()))
      .thenReturn(planoConLosMueblesUbicados(List.of()));

    // ejecucion
    whenConfirmoElFormulario(datosDelFormulario);

    // validacion
    thenElServicioRecibeLosMuebles(List.of(List.of("Silla", 0.5, 0.5), List.of("Mesa", 1.0, 1.0)));
  }

  @Test
  public void deberiaMapearElAccesoAlFormularioPorGet() {
    // preparacion
    // sin datos de entrada: el mapeo HTTP es metadata del metodo, no comportamiento

    // ejecucion
    List<Object> mapeo = whenBuscoElMapeoDe("irAConfigurarAmbiente");

    // validacion
    thenElMapeoEs(Arrays.asList("/plano/ambiente", RequestMethod.GET), mapeo);
  }

  @Test
  public void deberiaMapearLaConfirmacionDelFormularioPorPost() {
    // preparacion
    // sin datos de entrada: el mapeo HTTP es metadata del metodo, no comportamiento

    // ejecucion
    List<Object> mapeo = whenBuscoElMapeoDe("generarPlanoDesdeFormulario", DatosPlano.class);

    // validacion
    thenElMapeoEs(Arrays.asList("/plano/generar", RequestMethod.POST), mapeo);
  }

  /**
   * Arma el Plano que devolvería el servicio para una colocación exitosa: un MuebleUbicado por
   * cada mueble recibido y ninguna exclusión. Se usa en vez de un mock porque un mock de Plano
   * no dice nada sobre su contenido: ante cualquier código que inspeccione getMuebles(), Mockito
   * devuelve una lista vacía y un plano "sin muebles" se confunde con uno "con muebles".
   */
  private Plano planoConLosMueblesUbicados(List<Mueble> mueblesAColocar) {
    List<MuebleUbicado> ubicados = new ArrayList<>();
    for (Mueble mueble : mueblesAColocar) {
      ubicados.add(new MuebleUbicado(mueble, 0.0, 0.0));
    }
    return new Plano(ambiente, ubicados, List.of(), List.of());
  }

  private ModelAndView whenGeneroElPlanoCon(Ambiente ambiente, List<Mueble> muebles) {
    return controladorPlano2D.generarPlano(ambiente, muebles);
  }

  private void whenConfirmoElFormulario(DatosPlano datosDelFormulario) {
    controladorPlano2D.generarPlanoDesdeFormulario(datosDelFormulario);
  }

  /**
   * Lee el @RequestMapping por reflexion. Es la unica forma de verificar el routing sin MockMvc,
   * que este proyecto no usa para tests de controlador. Si el metodo no esta anotado devuelve
   * [null, null] para que falle el assert del Then y no un NullPointerException del When.
   */
  private List<Object> whenBuscoElMapeoDe(String nombreDelMetodo, Class<?>... tipos) {
    Method metodo;
    try {
      metodo = ControladorPlano2D.class.getMethod(nombreDelMetodo, tipos);
    } catch (NoSuchMethodException e) {
      return Arrays.asList(null, null);
    }
    RequestMapping anotacion = metodo.getAnnotation(RequestMapping.class);
    if (anotacion == null) {
      return Arrays.asList(null, null);
    }
    return Arrays.asList(anotacion.path()[0], anotacion.method()[0]);
  }

  private void thenElMapeoEs(List<Object> mapeoEsperado, List<Object> mapeoObtenido) {
    assertEquals(mapeoEsperado, mapeoObtenido);
  }

  private ModelAndView whenAccedoAlPasoDeConfiguracion() {
    return controladorPlano2D.irAConfigurarAmbiente();
  }

  private void thenLaVistaEs(String vistaEsperada, ModelAndView mav) {
    assertEquals(vistaEsperada, mav.getViewName());
  }

  private void thenElModeloTieneElPlano(Plano planoEsperado, ModelAndView mav) {
    assertSame(planoEsperado, mav.getModel().get("plano"));
  }

  private void thenElModeloTieneElMensajeDeError(String mensajeEsperado, ModelAndView mav) {
    assertEquals(mensajeEsperado, mav.getModel().get("error"));
  }

  private void thenElServicioRecibeLosMismosDatos(
    Ambiente ambienteEsperado,
    List<Mueble> mueblesEsperados
  ) {
    verify(servicioPlano2D).generarPlano(ambienteEsperado, mueblesEsperados);
  }

  @SuppressWarnings("unchecked")
  private void thenElServicioRecibeLosMuebles(List<Object> mueblesEsperados) {
    ArgumentCaptor<List<Mueble>> mueblesCapturados = ArgumentCaptor.forClass(List.class);
    verify(servicioPlano2D).generarPlano(any(Ambiente.class), mueblesCapturados.capture());
    List<Object> mueblesRecibidos = new ArrayList<>();
    for (Mueble mueble : mueblesCapturados.getValue()) {
      mueblesRecibidos.add(List.of(mueble.getNombre(), mueble.getAncho(), mueble.getLargo()));
    }
    assertEquals(mueblesEsperados, mueblesRecibidos);
  }

  /**
   * Verifica las dimensiones con las que se armo el Ambiente, y no con identidad de objeto:
   * Ambiente no define equals(), asi que comparar contra un Ambiente armado en el test solo
   * compararia referencias y pasaria aunque el mapeo deliverse cualquier valor.
   */
  private void thenElServicioRecibeLasDimensiones(Double anchoEsperado, Double largoEsperado) {
    ArgumentCaptor<Ambiente> ambienteCapturado = ArgumentCaptor.forClass(Ambiente.class);
    verify(servicioPlano2D).generarPlano(ambienteCapturado.capture(), anyList());
    Ambiente entregado = ambienteCapturado.getValue();
    assertEquals(
      List.of(anchoEsperado, largoEsperado),
      List.of(entregado.getAncho(), entregado.getLargo())
    );
  }
}
