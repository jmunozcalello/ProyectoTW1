package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.MuebleNoEncontrado;

public interface ServicioEdicionInventarioMuebles {
  void actualizar(int id, Mueble datos) throws MuebleNoEncontrado;

  void eliminar(int id) throws MuebleNoEncontrado;
}
