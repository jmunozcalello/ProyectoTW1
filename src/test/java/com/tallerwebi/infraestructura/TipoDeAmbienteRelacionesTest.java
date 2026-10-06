package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.TipoDeAmbiente;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Verifica el comportamiento de las relaciones de TipoDeAmbiente con Ambiente, Mueble y Usuario:
 * cascada de Usuario sobre sus Ambientes y ausencia de cascada en la Many-to-Many entre Mueble y
 * TipoDeAmbiente.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class TipoDeAmbienteRelacionesTest {

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  @Transactional
  @Rollback
  public void dadoQueUnUsuarioTieneAmbientes_cuandoLoBorro_entoncesSusAmbientesTambienSeBorran() {
    TipoDeAmbiente living = this.dadoQueExisteElTipoDeAmbiente("Living");
    Usuario usuario = this.dadoQueExisteElUsuario("duenio@test.com");
    Ambiente ambiente = this.dadoQueElUsuarioTieneElAmbiente(usuario, 4.0, 3.0, living);
    Long idDelAmbiente = ambiente.getId();

    this.cuandoBorro(usuario);

    assertThat(this.buscarAmbiente(idDelAmbiente), is(nullValue()));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueUnUsuarioTieneDosAmbientes_cuandoLoBorro_entoncesNingunoDeEllosSobrevive() {
    TipoDeAmbiente living = this.dadoQueExisteElTipoDeAmbiente("Living");
    TipoDeAmbiente pasillo = this.dadoQueExisteElTipoDeAmbiente("Pasillo");
    Usuario usuario = this.dadoQueExisteElUsuario("duenio@test.com");
    Ambiente living1 = this.dadoQueElUsuarioTieneElAmbiente(usuario, 4.0, 3.0, living);
    Ambiente pasillo1 = this.dadoQueElUsuarioTieneElAmbiente(usuario, 1.0, 5.0, pasillo);
    Long idDelLiving = living1.getId();
    Long idDelPasillo = pasillo1.getId();

    this.cuandoBorro(usuario);

    assertThat(this.buscarAmbiente(idDelLiving), is(nullValue()));
    assertThat(this.buscarAmbiente(idDelPasillo), is(nullValue()));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueDosAmbientesCompartenElTipo_cuandoBorroUno_entoncesElOtroConservaElTipo() {
    TipoDeAmbiente living = this.dadoQueExisteElTipoDeAmbiente("Living");
    Ambiente primerLiving = new Ambiente(4.0, 3.0, living);
    Ambiente segundoLiving = new Ambiente(5.0, 4.0, living);
    this.sessionFactory.getCurrentSession().persist(primerLiving);
    this.sessionFactory.getCurrentSession().persist(segundoLiving);
    this.sessionFactory.getCurrentSession().flush();
    Long idDelSegundoLiving = segundoLiving.getId();

    this.cuandoBorro(primerLiving);

    Ambiente sobreviviente = this.buscarAmbiente(idDelSegundoLiving);
    assertThat(sobreviviente, is(notNullValue()));
    assertThat(sobreviviente.getTipoDeAmbiente().getNombre(), is("Living"));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueUnAmbienteTieneTipo_cuandoLoVuelvoALeer_entoncesTraeSuTipoDeAmbiente() {
    TipoDeAmbiente cocina = this.dadoQueExisteElTipoDeAmbiente("Cocina");
    Ambiente ambiente = new Ambiente(3.0, 2.0, cocina);
    this.sessionFactory.getCurrentSession().persist(ambiente);
    this.sessionFactory.getCurrentSession().flush();
    Long idDelAmbiente = ambiente.getId();
    this.sessionFactory.getCurrentSession().clear();

    Ambiente leido = this.buscarAmbiente(idDelAmbiente);

    assertThat(leido, is(notNullValue()));
    assertThat(leido.getTipoDeAmbiente().getNombre(), is("Cocina"));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueUnMuebleTieneTiposDeAmbiente_cuandoBorroElMueble_entoncesLosTiposSiguenExistiendo() {
    TipoDeAmbiente living = this.dadoQueExisteElTipoDeAmbiente("Living");
    TipoDeAmbiente comedor = this.dadoQueExisteElTipoDeAmbiente("Comedor");
    Mueble mesa = this.dadoQueExisteElMuebleConTipos("Mesa de Comedor", living, comedor);
    Long idDelLiving = living.getId();
    Long idDelComedor = comedor.getId();

    this.cuandoBorro(mesa);

    assertThat(this.buscarTipoDeAmbiente(idDelLiving), is(notNullValue()));
    assertThat(this.buscarTipoDeAmbiente(idDelComedor), is(notNullValue()));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueUnTipoDeAmbienteEstaAsociadoAMuebles_cuandoBorroElTipo_entoncesLosMueblesSiguenExistiendo() {
    TipoDeAmbiente living = this.dadoQueExisteElTipoDeAmbiente("Living");
    Mueble sillon = this.dadoQueExisteElMuebleConTipos("Sillón Retró", living);
    int idDelMueble = sillon.getId();

    sillon.getTiposDeAmbiente().remove(living);
    this.sessionFactory.getCurrentSession().flush();
    this.cuandoBorro(living);

    Mueble sobreviviente = this.buscarMueble(idDelMueble);
    assertThat(sobreviviente, is(notNullValue()));
    assertThat(sobreviviente.getNombre(), is("Sillón Retró"));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueUnMuebleTieneDosTipos_cuandoLoVuelvoALeer_entoncesConservaAmbosTipos() {
    TipoDeAmbiente living = this.dadoQueExisteElTipoDeAmbiente("Living");
    TipoDeAmbiente comedor = this.dadoQueExisteElTipoDeAmbiente("Comedor");
    Mueble mesa = this.dadoQueExisteElMuebleConTipos("Mesa de Comedor", living, comedor);
    int idDelMueble = mesa.getId();
    this.sessionFactory.getCurrentSession().clear();

    Mueble leido = this.buscarMueble(idDelMueble);

    assertThat(leido.getTiposDeAmbiente(), hasSize(2));
    assertThat(
      leido.getTiposDeAmbiente().stream().map(TipoDeAmbiente::getNombre).toList(),
      is(Arrays.asList("Living", "Comedor"))
    );
  }

  private TipoDeAmbiente dadoQueExisteElTipoDeAmbiente(String nombre) {
    TipoDeAmbiente tipo = new TipoDeAmbiente(nombre);
    this.sessionFactory.getCurrentSession().persist(tipo);
    return tipo;
  }

  private Usuario dadoQueExisteElUsuario(String email) {
    Usuario usuario = new Usuario();
    usuario.setEmail(email);
    usuario.setPassword("1234");
    usuario.setRol("USER");
    this.sessionFactory.getCurrentSession().persist(usuario);
    return usuario;
  }

  private Ambiente dadoQueElUsuarioTieneElAmbiente(
    Usuario usuario,
    Double ancho,
    Double largo,
    TipoDeAmbiente tipo
  ) {
    Ambiente ambiente = new Ambiente(ancho, largo, tipo);
    usuario.agregarAmbiente(ambiente);
    this.sessionFactory.getCurrentSession().flush();
    return ambiente;
  }

  private Mueble dadoQueExisteElMuebleConTipos(String nombre, TipoDeAmbiente... tipos) {
    Mueble mueble = new Mueble();
    mueble.setNombre(nombre);
    mueble.setPrecio(100.0);
    for (TipoDeAmbiente tipo : tipos) {
      mueble.agregarTipoDeAmbiente(tipo);
    }
    this.sessionFactory.getCurrentSession().persist(mueble);
    this.sessionFactory.getCurrentSession().flush();
    return mueble;
  }

  private void cuandoBorro(Object entidad) {
    Session session = this.sessionFactory.getCurrentSession();
    session.flush();
    session.remove(entidad);
    session.flush();
    session.clear();
  }

  private Ambiente buscarAmbiente(Long id) {
    return this.sessionFactory.getCurrentSession().get(Ambiente.class, id);
  }

  private TipoDeAmbiente buscarTipoDeAmbiente(Long id) {
    return this.sessionFactory.getCurrentSession().get(TipoDeAmbiente.class, id);
  }

  private Mueble buscarMueble(int id) {
    return this.sessionFactory.getCurrentSession().get(Mueble.class, id);
  }
}
