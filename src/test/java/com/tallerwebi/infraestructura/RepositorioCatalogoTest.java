package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.RepositorioCatalogo;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import jakarta.transaction.Transactional;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class RepositorioCatalogoTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioCatalogo repositorioCatalogo;

  @BeforeEach
  public void init() {
    repositorioCatalogo = new RepositorioCatalogoImpl(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void DadoUnNuevoMuebleCuandoLoGuardoEntoncesSeGuarda() {
    String nombreMueble = "Mueble Nuevo";
    Mueble mueble = this.dadoQueTengoUnMueble(nombreMueble, 100.0, Estilo.Retro);
    this.cuandoGuardoUnMueble(mueble);
    this.entoncesSeGuardoElMueble(nombreMueble, 100.0, Estilo.Retro, mueble);
  }

  @Test
  @Transactional
  @Rollback
  public void DadoQueNoHayNingunMuebleCuandoBuscoUnoEntoncesObtengoNull() {
    Mueble mueble = this.dadoQueTengoUnMueble("Mueble inexistente", 200.0, Estilo.Retro);
    this.entoncesElMuebleObtenidoEsNull(mueble);
  }

  @Test
  @Transactional
  @Rollback
  public void DadoQueHayMueblesRegistradosCuandoPidoTodosEntoncesObtengoTodos() {
    Mueble mueble1 = this.dadoQueTengoUnMueble("Mueble 1", 150.0, Estilo.Retro);
    Mueble mueble2 = this.dadoQueTengoUnMueble("Mueble 2", 250.0, Estilo.Retro);
    this.cuandoGuardoUnMueble(mueble1);
    this.cuandoGuardoUnMueble(mueble2);

    List<Mueble> mueblesObtenidos = this.repositorioCatalogo.ObtenerTodosLosMuebles();

    assertThat(mueblesObtenidos.size(), is(equalTo(2)));
  }

  @Test
  @Transactional
  @Rollback
  public void DadoUnMuebleGuardadoCuandoLoBuscoPorIdEntoncesObtengoEseMueble() {
    Mueble mueble = this.dadoQueTengoUnMueble("Sillón Retró", 85000.0, Estilo.Retro);
    mueble.setDescripcion("Sillón de voluteadas tapizado en terciopelo.");
    this.cuandoGuardoUnMueble(mueble);

    Mueble muebleObtenido = this.repositorioCatalogo.ObtenerMueblePorId(mueble.getId());

    assertThat(muebleObtenido, is(notNullValue()));
    assertThat(muebleObtenido.getNombre(), is(equalTo("Sillón Retró")));
    assertThat(
      muebleObtenido.getDescripcion(),
      is(equalTo("Sillón de voluteadas tapizado en terciopelo."))
    );
  }

  @Test
  @Transactional
  @Rollback
  public void DadoQueNoHayNingunMuebleConEseIdCuandoLoBuscoEntoncesObtengoUnMuebleNull() {
    Mueble mueble = this.dadoQueTengoUnMueble("Mueble 1", 150.0, Estilo.Retro);
    this.cuandoGuardoUnMueble(mueble);

    Mueble muebleObtenido = this.repositorioCatalogo.ObtenerMueblePorId(mueble.getId() + 1000);

    assertThat(muebleObtenido, is(nullValue()));
  }

  private Mueble dadoQueTengoUnMueble(String nombre, double precio, Estilo estilo) {
    Mueble mueble = new Mueble();
    mueble.setNombre(nombre);
    mueble.setPrecio(precio);
    mueble.SetEstilo(estilo);
    return mueble;
  }

  private void cuandoGuardoUnMueble(Mueble mueble) {
    repositorioCatalogo.guardarMueble(mueble);
  }

  private void entoncesSeGuardoElMueble(
    String nombre,
    double precio,
    Estilo estilo,
    Mueble muebleEsperado
  ) {
    String hql = "FROM Mueble WHERE nombre = :nombre AND precio = :precio AND estilo = :estilo";
    Query query = this.sessionFactory.getCurrentSession().createQuery(hql, Mueble.class);
    query.setParameter("nombre", nombre);
    query.setParameter("precio", precio);
    query.setParameter("estilo", estilo);
    Mueble muebleObtenido = (Mueble) query.getSingleResult();
    this.entoncesElMuebleObtenidoEsCorrecto(muebleEsperado, muebleObtenido);
  }

  private void entoncesElMuebleObtenidoEsCorrecto(Mueble muebleEsperado, Mueble muebleObtenido) {
    assertThat(muebleObtenido.getNombre(), is(equalTo(muebleEsperado.getNombre())));
    assertThat(muebleObtenido.getPrecio(), is(equalTo(muebleEsperado.getPrecio())));
    assertThat(muebleObtenido.getEstilo(), is(equalTo(muebleEsperado.getEstilo())));
  }

  private void entoncesElMuebleObtenidoEsNull(Mueble muebleEsperado) {
    String hql = "FROM Mueble WHERE nombre = :nombre AND precio = :precio AND estilo = :estilo";
    Query query = this.sessionFactory.getCurrentSession().createQuery(hql, Mueble.class);
    query.setParameter("nombre", muebleEsperado.getNombre());
    query.setParameter("precio", muebleEsperado.getPrecio());
    query.setParameter("estilo", muebleEsperado.getEstilo());
    Mueble muebleObtenido = (Mueble) query.uniqueResult();
    assertNull(muebleObtenido);
  }
}
