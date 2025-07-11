package com.mycompany.atm.cs;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private int nip;
    
    public CuentaBancaria(String numeroCuenta, String titular, double saldo, int nip){
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.nip = nip;
    }
    
    public String getTitular(){
        return titular;
    }
    
    public double consultarSaldo(){
        return saldo;
    }
    
    public void depositar(double monto){
        if(monto > 0){
            saldo += monto;
        }
    }
    
    public void retirar(double monto){
        if(monto < saldo){
            saldo -= monto;
        }
    }
    
    public boolean autenticar(int claveIngresada) {
        return nip==claveIngresada;
    }
}
