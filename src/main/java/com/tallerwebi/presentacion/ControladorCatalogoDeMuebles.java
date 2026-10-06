package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.ServicioCatalogo;
import com.tallerwebi.dominio.TipoDeAmbiente;
import com.tallerwebi.dominio.excepcion.MuebleNoEncontrado;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.presentacion.excepcion.FiltroDeMueblesInvalidoException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    @RequestParam(name = "precioMax", required = false) String precioMaximo,
    @RequestParam(name = "precioMin", required = false) String precioMinimo,
    @RequestParam(name = "tipoDeAmbiente", required = false) String tipoDeAmbiente,
    @RequestParam(name = "orden", required = false) String orden
  ) {
    FiltroDeMuebles filtro;

    try {
      filtro =
        FiltroDeMuebles.delCatalogo(estilo, precioMaximo, precioMinimo, tipoDeAmbiente, orden);
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

  @RequestMapping(path = "/mueble/{id}", method = RequestMethod.GET)
  public ModelAndView detalleMueble(@PathVariable("id") int id) {
    Mueble mueble;

    try {
      mueble = this.servicioCatalogo.ObtenerMueblePorId(id);
    } catch (MuebleNoEncontrado e) {
      return new ModelAndView("redirect:/catalogo");
    }

    Map<String, Object> modelo = new ModelMap();
    modelo.put("mueble", mueble);

    return new ModelAndView("mueble", modelo);
  }

  private Map<String, Object> modeloDelCatalogo(FiltroDeMuebles filtro) {
    Map<String, Object> modelo = new ModelMap();
    modelo.put("estilos", Estilo.values());
    modelo.put("estiloSeleccionado", filtro.getEstilo());
    modelo.put("precioMaximo", filtro.getPrecioMaximo());
    modelo.put("precioMinimo", filtro.getPrecioMinimo());
    modelo.put("tipoDeAmbiente", filtro.getTipoDeAmbiente());
    modelo.put("orden", filtro.getOrden());
    List<Mueble> muebles = null;
    TipoDeAmbiente tipoDeAmbiente = this.aTipoDeAmbiente(filtro.getTipoDeAmbiente());

    try {
      muebles =
        this.servicioCatalogo.ObtenerMueblesFiltrados(
            filtro.getPrecioMinimo(),
            filtro.getPrecioMaximo(),
            filtro.getEstilo(),
            tipoDeAmbiente,
            filtro.getOrden()
          );
    } catch (PresupuestoNegativoException e) {
      modelo.put("error", "El presupuesto no puede ser negativo");
    }
    modelo.put("muebles", muebles);

    return modelo;
  }

  private TipoDeAmbiente aTipoDeAmbiente(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
      return null;
    }
    return new TipoDeAmbiente(nombre);
  }
}
