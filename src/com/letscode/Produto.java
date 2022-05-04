package com.letscode;

import java.time.LocalDateTime;

public class Produto {

    private Tipo tipo;
    private String marca;
    private String identificador;
    private String nome;
    private double precoCusto;
    private double precoVenda;

    private LocalDateTime ultimaCompra;

    private int emEstoque;

    Produto(Tipo tipo, String marca, String identificador, String nome){
        this.tipo = tipo;
        this.marca = marca;
        this.identificador = identificador;
        this.nome = nome;
    }

    public Tipo getTipo() {return tipo; }

    public String getMarca() {return marca; }

    public String getIdentificador() {return identificador; }

    public String getNome() {return nome;}

    public double getPrecoCusto(){return  this.precoCusto;}

    public double getPrecoVenda(){return this.precoVenda;}

    public LocalDateTime getUltimaCompra() {return this.ultimaCompra; }

    public int getEmEstoque() {return emEstoque; }


    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoCusto * this.tipo.getMarkup();
    }

    //identificadorFinal implementado para resolver questão de duas marcas com o mesmo identificador
    public String getIdentificadorFinal(){
        return (this.marca+this.identificador);
    }

    public void setUltimaCompra(LocalDateTime ultimaCompra) {
        this.ultimaCompra = ultimaCompra;
    }

    public void setEmEstoque(int emEstoque) {
        this.emEstoque = emEstoque;
    }

}
