API de Jogo de Bingo
API reativa para gerenciamento de jogos de Bingo, desenvolvida com Spring WebFlux e MongoDB.

Tecnologias
Java 17
Spring WebFlux
MongoDB
Docker (Opcional)
Requisitos
JDK 17+
MongoDB (rodando localmente ou via Docker)
Como Executar
Usando Docker (Recomendado)
Certifique-se de ter o Docker e Docker Compose instalados.

docker-compose up --build
A API estará disponível em http://localhost:8080.

Executando Localmente
Inicie uma instância do MongoDB na porta 27017.
Execute o projeto usando Gradle:
./gradlew bootRun
Documentação da API
Jogadores (/players)
Criar Jogador: POST /players
{
  "name": "Nome do Jogador",
  "email": "email@exemplo.com"
}
Listar Jogadores: GET /players
Buscar Jogador por ID: GET /players/{id}
Atualizar Jogador: PUT /players/{id}
Deletar Jogador: DELETE /players/{id}
Rodadas (/rounds)
Criar Rodada: POST /rounds
Gerar Cartela para Jogador: POST /rounds/{id}/bingo-card/{playerId}
Gera uma cartela com 20 números aleatórios (0-99).
Valida regras de sobreposição (máximo 1/4 de números iguais a outras cartelas).
Sortear Número: POST /rounds/{id}/generate-number
Sorteia um número único para a rodada.
Verifica se há um vencedor.
Envia e-mail (mock) para o vencedor e perdedores caso a rodada termine.
Buscar Último Número Sorteado: GET /rounds/{id}/current-number
Buscar Informações da Rodada: GET /rounds/{id}
Listar Rodadas: GET /rounds
Estrutura do Projeto
src/main/java/com/bingo/player: Módulo de Jogadores (Entity, Controller, Service, Repository).
src/main/java/com/bingo/round: Módulo de Rodadas e Cartelas.
Dockerfile & docker-compose.yml: Configuração para containerização.
Testes
Os testes unitários cobrem as regras de negócio principais (geração de cartelas, sorteio, verificação de vencedor).

Para rodar os testes:

./gradlew test
