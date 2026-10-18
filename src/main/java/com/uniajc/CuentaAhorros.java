package com.uniajc;


public final class CuentaAhorros extends Cuenta {

    private boolean activa;

    private static final float SALDO_MINIMO = 10000f;
    private static final float COMISION_RETIRO = 1000f;
    private static final int NUMERO_RETIROS_SIN_COMISION = 4;

    CuentaAhorros(float saldo, float tasaAnual) {
        super(saldo, tasaAnual);
        cuentaActivaSaldoMinimo();
    }

    // CAMBIO: if/else de 6 líneas -> una expresión.
    public boolean cuentaActivaSaldoMinimo() {
        activa = getSaldo() >= SALDO_MINIMO;
        return activa;
    }

    public boolean isActiva() {
        return activa;
    }

    // CAMBIO (bug): antes una cuenta inactiva no podía consignar y así nunca
    // volvía a activarse. Ahora siempre se puede consignar y se reevalúa el estado.
    @Override
    public void consignar(float cantidad) {
        super.consignar(cantidad);
        cuentaActivaSaldoMinimo();
    }

    @Override
    public void retirar(float cantidad) {
        if (activa) {
            super.retirar(cantidad);
            cuentaActivaSaldoMinimo();
        }
    }

    // CAMBIO: la comisión se ASIGNA (antes "+=" la acumulaba mes a mes) y luego
    // se delega en el padre, que la descuenta y calcula el interés.
    @Override
    public void extractoMensual() {
        int retirosExcedentes = Math.max(0, getNumeroRetiros() - NUMERO_RETIROS_SIN_COMISION);
        setComisionMensual(retirosExcedentes * COMISION_RETIRO);
        super.extractoMensual();
        cuentaActivaSaldoMinimo();
    }

    @Override
    public void imprimir() {
        super.imprimir();
        System.out.println("Cuenta activa: " + activa);
    }
}
