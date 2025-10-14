package com.exemplo.app;


import com.exemplo.core.domain.entidade.Usuario;

public class Main {
    public static void main(String[] args) {
        Usuario p = new Usuario("Patrono");
        System.out.println("Olá, " + p.getNome());
    }
}
