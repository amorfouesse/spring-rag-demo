# Spring AI RAG Demo


## Components

*   **Spring Boot Application:** The main application that orchestrates the RAG pipeline.
*   **PGVector:** A PostgreSQL extension used as a vector database to store and retrieve document embeddings.
*   **OpenAI Embedding Model:** Used to convert text documents and user queries into numerical vector representations.
*   **OpenAI Chat Model:** Used to generate responses based on the user's query and retrieved relevant information.
*   **DocumentLoader:** A Spring component that loads sample documents into the PGVector store on application startup.
*   **RagService:** A service that handles the RAG logic:
    *   Takes a user query.
    *   Performs a similarity search in the PGVector store to find relevant documents.
    *   Constructs a prompt for the OpenAI chat model, incorporating the original query and the retrieved document content.
    *   Returns the generated response from the OpenAI chat model.
*   **RagController:** A REST controller that exposes an endpoint (`/ai/rag`) to interact with the `RagService`.
*   **`rag-prompt.st`:** A prompt template used by the `RagService` to guide the AI model's response, ensuring it uses the provided information.

## How to setup

Pour crée la base de donnée vectorisé suivé la step 2 :
https://www.sohamkamani.com/java/spring-ai-rag-application/
## How to Run

Lancer le serveur:
```
gradle bootRun
```
Dans un terminal git bash faire cette commande pour connaitre les informations en BDD: 
```
 curl -X POST -H "Content-Type: application/json" http://localhost:8080/ai/rag -d '{"message": "a quoi ressemble les orks ?"}'
```