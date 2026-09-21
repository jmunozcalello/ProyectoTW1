package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPrecioFinal;
import com.tallerwebi.dominio.excepcion.TipoClienteInvalidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPrecioFinal {

    ServicioPrecioFinal servicioPrecioFinal;

    @Autowired
    public ControladorPrecioFinal(ServicioPrecioFinal servicioPrecioFinal) {
        this.servicioPrecioFinal = servicioPrecioFinal;
    }


    public ModelAndView calcularPrecio(double precio, String tipoCliente) {
        ModelMap model = new ModelMap();
        try{
            Double precioCalculado = this.servicioPrecioFinal.calcularPrecio(precio, tipoCliente);
            model.put("precioFinal", precioCalculado);
            return new ModelAndView("resultado", model);

        }catch(TipoClienteInvalidoException e){
            model.put("mensajeError","No existe el tipo de cliente");
            return new ModelAndView("listado-clases",model);
        }



    }
}
