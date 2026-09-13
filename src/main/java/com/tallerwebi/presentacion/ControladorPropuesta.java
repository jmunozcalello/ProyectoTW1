package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPropuesta {

  @RequestMapping(path = "/propuestas/{id}", method = RequestMethod.GET)
  public ModelAndView detallePropuesta(@PathVariable("id") Long id) {
    return new ModelAndView("propuesta");
  }

  @RequestMapping(path = "/ambientes/{id}/propuestas", method = RequestMethod.GET)
  public ModelAndView propuestasAmbiente(@PathVariable("id") Long id) {
    return new ModelAndView("propuestas");
  }
}
