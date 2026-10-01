package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.CategoriaDeMueble;
import com.tallerwebi.dominio.RepositorioCategoriaDeMueble;
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
public class RepositorioCategoriaDeMuebleTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioCategoriaDeMueble repositorioCategoriaDeMueble;

  @BeforeEach
  public void init() {
    repositorioCategoriaDeMueble = new RepositorioCategoriaDeMuebleImpl(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueHayCategoriasGuardadas_cuandoLasPido_entoncesLasObtengoOrdenadasPorNombre() {
    this.dadoQueExisteLaCategoria("Sofá", 2.1, 0.9);
    this.dadoQueExisteLaCategoria("Cama doble", 1.4, 1.9);
    this.dadoQueExisteLaCategoria("Mesa de comedor", 1.6, 0.9);

    List<CategoriaDeMueble> categorias = this.cuandoPidoTodasLasCategorias();

    assertThat(
      categorias.stream().map(CategoriaDeMueble::getNombre).toList(),
      contains("Cama doble", "Mesa de comedor", "Sofá")
    );
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueGuardeUnaCategoria_cuandoLasPido_entoncesConservaSusMedidasPromedio() {
    this.dadoQueExisteLaCategoria("Escritorio", 1.2, 0.6);

    CategoriaDeMueble escritorio = this.cuandoPidoTodasLasCategorias().get(0);

    assertThat(escritorio.getAnchoPromedio(), is(1.2));
    assertThat(escritorio.getProfundidadPromedio(), is(0.6));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueNoHayCategorias_cuandoLasPido_entoncesObtengoUnaListaVacia() {
    List<CategoriaDeMueble> categorias = this.cuandoPidoTodasLasCategorias();

    assertThat(categorias, is(empty()));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueGuardeUnaCategoria_cuandoLaBuscoPorSuId_entoncesLaObtengo() {
    this.dadoQueExisteLaCategoria("Sofá", 2.1, 0.9);
    CategoriaDeMueble cama = this.dadoQueExisteLaCategoria("Cama doble", 1.4, 1.9);

    CategoriaDeMueble encontrada = repositorioCategoriaDeMueble.buscarPorId(cama.getId());

    assertThat(encontrada.getNombre(), is("Cama doble"));
  }

  @Test
  @Transactional
  @Rollback
  public void dadoQueNoExisteLaCategoria_cuandoLaBuscoPorSuId_entoncesNoObtengoNada() {
    CategoriaDeMueble encontrada = repositorioCategoriaDeMueble.buscarPorId(999L);

    assertThat(encontrada, is(nullValue()));
  }

  private CategoriaDeMueble dadoQueExisteLaCategoria(
    String nombre,
    Double ancho,
    Double profundidad
  ) {
    CategoriaDeMueble categoria = new CategoriaDeMueble(nombre, ancho, profundidad);
    this.sessionFactory.getCurrentSession().persist(categoria);
    return categoria;
  }

  private List<CategoriaDeMueble> cuandoPidoTodasLasCategorias() {
    return repositorioCategoriaDeMueble.obtenerTodas();
  }
}
