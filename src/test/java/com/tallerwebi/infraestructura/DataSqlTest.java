package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.tallerwebi.datos.HsqldbConDatosTestConfig;
import com.tallerwebi.dominio.CategoriaDeMueble;
import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.ServicioCatalogo;
import com.tallerwebi.dominio.ServicioCatalogoImpl;
import com.tallerwebi.dominio.TipoDeAmbiente;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
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

    assertThat(muebles, hasSize(18));
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

  @Test
  @Transactional
  public void dadoQueArrancaLaAplicacion_cuandoConsultoElCatalogo_entoncesTodosLosMueblesTienenTipoDeAmbiente() {
    List<String> mueblesSinTipo =
      this.obtenerMuebles()
        .stream()
        .filter(mueble -> mueble.getTiposDeAmbiente().isEmpty())
        .map(Mueble::getNombre)
        .toList();

    assertThat(mueblesSinTipo, hasSize(0));
  }

  @Test
  @Transactional
  public void dadoQueArrancaLaAplicacion_cuandoConsultoCadaTipoDeAmbiente_entoncesAlMenosTieneUnMueble() {
    for (String nombre : TipoDeAmbiente.nombresConocidos()) {
      boolean tieneMuebles =
        this.obtenerMuebles()
          .stream()
          .anyMatch(mueble ->
            mueble.getTiposDeAmbiente().stream().anyMatch(tipo -> nombre.equals(tipo.getNombre()))
          );

      assertThat("El tipo " + nombre + " necesita al menos un mueble", tieneMuebles, is(true));
    }
  }

  @Test
  @Transactional
  public void dadoQueLosMueblesSembrados_cuandoFiltroElCatalogoPorHabitacion_entoncesAparecenLasCamas()
    throws PresupuestoNegativoException {
    ServicioCatalogo servicioCatalogo = new ServicioCatalogoImpl(
      new RepositorioCatalogoImpl(this.sessionFactory)
    );

    List<String> nombresDeHabitacion = servicioCatalogo
      .ObtenerMueblesFiltrados(null, null, null, new TipoDeAmbiente("Habitación"), null)
      .stream()
      .map(Mueble::getNombre)
      .toList();

    assertThat(nombresDeHabitacion, hasItem("Cama Boho"));
    assertThat(nombresDeHabitacion, not(hasItem("Mesa de Comedor Japandi")));
  }

  @Test
  @Transactional
  public void dadoQueLosMueblesSembrados_cuandoFiltroPorUnRangoDePrecio_entoncesSoloTraeLosDelRango()
    throws PresupuestoNegativoException {
    ServicioCatalogo servicioCatalogo = new ServicioCatalogoImpl(
      new RepositorioCatalogoImpl(this.sessionFactory)
    );

    List<Mueble> rango = servicioCatalogo.ObtenerMueblesFiltrados(
      20000.0,
      50000.0,
      null,
      null,
      null
    );

    assertThat(rango, is(not(empty())));
    for (Mueble mueble : rango) {
      assertThat(mueble.getPrecio() >= 20000.0 && mueble.getPrecio() <= 50000.0, is(true));
    }
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
