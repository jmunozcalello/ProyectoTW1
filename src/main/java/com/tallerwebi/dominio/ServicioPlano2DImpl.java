package com.tallerwebi.dominio;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioPlano2DImpl implements ServicioPlano2D {
    @Override
    public Plano generarPlano(Double ancho, Double largo, List<Mueble> muebles) {
       List<MuebleUbicado> mueblesUbicados = new ArrayList<>();

        for (Mueble mueble : muebles) {
            MuebleUbicado muebleUbicado = new MuebleUbicado(mueble, 0.0, 0.0);
            mueblesUbicados.add(muebleUbicado);
        }
        return new Plano(ancho, largo, mueblesUbicados);
    }

    @Override
    public boolean estaDentroDelPerimetro(MuebleUbicado muebleUbicado, Ambiente ambiente) {
        Double ubicacionX = muebleUbicado.getPosicionX();
        Double ubicacionY = muebleUbicado.getPosicionY();
        Double muebleAncho = muebleUbicado.getMueble().getAncho();
        Double muebleLargo = muebleUbicado.getMueble().getLargo();

        Double ambienteAncho = ambiente.getAncho();
        Double ambienteLargo = ambiente.getLargo();

        Boolean noSeSaleEnX = (ubicacionX >= 0) && (ubicacionX + muebleAncho <= ambienteAncho);
        Boolean noSeSaleEnY = (ubicacionY >= 0) && (ubicacionY + muebleLargo <= ambienteLargo);

        return noSeSaleEnX && noSeSaleEnY;



    }
}
