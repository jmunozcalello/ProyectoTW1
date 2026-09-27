package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.ServicioCatalogo;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRediseno {

  private final ServicioCatalogo servicioCatalogo;

  private static final String PRESUPUESTO = "presupuesto";
  private static final String ESTILO = "estilo";

  @Autowired
  public ControladorRediseno(ServicioCatalogo servicioCatalogo) {
    this.servicioCatalogo = servicioCatalogo;
  }

  @RequestMapping(path = "/muebles/redisenar", method = RequestMethod.GET)
  public ModelAndView redisenar() {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("estilos", Estilo.values());

    return new ModelAndView("redisenar", modelo);
  }

  @RequestMapping(path = "/muebles/redisenar", method = RequestMethod.POST)
  public ModelAndView generarPropuesta(
    @RequestParam(name = ESTILO, required = false) String estilo,
    @RequestParam(name = PRESUPUESTO, required = false) String presupuesto
  ) {
    Optional<FiltroDeMuebles> propuesta = FiltroDeMuebles.deUnaPropuesta(estilo, presupuesto);

    if (propuesta.isEmpty()) {
      return new ModelAndView("redirect:/muebles/redisenar");
    }

    return new ModelAndView(this.urlDelResultado(propuesta.get()));
  }

  @RequestMapping(path = "/muebles/redisenar/resultado", method = RequestMethod.GET)
  public ModelAndView resultadoRediseno(
    @RequestParam(name = ESTILO, required = false) String estilo,
    @RequestParam(name = PRESUPUESTO, required = false) String presupuesto
  ) throws PresupuestoNegativoException {
    Optional<FiltroDeMuebles> propuesta = FiltroDeMuebles.deUnaPropuesta(estilo, presupuesto);

    if (propuesta.isEmpty()) {
      return new ModelAndView("redirect:/muebles/redisenar");
    }

    return new ModelAndView("resultadosRediseno", this.modeloDelResultado(propuesta.get()));
  }

  private String urlDelResultado(FiltroDeMuebles propuesta) {
    return (
      "redirect:/muebles/redisenar/resultado?" +
      ESTILO +
      "=" +
      propuesta.getEstilo().name() +
      "&" +
      PRESUPUESTO +
      "=" +
      propuesta.getPrecioMaximo()
    );
  }

  private Map<String, Object> modeloDelResultado(FiltroDeMuebles propuesta)
    throws PresupuestoNegativoException {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put(ESTILO, propuesta.getEstilo());
    modelo.put(PRESUPUESTO, propuesta.getPrecioMaximo());
    modelo.put(
      "muebles",
      this.servicioCatalogo.ObtenerMueblesQueCumplan(
          propuesta.getEstilo(),
          propuesta.getPrecioMaximo()
        )
    );

    return modelo;
  }
}
