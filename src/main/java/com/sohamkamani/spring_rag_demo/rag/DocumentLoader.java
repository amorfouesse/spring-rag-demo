package com.sohamkamani.spring_rag_demo.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocumentLoader implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    public DocumentLoader(VectorStore vectorStore, JdbcTemplate jdbcTemplate) {
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("Suppression des anciens vecteurs...");
        jdbcTemplate.execute("TRUNCATE TABLE vector_store CASCADE");
        List<Document> documents = List.of(
                new Document(
                        "La potion de soin, est un élixir à base de citron fermenté, d'eau et de sucre qui se passe en 2 étapes de fermentation."),
                new Document(
                        "La recette de cette potion nécessite 2 citrons, 1 litre d'eau, 200 grammes de sucre, un entonnoir pour filtrer, un bocal pour tout contenir pour la première phase de fermentation et une bouteille qui peut se fermer hermétiquement pour la seconde phase de fermentation."),
                new Document(
                        "Pour préparer la potion, il faudra mettre les peaux de citrons dans le bocal, avec l'eau, le sucre et mélanger tout ça ainsi que patienté plusieurs jours pour que cela fermente."),
                new Document(
                        "Si des bulles apparaissent en mélangeant, c'est bon, la première phase de fermentation est terminer."),
                new Document(
                        "Une fois la première phase de fermentation passé, filtrer la solution dans la bouteille qui se ferme hermétiquement et patienté jusqu'a avoir du gaz qui se forme !"));
        vectorStore.add(documents);
        System.out.println("Documents loaded into VectorStore.");
    }
}
