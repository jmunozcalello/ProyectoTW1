package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.ServicioAmbiente;
import com.tallerwebi.dominio.ServicioCatalogo;
import com.tallerwebi.dominio.ServicioRediseño;
import com.tallerwebi.dominio.TipoDeAmbiente;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.presentacion.excepcion.FiltroDeMueblesInvalidoException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRediseno {

  private final ServicioCatalogo servicioCatalogo;
  private final ServicioRediseño servicioRediseño;
  private final ServicioAmbiente servicioAmbiente;

  private static final String PRESUPUESTO = "presupuesto";
  private static final String ESTILO = "estilo";
  private static final String AMBIENTE = "ambiente";

  @Autowired
  public ControladorRediseno(
    ServicioCatalogo servicioCatalogo,
    ServicioRediseño servicioRediseño,
    ServicioAmbiente servicioAmbiente
  ) {
    this.servicioCatalogo = servicioCatalogo;
    this.servicioRediseño = servicioRediseño;
    this.servicioAmbiente = servicioAmbiente;
  }

  @RequestMapping(path = "/ambientes/{id}/redisenar", method = RequestMethod.GET)
  public ModelAndView redisenar(@PathVariable("id") Long id) {
    Map<String, Object> modelo = new ModelMap();
    modelo.put("estilos", Estilo.values());
    modelo.put(AMBIENTE, this.servicioAmbiente.buscarAmbiente(id));

    return new ModelAndView("redisenar", modelo);
  }

  @RequestMapping(path = "/ambientes/{id}/redisenar", method = RequestMethod.POST)
  public ModelAndView generarPropuesta(
    @PathVariable("id") Long id,
    @RequestParam(name = ESTILO, required = false) String estilo,
    @RequestParam(name = PRESUPUESTO, required = false) String presupuesto
  ) {
    FiltroDeMuebles propuesta;

    try {
      propuesta = FiltroDeMuebles.deUnaPropuesta(estilo, presupuesto);
    } catch (FiltroDeMueblesInvalidoException e) {
      return this.redirectAlFormulario(id);
    }

    return new ModelAndView("resultadosRediseno", this.modeloDelResultado(id, propuesta));
  }

  private ModelAndView redirectAlFormulario(Long id) {
    return new ModelAndView("redirect:/ambientes/" + id + "/redisenar");
  }

  private Map<String, Object> modeloDelResultado(Long id, FiltroDeMuebles propuesta) {
    Ambiente ambiente = this.servicioAmbiente.buscarAmbiente(id);
    TipoDeAmbiente tipo = ambiente.getTipoDeAmbiente();
    Map<String, Object> modelo = new ModelMap();

    modelo.put("propuesta", new DatosPropuesta(propuesta.getEstilo(), propuesta.getPrecioMaximo()));
    modelo.put(AMBIENTE, ambiente);
    modelo.put("tipoDeAmbiente", tipo == null ? null : tipo.getNombre());
    modelo.put("recomendacion", this.servicioRediseño.recomendar(propuesta.getEstilo()));

    List<Mueble> muebles = null;

    try {
      muebles =
        this.servicioCatalogo.ObtenerMueblesQueCumplan(
            null,
            propuesta.getEstilo(),
            propuesta.getPrecioMaximo()
          );
    } catch (PresupuestoNegativoException e) {
      modelo.put("error", "El presupuesto no puede ser negativo");
    }
    modelo.put("muebles", muebles);

    return modelo;
  }
}
