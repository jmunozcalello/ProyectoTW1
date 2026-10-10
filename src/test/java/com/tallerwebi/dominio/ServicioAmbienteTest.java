package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioAmbienteTest {

  private RepositorioAmbiente repositorioAmbiente;
  private ServicioAmbiente servicioAmbiente;

  @BeforeEach
  public void init() {
    this.repositorioAmbiente = mock(RepositorioAmbiente.class);
    this.servicioAmbiente = new ServicioAmbienteImpl(this.repositorioAmbiente);
  }

  @Test
  public void dadoQueHayAmbientes_cuandoLosPido_entoncesLosTraigoDelRepositorio() {
    List<Ambiente> ambientes = new ArrayList<>();
    ambientes.add(new Ambiente(4.0, 3.0, new TipoDeAmbiente("Habitación")));
    when(this.repositorioAmbiente.obtenerTodos()).thenReturn(ambientes);

    List<Ambiente> obtenidos = this.servicioAmbiente.obtenerAmbientes();

    assertThat(obtenidos, contains(ambientes.get(0)));
  }

  @Test
  public void dadoUnEmail_cuandoPidoLosAmbientesDeEseUsuario_entoncesLosTraigoDelRepositorio() {
    List<Ambiente> ambientes = new ArrayList<>();
    ambientes.add(new Ambiente(4.0, 3.0, new TipoDeAmbiente("Habitación")));
    when(this.repositorioAmbiente.obtenerAmbientesDe("test@unlam.edu.ar")).thenReturn(ambientes);

    List<Ambiente> obtenidos = this.servicioAmbiente.obtenerAmbientesDe("test@unlam.edu.ar");

    assertThat(obtenidos, contains(ambientes.get(0)));
  }

  @Test
  public void dadoUnId_cuandoBuscoElAmbiente_entoncesLoTraigoDelRepositorio() {
    Ambiente ambiente = new Ambiente(4.0, 3.0, new TipoDeAmbiente("Habitación"));
    when(this.repositorioAmbiente.buscarPorId(1L)).thenReturn(ambiente);

    Ambiente obtenido = this.servicioAmbiente.buscarAmbiente(1L);

    assertThat(obtenido, is(ambiente));
  }

  @Test
  public void dadoUnIdNulo_cuandoBuscoElAmbiente_entoncesNoVoyAlRepositorio() {
    Ambiente obtenido = this.servicioAmbiente.buscarAmbiente(null);

    assertThat(obtenido, is(nullValue()));
    verify(this.repositorioAmbiente, never()).buscarPorId(null);
  }
}
