package com.tallerwebi.dominio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("servicioAmbiente")
@Transactional
public class ServicioAmbienteImpl implements ServicioAmbiente {

  private final RepositorioAmbiente repositorioAmbiente;

  @Autowired
  public ServicioAmbienteImpl(RepositorioAmbiente repositorioAmbiente) {
    this.repositorioAmbiente = repositorioAmbiente;
  }

  @Override
  public List<Ambiente> obtenerAmbientes() {
    return repositorioAmbiente.obtenerTodos();
  }

  @Override
  public List<Ambiente> obtenerAmbientesDe(String email) {
    return repositorioAmbiente.obtenerAmbientesDe(email);
  }

  @Override
  public Ambiente buscarAmbiente(Long id) {
    if (id == null) {
      return null;
    }
    return repositorioAmbiente.buscarPorId(id);
  }
}
