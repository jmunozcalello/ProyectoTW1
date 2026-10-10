package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mueble;
import com.tallerwebi.dominio.RepositorioCatalogo;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioCatalogo")
public class RepositorioCatalogoImpl implements RepositorioCatalogo {

  private final SessionFactory sessionFactory;

  @Autowired
  public RepositorioCatalogoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardarMueble(Mueble mueble) {
    sessionFactory.getCurrentSession().persist(mueble);
  }

  @Override
  public List<Mueble> ObtenerTodosLosMuebles() {
    String query = "FROM Mueble m LEFT JOIN FETCH m.categoriaDeMueble";
    List<Mueble> muebles = sessionFactory
      .getCurrentSession()
      .createQuery(query, Mueble.class)
      .getResultList();
    return muebles;
  }

  @Override
  public Mueble ObtenerMueblePorId(int id) {
    String query = "FROM Mueble m LEFT JOIN FETCH m.categoriaDeMueble WHERE m.id = :id";
    Query<Mueble> hibernateQuery = sessionFactory
      .getCurrentSession()
      .createQuery(query, Mueble.class)
      .setParameter("id", id);
    return hibernateQuery.uniqueResult();
  }
}
