package com.uniajc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestCuenta {

    private static final float DELTA = 0.001f;

    @Test
    void inicializaSaldoYTasaAnual() {
        Cuenta cuenta = new Cuenta(1000f, 0.12f);

        assertEquals(1000f, cuenta.saldo, DELTA);
        assertEquals(0.12f, cuenta.tasaAnual, DELTA);
    }

    @Test
    void consignarAumentaSaldoYCuentaLaConsignacion() {
        Cuenta cuenta = new Cuenta(1000f, 0.12f);

        cuenta.consignar(250f);

        assertEquals(1250f, cuenta.saldo, DELTA);
        assertEquals(1, cuenta.numeroConsignaciones);
    }

    @Test
    void retirarConSaldoSuficienteActualizaSaldoYContador() {
        Cuenta cuenta = new Cuenta(1000f, 0.12f);

        cuenta.retirar(300f);

        assertEquals(700f, cuenta.saldo, DELTA);
        assertEquals(1, cuenta.numeroRetiros);
    }

    @Test
    void retirarMasDelSaldoNoModificaLaCuenta() {
        Cuenta cuenta = new Cuenta(1000f, 0.12f);

        cuenta.retirar(1200f);

        assertEquals(1000f, cuenta.saldo, DELTA);
        assertEquals(0, cuenta.numeroRetiros);
    }

    @Test
    void calcularInteresAgregaElInteresDeUnMes() {
        Cuenta cuenta = new Cuenta(1200f, 0.12f);

        cuenta.calcularInteres();

        assertEquals(1212f, cuenta.saldo, DELTA);
    }

    @Test
    void extractoMensualAplicaComisionAntesDelInteres() {
        Cuenta cuenta = new Cuenta(1200f, 0.12f);
        cuenta.comisionMensual = 5f;

        cuenta.extractoMensual();

        assertEquals(1206.95f, cuenta.saldo, DELTA);
    }
}
