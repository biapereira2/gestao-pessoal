package com.exemplo.app;


import com.exemplo.core.domain.entidade.Pessoa;

public class Main {
    public static void main(String[] args) {
        Pessoa p = new Pessoa("Patrono");
        System.out.println("Olá, " + p.getNome());
    }
}
