package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Muro;
import com.tallerwebi.dominio.ServicioObstaculos;
import com.tallerwebi.dominio.TipoDeObstaculo;
import com.tallerwebi.dominio.excepcion.ValidacionException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Paso de obstáculos fijos del armado del plano. Recibe las medidas y los muebles del paso
 * anterior, los conserva en el formulario y, si los obstáculos son válidos, sigue a la
 * generación del plano con todos los datos.
 */
@Controller
public class ControladorObstaculos {

  private static final String VISTA = "obstaculos-config";

  private ServicioObstaculos servicioObstaculos;

  @Autowired
  public ControladorObstaculos(ServicioObstaculos servicioObstaculos) {
    this.servicioObstaculos = servicioObstaculos;
  }

  @RequestMapping(path = "/plano/obstaculos", method = RequestMethod.POST)
  public ModelAndView irACargarObstaculos(DatosPlano datosDelFormulario) {
    return new ModelAndView(VISTA, modeloDelPaso(datosDelFormulario));
  }

  @RequestMapping(path = "/plano/obstaculos/confirmar", method = RequestMethod.POST)
  public ModelAndView confirmarObstaculos(DatosPlano datosDelFormulario) {
    try {
      servicioObstaculos.validarObstaculos(datosDelFormulario.aAmbiente());
    } catch (ValidacionException e) {
      Map<String, Object> modelo = modeloDelPaso(datosDelFormulario);
      modelo.put("error", e.getMessage());
      return new ModelAndView(VISTA, modelo);
    }
    return new ModelAndView("forward:/plano/generar");
  }

  private Map<String, Object> modeloDelPaso(DatosPlano datosDelFormulario) {
    Map<String, Object> modelo = new ModelMap();
    modelo.put("datosPlano", datosDelFormulario);
    modelo.put("tiposDeObstaculo", TipoDeObstaculo.values());
    modelo.put("muros", Muro.values());
    return modelo;
  }
}
