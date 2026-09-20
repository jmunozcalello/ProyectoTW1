package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorAmbiente {

  @RequestMapping(path = "/ambientes", method = RequestMethod.GET)
  public ModelAndView ambientes() {
    return new ModelAndView("ambientes");
  }

  @RequestMapping(path = "/ambientes/crear", method = RequestMethod.GET)
  public ModelAndView crearAmbiente() {
    return new ModelAndView("crearAmbiente");
  }

  @RequestMapping(path = "/ambientes/{id}", method = RequestMethod.GET)
  public ModelAndView detalleAmbiente(@PathVariable("id") Long id) {
    return new ModelAndView("ambiente");
  }
}
