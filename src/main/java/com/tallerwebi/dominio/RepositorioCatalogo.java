package com.tallerwebi.dominio;

import java.util.List;
import java.util.Optional;

public interface RepositorioCatalogo {
  void guardarMueble(Mueble mueble);
  List<Mueble> ObtenerTodosLosMuebles();
  Optional<Mueble> ObtenerMueblePorId(int id);
}
