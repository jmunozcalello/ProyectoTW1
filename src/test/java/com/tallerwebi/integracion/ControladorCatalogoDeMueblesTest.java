package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.ServicioCatalogo;
import com.tallerwebi.dominio.excepcion.MuebleNoEncontrado;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.presentacion.ControladorCatalogoDeMuebles;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorCatalogoDeMueblesTest {

  private static final String CATALOGO = "catalogo";
  private static final String REDIRECCION_AL_CATALOGO = "redirect:/catalogo";

  private ServicioCatalogo servicioCatalogo;
  private ControladorCatalogoDeMuebles controladorCatalogoDeMuebles;

  @BeforeEach
  public void init() {
    this.servicioCatalogo = mock(ServicioCatalogo.class);
    this.controladorCatalogoDeMuebles = new ControladorCatalogoDeMuebles(this.servicioCatalogo);
  }

  @Test
  public void dadoQueNoAplicoFiltros_cuandoNavegoAlCatalogo_entoncesVeoTodosLosMueblesDelCatalogo()
    throws Exception {
    Mueble retro = unMueble("Sillón Retró", 85000.0, Estilo.Retro);
    Mueble japandi = unMueble("Mesa Japandi", 150000.0, Estilo.Japandi);
    this.dadoQueElCatalogoCumpleCon(null, null, List.of(retro, japandi));

    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo(null, null);

    assertThat(modelAndView.getViewName(), is(CATALOGO));
    assertThat(this.obtenerMueblesDelModel(modelAndView), containsInAnyOrder(retro, japandi));
  }

  @Test
  public void dadoQueNavegoAlCatalogo_cuandoLoHago_entoncesReciboLosEstilosParaPoderFiltrar()
    throws Exception {
    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo(null, null);

    assertThat(modelAndView.getModel().get("estilos"), is(Estilo.values()));
  }

  @Test
  public void dadoQueNoAplicoFiltros_cuandoNavegoAlCatalogo_entoncesElModeloNoTraeFiltroDeEstiloNiDePrecio()
    throws Exception {
    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo(null, null);

    assertThat(modelAndView.getModel().get("estiloSeleccionado"), is(nullValue()));
    assertThat(modelAndView.getModel().get("precioMaximo"), is(nullValue()));
  }

  @Test
  public void dadoQueFiltroPorEstilo_cuandoNavegoAlCatalogo_entoncesLePidoAlCatalogoSoloEseEstilo()
    throws Exception {
    Mueble retro = unMueble("Sillón Retró", 85000.0, Estilo.Retro);
    this.dadoQueElCatalogoCumpleCon(Estilo.Retro, null, List.of(retro));

    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo("Retro", null);

    assertThat(this.obtenerMueblesDelModel(modelAndView), contains(retro));
    assertThat(modelAndView.getModel().get("estiloSeleccionado"), is(Estilo.Retro));
    verify(this.servicioCatalogo).ObtenerMueblesQueCumplan(Estilo.Retro, null);
  }

  @Test
  public void dadoQueFiltroPorPrecioMaximo_cuandoNavegoAlCatalogo_entoncesLePidoSoloLosMueblesQueNoLoSuperan()
    throws Exception {
    Mueble barato = unMueble("Lámpara de Pie Retro", 25000.0, Estilo.Retro);
    this.dadoQueElCatalogoCumpleCon(null, 50000.0, List.of(barato));

    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo(null, "50000");

    assertThat(this.obtenerMueblesDelModel(modelAndView), contains(barato));
    assertThat(modelAndView.getModel().get("precioMaximo"), is(50000.0));
    verify(this.servicioCatalogo).ObtenerMueblesQueCumplan(null, 50000.0);
  }

  @Test
  public void dadoQueFiltroPorEstiloYPrecioMaximo_cuandoNavegoAlCatalogo_entoncesLePidoLosQueCumplenAmbos()
    throws Exception {
    this.dadoQueElCatalogoCumpleCon(Estilo.Retro, 50000.0, List.of());

    this.cuandoNavegoAlCatalogo("Retro", "50000");

    verify(this.servicioCatalogo).ObtenerMueblesQueCumplan(Estilo.Retro, 50000.0);
  }

  @Test
  public void dadoQueElijoTodosLosEstilos_cuandoFiltroPorElCatalogo_entoncesNoFiltroPorEstilo()
    throws Exception {
    this.dadoQueElCatalogoCumpleCon(null, 50000.0, List.of());

    this.cuandoNavegoAlCatalogo("", "50000");

    verify(this.servicioCatalogo).ObtenerMueblesQueCumplan(null, 50000.0);
  }

  @Test
  public void dadoQueElCatalogoEstaVacio_cuandoNavegoAlCatalogo_entoncesVeoLaVistaSinMuebles()
    throws Exception {
    this.dadoQueElCatalogoCumpleCon(null, null, List.of());

    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo(null, null);

    assertThat(this.obtenerMueblesDelModel(modelAndView), empty());
  }

  @Test
  public void dadoQueElCatalogoRechazaElPrecioMaximo_cuandoNavegoAlCatalogo_entoncesVeoElErrorYNoLosMuebles()
    throws Exception {
    when(this.servicioCatalogo.ObtenerMueblesQueCumplan(Estilo.Retro, 50000.0))
      .thenThrow(new PresupuestoNegativoException("El presupuesto no puede ser negativo"));

    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo("Retro", "50000");

    assertThat(modelAndView.getViewName(), is(CATALOGO));
    assertThat(modelAndView.getModel().get("error"), is("El presupuesto no puede ser negativo"));
    assertThat(modelAndView.getModel().get("muebles"), is(nullValue()));
  }

  @Test
  public void dadoQueNavegoAlCatalogo_conUnEstiloQueNoExiste_entoncesVuelvoAlCatalogoSinFiltros()
    throws Exception {
    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo("NoExiste", null);

    this.entoncesVuelvoAlCatalogoSinConsultarElServicio(modelAndView);
  }

  @Test
  public void dadoQueNavegoAlCatalogo_conUnPrecioMaximoNegativo_entoncesVuelvoAlCatalogoSinFiltros()
    throws Exception {
    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo(null, "-1");

    this.entoncesVuelvoAlCatalogoSinConsultarElServicio(modelAndView);
  }

  @Test
  public void dadoQueNavegoAlCatalogo_conUnPrecioMaximoQueNoEsNumero_entoncesVuelvoAlCatalogoSinFiltros()
    throws Exception {
    ModelAndView modelAndView = this.cuandoNavegoAlCatalogo(null, "caro");

    this.entoncesVuelvoAlCatalogoSinConsultarElServicio(modelAndView);
  }

  @Test
  public void dadoQueNavegoAMuebles_cuandoLoHago_entoncesVeoLaVistaDeMuebles() throws Exception {
    ModelAndView modelAndView = this.controladorCatalogoDeMuebles.muebles();

    assertThat(modelAndView.getViewName(), is("muebles"));
  }

  @Test
  public void dadoQueNavegoACrearMueble_cuandoLoHago_entoncesVeoElFormularioDeCreacion()
    throws Exception {
    ModelAndView modelAndView = this.controladorCatalogoDeMuebles.crearMueble();

    assertThat(modelAndView.getViewName(), is("crearMueble"));
  }

  @Test
  public void dadoQueNavegoAlDetalleDeUnMueble_cuandoLoHago_entoncesVeoLosDatosDeEseMueble()
    throws Exception {
    Mueble retro = unMueble("Sillón Retró", 85000.0, Estilo.Retro);
    retro.setId(7);
    retro.setDescripcion("Sillón de voluteadas tapizado en terciopelo.");
    when(this.servicioCatalogo.ObtenerMueblePorId(7)).thenReturn(retro);

    ModelAndView modelAndView = this.controladorCatalogoDeMuebles.detalleMueble(7);

    assertThat(modelAndView.getViewName(), is("mueble"));
    assertThat(modelAndView.getModel().get("mueble"), is(retro));
  }

  @Test
  public void dadoQueNavegoAlDetalleDeUnMuebleQueNoExiste_cuandoLoHago_entoncesVuelvoAlCatalogo()
    throws Exception {
    when(this.servicioCatalogo.ObtenerMueblePorId(999))
      .thenThrow(new MuebleNoEncontrado("No existe el mueble 999"));

    ModelAndView modelAndView = this.controladorCatalogoDeMuebles.detalleMueble(999);

    assertThat(modelAndView.getViewName(), is(REDIRECCION_AL_CATALOGO));
  }

  private void entoncesVuelvoAlCatalogoSinConsultarElServicio(ModelAndView modelAndView)
    throws Exception {
    assertThat(modelAndView.getViewName(), is(REDIRECCION_AL_CATALOGO));
    verifyNoInteractions(this.servicioCatalogo);
  }

  private ModelAndView cuandoNavegoAlCatalogo(String estilo, String precioMaximo) {
    return this.controladorCatalogoDeMuebles.catalogo(estilo, precioMaximo);
  }

  private void dadoQueElCatalogoCumpleCon(Estilo estilo, Double precioMaximo, List<Mueble> muebles)
    throws Exception {
    when(this.servicioCatalogo.ObtenerMueblesQueCumplan(estilo, precioMaximo)).thenReturn(muebles);
  }

  private static Mueble unMueble(String nombre, double precio, Estilo estilo) {
    Mueble mueble = new Mueble();
    mueble.setNombre(nombre);
    mueble.setPrecio(precio);
    mueble.setEstilo(estilo);
    return mueble;
  }

  @SuppressWarnings("unchecked")
  private List<Mueble> obtenerMueblesDelModel(ModelAndView modelAndView) {
    return (List<Mueble>) modelAndView.getModel().get("muebles");
  }
}
