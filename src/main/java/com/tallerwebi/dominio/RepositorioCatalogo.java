package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCatalogo {
  void guardarMueble(Mueble mueble);
  List<Mueble> ObtenerTodosLosMuebles();
  Mueble ObtenerMueblePorId(int id);
}
