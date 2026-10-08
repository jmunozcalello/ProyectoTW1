package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.Muro;
import com.tallerwebi.dominio.Plano;
import com.tallerwebi.dominio.ServicioCategoriaDeMueble;
import com.tallerwebi.dominio.ServicioPlano2D;
import com.tallerwebi.dominio.TipoDeObstaculo;
import com.tallerwebi.dominio.excepcion.ValidacionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPlano2D {

  private ServicioPlano2D servicioPlano2D;
  private ServicioCategoriaDeMueble servicioCategoriaDeMueble;

  @Autowired
  public ControladorPlano2D(
    ServicioPlano2D servicioPlano2D,
    ServicioCategoriaDeMueble servicioCategoriaDeMueble
  ) {
    this.servicioPlano2D = servicioPlano2D;
    this.servicioCategoriaDeMueble = servicioCategoriaDeMueble;
  }

  @RequestMapping(path = "/plano/ambiente", method = RequestMethod.GET)
  public ModelAndView irAConfigurarAmbiente() {
    Map<String, Object> modelo = new ModelMap();
    modelo.put("categorias", servicioCategoriaDeMueble.obtenerCategorias());
    modelo.put("tiposDeObstaculo", TipoDeObstaculo.values());
    modelo.put("muros", Muro.values());
    return new ModelAndView("ambiente-config", modelo);
  }

  public ModelAndView generarPlano(Ambiente ambiente, List<Mueble> muebles) {
    try {
      Plano plano = servicioPlano2D.generarPlano(ambiente, muebles);
      return new ModelAndView("plano-interactivo", "plano", plano);
    } catch (ValidacionException e) {
      return planoConError(e);
    }
  }

  private ModelAndView planoConError(ValidacionException error) {
    Map<String, Object> modelo = new ModelMap();
    modelo.put("error", error.getMessage());
    return new ModelAndView("plano-interactivo", modelo);
  }

  /**
   * Entrada HTTP del formulario. Spring entrega aca el {@link DatosPlano} ya bindeado desde el
   * request; este metodo arma los muebles con el servicio de categorías (HU-08: completa las
   * medidas vacías con el promedio) y delega en {@link #generarPlano}, que es quien decide la
   * vista y el modelo.
   */
  @RequestMapping(path = "/plano/generar", method = RequestMethod.POST)
  public ModelAndView generarPlanoDesdeFormulario(DatosPlano datosDelFormulario) {
    Ambiente ambiente = datosDelFormulario.aAmbiente();
    List<Mueble> muebles = new ArrayList<>();
    try {
      for (DatosMueble datosMueble : datosDelFormulario.getMuebles()) {
        muebles.add(
          servicioCategoriaDeMueble.crearMueble(
            datosMueble.getNombre(),
            datosMueble.getCategoria(),
            datosMueble.getAncho(),
            datosMueble.getProfundidad()
          )
        );
      }
    } catch (ValidacionException e) {
      return planoConError(e);
    }
    return generarPlano(ambiente, muebles);
  }
}
