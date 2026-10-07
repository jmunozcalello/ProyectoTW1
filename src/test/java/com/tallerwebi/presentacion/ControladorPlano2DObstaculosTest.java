package com.tallerwebi.presentacion;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Muro;
import com.tallerwebi.dominio.Obstaculo;
import com.tallerwebi.dominio.Plano;
import com.tallerwebi.dominio.ServicioCategoriaDeMueble;
import com.tallerwebi.dominio.ServicioPlano2D;
import com.tallerwebi.dominio.TipoDeObstaculo;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.ModelAndView;

public class ControladorPlano2DObstaculosTest {

  private ServicioPlano2D servicioPlano2D;
  private ControladorPlano2D controladorPlano2D;

  @BeforeEach
  public void init() {
    servicioPlano2D = mock(ServicioPlano2D.class);
    controladorPlano2D =
      new ControladorPlano2D(servicioPlano2D, mock(ServicioCategoriaDeMueble.class));
    when(servicioPlano2D.generarPlano(any(Ambiente.class), anyList()))
      .thenReturn(new Plano(new Ambiente(4.0, 3.0), List.of(), List.of(), List.of()));
  }

  @Test
  public void deberiaPasarleAlServicioElAmbienteConLosObstaculosDelFormulario() {
    DatosPlano formulario = givenUnFormularioConElObstaculo(
      new DatosObstaculo(TipoDeObstaculo.RADIADOR, Muro.DERECHO, 0.5, 0.8, 0.15)
    );

    controladorPlano2D.generarPlanoDesdeFormulario(formulario);

    Obstaculo obstaculo = thenElAmbienteQueRecibeElServicio().getObstaculos().get(0);
    assertEquals(TipoDeObstaculo.RADIADOR, obstaculo.getTipo());
    assertEquals(Muro.DERECHO, obstaculo.getMuro());
    assertEquals(
      List.of(0.5, 0.8, 0.15),
      List.of(obstaculo.getPosicion(), obstaculo.getAncho(), obstaculo.getProfundidad())
    );
  }

  @Test
  public void deberiaOfrecerEnElFormularioLosTiposDeObstaculoYLosMuros() {
    ModelAndView mav = controladorPlano2D.irAConfigurarAmbiente();

    assertArrayEquals(TipoDeObstaculo.values(), (Object[]) mav.getModel().get("tiposDeObstaculo"));
    assertArrayEquals(Muro.values(), (Object[]) mav.getModel().get("muros"));
  }

  @Test
  public void deberiaIgnorarLasFilasDeObstaculoSinMedidas() {
    DatosPlano formulario = givenUnFormularioConElObstaculo(
      new DatosObstaculo(TipoDeObstaculo.RADIADOR, Muro.SUPERIOR, null, null, null)
    );

    controladorPlano2D.generarPlanoDesdeFormulario(formulario);

    assertEquals(List.of(), thenElAmbienteQueRecibeElServicio().getObstaculos());
  }

  private DatosPlano givenUnFormularioConElObstaculo(DatosObstaculo datosObstaculo) {
    List<DatosObstaculo> obstaculos = new ArrayList<>();
    obstaculos.add(datosObstaculo);
    DatosPlano formulario = new DatosPlano();
    formulario.setAncho(4.0);
    formulario.setLargo(3.0);
    formulario.setObstaculos(obstaculos);
    return formulario;
  }

  private Ambiente thenElAmbienteQueRecibeElServicio() {
    ArgumentCaptor<Ambiente> ambiente = ArgumentCaptor.forClass(Ambiente.class);
    verify(servicioPlano2D).generarPlano(ambiente.capture(), anyList());
    return ambiente.getValue();
  }
}
