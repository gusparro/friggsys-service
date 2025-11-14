# FriggSys Service

FriggSys Service é uma API robusta e flexível para administração de usuários, sistemas e perfis de acesso. Inspirado na deusa nórdica Frigg, guardiã da ordem e do conhecimento.

## Tecnologias Utilizadas

- **Linguagem:** Java <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="24" height="20" style="vertical-align: baseline;" />
- **Framework:** Spring Boot <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="24" height="16" style="vertical-align: middle;" />
- **Banco de Dados:** PostgreSQL <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="24" height="16" style="vertical-align: middle;" />

## Instalação

#### 1. Clone o repositório:

```bash
  git clone https://github.com/gusparro/friggsys-service.git
```

#### 2. Instale as dependências:

```bash
   mvn clean install
```

#### 3. Inicie o microsserviço:

```bash
   mvn spring-boot:run
```

## Documentação da API

A documentação completa da API está disponível através do Swagger UI. Após iniciar a aplicação, você pode acessar:

### Swagger UI
```
http://localhost:8080/friggsys-service/api/v1/swagger-ui.html
```

### OpenAPI JSON
```
http://localhost:8080/friggsys-service/api/v1/api-docs
```

## Cobertura de Testes (JaCoCo)

O projeto utiliza o JaCoCo para análise de cobertura de código. A cobertura mínima exigida é de 80% por pacote.

### Gerar Relatório de Cobertura

Para executar os testes e gerar o relatório de cobertura:

```bash
   mvn clean verify
```

**Atenção**: O comando acima irá falhar se a cobertura estiver abaixo de 80%, mas o relatório será gerado mesmo assim.

### Visualizar Relatório

Após a execução, o relatório HTML estará disponível em:
```
target/site/jacoco/index.html