package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.ServicioAmbiente;
import com.tallerwebi.dominio.TipoDeAmbiente;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorAmbienteTest {

  private ServicioAmbiente servicioAmbiente;
  private ControladorAmbiente controladorAmbiente;

  @BeforeEach
  public void init() {
    this.servicioAmbiente = mock(ServicioAmbiente.class);
    this.controladorAmbiente = new ControladorAmbiente(this.servicioAmbiente);
  }

  @Test
  public void dadoQueEntroAMisAmbientes_entoncesVeoLaListaDeAmbientes() {
    List<Ambiente> ambientes = new ArrayList<>();
    ambientes.add(new Ambiente(4.0, 3.0, new TipoDeAmbiente("Habitación")));
    when(this.servicioAmbiente.obtenerAmbientes()).thenReturn(ambientes);

    ModelAndView modelAndView = this.controladorAmbiente.ambientes();

    assertThat(modelAndView.getViewName(), is("ambientes"));
    assertThat(this.obtenerAmbientesDelModel(modelAndView), contains(ambientes.get(0)));
  }

  @Test
  public void dadoQueEntroAlDetalleDeUnAmbiente_entoncesVeoEseAmbiente() {
    Ambiente ambiente = new Ambiente(4.0, 3.0, new TipoDeAmbiente("Habitación"));
    when(this.servicioAmbiente.buscarAmbiente(1L)).thenReturn(ambiente);

    ModelAndView modelAndView = this.controladorAmbiente.detalleAmbiente(1L);

    assertThat(modelAndView.getViewName(), is("ambiente"));
    assertThat(modelAndView.getModel().get("ambiente"), is(ambiente));
  }

  @SuppressWarnings("unchecked")
  private List<Ambiente> obtenerAmbientesDelModel(ModelAndView modelAndView) {
    return (List<Ambiente>) modelAndView.getModel().get("ambientes");
  }
}
