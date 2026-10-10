package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioAmbiente {
  List<Ambiente> obtenerAmbientes();
  List<Ambiente> obtenerAmbientesDe(String email);
  Ambiente buscarAmbiente(Long id);
}
