package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * HU-08: dimensiones estandarizadas por categoría.
 * Dado que elijo una categoría, cuando cargo el mueble, entonces sus medidas se completan con el
 * promedio de la categoría.
 * Dado que escribí una medida a mano, cuando cargo el mueble, entonces se respeta mi valor.
 */
public class ServicioCategoriaDeMuebleTest {

  private static final Long ID_CAMA_DOBLE = 2L;

  private RepositorioCategoriaDeMueble repositorioCategoriaDeMueble;
  private ServicioCategoriaDeMueble servicioCategoriaDeMueble;

  @BeforeEach
  public void init() {
    repositorioCategoriaDeMueble = mock(RepositorioCategoriaDeMueble.class);
    servicioCategoriaDeMueble = new ServicioCategoriaDeMuebleImpl(repositorioCategoriaDeMueble);
  }

  @Test
  public void dadoQueElijoUnaCategoriaSinCargarMedidas_cuandoCreoElMueble_entoncesTomaLasMedidasPromedio() {
    this.dadoQueExisteLaCamaDoble();

    Mueble mueble = this.cuandoCreoElMueble("Mi cama", ID_CAMA_DOBLE, null, null);

    this.entoncesSusMedidasSon(1.4, 1.9, mueble);
  }

  private void dadoQueExisteLaCamaDoble() {
    when(repositorioCategoriaDeMueble.buscarPorId(ID_CAMA_DOBLE))
      .thenReturn(new CategoriaDeMueble("Cama doble", 1.4, 1.9));
  }

  private Mueble cuandoCreoElMueble(
    String nombre,
    Long idCategoria,
    Double ancho,
    Double profundidad
  ) {
    return servicioCategoriaDeMueble.crearMueble(nombre, idCategoria, ancho, profundidad);
  }

  private void entoncesSusMedidasSon(
    Double anchoEsperado,
    Double profundidadEsperada,
    Mueble mueble
  ) {
    assertThat("ancho", mueble.getAncho(), is(anchoEsperado));
    assertThat("profundidad", mueble.getLargo(), is(profundidadEsperada));
  }
}
