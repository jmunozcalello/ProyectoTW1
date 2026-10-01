package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.ValidacionException;
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
  private static final Long SIN_CATEGORIA = null;

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

  @Test
  public void dadoQueElijoUnaCategoriaYEscriboElAncho_cuandoCreoElMueble_entoncesRespetaMiAncho() {
    this.dadoQueExisteLaCamaDoble();

    Mueble mueble = this.cuandoCreoElMueble("Mi cama", ID_CAMA_DOBLE, 1.5, null);

    this.entoncesSusMedidasSon(1.5, 1.9, mueble);
  }

  @Test
  public void dadoQueElijoUnaCategoriaYEscriboLaProfundidad_cuandoCreoElMueble_entoncesRespetaMiProfundidad() {
    this.dadoQueExisteLaCamaDoble();

    Mueble mueble = this.cuandoCreoElMueble("Mi cama", ID_CAMA_DOBLE, null, 2.0);

    this.entoncesSusMedidasSon(1.4, 2.0, mueble);
  }

  @Test
  public void dadoQueNoElijoCategoria_cuandoCreoElMueble_entoncesUsaLasMedidasQueCargue() {
    Mueble mueble = this.cuandoCreoElMueble("Baúl", SIN_CATEGORIA, 0.9, 0.45);

    this.entoncesSusMedidasSon(0.9, 0.45, mueble);
  }

  @Test
  public void dadoQueLaCategoriaElegidaNoExiste_cuandoCreoElMueble_entoncesInformaElError() {
    ValidacionException error = assertThrows(
      ValidacionException.class,
      () -> this.cuandoCreoElMueble("Mi cama", 999L, null, null)
    );

    assertThat(error.getMessage(), is("La categoría elegida para Mi cama no existe"));
  }

  @Test
  public void dadoQueNoElijoCategoriaYDejoUnaMedidaVacia_cuandoCreoElMueble_entoncesMePideCompletarla() {
    ValidacionException error = assertThrows(
      ValidacionException.class,
      () -> this.cuandoCreoElMueble("Baúl", SIN_CATEGORIA, 0.9, null)
    );

    assertThat(error.getMessage(), is("Completá las medidas de Baúl o elegí una categoría"));
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
