package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.presentacion.excepcion.FiltroDeMueblesInvalidoException;
import org.junit.jupiter.api.Test;

public class FiltroDeMueblesTest {

  @Test
  public void dadoQueNoVieneNingunParametro_cuandoCreoElFiltroDelCatalogo_entoncesNoFiltraPorNada()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo(null, null);

    assertThat(filtro.getEstilo(), is(nullValue()));
    assertThat(filtro.getPrecioMaximo(), is(nullValue()));
  }

  @Test
  public void dadoQueVieneElEstiloEnBlanco_cuandoCreoElFiltroDelCatalogo_entoncesNoFiltraPorEstilo()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo("", "50000");

    assertThat(filtro.getEstilo(), is(nullValue()));
    assertThat(filtro.getPrecioMaximo(), is(50000.0));
  }

  @Test
  public void dadoQueVieneUnEstiloConocido_cuandoCreoElFiltroDelCatalogo_entoncesFiltraPorEseEstilo()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo("Boho", null);

    assertThat(filtro.getEstilo(), is(Estilo.Boho));
    assertThat(filtro.getPrecioMaximo(), is(nullValue()));
  }

  @Test
  public void dadoQueVieneUnEstiloDesconocido_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.delCatalogo("NoExiste", "50000")
    );
  }

  @Test
  public void dadoQueVieneSoloUnPrecioMaximo_cuandoCreoElFiltroDelCatalogo_entoncesFiltraPorPrecioYNoPorEstilo()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo(null, "50000");

    assertThat(filtro.getEstilo(), is(nullValue()));
    assertThat(filtro.getPrecioMaximo(), is(50000.0));
  }

  @Test
  public void dadoQueVieneUnPrecioMaximoNegativo_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.delCatalogo("Boho", "-1")
    );
  }

  @Test
  public void dadoQueVieneUnPrecioMaximoQueNoEsNumero_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.delCatalogo("Boho", "caro")
    );
  }

  @Test
  public void dadoUnaPropuestaConEstiloYPresupuesto_cuandoLaCreo_entoncesFiltraPorEstiloYTopeDePrecio()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles propuesta = FiltroDeMuebles.deUnaPropuesta("Japandi", "60000");

    assertThat(propuesta.getEstilo(), is(Estilo.Japandi));
    assertThat(propuesta.getPrecioMaximo(), is(60000.0));
  }

  @Test
  public void dadoUnaPropuestaSinPresupuesto_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.deUnaPropuesta("Japandi", null)
    );
  }

  @Test
  public void dadoUnaPropuestaSinEstilo_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.deUnaPropuesta(null, "60000")
    );
  }

  @Test
  public void dadoUnaPropuestaConUnEstiloDesconocido_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.deUnaPropuesta("NoExiste", "60000")
    );
  }

  @Test
  public void dadoUnaPropuestaConUnPresupuestoNegativo_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.deUnaPropuesta("Japandi", "-1")
    );
  }

  @Test
  public void dadoUnaPropuestaConUnPresupuestoQueNoEsNumero_cuandoLaCreo_entoncesNoHayPropuestaValida() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.deUnaPropuesta("Japandi", "mucho")
    );
  }

  @Test
  public void dadoUnFiltroDelCatalogo_cuandoConsultoSusCriterios_entornoLosMismoValoresDelFormulario()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo("Retro", "85000");

    assertThat(filtro.getEstilo(), is(Estilo.Retro));
    assertThat(filtro.getPrecioMaximo(), is(85000.0));
  }
}
