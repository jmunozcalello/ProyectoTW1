package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.tallerwebi.datos.HsqldbConDatosTestConfig;
import com.tallerwebi.dominio.CategoriaDeMueble;
import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import jakarta.transaction.Transactional;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HsqldbConDatosTestConfig.class })
public class DataSqlTest {

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  @Transactional
  public void dadoQueArrancaLaAplicacion_cuandoConsultoElCatalogo_entoncesEstanLosMueblesSembrados() {
    List<Mueble> muebles = this.obtenerMuebles();

    assertThat(muebles, hasSize(9));
    assertThat(
      muebles.stream().map(Mueble::getEstilo).distinct().toList(),
      containsInAnyOrder(Estilo.values())
    );
  }

  @Test
  @Transactional
  public void dadoQueArrancaLaAplicacion_cuandoConsultoUnMueblePorId_entoncesElEstiloSeGuardaComoTexto() {
    Mueble primerMueble =
      this.sessionFactory.getCurrentSession()
        .createQuery("FROM Mueble WHERE id = :id", Mueble.class)
        .setParameter("id", 1)
        .getSingleResult();

    assertThat(primerMueble.getNombre(), is("Sillón Retró"));
    assertThat(primerMueble.getPrecio(), is(85000.0));
    assertThat(primerMueble.getEstilo(), is(Estilo.Retro));
  }

  @Test
  @Transactional
  public void dadoQueArrancaLaAplicacion_cuandoConsultoElCatalogo_entoncesTodosLosMueblesTienenDescripcion() {
    List<String> descripcionesVacias =
      this.obtenerMuebles()
        .stream()
        .filter(mueble -> mueble.getDescripcion() == null || mueble.getDescripcion().isBlank())
        .map(Mueble::getNombre)
        .toList();

    assertThat(descripcionesVacias, hasSize(0));
  }

  @Test
  @Transactional
  public void dadoQueArrancaLaAplicacion_cuandoConsultoLasCategorias_entoncesEstanSembradas() {
    assertThat(this.obtenerCategorias(), is(not(empty())));
  }

  @Test
  @Transactional
  public void dadoQueArrancaLaAplicacion_cuandoConsultoLasCategorias_entoncesTodasTienenMedidasPositivas() {
    List<String> categoriasConMedidasInvalidas =
      this.obtenerCategorias()
        .stream()
        .filter(categoria ->
          !esPositivo(categoria.getAnchoPromedio()) ||
          !esPositivo(categoria.getProfundidadPromedio())
        )
        .map(CategoriaDeMueble::getNombre)
        .toList();

    assertThat(categoriasConMedidasInvalidas, hasSize(0));
  }

  private List<CategoriaDeMueble> obtenerCategorias() {
    return this.sessionFactory.getCurrentSession()
      .createQuery("FROM CategoriaDeMueble", CategoriaDeMueble.class)
      .getResultList();
  }

  private static boolean esPositivo(Double medida) {
    return medida != null && medida > 0;
  }

  private List<Mueble> obtenerMuebles() {
    return this.sessionFactory.getCurrentSession()
      .createQuery("FROM Mueble", Mueble.class)
      .getResultList();
  }
}
