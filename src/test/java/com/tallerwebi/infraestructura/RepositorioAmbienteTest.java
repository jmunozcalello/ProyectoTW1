package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.RepositorioAmbiente;
import com.tallerwebi.dominio.TipoDeAmbiente;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import jakarta.transaction.Transactional;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class RepositorioAmbienteTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioAmbiente repositorioAmbiente;

  @BeforeEach
  public void init() {
    repositorioAmbiente = new RepositorioAmbienteImpl(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void dadoUnAmbienteGuardado_cuandoLoBuscoPorId_entoncesTraigoSuTipoDeAmbiente() {
    Ambiente ambiente = this.dadoQueExisteElAmbiente(4.0, 3.0, "Habitación");

    Ambiente obtenido = this.repositorioAmbiente.buscarPorId(ambiente.getId());

    assertThat(obtenido, is(notNullValue()));
    assertThat(obtenido.getAncho(), is(4.0));
    assertThat(obtenido.getLargo(), is(3.0));
    assertThat(obtenido.getTipoDeAmbiente().getNombre(), is("Habitación"));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueNoHayAmbienteConEseId_cuandoLoBusco_entoncesObtengoNull() {
    Ambiente ambiente = this.dadoQueExisteElAmbiente(4.0, 3.0, "Living");

    Ambiente obtenido = this.repositorioAmbiente.buscarPorId(ambiente.getId() + 1000);

    assertThat(obtenido, is(nullValue()));
  }

  @Test
  @Transactional
  @Rollback
  public void dadosVariosAmbientesGuardados_cuandoPidoTodos_entoncesObtengoTodos() {
    Ambiente habitacion = this.dadoQueExisteElAmbiente(4.0, 3.0, "Habitación");
    Ambiente cocina = this.dadoQueExisteElAmbiente(5.0, 3.0, "Cocina");

    List<Ambiente> ambientes = this.repositorioAmbiente.obtenerTodos();

    assertThat(ambientes, hasSize(2));
    assertThat(ambientes, contains(habitacion, cocina));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoUnUsuarioConAmbientesConMuebles_cuandoPidoLosDeSuEmail_entoncesTraigoLosAmbientesYSusMuebles() {
    Usuario usuario = new Usuario();
    usuario.setEmail("test@unlam.edu.ar");
    this.sessionFactory.getCurrentSession().persist(usuario);

    Ambiente habitacion = this.dadoQueExisteElAmbientePara(4.0, 3.0, "Habitación", usuario);
    Mueble cama = this.dadoQueExisteElMueble("Cama");
    Mueble escritorio = this.dadoQueExisteElMueble("Escritorio");
    habitacion.agregarMueble(cama);
    habitacion.agregarMueble(escritorio);
    this.sessionFactory.getCurrentSession().persist(habitacion);
    this.sessionFactory.getCurrentSession().flush();
    this.sessionFactory.getCurrentSession().clear();

    List<Ambiente> ambientes = this.repositorioAmbiente.obtenerAmbientesDe("test@unlam.edu.ar");

    assertThat(ambientes, hasSize(1));
    assertThat(ambientes.get(0).getMuebles(), contains(cama, escritorio));
  }

  private Ambiente dadoQueExisteElAmbientePara(
    Double ancho,
    Double largo,
    String tipo,
    Usuario usuario
  ) {
    TipoDeAmbiente tipoDeAmbiente = new TipoDeAmbiente(tipo);
    this.sessionFactory.getCurrentSession().persist(tipoDeAmbiente);
    Ambiente ambiente = new Ambiente(ancho, largo, tipoDeAmbiente);
    ambiente.setUsuario(usuario);
    this.sessionFactory.getCurrentSession().persist(ambiente);
    return ambiente;
  }

  private Mueble dadoQueExisteElMueble(String nombre) {
    Mueble mueble = new Mueble(nombre, 1.0, 2.0);
    this.sessionFactory.getCurrentSession().persist(mueble);
    return mueble;
  }

  private Ambiente dadoQueExisteElAmbiente(Double ancho, Double largo, String tipo) {
    TipoDeAmbiente tipoDeAmbiente = new TipoDeAmbiente(tipo);
    this.sessionFactory.getCurrentSession().persist(tipoDeAmbiente);
    Ambiente ambiente = new Ambiente(ancho, largo, tipoDeAmbiente);
    this.sessionFactory.getCurrentSession().persist(ambiente);
    this.sessionFactory.getCurrentSession().flush();
    return ambiente;
  }
}
