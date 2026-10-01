package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.CategoriaDeMueble;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.Plano;
import com.tallerwebi.dominio.ServicioCategoriaDeMueble;
import com.tallerwebi.dominio.ServicioPlano2D;
import com.tallerwebi.dominio.excepcion.ValidacionException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * HU-08: dimensiones estandarizadas por categoría.
 * Dado que elijo una categoría, cuando confirmo el formulario, entonces el plano se genera con las
 * medidas que arma el servicio de categorías (promedio o lo que escribí a mano).
 */
public class ControladorPlano2DDimensionesTest {

  private static final Long ID_CAMA_DOBLE = 2L;

  private ServicioPlano2D servicioPlano2D;
  private ServicioCategoriaDeMueble servicioCategoriaDeMueble;
  private ControladorPlano2D controladorPlano2D;

  @BeforeEach
  public void init() {
    servicioPlano2D = mock(ServicioPlano2D.class);
    servicioCategoriaDeMueble = mock(ServicioCategoriaDeMueble.class);
    controladorPlano2D = new ControladorPlano2D(servicioPlano2D, servicioCategoriaDeMueble);
    when(servicioPlano2D.generarPlano(any(Ambiente.class), anyList()))
      .thenReturn(new Plano(new Ambiente(4.0, 3.0), List.of(), List.of(), List.of()));
  }

  @Test
  public void deberiaGenerarElPlanoConElMuebleQueArmaElServicioDeCategorias() {
    // preparacion
    Mueble camaConMedidasPromedio = new Mueble("Mi cama", 1.4, 1.9);
    when(servicioCategoriaDeMueble.crearMueble("Mi cama", ID_CAMA_DOBLE, null, null))
      .thenReturn(camaConMedidasPromedio);
    DatosPlano formulario = givenUnFormularioConElMueble("Mi cama", ID_CAMA_DOBLE, null, null);

    // ejecucion
    whenConfirmoElFormulario(formulario);

    // validacion
    thenElPlanoSeGeneraCon(camaConMedidasPromedio);
  }

  @Test
  public void deberiaMostrarElErrorEnElPlanoCuandoElServicioDeCategoriasRechazaUnMueble() {
    // preparacion
    when(servicioCategoriaDeMueble.crearMueble("Baúl", null, 0.9, null))
      .thenThrow(new ValidacionException("Completá las medidas de Baúl o elegí una categoría"));
    DatosPlano formulario = givenUnFormularioConElMueble("Baúl", null, 0.9, null);

    // ejecucion
    ModelAndView mav = whenConfirmoElFormulario(formulario);

    // validacion
    thenSeMuestraElError("Completá las medidas de Baúl o elegí una categoría", mav);
  }

  @Test
  public void deberiaOfrecerEnElFormularioLasCategoriasDelServicio() {
    // preparacion
    List<CategoriaDeMueble> categorias = List.of(
      new CategoriaDeMueble("Cama doble", 1.4, 1.9),
      new CategoriaDeMueble("Silla", 0.45, 0.5)
    );
    when(servicioCategoriaDeMueble.obtenerCategorias()).thenReturn(categorias);

    // ejecucion
    ModelAndView mav = controladorPlano2D.irAConfigurarAmbiente();

    // validacion
    assertSame(categorias, mav.getModel().get("categorias"));
  }

  private DatosPlano givenUnFormularioConElMueble(
    String nombre,
    Long idCategoria,
    Double ancho,
    Double profundidad
  ) {
    DatosMueble datosMueble = new DatosMueble(nombre, ancho, profundidad);
    datosMueble.setCategoria(idCategoria);
    List<DatosMueble> muebles = new ArrayList<>();
    muebles.add(datosMueble);
    DatosPlano formulario = new DatosPlano();
    formulario.setAncho(4.0);
    formulario.setLargo(3.0);
    formulario.setMuebles(muebles);
    return formulario;
  }

  private ModelAndView whenConfirmoElFormulario(DatosPlano formulario) {
    return controladorPlano2D.generarPlanoDesdeFormulario(formulario);
  }

  private void thenSeMuestraElError(String mensajeEsperado, ModelAndView mav) {
    assertEquals("plano-interactivo", mav.getViewName());
    assertEquals(mensajeEsperado, mav.getModel().get("error"));
  }

  /**
   * Compara por identidad: el controlador no debe rearmar el mueble, tiene que pasar al plano
   * exactamente el que decidió el servicio de categorías.
   */
  @SuppressWarnings("unchecked")
  private void thenElPlanoSeGeneraCon(Mueble muebleEsperado) {
    ArgumentCaptor<List<Mueble>> muebles = ArgumentCaptor.forClass(List.class);
    verify(servicioPlano2D).generarPlano(any(Ambiente.class), muebles.capture());
    assertSame(muebleEsperado, muebles.getValue().get(0));
  }
}
