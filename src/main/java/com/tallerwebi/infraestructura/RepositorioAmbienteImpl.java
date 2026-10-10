package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.RepositorioAmbiente;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioAmbiente")
public class RepositorioAmbienteImpl implements RepositorioAmbiente {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioAmbienteImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Ambiente buscarPorId(Long id) {
    String query = "SELECT a FROM Ambiente a LEFT JOIN FETCH a.muebles WHERE a.id = :id";
    Ambiente ambiente =
      this.sessionFactory.getCurrentSession()
        .createQuery(query, Ambiente.class)
        .setParameter("id", id)
        .uniqueResult();
    return ambiente;
  }

  @Override
  public List<Ambiente> obtenerTodos() {
    String query = "FROM Ambiente ORDER BY id";
    List<Ambiente> ambientes =
      this.sessionFactory.getCurrentSession().createQuery(query, Ambiente.class).getResultList();
    return ambientes;
  }

  @Override
  public List<Ambiente> obtenerAmbientesDe(String email) {
    String query =
      "SELECT a FROM Ambiente a LEFT JOIN FETCH a.muebles WHERE a.usuario.email = :email ORDER BY a.id";
    List<Ambiente> ambientes =
      this.sessionFactory.getCurrentSession()
        .createQuery(query, Ambiente.class)
        .setParameter("email", email)
        .getResultList();
    return ambientes;
  }
}
