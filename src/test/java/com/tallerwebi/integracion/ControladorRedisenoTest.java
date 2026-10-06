package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.Color;
import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Iluminacion;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.Recomendacion;
import com.tallerwebi.dominio.ServicioCatalogo;
import com.tallerwebi.dominio.ServicioRediseño;
import com.tallerwebi.dominio.TipoDeAmbiente;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.presentacion.ControladorRediseno;
import com.tallerwebi.presentacion.DatosPropuesta;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorRedisenoTest {

  private static final String FORMULARIO = "redirect:/muebles/redisenar";
  private static final String RESULTADOS = "resultadosRediseno";
  private static final Double PRESUPUESTO = 60000.0;
  private static final Estilo ESTILO = Estilo.Japandi;

  private ServicioCatalogo servicioCatalogo;
  private ServicioRediseño servicioRediseño;
  private ControladorRediseno controladorRediseno;

  @BeforeEach
  public void init() {
    this.servicioCatalogo = mock(ServicioCatalogo.class);
    this.servicioRediseño = mock(ServicioRediseño.class);
    this.controladorRediseno =
      new ControladorRediseno(this.servicioCatalogo, this.servicioRediseño);
  }

  @Test
  public void dadoQueNavegoAlFormularioDeRediseno_cuandoLoHago_entoncesReciboLosEstilosParaElegir()
    throws Exception {
    ModelAndView modelAndView = this.controladorRediseno.redisenar();

    assertThat(modelAndView.getViewName(), is("redisenar"));
    assertThat(modelAndView.getModel().get("estilos"), is(Estilo.values()));
  }

  @Test
  public void dadoQueCompletoElFormularioDeRediseno_cuandoLoEnvio_entoncesVeoElResultadoConLoElegido()
    throws Exception {
    List<Mueble> muebles = new ArrayList<>();
    this.dadoQueElCatalogoCumpleCon(ESTILO, PRESUPUESTO, muebles);

    ModelAndView modelAndView = this.cuandoEnvioElFormulario(ESTILO, PRESUPUESTO);

    assertThat(modelAndView.getViewName(), is(RESULTADOS));
    DatosPropuesta propuesta = this.obtenerPropuestaDelModel(modelAndView);
    assertThat(propuesta.getEstilo(), is(ESTILO));
    assertThat(propuesta.getPresupuesto(), is(PRESUPUESTO));
  }

  @Test
  public void dadoQueEnvioElFormularioDeRediseno_cuandoLoHago_entoncesLePidoAlCatalogoLosMueblesDeLoElegido()
    throws Exception {
    List<Mueble> muebles = new ArrayList<>();
    this.dadoQueElCatalogoCumpleCon(ESTILO, PRESUPUESTO, muebles);

    this.cuandoEnvioElFormulario(ESTILO, PRESUPUESTO);

    verify(this.servicioCatalogo).ObtenerMueblesQueCumplan(null, ESTILO, PRESUPUESTO);
    verify(this.servicioRediseño).recomendar(ESTILO);
  }

  @Test
  public void dadoUnMuebleQueCumpleConLoElegido_cuandoVeoElResultado_entoncesVeoEseMueble()
    throws Exception {
    Mueble affordable = unMueble("Sillón Japandi", 60000.0, ESTILO);
    List<Mueble> muebles = new ArrayList<>();
    muebles.add(affordable);
    this.dadoQueElCatalogoCumpleCon(ESTILO, PRESUPUESTO, muebles);

    ModelAndView modelAndView = this.cuandoEnvioElFormulario(ESTILO, PRESUPUESTO);

    assertThat(this.obtenerMueblesDelModel(modelAndView), contains(affordable));
  }

  @Test
  public void dadoQueNingunMuebleCumpleConLoElegido_cuandoVeoElResultado_entoncesVeoLaVistaSinMuebles()
    throws Exception {
    List<Mueble> muebles = new ArrayList<>();
    this.dadoQueElCatalogoCumpleCon(ESTILO, PRESUPUESTO, muebles);

    ModelAndView modelAndView = this.cuandoEnvioElFormulario(ESTILO, PRESUPUESTO);

    assertThat(this.obtenerMueblesDelModel(modelAndView), empty());
  }

  @Test
  public void dadoQueEnvioElFormularioDeRediseno_cuandoLoHago_entoncesElModeloTraeLaPaletaYLaIluminacionRecomendadas()
    throws Exception {
    ArrayList<Color> colores = new ArrayList<>();
    colores.add(new Color("Arena", "#E6D5BC"));
    colores.add(new Color("Madera clara", "#C8A27A"));
    colores.add(new Color("Gris", "#9E9E9E"));
    Recomendacion recomendacion = new Recomendacion(ESTILO, colores, Iluminacion.CALIDA);
    when(this.servicioRediseño.recomendar(ESTILO)).thenReturn(recomendacion);
    List<Mueble> muebles = new ArrayList<>();
    this.dadoQueElCatalogoCumpleCon(ESTILO, PRESUPUESTO, muebles);

    ModelAndView modelAndView = this.cuandoEnvioElFormulario(ESTILO, PRESUPUESTO);

    Recomendacion recomendacionObtenida = (Recomendacion) modelAndView
      .getModel()
      .get("recomendacion");
    assertThat(recomendacionObtenida.getEstilo(), is(ESTILO));
    assertThat(recomendacionObtenida.getColores(), hasSize(3));
    assertThat(recomendacionObtenida.getIluminacion(), is(Iluminacion.CALIDA));
  }

  @Test
  public void dadoQueElijoOtroEstilo_cuandoVeoElResultado_entoncesCambiaLaIluminacionRecomendada()
    throws Exception {
    Recomendacion recomendacion = new Recomendacion(
      Estilo.Industrial,
      new ArrayList<>(),
      Iluminacion.FRIA
    );
    when(this.servicioRediseño.recomendar(Estilo.Industrial)).thenReturn(recomendacion);
    List<Mueble> muebles = new ArrayList<>();
    this.dadoQueElCatalogoCumpleCon(Estilo.Industrial, PRESUPUESTO, muebles);

    ModelAndView modelAndView = this.cuandoEnvioElFormulario(Estilo.Industrial, PRESUPUESTO);

    Recomendacion recomendacionObtenida = (Recomendacion) modelAndView
      .getModel()
      .get("recomendacion");

    assertThat(recomendacionObtenida.getEstilo(), is(Estilo.Industrial));
    assertThat(recomendacionObtenida.getIluminacion(), is(Iluminacion.FRIA));
  }

  @Test
  public void dadoQueElCatalogoRechazaElPresupuesto_cuandoVeoElResultado_entoncesVeoElErrorYNoLosMuebles()
    throws Exception {
    when(this.servicioRediseño.recomendar(ESTILO)).thenReturn(this.unaRecomendacion(ESTILO));
    when(this.servicioCatalogo.ObtenerMueblesQueCumplan(null, ESTILO, PRESUPUESTO))
      .thenThrow(new PresupuestoNegativoException("El presupuesto no puede ser negativo"));

    ModelAndView modelAndView = this.cuandoEnvioElFormulario(ESTILO, PRESUPUESTO);

    assertThat(modelAndView.getViewName(), is(RESULTADOS));
    assertThat(modelAndView.getModel().get("error"), is("El presupuesto no puede ser negativo"));
    assertThat(modelAndView.getModel().get("muebles"), is(nullValue()));
  }

  @Test
  public void dadoQueElijoUnTipoDeAmbiente_cuandoEnvioElFormulario_entoncesSeLoPidoAlCatalogo()
    throws Exception {
    TipoDeAmbiente living = new TipoDeAmbiente("Living");
    List<Mueble> muebles = new ArrayList<>();
    when(this.servicioCatalogo.ObtenerMueblesQueCumplan(living, ESTILO, PRESUPUESTO))
      .thenReturn(muebles);

    ModelAndView modelAndView =
      this.controladorRediseno.generarPropuesta(
          ESTILO.name(),
          String.valueOf(PRESUPUESTO),
          "Living"
        );

    verify(this.servicioCatalogo).ObtenerMueblesQueCumplan(living, ESTILO, PRESUPUESTO);
    assertThat(modelAndView.getViewName(), is(RESULTADOS));
  }

  @Test
  public void dadoQueEnvioElFormularioDeRediseno_conUnEstiloQueNoExiste_entoncesVuelvoAlFormulario()
    throws Exception {
    ModelAndView modelAndView = this.cuandoEnvioElFormularioConTexto("NoExiste", "60000");

    this.entoncesVuelvoAlFormulario(modelAndView);
  }

  @Test
  public void dadoQueEnvioElFormularioDeRediseno_sinPresupuesto_entoncesVuelvoAlFormulario()
    throws Exception {
    ModelAndView modelAndView = this.cuandoEnvioElFormularioConTexto("Japandi", null);

    this.entoncesVuelvoAlFormulario(modelAndView);
  }

  @Test
  public void dadoQueEnvioElFormularioDeRediseno_conUnPresupuestoNegativo_entoncesVuelvoAlFormulario()
    throws Exception {
    ModelAndView modelAndView = this.cuandoEnvioElFormularioConTexto("Japandi", "-1");

    this.entoncesVuelvoAlFormulario(modelAndView);
  }

  @Test
  public void dadoQueEnvioElFormularioDeRediseno_sinParametros_entoncesVuelvoAlFormulario()
    throws Exception {
    ModelAndView modelAndView = this.cuandoEnvioElFormularioConTexto(null, null);

    this.entoncesVuelvoAlFormulario(modelAndView);
  }

  private void entoncesVuelvoAlFormulario(ModelAndView modelAndView) {
    assertThat(modelAndView.getViewName(), is(FORMULARIO));
    verifyNoInteractions(this.servicioCatalogo, this.servicioRediseño);
  }

  private ModelAndView cuandoEnvioElFormularioConTexto(String estilo, String presupuesto) {
    return this.controladorRediseno.generarPropuesta(estilo, presupuesto, null);
  }

  private ModelAndView cuandoEnvioElFormulario(Estilo estilo, Double presupuesto) {
    return this.cuandoEnvioElFormularioConTexto(estilo.name(), String.valueOf(presupuesto));
  }

  private void dadoQueElCatalogoCumpleCon(Estilo estilo, Double presupuesto, List<Mueble> muebles)
    throws Exception {
    when(this.servicioCatalogo.ObtenerMueblesQueCumplan(null, estilo, presupuesto))
      .thenReturn(muebles);
  }

  private Recomendacion unaRecomendacion(Estilo estilo) {
    return new Recomendacion(estilo, new ArrayList<>(), Iluminacion.CALIDA);
  }

  private static Mueble unMueble(String nombre, double precio, Estilo estilo) {
    Mueble mueble = new Mueble();
    mueble.setNombre(nombre);
    mueble.setPrecio(precio);
    mueble.SetEstilo(estilo);
    return mueble;
  }

  @SuppressWarnings("unchecked")
  private List<Mueble> obtenerMueblesDelModel(ModelAndView modelAndView) {
    return (List<Mueble>) modelAndView.getModel().get("muebles");
  }

  private DatosPropuesta obtenerPropuestaDelModel(ModelAndView modelAndView) {
    return (DatosPropuesta) modelAndView.getModel().get("propuesta");
  }
}
