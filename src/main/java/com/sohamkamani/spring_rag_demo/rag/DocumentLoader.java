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

        //chunquer en une seul phrase permet d'avoir un sens très préçis.
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
                        "Une fois la première phase de fermentation passé, filtrer la solution dans la bouteille qui se ferme hermétiquement et patienté jusqu'a avoir du gaz qui se forme !"),
                new Document(
                        "La potion de soin vous apportera de bon probiotiques grâce à la levure sauvage présente sur la peau des fruits"));
       /* List<Document> documents = List.of(
                new Document("StarlightDB is a serverless graph database designed for real-time analytics on complex, interconnected data."),
                new Document("The core of StarlightDB is its 'Quantum-Leap' query engine, which uses speculative execution to deliver query results up to 100x faster than traditional graph databases."),
                new Document("StarlightDB features 'Chrono-Sync' for effortless time-travel queries, allowing developers to query the state of their graph at any point in the past."),
                new Document("StarlightDB includes a built-in visualization tool called 'Nebula' that renders interactive 3D graphs directly within the development environment for easier analysis."),
                new Document("Security in StarlightDB is handled by 'Cosmic Shield', which provides end-to-end encryption and fine-grained access control at the node and edge level.")
        );*/
        //TODO: faire du decoupage de chunk avec gemini
        vectorStore.add(documents);
        System.out.println("Documents loaded into VectorStore.");
    }
}
