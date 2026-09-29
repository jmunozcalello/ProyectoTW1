package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.ServicioCatalogo;
import com.tallerwebi.dominio.excepcion.MuebleNoEncontrado;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.presentacion.excepcion.FiltroDeMueblesInvalidoException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorCatalogoDeMuebles {

  private final ServicioCatalogo servicioCatalogo;

  @Autowired
  public ControladorCatalogoDeMuebles(ServicioCatalogo servicioCatalogo) {
    this.servicioCatalogo = servicioCatalogo;
  }

  @RequestMapping(path = "/catalogo", method = RequestMethod.GET)
  public ModelAndView catalogo(
    @RequestParam(name = "estilo", required = false) String estilo,
    @RequestParam(name = "precioMax", required = false) String precioMaximo
  ) throws PresupuestoNegativoException {
    FiltroDeMuebles filtro;

    try {
      filtro = FiltroDeMuebles.delCatalogo(estilo, precioMaximo);
    } catch (FiltroDeMueblesInvalidoException e) {
      return new ModelAndView("redirect:/catalogo");
    }

    return new ModelAndView("catalogo", this.modeloDelCatalogo(filtro));
  }

  @RequestMapping(path = "/muebles", method = RequestMethod.GET)
  public ModelAndView muebles() {
    return new ModelAndView("muebles");
  }

  @RequestMapping(path = "/muebles/crear", method = RequestMethod.GET)
  public ModelAndView crearMueble() {
    return new ModelAndView("crearMueble");
  }

  @RequestMapping(path = "/muebles/{id}", method = RequestMethod.GET)
  public ModelAndView detalleMueble(@PathVariable("id") int id) {
    Mueble mueble;

    try {
      mueble = this.servicioCatalogo.ObtenerMueblePorId(id);
    } catch (MuebleNoEncontrado e) {
      return new ModelAndView("redirect:/catalogo");
    }

    return new ModelAndView("mueble", Map.of("mueble", mueble));
  }

  private Map<String, Object> modeloDelCatalogo(FiltroDeMuebles filtro)
    throws PresupuestoNegativoException {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("estilos", Estilo.values());
    modelo.put("estiloSeleccionado", filtro.getEstilo());
    modelo.put("precioMaximo", filtro.getPrecioMaximo());
    modelo.put(
      "muebles",
      this.servicioCatalogo.ObtenerMueblesQueCumplan(filtro.getEstilo(), filtro.getPrecioMaximo())
    );

    return modelo;
  }
}
