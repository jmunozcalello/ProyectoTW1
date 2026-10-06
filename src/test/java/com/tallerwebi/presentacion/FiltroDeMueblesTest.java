package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.OrdenDeMuebles;
import com.tallerwebi.presentacion.excepcion.FiltroDeMueblesInvalidoException;
import org.junit.jupiter.api.Test;

public class FiltroDeMueblesTest {

  @Test
  public void dadoQueNoVieneNingunParametro_cuandoCreoElFiltroDelCatalogo_entoncesNoFiltraPorNada()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo(null, null, null, null, null);

    assertThat(filtro.getEstilo(), is(nullValue()));
    assertThat(filtro.getPrecioMaximo(), is(nullValue()));
  }

  @Test
  public void dadoQueVieneElEstiloEnBlanco_cuandoCreoElFiltroDelCatalogo_entoncesNoFiltraPorEstilo()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo("", "50000", null, null, null);

    assertThat(filtro.getEstilo(), is(nullValue()));
    assertThat(filtro.getPrecioMaximo(), is(50000.0));
  }

  @Test
  public void dadoQueVieneUnEstiloConocido_cuandoCreoElFiltroDelCatalogo_entoncesFiltraPorEseEstilo()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo("Boho", null, null, null, null);

    assertThat(filtro.getEstilo(), is(Estilo.Boho));
    assertThat(filtro.getPrecioMaximo(), is(nullValue()));
  }

  @Test
  public void dadoQueVieneUnEstiloDesconocido_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.delCatalogo("NoExiste", "50000", null, null, null)
    );
  }

  @Test
  public void dadoQueVieneSoloUnPrecioMaximo_cuandoCreoElFiltroDelCatalogo_entoncesFiltraPorPrecioYNoPorEstilo()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo(null, "50000", null, null, null);

    assertThat(filtro.getEstilo(), is(nullValue()));
    assertThat(filtro.getPrecioMaximo(), is(50000.0));
  }

  @Test
  public void dadoQueVieneUnPrecioMaximoNegativo_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.delCatalogo("Boho", "-1", null, null, null)
    );
  }

  @Test
  public void dadoQueVieneUnPrecioMaximoQueNoEsNumero_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.delCatalogo("Boho", "caro", null, null, null)
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
  public void dadoQueVieneUnPrecioMinimo_cuandoCreoElFiltroDelCatalogo_entoncesFiltraDesdeEsePrecio()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo(null, null, "20000", null, null);

    assertThat(filtro.getPrecioMinimo(), is(20000.0));
    assertThat(filtro.getPrecioMaximo(), is(nullValue()));
  }

  @Test
  public void dadoQueVieneUnPrecioMinimoNegativo_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.delCatalogo(null, null, "-1", null, null)
    );
  }

  @Test
  public void dadoQueVieneUnTipoDeAmbienteConocido_cuandoCreoElFiltroDelCatalogo_entoncesFiltraPorEseTipo()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo(null, null, null, "Living", null);

    assertThat(filtro.getTipoDeAmbiente(), is("Living"));
  }

  @Test
  public void dadoQueVieneUnTipoDeAmbienteDesconocido_cuandoCreoElFiltroDelCatalogo_entoncesSeCreaElFiltroConEseTipo()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo(null, null, null, "Garaje", null);

    assertThat(filtro.getTipoDeAmbiente(), is("Garaje"));
  }

  @Test
  public void dadoQueVieneUnOrdenValido_cuandoCreoElFiltroDelCatalogo_entoncesOrdenaPorEseCriterio()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo(null, null, null, null, "PRECIO_DESC");

    assertThat(filtro.getOrden(), is(OrdenDeMuebles.PRECIO_DESC));
  }

  @Test
  public void dadoQueVieneUnOrdenDesconocido_cuandoCreoElFiltroDelCatalogo_entoncesNoHayFiltroValido() {
    assertThrows(
      FiltroDeMueblesInvalidoException.class,
      () -> FiltroDeMuebles.delCatalogo(null, null, null, null, "CREADO")
    );
  }

  @Test
  public void dadoUnFiltroDelCatalogo_cuandoConsultoSusCriterios_entornoLosMismoValoresDelFormulario()
    throws FiltroDeMueblesInvalidoException {
    FiltroDeMuebles filtro = FiltroDeMuebles.delCatalogo("Retro", "85000", null, null, null);

    assertThat(filtro.getEstilo(), is(Estilo.Retro));
    assertThat(filtro.getPrecioMaximo(), is(85000.0));
  }
}
