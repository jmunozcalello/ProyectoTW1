package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.CategoriaDeMueble;
import com.tallerwebi.dominio.RepositorioCategoriaDeMueble;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioCategoriaDeMueble")
public class RepositorioCategoriaDeMuebleImpl implements RepositorioCategoriaDeMueble {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioCategoriaDeMuebleImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public List<CategoriaDeMueble> obtenerTodas() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("FROM CategoriaDeMueble ORDER BY nombre", CategoriaDeMueble.class)
      .getResultList();
  }

  @Override
  public CategoriaDeMueble buscarPorId(Long id) {
    return sessionFactory.getCurrentSession().get(CategoriaDeMueble.class, id);
  }
}
