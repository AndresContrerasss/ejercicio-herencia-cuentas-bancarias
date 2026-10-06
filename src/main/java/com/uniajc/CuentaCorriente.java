package com.uniajc;

public class CuentaCorriente extends Cuenta {

    protected float sobregiro;

    CuentaCorriente(float saldo, float tasaAnual) {
        super(saldo, tasaAnual);
    }

    @Override
    public void consignar(float cantidad) { }

    @Override
    public void retirar(float cantidad) { }

    @Override
    public void calcularInteres() { }

    @Override
    public void extractoMensual() { }

}
