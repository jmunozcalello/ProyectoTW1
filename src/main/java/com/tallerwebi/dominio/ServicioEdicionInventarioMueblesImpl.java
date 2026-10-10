package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.MuebleNoEncontrado;
import java.util.List;

/**
 * A proposito NO es un bean de Spring: no lleva @Service ni @Transactional.
 * <p>
 * No se pueden agregar mientras el inventario siga llegando por constructor.
 * Spring, al ver un "List&lt;Mueble&gt;", lo interpreta como "todos los beans de tipo
 * Mueble que existan" e inyecta una lista VACIA si no hay ninguno. Y Mueble es solo
 * una @Entity, no un bean.
 * <p>
 * NO agregar @Service "para que Spring la reconozca" sin resolver eso antes.
 */
public class ServicioEdicionInventarioMueblesImpl implements ServicioEdicionInventarioMuebles {

  private final List<Mueble> inventario;

  public ServicioEdicionInventarioMueblesImpl(List<Mueble> inventario) {
    this.inventario = inventario;
  }

  @Override
  public void actualizar(int id, Mueble datos) throws MuebleNoEncontrado {
    Mueble mueble = this.buscarPorId(id);
    mueble.setNombre(datos.getNombre());
    mueble.setAncho(datos.getAncho());
    mueble.setLargo(datos.getLargo());
    mueble.setDescripcion(datos.getDescripcion());
    mueble.setPrecio(datos.getPrecio());
    mueble.setEstilo(datos.getEstilo());
  }

  @Override
  public void eliminar(int id) throws MuebleNoEncontrado {
    this.buscarPorId(id);
    this.inventario.removeIf(mueble -> mueble.getId() == id);
  }

  private Mueble buscarPorId(int id) throws MuebleNoEncontrado {
    for (Mueble mueble : this.inventario) {
      if (mueble.getId() == id) {
        return mueble;
      }
    }
    throw new MuebleNoEncontrado("No se encontro un mueble con el id " + id);
  }
}
