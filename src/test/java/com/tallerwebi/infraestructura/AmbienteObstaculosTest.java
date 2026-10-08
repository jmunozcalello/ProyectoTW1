package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.Ambiente;
import com.tallerwebi.dominio.Muro;
import com.tallerwebi.dominio.Obstaculo;
import com.tallerwebi.dominio.TipoDeAmbiente;
import com.tallerwebi.dominio.TipoDeObstaculo;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import jakarta.transaction.Transactional;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/** Verifica que los obstáculos fijos se guarden y se recuperen junto con su ambiente. */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class AmbienteObstaculosTest {

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  @Transactional
  @Rollback
  public void dadoQueUnAmbienteTieneUnObstaculo_cuandoLoGuardoYLoRecupero_entoncesConservaElObstaculo() {
    Session sesion = this.sessionFactory.getCurrentSession();
    TipoDeAmbiente living = new TipoDeAmbiente("Living");
    sesion.persist(living);
    Ambiente ambiente = new Ambiente(4.0, 3.0, living);
    ambiente.agregarObstaculo(
      new Obstaculo(TipoDeObstaculo.RADIADOR, Muro.DERECHO, 0.5, 0.8, 0.15)
    );
    sesion.persist(ambiente);
    sesion.flush();
    sesion.clear();

    Ambiente recuperado = sesion.get(Ambiente.class, ambiente.getId());

    assertThat(recuperado.getObstaculos(), hasSize(1));
    Obstaculo obstaculo = recuperado.getObstaculos().get(0);
    assertThat(obstaculo.getTipo(), is(TipoDeObstaculo.RADIADOR));
    assertThat(obstaculo.getMuro(), is(Muro.DERECHO));
    assertThat(
      List.of(obstaculo.getPosicion(), obstaculo.getAncho(), obstaculo.getProfundidad()),
      contains(0.5, 0.8, 0.15)
    );
  }
}
