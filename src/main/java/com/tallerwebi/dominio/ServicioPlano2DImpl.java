package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ValidacionException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ServicioPlano2DImpl implements ServicioPlano2D {

  @Override
  public Plano generarPlano(Ambiente ambiente, List<Mueble> muebles) {
    Double ancho = ambiente.getAncho();
    if (ancho == null || ancho <= 0) {
      throw new ValidacionException("El ancho del ambiente debe ser mayor a 0");
    }
    for (Obstaculo obstaculo : ambiente.getObstaculos()) {
      obstaculo.validarEn(ambiente);
    }

    List<MuebleUbicado> mueblesUbicados = new ArrayList<>();
    List<Mueble> mueblesExcluidos = new ArrayList<>();
    List<String> motivosDeExclusion = new ArrayList<>();

    if (muebles.isEmpty()) {
      Plano plano = new Plano(ambiente, mueblesUbicados, mueblesExcluidos, motivosDeExclusion);
      plano.setMensaje("No hay mobiliario para distribuir");
      return plano;
    }

    RecorridoPerimetral recorrido = new RecorridoPerimetral(ambiente);
    for (Mueble mueble : ordenarPorAreaDescendente(muebles)) {
      MuebleUbicado muebleUbicado = recorrido.ubicar(mueble);
      if (muebleUbicado != null) {
        mueblesUbicados.add(muebleUbicado);
      } else {
        mueblesExcluidos.add(mueble);
        motivosDeExclusion.add("El mueble " + mueble.getNombre() + " no entra en el ambiente");
      }
    }
    return new Plano(ambiente, mueblesUbicados, mueblesExcluidos, motivosDeExclusion);
  }

  /**
   * El criterio de orden de colocacion es mayor area primero. Con el sort a igual
   * area se respeta el orden en que el usuario cargo los muebles. Se ordena sobre una copia porque
   * la lista que recibe el servicio puede ser inmutable.
   */
  private List<Mueble> ordenarPorAreaDescendente(List<Mueble> muebles) {
    List<Mueble> copia = new ArrayList<>(muebles);
    copia.sort(Comparator.comparingDouble(ServicioPlano2DImpl::areaDe).reversed());
    return copia;
  }

  private static double areaDe(Mueble mueble) {
    return mueble.getAncho() * mueble.getLargo();
  }

  @Override
  public boolean estaDentroDelPerimetro(MuebleUbicado muebleUbicado, Ambiente ambiente) {
    Double ubicacionX = muebleUbicado.getPosicionX();
    Double ubicacionY = muebleUbicado.getPosicionY();
    Double muebleAncho = muebleUbicado.getMueble().getAncho();
    Double muebleLargo = muebleUbicado.getMueble().getLargo();

    Double ambienteAncho = ambiente.getAncho();
    Double ambienteLargo = ambiente.getLargo();

    Boolean noSeSaleEnX =
      (ubicacionX >= -ZonaNoDisponible.TOLERANCIA) &&
      (ubicacionX + muebleAncho <= ambienteAncho + ZonaNoDisponible.TOLERANCIA);
    Boolean noSeSaleEnY =
      (ubicacionY >= -ZonaNoDisponible.TOLERANCIA) &&
      (ubicacionY + muebleLargo <= ambienteLargo + ZonaNoDisponible.TOLERANCIA);

    return noSeSaleEnX && noSeSaleEnY;
  }

  /**
   * Recorre los muros perimetrales del ambiente en orden fijo: muro superior de izquierda a
   * derecha, luego derecho de arriba hacia abajo, luego inferior de derecha a izquierda, y
   * finalmente izquierdo de abajo hacia arriba. Cada mueble se apoya contra el muro que le
   * toca y el cursor avanza hasta el final del tramo. Cada tramo deja libre la esquina que
   * comparte con el tramo anterior, de modo que ningun mueble los cubre dos veces.
   */
  private class RecorridoPerimetral {

    private static final int MURO_SUPERIOR = 0;
    private static final int MURO_DERECHO = 1;
    private static final int MURO_INFERIOR = 2;
    private static final int MURO_IZQUIERDO = 3;
    private static final int CANTIDAD_DE_TRAMOS = 4;

    private final Ambiente ambiente;
    private int tramoActual = MURO_SUPERIOR;
    private Double posicionEnElTramo = 0.0;
    private final List<ZonaNoDisponible> zonasNoDisponibles;

    public RecorridoPerimetral(Ambiente ambiente) {
      this.ambiente = ambiente;
      this.zonasNoDisponibles =
        ambiente
          .getObstaculos()
          .stream()
          .map(obstaculo -> obstaculo.zonaNoDisponible(ambiente))
          .toList();
    }

    public MuebleUbicado ubicar(Mueble mueble) {
      int tramoInicial = tramoActual;
      Double posicionInicial = posicionEnElTramo;
      MuebleUbicado ubicado = recorrer(mueble, true);
      if (ubicado == null) {
        volverA(tramoInicial, posicionInicial);
        ubicado = recorrer(mueble, false);
      }
      if (ubicado == null) {
        volverA(tramoInicial, posicionInicial);
      }
      return ubicado;
    }

    /**
     * Busca desde el cursor la primera posicion donde entra el mueble. Si evitarObstaculos es
     * false, ignora las zonas no disponibles y marca el mueble en conflicto cuando pisa alguna.
     */
    private MuebleUbicado recorrer(Mueble mueble, boolean evitarObstaculos) {
      Double recorridoEnElTramo = posicionEnElTramo;
      while (tramoActual < CANTIDAD_DE_TRAMOS) {
        if (sigueAdentroDelTramo(recorridoEnElTramo, mueble)) {
          MuebleUbicado candidato = new MuebleUbicado(
            mueble,
            coordenadaXDelTramo(mueble, recorridoEnElTramo),
            coordenadaYDelTramo(mueble, recorridoEnElTramo)
          );
          if (estaDentroDelPerimetro(candidato, ambiente)) {
            ZonaNoDisponible zona = zonaPisadaPor(candidato);
            if (zona == null || !evitarObstaculos) {
              avanzarEnElTramo(mueble, recorridoEnElTramo);
              return new MuebleUbicado(
                mueble,
                candidato.getPosicionX(),
                candidato.getPosicionY(),
                zona != null
              );
            }
            Double despuesDeLaZona = recorridoDespuesDe(zona, mueble);
            if (avanzaEnElTramo(recorridoEnElTramo, despuesDeLaZona)) {
              recorridoEnElTramo = despuesDeLaZona;
              continue;
            }
          }
        }
        tramoActual++;
        recorridoEnElTramo = inicioDelTramo(mueble);
      }
      return null;
    }

    private void volverA(int tramo, Double posicion) {
      tramoActual = tramo;
      posicionEnElTramo = posicion;
    }

    private ZonaNoDisponible zonaPisadaPor(MuebleUbicado candidato) {
      return zonasNoDisponibles
        .stream()
        .filter(zona -> zona.seSolapaCon(candidato))
        .findFirst()
        .orElse(null);
    }

    /** Posicion del cursor que deja al mueble justo despues de la zona, en el sentido del tramo. */
    private Double recorridoDespuesDe(ZonaNoDisponible zona, Mueble mueble) {
      return switch (tramoActual) {
        case MURO_DERECHO -> zona.getPosicionY() + zona.getLargo();
        case MURO_INFERIOR -> ambiente.getAncho() - zona.getPosicionX();
        case MURO_IZQUIERDO -> zona.getPosicionY() - mueble.getLargo();
        default -> zona.getPosicionX() + zona.getAncho();
      };
    }

    /** Evita bucles: solo se salta si el cursor avanza de verdad en el sentido del tramo. */
    private Boolean avanzaEnElTramo(Double desde, Double hasta) {
      return (hasta - desde) * sentidoDelTramo() > 0;
    }

    private Double extensionEnElTramo(Mueble mueble) {
      return switch (tramoActual) {
        case MURO_SUPERIOR, MURO_INFERIOR -> mueble.getAncho();
        default -> mueble.getLargo();
      };
    }

    /**
     * El cursor corre en positivo salvo en el muro izquierdo, que se recorre de abajo hacia
     * arriba. Todos los tramos dejan libres las dos esquinas que tocan, asi que el recorrido
     * util va desde el inicio hasta el fin, y el fin es la ultima posicion valida del cursor.
     */
    private Double sentidoDelTramo() {
      return switch (tramoActual) {
        case MURO_IZQUIERDO -> -1.0;
        default -> 1.0;
      };
    }

    private Double inicioDelTramo(Mueble mueble) {
      return switch (tramoActual) {
        case MURO_DERECHO -> mueble.getLargo();
        case MURO_INFERIOR -> mueble.getAncho();
        case MURO_IZQUIERDO -> ambiente.getLargo() - 2 * mueble.getLargo();
        default -> 0.0;
      };
    }

    private Double finDelTramo(Mueble mueble) {
      return switch (tramoActual) {
        case MURO_SUPERIOR, MURO_INFERIOR -> ambiente.getAncho() - mueble.getAncho();
        case MURO_DERECHO -> ambiente.getLargo() - 2 * mueble.getLargo();
        case MURO_IZQUIERDO -> mueble.getLargo();
        default -> ambiente.getLargo() - mueble.getLargo();
      };
    }

    private Boolean sigueAdentroDelTramo(Double recorridoEnElTramo, Mueble mueble) {
      return (
        (recorridoEnElTramo - finDelTramo(mueble)) * sentidoDelTramo() <=
        ZonaNoDisponible.TOLERANCIA
      );
    }

    private Double coordenadaXDelTramo(Mueble mueble, Double avance) {
      return switch (tramoActual) {
        case MURO_SUPERIOR -> avance;
        case MURO_DERECHO -> ambiente.getAncho() - mueble.getAncho();
        case MURO_INFERIOR -> ambiente.getAncho() - avance - mueble.getAncho();
        default -> 0.0;
      };
    }

    private Double coordenadaYDelTramo(Mueble mueble, Double avance) {
      return switch (tramoActual) {
        case MURO_SUPERIOR -> 0.0;
        case MURO_DERECHO -> avance;
        case MURO_INFERIOR -> ambiente.getLargo() - mueble.getLargo();
        default -> avance;
      };
    }

    private void avanzarEnElTramo(Mueble mueble, Double recorridoEnElTramo) {
      Double nuevoRecorrido = recorridoEnElTramo + sentidoDelTramo() * extensionEnElTramo(mueble);
      if (
        (nuevoRecorrido - finDelTramo(mueble)) * sentidoDelTramo() > ZonaNoDisponible.TOLERANCIA
      ) {
        tramoActual++;
        posicionEnElTramo = inicioDelTramo(mueble);
        return;
      }
      posicionEnElTramo = nuevoRecorrido;
    }
  }
}
