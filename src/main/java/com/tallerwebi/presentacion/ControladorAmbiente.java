package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioAmbiente;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorAmbiente {

  private final ServicioAmbiente servicioAmbiente;

  @Autowired
  public ControladorAmbiente(ServicioAmbiente servicioAmbiente) {
    this.servicioAmbiente = servicioAmbiente;
  }

  @RequestMapping(path = "/ambientes", method = RequestMethod.GET)
  public ModelAndView ambientes(HttpServletRequest request) {
    Map<String, Object> modelo = new ModelMap();
    String email = (String) request.getSession().getAttribute("USUARIO_EMAIL");
    modelo.put("ambientes", servicioAmbiente.obtenerAmbientesDe(email));
    return new ModelAndView("ambientes", modelo);
  }

  @RequestMapping(path = "/ambientes/crear", method = RequestMethod.GET)
  public ModelAndView crearAmbiente() {
    return new ModelAndView("crearAmbiente");
  }

  @RequestMapping(path = "/ambientes/{id}", method = RequestMethod.GET)
  public ModelAndView detalleAmbiente(@PathVariable("id") Long id) {
    Map<String, Object> modelo = new ModelMap();
    modelo.put("ambiente", servicioAmbiente.buscarAmbiente(id));
    return new ModelAndView("ambiente", modelo);
  }
}
