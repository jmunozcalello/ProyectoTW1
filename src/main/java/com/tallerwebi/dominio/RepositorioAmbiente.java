package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioAmbiente {
  Ambiente buscarPorId(Long id);
  List<Ambiente> obtenerTodos();
  List<Ambiente> obtenerAmbientesDe(String email);
}
