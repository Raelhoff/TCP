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
public class User {
    private int id;
    private int idade;
    private int peso;
    private int altura;
    private int tamanhoNome;
    private String nome;
    
    public User() {
    
    }

    public User( int idade, int peso, int altura, int tamanhoNome, String nome) {
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.tamanhoNome = tamanhoNome;
        this.nome = nome;
    }


    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getTamanhoNome() {
        return tamanhoNome;
    }

    public void setTamanhoNome(int tamanhoNome) {
        this.tamanhoNome = tamanhoNome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    
}
