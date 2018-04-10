# Teoria de Resposta ao Item
Projeto Acadêmico de Introdução à Estatística sobre Teoria de Resposta ao Item

## To Do List

- Compilar
  - Sério, não compilei ainda. Acredito que vá dar mil erros. Então se alguém puder compilar, corrigir e colocar aqui, seria top.
  - Até aqui, o código não faz nada objetivamente. Então talvez seja legal fazer o main imprimir o score dos candidatos para as provas criadas, que tal?
- Parte 1
  - Selecionando o melhor aluno
    - Criar rotina para aplicar a mesma prova n vezes para um determinado candidato
  - Selecionando a melhor prova
    - Para a melhor prova, imagina-se que ela seja composta pelas questões com maior probabilidade de favorecer o aluno de theta maior, portanto talvez devemos criar um método na classe Question que compare dois alunos o quanto essa questão favorece o aluno a em relação ao aluno b?
  - Intervalo de confianca
- Parte 2
  - Estimador Pontual - Alunos
  - Selecionando o melhor aluno - Habilidade do Aluno
  - Intervalo de confian ̧ca - Habilidade do Aluno
  - Intervalo de confiança - t-student
- Relatório

## O que foi feito

- Classes:
  - Candidato (Candidate) - entidade do candidato/aluno avaliado; contém o aspecto theta
  - Questão (Question) - entidade da questão; contém os indicadores a (parâmetro de discriminação) e b (parâmetro de dificuldade)
  - Prova (Exam) - entidade da prova; contém uma coleção de questões; pode ser aplicada a um candidato para indicar sua pontuação
  - Acervo de Questões (QuestionLibrary) - entidade da coleção de questões disponíveis; contém uma coleção de questões; é gerada a partir de um arquivo com os parâmetros a e b das questões
  - TRI - classe estática de entrada que contém método estático para indicar a possibilidade de um candidato/aluno acertar uma questão com base nos parâmetros theta (do candidato/aluno), a (da questão) e b (da questão)
