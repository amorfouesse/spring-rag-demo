package com.sohamkamani.spring_rag_demo.rag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage; // Changed import
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final Logger logger = LoggerFactory.getLogger(RagService.class);

    @Value("classpath:/prompts/rag-prompt.st")
    private Resource ragPromptTemplate;

    public RagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
                .build();
        this.vectorStore = vectorStore;
    }

    public String retrieveAndGenerate(String message) {
        // le message de l'utilisateur est convertit en embbeding
        // puis effectue une recherche de similarité sémantique pour trouver les bouts de textes les plus proches du sens de la question

        List<Document> similarDocuments = vectorStore
                .similaritySearch(SearchRequest
                        .builder()
                        .query(message)
                        // ne récupère que les 4 segments les plus pertinents pour ne pas surcharger le contexte de l'IA.
                        .topK(5)
                        .build());

        logger.info("\n>>> Similar documents: {}",similarDocuments);
        // les llm ne prennent pas d'objets Java en entrée, ils ne comprennent que le texte brut.
        // On concatène donc les 4 documents trouvés, séparés par des sauts de ligne.
        String information = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        // utilise le template de réponse à l'utilisateur
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(ragPromptTemplate);
        Prompt prompt = new Prompt(List.of(
                systemPromptTemplate.createMessage(
                        Map.of("information", information)),
                new UserMessage(message)));
        logger.info("\n>>> Prompt: {}", prompt.getContents());

        //l'appel final au llm avec Instructions + Contexte documentaire + Question utilisateur
        // content -> renvoie uniquement la réponse
        return chatClient.prompt(prompt).call().content();
    }
}
