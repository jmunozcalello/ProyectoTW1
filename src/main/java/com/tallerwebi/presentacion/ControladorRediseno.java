package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.ServicioCatalogo;
import com.tallerwebi.dominio.ServicioRediseño;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.presentacion.excepcion.FiltroDeMueblesInvalidoException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRediseno {

  private final ServicioCatalogo servicioCatalogo;
  private final ServicioRediseño servicioRediseño;

  private static final String PRESUPUESTO = "presupuesto";
  private static final String ESTILO = "estilo";

  @Autowired
  public ControladorRediseno(ServicioCatalogo servicioCatalogo, ServicioRediseño servicioRediseño) {
    this.servicioCatalogo = servicioCatalogo;
    this.servicioRediseño = servicioRediseño;
  }

  @RequestMapping(path = "/muebles/redisenar", method = RequestMethod.GET)
  public ModelAndView redisenar() {
    Map<String, Object> modelo = new ModelMap();
    modelo.put("estilos", Estilo.values());

    return new ModelAndView("redisenar", modelo);
  }

  @RequestMapping(path = "/muebles/redisenar", method = RequestMethod.POST)
  public ModelAndView generarPropuesta(
    @RequestParam(name = ESTILO, required = false) String estilo,
    @RequestParam(name = PRESUPUESTO, required = false) String presupuesto
  ) {
    FiltroDeMuebles propuesta;

    try {
      propuesta = FiltroDeMuebles.deUnaPropuesta(estilo, presupuesto);
    } catch (FiltroDeMueblesInvalidoException e) {
      return this.redirectAlFormulario();
    }

    return new ModelAndView("resultadosRediseno", this.modeloDelResultado(propuesta));
  }

  private ModelAndView redirectAlFormulario() {
    return new ModelAndView("redirect:/muebles/redisenar");
  }

  private Map<String, Object> modeloDelResultado(FiltroDeMuebles propuesta) {
    Map<String, Object> modelo = new ModelMap();
    modelo.put("propuesta", new DatosPropuesta(propuesta.getEstilo(), propuesta.getPrecioMaximo()));
    modelo.put("recomendacion", this.servicioRediseño.recomendar(propuesta.getEstilo()));
    List<Mueble> muebles = null;
    try {
      muebles =
        this.servicioCatalogo.ObtenerMueblesQueCumplan(
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
