# 📈 Cotizator

API REST em Java para balanceamento inteligente de carteira de ações da B3.

## 🚀 Funcionalidades

### Gerenciamento de Ações
- **Cadastrar ações**: Adiciona ações à carteira com percentual desejado
- **Listar ações**: Visualiza todas as ações cadastradas
- **Atualizar ações**: Modifica percentuais e dados das ações
- **Remover ações**: Exclui ações da carteira

### Cálculo de Balanceamento
- **Cálculo automático**: Determina quantidade ideal de cada ação
- **Modo estrito**: Respeita limite máximo do valor disponível
- **Modo flexível**: Permite ultrapassar ligeiramente o valor para melhor distribuição
- **Relatório detalhado**: Mostra valor gasto, variação e distribuição final

## 🏗️ Arquitetura

### Stack Tecnológica
- **Java 21** - Linguagem principal
- **Spring Boot 3.5.5** - Framework web
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco em memória
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização

### Estrutura do Projeto
```
src/
├── main/java/br/com/eduardo/cotizator/
│   ├── config/          # Configurações da aplicação
│   ├── controller/      # Endpoints REST
│   ├── dto/            # Objetos de transferência
│   ├── model/          # Entidades JPA
│   ├── repository/     # Acesso a dados
│   ├── service/        # Lógica de negócio
│   └── CotizatorApplication.java
└── resources/
    ├── application.yml  # Configurações
    └── schema.sql      # Script do banco
```

### Arquitetura em Camadas
```
┌─────────────────┐
│   Controller    │ ← REST API endpoints
├─────────────────┤
│    Service      │ ← Lógica de negócio
├─────────────────┤
│   Repository    │ ← Acesso a dados
├─────────────────┤
│     Model       │ ← Entidades JPA
└─────────────────┘
```

## 📡 API Endpoints

### Ações
```http
GET    /api/stock           # Lista todas as ações
POST   /api/stock           # Cadastra/atualiza ação
DELETE /api/stock/{cod}     # Remove ação por código
```

### Cálculo de Carteira
```http
POST   /api/calculate # Calcula balanceamento
```

### Exemplos de Uso

#### Cadastrar/Editar Ação
```bash
curl -X POST http://localhost:8080/api/stock \
  -H "Content-Type: application/json" \
  -d '{
    "cod": "PETR4",
    "percentage": 30
  }'
```

#### Remover Ação
```bash
curl -X DELETE http://localhost:8080/api/stock/PETR4
```

#### Calcular Carteira
```bash
curl -X POST http://localhost:8080/api/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 10000,
    "strict_limit": true
  }'
```

## 🐳 Como Executar com Docker

### Pré-requisitos
- Docker instalado
- Git

### Passos

1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd cotizator
```

2. **Configure as variáveis de ambiente**
```bash
# Copie o arquivo de exemplo
cp src/main/resources/application.yml.exemple src/main/resources/application.yml

# Edite e adicione seu token da API B3
# Obtenha em: https://brapi.dev/
```

3. **Build da imagem Docker**
```bash
docker build -t cotizator .
```

4. **Execute o container**
```bash
docker run -p 8080:8080 cotizator
```

5. **Acesse a aplicação**
- API: http://localhost:8080/api
- Console H2: http://localhost:8080/h2-console

### Variáveis de Ambiente
```yaml
b3:
  api:
    url: https://brapi.dev/api/
    token: SEU_TOKEN_AQUI
```

## 🔧 Desenvolvimento Local

### Pré-requisitos
- Java 21
- Maven 3.6+

### Executar
```bash
# Instalar dependências
./mvnw clean install

# Executar aplicação
./mvnw spring-boot:run
```

### Testes
```bash
./mvnw test
```

## 📊 Exemplo de Funcionamento

1. **Cadastre ações com percentuais desejados:**
   - PETR4: 30%
   - VALE3: 25%
   - ITUB4: 20%
   - BBDC4: 25%

2. **Informe valor disponível:** R$ 10.000

3. **Resultado do cálculo:**
   - Sistema consulta preços atuais na B3
   - Calcula quantidade ideal de cada ação
   - Retorna distribuição otimizada e valor restante

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

---

## 📬 Contato

**Nome:** Eduardo Garcia  
**E-mail:** eg47202@gmail.com