package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;

public class ServicioPrecioFinalTest {


    /**
     * si un cliente es de tipo normal el precio final es el precio base
     * si un cliente es vip, el precio final es el precio base con un 10% de descuento
     * si un cliente es premium, el precio final es el precio base con un 20% de descuento
     *
     */

    // un test unitario prueba una sola clase en particular

    ServicioPrecioFinal servicioPrecioFinal = new ServicioPrecioFinalImpl();

    @Test
    public void deberiaDevolverElMismoPrecioParaClienteNormal() {

        //preparacion --> givenTengoUnCliente();

        //ejecucion --> when
        Double precio = whenCalculoPrecioFinalParaClienteNormal();

        //validacion --> then
//        thenElPrecioFinalEsElMismoQueElPrecioBase(precio);


    }

    private void thenElPrecioFinalEsElMismoQueElPrecioBase() {
    }

    private Double whenCalculoPrecioFinalParaClienteNormal() {
        Double precioFinal = servicioPrecioFinal.calcularPrecio(1000.00,"NORMAL");
        return precioFinal;
    }


}
