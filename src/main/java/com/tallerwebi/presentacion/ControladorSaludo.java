package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class ControladorSaludo {

    @RequestMapping("/saludo")
    public ModelAndView irASaludo() {
        ModelAndView model = new ModelAndView("saludo");
        return model;
    }

    @RequestMapping("/saludar")
    public ModelAndView saludar(@RequestParam("nombre") String nombre) {
        Map<String, Object> model = new ModelMap();
        model.put("nombre", nombre);
        return new ModelAndView("saludar",model);

    }


}
