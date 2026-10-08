package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Muro;
import com.tallerwebi.dominio.ServicioObstaculos;
import com.tallerwebi.dominio.TipoDeObstaculo;
import com.tallerwebi.dominio.excepcion.ValidacionException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.ModelAndView;

public class ControladorObstaculosTest {

  private ServicioObstaculos servicioObstaculos;
  private ControladorObstaculos controladorObstaculos;
  private DatosPlano formulario;

  @BeforeEach
  public void init() {
    servicioObstaculos = mock(ServicioObstaculos.class);
    controladorObstaculos = new ControladorObstaculos(servicioObstaculos);
    formulario = new DatosPlano();
    formulario.setAncho(4.0);
    formulario.setLargo(3.0);
  }

  @Test
  public void deberiaMostrarElPasoDeObstaculosConLosDatosDelPasoAnterior() {
    // ejecucion
    ModelAndView mav = controladorObstaculos.irACargarObstaculos(formulario);

    // validacion
    assertEquals("obstaculos-config", mav.getViewName());
    assertEquals(formulario, mav.getModel().get("datosPlano"));
  }

  @Test
  public void deberiaOfrecerLosTiposDeObstaculoYLosMuros() {
    // ejecucion
    ModelAndView mav = controladorObstaculos.irACargarObstaculos(formulario);

    // validacion
    assertArrayEquals(TipoDeObstaculo.values(), (Object[]) mav.getModel().get("tiposDeObstaculo"));
    assertArrayEquals(Muro.values(), (Object[]) mav.getModel().get("muros"));
  }

  @Test
  public void deberiaValidarLosObstaculosDelAmbienteYSeguirAlPlanoCuandoSonValidos() {
    // preparacion
    formulario.setObstaculos(
      new ArrayList<>(
        List.of(new DatosObstaculo(TipoDeObstaculo.RADIADOR, Muro.DERECHO, 0.5, 0.8, 0.15))
      )
    );

    // ejecucion
    ModelAndView mav = controladorObstaculos.confirmarObstaculos(formulario);

    // validacion
    ArgumentCaptor<Ambiente> ambiente = ArgumentCaptor.forClass(Ambiente.class);
    verify(servicioObstaculos).validarObstaculos(ambiente.capture());
    assertEquals(Muro.DERECHO, ambiente.getValue().getObstaculos().get(0).getMuro());
    assertEquals("forward:/plano/generar", mav.getViewName());
  }

  @Test
  public void deberiaVolverAlPasoDeObstaculosConElErrorCuandoUnObstaculoNoEsValido() {
    // preparacion
    doThrow(new ValidacionException("Radiador se sale del muro derecho"))
      .when(servicioObstaculos)
      .validarObstaculos(any(Ambiente.class));

    // ejecucion
    ModelAndView mav = controladorObstaculos.confirmarObstaculos(formulario);

    // validacion
    assertEquals("obstaculos-config", mav.getViewName());
    assertEquals("Radiador se sale del muro derecho", mav.getModel().get("error"));
    assertEquals(formulario, mav.getModel().get("datosPlano"));
    assertArrayEquals(Muro.values(), (Object[]) mav.getModel().get("muros"));
  }
}
