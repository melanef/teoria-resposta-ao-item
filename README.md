# Teoria de Resposta ao Item
Projeto Acadêmico de Introdução à Estatística sobre Teoria de Resposta ao Item

## To Do List

- Parte 2
  - Selecionando o melhor aluno - Habilidade do Aluno
  - Intervalo de confiança - Habilidade do Aluno

## O que foi feito

- Classes:
  - Candidato (Candidate) - entidade do candidato/aluno avaliado; contém o aspecto theta
  - Questão (Question) - entidade da questão; contém os indicadores a (parâmetro de discriminação) e b (parâmetro de dificuldade)
  - Prova (Exam) - entidade da prova; contém uma coleção de questões; pode ser aplicada a um candidato para indicar sua pontuação
  - Acervo de Questões (QuestionLibrary) - entidade da coleção de questões disponíveis; contém uma coleção de questões; é gerada a partir de um arquivo com os parâmetros a e b das questões
  - TRI - classe estática de entrada que contém método estático para indicar a possibilidade de um candidato/aluno acertar uma questão com base nos parâmetros theta (do candidato/aluno), a (da questão) e b (da questão); é nessa classe que está a lógica de geração dos arquivos de saída de cada item de cada parte
  - Comparador de Questões (QuestionComparator) - implementação de Comparator que recebe, no construtor, os dois candidatos a serem utilizados para indicar se uma questão favorece mais ou menos o primeiro destes candidatos
- Objetivos:
  - Parte 1
    - Selecionando o melhor aluno
    - Selecionando a melhor prova
    - Relatório
    
  - Parte 2
    - Estimador Pontual - Alunos
    - Intervalo de confiança
