/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author RaelH
 */
public class Data {
    private byte  dia;
    private byte  mes;
    private byte  ano;
    private byte  hora;
    private byte  minuto;
    private byte  segundos;

    public Data() {
    }

    public byte getDia() {
        return dia;
    }

    public void setDia(byte dia) {
        this.dia = dia;
    }

    public byte getMes() {
        return mes;
    }

    public void setMes(byte mes) {
        this.mes = mes;
    }

    public byte getAno() {
        return ano;
    }

    public void setAno(byte ano) {
        this.ano = ano;
    }

    public byte getHora() {
        return hora;
    }

    public void setHora(byte hora) {
        this.hora = hora;
    }

    public byte getMinuto() {
        return minuto;
    }

    public void setMinuto(byte minuto) {
        this.minuto = minuto;
    }

    public byte getSegundos() {
        return segundos;
    }

    public void setSegundos(byte segundos) {
        this.segundos = segundos;
    }

    
    
    
}
