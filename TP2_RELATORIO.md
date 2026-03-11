# TP2 - Relatório de Refatoração

## Introdução

Este relatório documenta o processo de refatoração realizado no projeto BuildPipeline-Refactoring-Kata, parte do TP2 da disciplina de Refatoração. O objetivo foi aplicar as técnicas de refatoração aprendidas para melhorar a qualidade do código java, seguindo os cinco exercícios propostos do enunciado: verificação inicial e testes automatizados, reestruturação de métodos complexos, expressividade e clareza com variáveis, melhoria de assinaturas e encapsulamento, e reorganização de classes e processos.

## Processo de Decisão

### 1. Verificação Inicial e Testes Automatizados
Antes de qualquer modificação, foi priorizado o entendimento completo do código existente. O método Pipeline.run() foi analisado para identificar todos os caminhos possíveis de execução, com isso foram criados 5 testes unitários cobrindo cenários como testes passando/falhando, ausência de testes, deploy bem-sucedido/falhado e email habilitado/desabilitado.

### 2. Reestruturação de Métodos Complexos
O método run() original continha múltiplas responsabilidades e estruturas condicionais aninhadas, violando o princípio da responsabilidade única. Com isso foram extraídos métodos privados como runTests(), determineEmailMessage() , sendEmailSummaryIfEnabled() e deploy(). 

### 3. Expressividade e Clareza com Variáveis
Variáveis booleanas foram introduzidas para substituir expressões condicionais repetitivas, melhorando a legibilidade.

### 4. Melhorando Assinaturas e Encapsulamento
Métodos extraídos foram mantidos privados para encapsulamento, evitando exposição desnecessária. Assinaturas foram simplificadas com parâmetros claros.
Métodos como determineEmailMessage agrupam lógica relacionada, reduzindo acoplamento.

### 5. Reorganizando Classes e Processos
A classe `Pipeline` agora tem responsabilidades bem definidas: orquestração no método público e execução de etapas em métodos privados.
O processo de pipeline foi dividido em etapas sequenciais: execução de testes, deploy condicional e envio de email, tornando a arquitetura mais modular.

## Dificuldades Encontradas

- **Preservação de Comportamento**: Garantir que a refatoração não alterasse o comportamento original foi desafiador, especialmente com lógicas condicionais complexas. Os testes automatizados foram cruciais para validar cada mudança.
- **Configuração do Ambiente**: Inicialmente, houve problemas com versões incompatíveis do JDK (Gradle 6.2.2 não suporta Java 21). Resolvido ajustando para JDK 11 e corrigindo o POM.xml.
- **Testes com Dependências**: Criar testes que verificassem logs e emails exigiu o uso correto de mocks e uma classe auxiliar (CapturingLogger).

## Aprendizados Obtidos

- **Utilidade de Testes**: Testes automatizados são essenciais para refatoração segura, permitindo mudanças com assegurações de funcionalidade.
- **Refatoração Incremental**: Técnicas sobre mudanças pequenas como, extração de métodos e variáveis descritivas e de boa legibilidade.

## Conclusão

A refatoração transformou um método monolítico em um código modular, legível e testável. O processo seguiu boas práticas de desenvolvimento, com ênfase em testes automatizados para proteger contra regressões. As dificuldades encontradas reforçaram a importância de um ambiente de desenvolvimento bem configurado e da disciplina em mudanças incrementais.