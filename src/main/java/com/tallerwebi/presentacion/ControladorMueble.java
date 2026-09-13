package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorMueble {

  @RequestMapping(path = "/muebles", method = RequestMethod.GET)
  public ModelAndView muebles() {
    return new ModelAndView("muebles");
  }

  @RequestMapping(path = "/muebles/crear", method = RequestMethod.GET)
  public ModelAndView crearMueble() {
    return new ModelAndView("crearMueble");
  }

  @RequestMapping(path = "/muebles/redisenar", method = RequestMethod.GET)
  public ModelAndView redisenar() {
    return new ModelAndView("redisenar");
  }

  @RequestMapping(path = "/muebles/{id}", method = RequestMethod.GET)
  public ModelAndView detalleMueble(@PathVariable("id") Long id) {
    return new ModelAndView("mueble");
  }
}
