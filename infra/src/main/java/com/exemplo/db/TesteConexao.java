package com.exemplo.db;

import com.exemplo.core.domain.entidade.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TesteConexao {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestaoPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Usuario u = new Usuario("Ygor", "ygor@email.com", "123");
        em.persist(u);
        em.getTransaction().commit();

        em.close();
        emf.close();
        System.out.println("Usuário salvo!");
    }
}
