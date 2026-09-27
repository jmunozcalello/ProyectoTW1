package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.Estilo;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class FiltroDeMueblesTest {

  @Test
  public void dadoQueNoVieneNingunParametro_cuandoCreoElFiltroDelCatalogo_entoncesNoFiltraPorNada() {
    Optional<FiltroDeMuebles> filtro = FiltroDeMuebles.delCatalogo(null, null);

    assertThat(filtro.isPresent(), is(true));
    assertThat(filtro.get().getEstilo(), is(nullValue()));
    assertThat(filtro.get().getPrecioMaximo(), is(nullValue()));
  }

  @Test
  public void dadoQueVieneElEstiloEnBlanco_cuandoCreoElFiltroDelCatalogo_entoncesNoFiltraPorEstilo() {
    Optional<FiltroDeMuebles> filtro = FiltroDeMuebles.delCatalogo("", "50000");

    assertThat(filtro.isPresent(), is(true));
    assertThat(filtro.get().getEstilo(), is(nullValue()));
    assertThat(filtro.get().getPrecioMaximo(), is(50000.0));
  }

  @Test
  public void dadoQueVieneUnEstiloConocido_cuandoCreoElFiltroDelCatalogo_entoncesFiltraPorEseEstilo() {
    Optional<FiltroDeMuebles> filtro = FiltroDeMuebles.delCatalogo("Boho", null);

    assertThat(filtro.get().getEstilo(), is(Estilo.Boho));
    assertThat(filtro.get().getPrecioMaximo(), is(nullValue()));
  }

  @Test
  public void dadoQueVieneUnEstiloDesconocido_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThat(FiltroDeMuebles.delCatalogo("NoExiste", "50000").isPresent(), is(false));
  }

  @Test
  public void dadoQueVieneSoloUnPrecioMaximo_cuandoCreoElFiltroDelCatalogo_entoncesFiltraPorPrecioYNoPorEstilo() {
    Optional<FiltroDeMuebles> filtro = FiltroDeMuebles.delCatalogo(null, "50000");

    assertThat(filtro.get().getEstilo(), is(nullValue()));
    assertThat(filtro.get().getPrecioMaximo(), is(50000.0));
  }

  @Test
  public void dadoQueVieneUnPrecioMaximoNegativo_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThat(FiltroDeMuebles.delCatalogo("Boho", "-1").isPresent(), is(false));
  }

  @Test
  public void dadoQueVieneUnPrecioMaximoQueNoEsNumero_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThat(FiltroDeMuebles.delCatalogo("Boho", "caro").isPresent(), is(false));
  }

  @Test
  public void dadoUnaPropuestaConEstiloYPresupuesto_cuandoLaCreo_entoncesFiltraPorEstiloYTopeDePrecio() {
    Optional<FiltroDeMuebles> propuesta = FiltroDeMuebles.deUnaPropuesta("Japandi", "60000");

    assertThat(propuesta.isPresent(), is(true));
    assertThat(propuesta.get().getEstilo(), is(Estilo.Japandi));
    assertThat(propuesta.get().getPrecioMaximo(), is(60000.0));
  }

  @Test
  public void dadoUnaPropuestaSinPresupuesto_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThat(FiltroDeMuebles.deUnaPropuesta("Japandi", null).isPresent(), is(false));
  }

  @Test
  public void dadoUnaPropuestaSinEstilo_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThat(FiltroDeMuebles.deUnaPropuesta(null, "60000").isPresent(), is(false));
  }

  @Test
  public void dadoUnaPropuestaConUnEstiloDesconocido_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThat(FiltroDeMuebles.deUnaPropuesta("NoExiste", "60000").isPresent(), is(false));
  }

  @Test
  public void dadoUnaPropuestaConUnPresupuestoNegativo_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThat(FiltroDeMuebles.deUnaPropuesta("Japandi", "-1").isPresent(), is(false));
  }

  @Test
  public void dadoUnaPropuestaConUnPresupuestoQueNoEsNumero_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThat(FiltroDeMuebles.deUnaPropuesta("Japandi", "mucho").isPresent(), is(false));
  }

  @Test
  public void dadoUnFiltroDelCatalogo_cuandoConsultoSusCriterios_entornoLosMismoValoresDelFormulario() {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo("Retro", "85000").orElse(null);

    assertThat(filtro, is(notNullValue()));
    assertThat(filtro.getEstilo(), is(Estilo.Retro));
    assertThat(filtro.getPrecioMaximo(), is(85000.0));
  }
}
