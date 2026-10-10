package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioAmbiente {
  List<Ambiente> obtenerAmbientes();
  Ambiente buscarAmbiente(Long id);
}
