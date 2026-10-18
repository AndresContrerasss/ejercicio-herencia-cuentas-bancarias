package com.uniajc;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * CAMBIOS en los tests:
 *  - Cuenta ahora es abstracta, por eso se prueba con CuentaDePrueba (subclase vacía).
 *  - Se usan getters en vez de leer los campos (encapsulación).
 *  - Los tests que describían bugs ("...Actualmente") ahora verifican el comportamiento correcto.
 */
class CuentaTest {

    private static final float DELTA = 0.1f;

    private static class CuentaDePrueba extends Cuenta {
        CuentaDePrueba(float saldo, float tasaAnual) {
            super(saldo, tasaAnual);
        }
    }

    @Test
    void cuentaEsAbstracta() {
        assertTrue(Modifier.isAbstract(Cuenta.class.getModifiers()));
    }

    @Test
    void inicializaSaldoYTasaAnual() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);

        assertEquals(1000f, cuenta.getSaldo(), DELTA);
        assertEquals(0.12f, cuenta.getTasaAnual(), DELTA);
    }

    @Test
    void consignarAumentaSaldoYCuentaLaConsignacion() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);

        cuenta.consignar(250f);

        assertEquals(1250f, cuenta.getSaldo(), DELTA);
        assertEquals(1, cuenta.getNumeroConsignaciones());
    }

    @Test
    void consignarCeroNoCambiaSaldoPeroCuentaLaOperacion() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);

        cuenta.consignar(0f);

        assertEquals(1000f, cuenta.getSaldo(), DELTA);
        assertEquals(1, cuenta.getNumeroConsignaciones());
    }

    @Test
    void consignarCantidadNegativaLanzaExcepcionYNoCambiaLaCuenta() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);

        assertThrows(IllegalArgumentException.class, () -> cuenta.consignar(-250f));

        assertEquals(1000f, cuenta.getSaldo(), DELTA);
        assertEquals(0, cuenta.getNumeroConsignaciones());
    }

    @Test
    void retirarConSaldoSuficienteActualizaSaldoYContador() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);

        cuenta.retirar(300f);

        assertEquals(700f, cuenta.getSaldo(), DELTA);
        assertEquals(1, cuenta.getNumeroRetiros());
    }

    @Test
    void retirarSaldoExactoDejaLaCuentaEnCero() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);

        cuenta.retirar(1000f);

        assertEquals(0f, cuenta.getSaldo(), DELTA);
        assertEquals(1, cuenta.getNumeroRetiros());
    }

    @Test
    void retirarMasDelSaldoNoModificaLaCuenta() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);

        cuenta.retirar(1200f);

        assertEquals(1000f, cuenta.getSaldo(), DELTA);
        assertEquals(0, cuenta.getNumeroRetiros());
    }

    @Test
    void retirarCantidadNegativaLanzaExcepcionYNoAumentaSaldo() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);

        assertThrows(IllegalArgumentException.class, () -> cuenta.retirar(-250f));

        assertEquals(1000f, cuenta.getSaldo(), DELTA);
        assertEquals(0, cuenta.getNumeroRetiros());
    }

    @Test
    void calcularInteresAgregaElInteresDeUnMes() {
        Cuenta cuenta = new CuentaDePrueba(1200f, 0.12f);

        cuenta.calcularInteres();

        assertEquals(1212f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void calcularInteresConTasaCeroNoCambiaSaldo() {
        Cuenta cuenta = new CuentaDePrueba(1200f, 0f);

        cuenta.calcularInteres();

        assertEquals(1200f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void extractoMensualAplicaComisionAntesDelInteres() {
        Cuenta cuenta = new CuentaDePrueba(1200f, 0.12f);
        cuenta.setComisionMensual(5f);

        cuenta.extractoMensual();

        // (1200 - 5) * 1.01 = 1206.95
        assertEquals(1206.95f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void extractoMensualConComisionCeroSoloAplicaInteres() {
        Cuenta cuenta = new CuentaDePrueba(1200f, 0.12f);

        cuenta.extractoMensual();

        assertEquals(1212f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void extractoMensualReiniciaLosContadores() {
        Cuenta cuenta = new CuentaDePrueba(1000f, 0.12f);
        cuenta.consignar(100f);
        cuenta.retirar(50f);

        cuenta.extractoMensual();

        assertEquals(0, cuenta.getNumeroConsignaciones());
        assertEquals(0, cuenta.getNumeroRetiros());
    }

    @Test
    void comisionNegativaLanzaExcepcionYNoCambiaLaComision() {
        Cuenta cuenta = new CuentaDePrueba(1200f, 0.12f);

        assertThrows(IllegalArgumentException.class, () -> cuenta.setComisionMensual(-5f));

        assertEquals(0f, cuenta.getComisionMensual(), DELTA);
    }

    @Test
    void extractoMensualPuedeDejarSaldoNegativoSiComisionSuperaSaldo() {
        Cuenta cuenta = new CuentaDePrueba(100f, 0.12f);
        cuenta.setComisionMensual(101f);

        cuenta.extractoMensual();

        // saldo = -1; el interés de un mes sobre -1 es -0.01 -> -1.01
        assertEquals(-1.01f, cuenta.getSaldo(), DELTA);
    }
}
