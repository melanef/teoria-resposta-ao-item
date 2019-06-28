import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * TRI.
 * Pontro de entrada do programa com método main, métodos estáticos para cada um dos itens pedidos na definição e o 
 * método estático "chance" que calcula a probabilidade de um candidato acertar uma questão (dados o parâmetro de 
 * habilidade do candidato e os parâmetros discriminatório e de dificuldade da questão).
 */
public class TRI {
    /**
     * Constante: QUESTIONS_PATH -- indica o endereço do arquivo que contém as informações das questões.
     */
    public static final String QUESTIONS_PATH = "../data/questoes.txt";

    /**
     * Constante: RESULTS_PATH -- indica o endereço do arquivo que contém os resultados dos alunos a cada questão 
     * (1 = acerto; 0 = erro)
     */
    public static final String RESULTS_PATH = "../data/respostas.txt";

    /**
     * Constante: SEPARATOR (separador) -- caractere usado para separar os valores nas linhas do arquivo.
     */
    public static final String SEPARATOR = " ";

    /**
     * Constante: PATTERN (padrão) -- indica o padrão de esperado erm cada linha do arquivo de questões.
     */
    public static final String PATTERN = "[0-9\\.]+ [0-9\\.]+";
    
    /**
     * Constante: HIT_INDICATOR (indicador de acerto) -- caractere usado para indicar um acerto nos arquivos de 
     * respostas.
     */
    public static final String HIT_INDICATOR = "1";

    /**
     * Constante: I1_OUTPUT_PATH -- indica o endereço do arquivo de saída que deve ser gerado para o item 1.
     */
    public static final String I1_OUTPUT_PATH = "../outputs/I1.txt";

    /**
     * Constante: I2_OUTPUT_PATH -- indica o endereço do arquivo de saída que deve ser gerado para o item 2.
     */
    public static final String I2_OUTPUT_PATH = "../outputs/I2.txt";

    /**
     * Constante: I3_OUTPUT_PATH -- indica o endereço do arquivo de saída que deve ser gerado para o item 3.
     */
    public static final String I3_OUTPUT_PATH = "../outputs/II1.txt";

    /**
     * Constante: I4_OUTPUT_PATH -- indica o endereço do arquivo de saída que deve ser gerado para o item 4.
     */
    public static final String I4_OUTPUT_PATH = "../outputs/II2.txt";

    /**
     * Constante: I5_OUTPUT_PATH -- indica o endereço do arquivo de saída que deve ser gerado para o item 5.
     */
    public static final String I5_OUTPUT_PATH = "../outputs/II3.txt";

    /**
     * Constante: I6_OUTPUT_PATH -- indica o endereço do arquivo de saída que deve ser gerado para o item 5.
     */
    public static final String I6_OUTPUT_PATH = "../outputs/II4.txt";

    /**
     * Constante: TRIES -- indica a quantidade de simulações devem ser executadas para cada item.
     */
    public static final double TRIES = 100000;

    /**
     * Constante: TRUST_DEGREE -- indica o grau de confiança exigido para os itens 5 e 6.
     */
    public static final double TRUST_DEGREE = 0.1;

    /**
     * Constante: THETA_PRECISION (precisão theta) -- indica o nível de precisão desejado, em quantidade de casas 
     * decimais para a definição do theta
     */
    public static final double THETA_PRECISION = 5;
    
    /**
     * Constante: MINIMUM_BOUNDARY (limite inferior) -- valor mínimo para estimativa do theta (habilidade do aluno) pelo
     * método da bissecção.
     */
    public static final double MINIMUM_BOUNDARY = -15.0;
    
    /**
     * Constante: MAXIMUM_BOUNDARY (limite superior) -- valor maximo para estimativa do theta (habilidade do aluno) pelo
     * método da bissecção.
     */
    public static final double MAXIMUM_BOUNDARY = 15.0;
    
    /**
     * Constante: DERIVATIVE_FACTOR (fator de derivação) -- valor a ser usado como diferencial no cálculo da derivada
     * através do método do limite.
     */
    public static final double DERIVATIVE_FACTOR = 0.0001;
    
    /**
     * Constante: LEVEL_MARGIN (margem de nível) -- valor a ser considerado como proximidade tolerante para a busca de 
     * zeros de uma função matemática através do método da bissecção.
     */
    public static final double LEVEL_MARGIN = 0.00001;

    /**
     * Método estático. Invoca método a seguir, facilitando a consulta dos parâmetros da questão recebida como segundo 
     * parâmetro.
     *
     * @param   double      theta       Parâmetro de habilidade do candidato
     * @param   Question    question    Questão avaliada
     *
     * @return  double
     */
    public static double chance(double theta, Question question)
    {
        return TRI.chance(theta, question.getA(), question.getB());
    }

    /**
     * Método estático. Cálculo da probabilidade de um candidato acertar uma questão, dados seu parâmetro de habilidade 
     * e os parâmetros de discriminação e dificuldade da questão.
     *
     * @param   double  theta   Parâmetro de habilidade do candidato
     * @param   double  a       Parâmetro de discriminação da questão
     * @param   double  b       Parâmetro de dificuldade da questão
     *
     * @return  double
     */
    public static double chance(double theta, double a, double b)
    {
        return (Math.pow(Math.E, a * (theta - b)) / (1.0 + Math.pow(Math.E, (a * (theta - b)))));
    }

    /**
     * Método estático. Ponto de entrada do programa. Aqui instanciamos nosso repositório de questões indicando o 
     * arquivo de questões a ser usado, bem como nossa coleção de resultados dos candidatos amostrais, indicando o
     * arquivo de resultados a ser usado, instanciamos também nossos 5 candidatos modelo e, finalmente, invocamos os 
     * métodos estáticos de cada item.
     *
     * @param   String[]    args    Parâmetro padrão de argumentos da linha de comando. Descartado nesse caso.
     *
     * @return  void
     */
    public static void main(String [] args)
    {
        List<List<List<ResultSet>>> examsCandidatesResultSets;
        QuestionLibrary library = QuestionLibrary.createFromFile(TRI.QUESTIONS_PATH);

        Candidate [] candidates = new Candidate[5];
        candidates[0] = new Candidate(-1.0);
        candidates[1] = new Candidate(-0.5);
        candidates[2] = new Candidate(0.0);
        candidates[3] = new Candidate(0.5);
        candidates[4] = new Candidate(1.0);

        /**
         * Cria provas aleatórias
         */
        Exam [] exams = new Exam[4];
        exams[0] = new Exam(10, library);
        exams[1] = new Exam(20, library);
        exams[2] = new Exam(50, library);
        exams[3] = new Exam(100, library);

        /**
         * Aplica as provas aleatórias aos 5 candidatos
         */
        examsCandidatesResultSets = TRI.applyExams(candidates, exams);

        /**
         * Conta vezes que candidato 5 foi melhor do que os demais nas aplicações de provas aleatórias
         */
        TRI.I1(examsCandidatesResultSets);
        
        /**
         * Cria as provas com as questões que mais favorecem o aluno 5 em detrimento do aluno 4
         */
        Exam [] bestExams = TRI.getBestExams(candidates[4], candidates[3], library);
        exams[0] = bestExams[0];
        exams[1] = bestExams[1];
        exams[2] = bestExams[2];

        /**
         * Aplica as provas que favorecem o aluno 5 aos 5 candidatos
         */
        examsCandidatesResultSets = TRI.applyExams(candidates, exams);
        
        TRI.I2(examsCandidatesResultSets, bestExams);

        List<ResultSet> results = ResultSet.createFromFile(TRI.RESULTS_PATH);

        TRI.I3(results, library);
        
        TRI.I4(examsCandidatesResultSets, exams);

        TRI.I5(examsCandidatesResultSets, exams);

        TRI.I6(examsCandidatesResultSets, exams);
    }

    /**
     * Item 1.
     * Criamos 4 provas, com 10, 20, 50 e 100 questões escolhidas aleatoriamente
     * do repositório. Para cada uma das 4 provas, executamos o seguinte:
     *   Para cada um dos 4 candidatos primeiros candidatos (em ordem de
     *   parâmetro de habilidade, do menor para o maior, excluindo o de maior
     *   habilidade), executamos o seguinte:
     *     Aplicamos a prova para o candidato atual e para o melhor candidato e,
     *     se o melhor candidato obtiver melhor avaliação do que o candidato
     *     atual, incrementamos um contador de vezes que o melhor candidato foi
     *     de fato melhor do que o atual. Repetimos esse procedimento TRIES
     *     vezes.
     *     Ao fim desse procedimento, é possível obter a relação de vezes nas
     *     quais o melhor candidato foi melhor do que o candidato atual pela
     *     quantidade de simulações executadas, e assim, estimar a probabilidade
     *     do melhor candidato ser melhor do que o candidato atual.
     *     O valor desta relação (entre 0 e 1) é incluído na string que será
     *     usada para dar saída.
     *
     * @param   Candidate[]     candidates  Candidatos instanciados
     * @param   QuestionLibrary library     Repositório de questões criado
     *
     * @return  void
     */
    protected static void I1(List<List<List<ResultSet>>> examsCandidatesResultSets)
    {
        try {
            PrintWriter writer = new PrintWriter(I1_OUTPUT_PATH, "UTF-8");

            int bestCandidateIndex = examsCandidatesResultSets.get(0).size() - 1;
            for (int i = 0; i < examsCandidatesResultSets.size(); i++) {
                String examLine = "";

                /**
                 * Compara resultados obtidos por cada aluno com o aluno 5 em função da quantidade de acertos de cada.
                 */
                for (int j = 0; j < bestCandidateIndex; j++) {
                    double timesBestIsBetter = TRI.compareScores(
                        (ArrayList<ResultSet>) examsCandidatesResultSets.get(i).get(bestCandidateIndex),
                        (ArrayList<ResultSet>) examsCandidatesResultSets.get(i).get(j)
                    );
                    
                    double probability = timesBestIsBetter / TRIES;
                    examLine = examLine + probability + " ";
                }

                writer.println(examLine.trim());
            }

            writer.close();
        }
        catch (IOException exception) {
            System.out.println("Erro na escrita do arquivo de saída I1: " + exception.getMessage());
        }
    }  

    /**
     * Item 2.
     * Criamos 3 provas, com 10, 20, 50 questões escolhidas especificamente para
     * favorecer o candidato 5 (considerado favorito por ser o de maior parâmetro
     * de habilidade) em comparação com o candidato 4 (o segundo melhor, de acordo
     * com o parâmetro de habilidade).
     * Para isso, primeiro precisamos copiar a relação de questões do nosso
     * repositório para uma lista que podemos manipular. Manipulamos esta lista
     * usando nosso comparador e método sort da classe Collections, ou seja,
     * ordenamos a lista de questões de forma que as primeiras questões sejam
     * aquelas que apresentem maior diferença entre as chance de acerto dos
     * alunos 5 e 4 acertarem.
     * Desta maneira, todas as questões da prova de 10 questões estão contidas
     * nas demais provas, já que esta ordem não muda.
     * Para cada uma das 3 provas, executamos o seguinte:
     *   Geramos a string de saída com os identificadores numéricos das questões
     *   que compõem a prova. Para cada um dos 4 candidatos primeiros candidatos
     *   (em ordem de parâmetro de habilidade, do menor para o maior, excluindo
     *   o de maior habilidade), executamos o seguinte:
     *     Aplicamos a prova para o candidato atual e para o melhor candidato e,
     *     se o melhor candidato obtiver melhor avaliação do que o candidato
     *     atual, incrementamos um contador de vezes que o melhor candidato foi
     *     de fato melhor do que o atual. Repetimos esse procedimento TRIES
     *     vezes.
     *     Ao fim desse procedimento, é possível obter a relação de vezes nas
     *     quais o melhor candidato foi melhor do que o candidato atual pela
     *     quantidade de simulações executadas, e assim, estimar a probabilidade
     *     do melhor candidato ser melhor do que o candidato atual.
     *     O valor desta relação (entre 0 e 1) é incluído na string que será
     *     usada para dar saída.
     *
     * @param   Candidate[]     candidates  Candidatos instanciados
     * @param   QuestionLibrary library     Repositório de questões criado
     *
     * @return  void
     */
    protected static void I2(List<List<List<ResultSet>>> examsCandidatesResultSets, Exam [] exams)
    {
        try {
            PrintWriter writer = new PrintWriter(I2_OUTPUT_PATH, "UTF-8");
            
            int bestCandidateIndex = examsCandidatesResultSets.get(0).size() - 1;
            for (int i = 0; i < exams.length; i++) {
                String examQuestionsLine = "";
                String examLine = "";

                for (int j = 0; j < exams[i].size(); j++) {
                    Question currentQuestion = exams[i].getQuestion(j);
                    examQuestionsLine = examQuestionsLine + currentQuestion.getId() + " ";
                }

                /**
                 * Compara resultados obtidos por cada aluno com o aluno 5 em função da quantidade de acertos de cada.
                 */
                for (int j = 0; j < bestCandidateIndex; j++) {
                    double timesBestIsBetter = TRI.compareScores(
                        (ArrayList<ResultSet>) examsCandidatesResultSets.get(i).get(bestCandidateIndex),
                        (ArrayList<ResultSet>) examsCandidatesResultSets.get(i).get(j)
                    );

                    double probability = timesBestIsBetter / TRIES;
                    examLine = examLine + probability + " ";
                }

                writer.println(examQuestionsLine.trim());
                writer.println(examLine.trim());
            }

            writer.close();
        }
        catch (IOException exception) {
            System.out.println("Erro na escrita do arquivo de saída I2: " + exception.getMessage());
        }
    }

    /**
     * Item 3.
     * Para cada um dos resultados lidos previamente, executamos o seguinte:
     *   Aplicamos o método do Estimador de Máxima Verossimilhança, com uma
     *   abordagem computacional que abre mão de manipulações algébricas, porém
     *   beneficia-se de análise numérica. O método da bissecção foi o escolhido
     *   para a identificação de possíveis raízes da derivada da função log-
     *   likelihood. Em vez de usarmos derivadas com regras de derivação,
     *   aplicamos a definição formal em função de limites para tal.
     *
     *   O procedimento prático consiste em:
     *     Definimos duas fronteiras e um ponto médio entre elas, e calculamos a
     *     derivada da função log-likelihood nestes três pontos. Comparando os
     *     pontos dois-a-dois (limite inferior com ponto médio, e ponto médio
     *     com limite superior), podemos identificar se houve inversão no sinal
     *     da derivada (o que indica inversão de sentido), um forte indício de
     *     que há um ponto de máximo entre estes. Atualizamos os pontos de
     *     limite e repetimos o procedimento até que o valor encontrado para a
     *     derivada da função log-likelihood no ponto médio seja próxima o
     *     bastante de zero.
     *
     * @param List<ResultSet>  results Coleção de resultados
     * @param QuestionLibrary           library Repositório de questões criado
     *
     * @return void
     */
    protected static void I3(List<ResultSet> results, QuestionLibrary library)
    {
        try {
            PrintWriter writer = new PrintWriter(I3_OUTPUT_PATH, "UTF-8");

            String format = "%." + (int) TRI.THETA_PRECISION + "f";

            Exam exam = new Exam(100, library);
            Iterator<ResultSet> candidateIterator = results.iterator();
            while (candidateIterator.hasNext()) {
                ResultSet current = candidateIterator.next();
                writer.printf(format, current.estimate(exam));
                writer.println();
            }

            writer.close();
        }
        catch (IOException exception) {
            System.out.println("Erro na escrita do arquivo de saída I3: " + exception.getMessage());
        }
    }

    /**
     * Item 4.
     *
     * @param Candidate[]       candidates  Candidatos instanciados
     * @param Exam[]            exams       Provas criadas anteriormente
     * @param QuestionLibrary   library     Repositório de questões criadas
     *
     * @return void
     */
    protected static void I4(List<List<List<ResultSet>>> examsCandidatesResultSets, Exam [] exams)
    {
        try {
            PrintWriter writer = new PrintWriter(I4_OUTPUT_PATH, "UTF-8");

            int bestCandidateIndex = examsCandidatesResultSets.get(0).size() - 1;
            for (int i = 0; i < examsCandidatesResultSets.size(); i++) {
                String examLine = "";
                
                /**
                 * Compara resultados obtidos por cada aluno com o aluno 5 em função da habilidade estimada (theta) de 
                 * cada.
                 */
                for (int j = 0; j < bestCandidateIndex; j++) {
                    double timesBestIsBetter = TRI.compareThetas(
                        exams[i],
                        (ArrayList<ResultSet>) examsCandidatesResultSets.get(i).get(bestCandidateIndex),
                        (ArrayList<ResultSet>) examsCandidatesResultSets.get(i).get(j)
                    );

                    double probability = timesBestIsBetter / TRIES;
                    examLine = examLine + probability + " ";
                }
                
                writer.println(examLine.trim());
            }
            
            writer.close();
        }
        catch (IOException exception) {
            System.out.println("Erro na escrita do arquivo de saída I4: " + exception.getMessage());
        }
        catch (Exception exception) {
            System.out.println("Exception: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    /**
     * Item 5.
     *
     *
     * @return void
     */
    protected static void I5(List<List<List<ResultSet>>> examsCandidatesResultSets, Exam [] exams)
    {
        try {
            PrintWriter writer = new PrintWriter(I5_OUTPUT_PATH, "UTF-8");
            String format = "%." + (int) TRI.THETA_PRECISION + "f";

            int bestCandidateIndex = examsCandidatesResultSets.get(0).size() - 1;
            for (int i = 0; i < examsCandidatesResultSets.size(); i++) {
                List<List<ResultSet>> currentExamCandidatesResultSets = (List<List<ResultSet>>) examsCandidatesResultSets.get(i);
                for (int j = 0; j < bestCandidateIndex; j++) {
                    if (j != 0) {
                        writer.printf(" ");
                    }

                    ArrayList<ResultSet> currentCandidateResultSets = (ArrayList<ResultSet>) currentExamCandidatesResultSets.get(j);
                    int [] thetaFrequency = TRI.getThetaFrequencyDomain();

                    for (int k = 0; k < TRIES; k++) {
                        ResultSet currentResultSet = currentCandidateResultSets.get(k);
                        thetaFrequency[TRI.convertThetaToIndex(currentResultSet.estimate(exams[i]))]++;
                    }

                    int volumes [][] = new int[thetaFrequency.length + 1][thetaFrequency.length + 1];
                    for (int k = 0; k < thetaFrequency.length; k++) {
                        for (int l = 0; l < thetaFrequency.length; l++) {
                            volumes[k][l] = 0;
                            for (int m = k; m <= l; m++) {
                                volumes[k][l] += thetaFrequency[m];
                            }
                        }
                    }

                    int leftEdge = 0;
                    int rightEdge = thetaFrequency.length - 1;
                    int gap = rightEdge - leftEdge;
                    double expectedVolume = (1.0 - TRUST_DEGREE) * TRIES;

                    for (int k = 0; k < thetaFrequency.length; k++) {
                        for (int l = thetaFrequency.length - 1; l > k; l--) {
                            if (volumes[k][l] >= expectedVolume && gap > (l - k)) {
                                leftEdge = k;
                                rightEdge = l;
                                gap = rightEdge - leftEdge;
                            }
                        }
                    }

                    writer.printf(format, TRI.convertIndexToTheta((double) leftEdge));
                    writer.printf(" ");
                    writer.printf(format, TRI.convertIndexToTheta((double) rightEdge));
                }


                writer.println();
            }

            writer.close();
        }
        catch (IOException exception) {
            System.out.println("Erro na escrita do arquivo de saída I5: " + exception.getMessage());
        }
    }

    /**
     * Item 6.
     * Criamos 4 provas, com 10, 20, 50 e 100 questões escolhidas aleatoriamente
     * do repositório. Para cada uma das 4 provas, executamos o seguinte:
     *   Para cada um dos 5 candidatos, executamos o seguinte:
     *     Alocamos um arranjo de n+1 posições onde n corresponde à quantidade
     *     de questões na prova atual. Este arranjo é inicializado com 0 em todas
     *     as posições. Aplicamos a prova atual TRIES vezes para o candidato
     *     atual e incrementamos o valor na posição correspondente à quantidade
     *     de acertos na prova.
     *     Ao fim deste procedimento, temos, neste arranjo, a distribuição
     *     amostral dos resultados obtidos pelo candidato atual na prova atual.
     *     Comparamos todos os intervalos possíveis na distribuição amostral de
     *     maneira que a frequência acumulada nestes intervalos seja superior a
     *     1 - TRUST_DEGREE. O menor intervalo é o escolhido para a saída da
     *     estimativa do resultado deste candidato.
     *
     * @param   Candidate[]     candidates  Candidatos instanciados
     * @param   QuestionLibrary library     Repositório de questões criado
     *
     * @return  void
     */
    protected static void I6(List<List<List<ResultSet>>> examsCandidatesResultSets, Exam [] exams)
    {
        try {
            PrintWriter writer = new PrintWriter(I6_OUTPUT_PATH, "UTF-8");

            int bestCandidateIndex = examsCandidatesResultSets.get(0).size() - 1;
            for (int i = 0; i < examsCandidatesResultSets.size(); i++) {
                List<List<ResultSet>> currentExamCandidatesResultSets = (List<List<ResultSet>>) examsCandidatesResultSets.get(i);
                String examLine = "";

                /**
                 * Aplica as provas recém criadas comparando a chance do aluno 5 ser melhor do que cada um dos outros 4 alunos
                 */
                for (int j = 0; j < bestCandidateIndex; j++) {
                    ArrayList<ResultSet> currentCandidateResultSets = (ArrayList<ResultSet>) currentExamCandidatesResultSets.get(j);
                    int [] scoreFrequency = new int[exams[i].size() + 1];

                    for (int k = 0; k < scoreFrequency.length; k++) {
                        scoreFrequency[k] = 0;
                    }

                    for (int k = 0; k < TRIES; k++) {
                        ResultSet currentResultSet = currentCandidateResultSets.get(k);
                        scoreFrequency[(int) currentResultSet.getScore()]++;
                    }

                    int volumes [][] = new int[scoreFrequency.length + 1][scoreFrequency.length + 1];
                    for (int k = 0; k < scoreFrequency.length; k++) {
                        for (int l = 0; l < scoreFrequency.length; l++) {
                            volumes[k][l] = 0;
                            for (int m = k; m <= l; m++) {
                                volumes[k][l] += scoreFrequency[m];
                            }
                        }
                    }

                    int leftEdge = 0;
                    int rightEdge = scoreFrequency.length - 1;
                    int gap = rightEdge - leftEdge;
                    double expectedVolume = (1.0 - TRUST_DEGREE) * TRIES;

                    for (int k = 0; k < scoreFrequency.length; k++) {
                        for (int l = scoreFrequency.length - 1; l > k; l--) {
                            if (volumes[k][l] >= expectedVolume && gap > (l - k)) {
                                leftEdge = k;
                                rightEdge = l;
                                gap = rightEdge - leftEdge;
                            }
                        }
                    }

                    examLine = examLine + leftEdge + " " + rightEdge + " ";
                }

                writer.println(examLine.trim());
            }

            writer.close();
        }
        catch (IOException exception) {
            System.out.println("Erro na escrita do arquivo de saída I6: " + exception.getMessage());
        }
    }

    protected static Exam[] getBestExams(Candidate bestCandidate, Candidate worstCandidate, QuestionLibrary library)
    {
        List<Question> questions = library.getQuestions();
        Collections.sort(questions, new QuestionComparator(bestCandidate, worstCandidate));
        
        Exam [] exams = new Exam[3];
        exams[0] = new Exam(10);
        exams[1] = new Exam(20);
        exams[2] = new Exam(50);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < exams[i].size(); j++) {
                Question currentQuestion = questions.get(j);
                exams[i].addQuestion(j, currentQuestion);
            }
        }

        return exams;
    }

    protected static List<List<List<ResultSet>>> applyExams(Candidate [] candidates, Exam [] exams)
    {
        List<List<List<ResultSet>>> examsCandidatesResultSets = new ArrayList<List<List<ResultSet>>>(exams.length);

        for (int i = 0; i < exams.length; i++) {
            List<List<ResultSet>> candidatesResultSets = new ArrayList<List<ResultSet>>(candidates.length);

            for (int j = 0; j < candidates.length; j++) {
                List<ResultSet> resultSets = new ArrayList<ResultSet>((int) TRIES);

                for (int k = 0; k < TRIES; k++) {
                    resultSets.add(k, exams[i].tryCandidate(candidates[j]));
                }

                candidatesResultSets.add(j, resultSets);
            }

            examsCandidatesResultSets.add(i, candidatesResultSets);
        }

        return examsCandidatesResultSets;
    }

    protected static double compareScores(List<ResultSet> bestCandidateResultSets, List<ResultSet> currentCandidateResultSets)
    {
        double timesBestIsBetter = 0;
        for (int i = 0; i < TRIES; i++) {
            ResultSet bestCandidate = bestCandidateResultSets.get(i);
            ResultSet currentCandidate = currentCandidateResultSets.get(i);

            if (bestCandidate.getScore() > currentCandidate.getScore()) {
                timesBestIsBetter++;
            }
        }

        return timesBestIsBetter;
    }

    protected static double compareThetas(Exam exam, List<ResultSet> bestCandidateResultSets, List<ResultSet> currentCandidateResultSets)
    {
        double timesBestIsBetter = 0;
        for (int i = 0; i < TRIES; i++) {
            ResultSet bestCandidate = bestCandidateResultSets.get(i);
            ResultSet currentCandidate = currentCandidateResultSets.get(i);

            if (bestCandidate.estimate(exam) > currentCandidate.estimate(exam)) {
                timesBestIsBetter++;
            }
        }

        return timesBestIsBetter;
    }

    protected static int[] getThetaFrequencyDomain()
    {
        int domainSize = (int) ((Math.abs(TRI.MAXIMUM_BOUNDARY) * Math.pow(10.0, TRI.THETA_PRECISION)) + (Math.abs(TRI.MINIMUM_BOUNDARY) * Math.pow(10.0, TRI.THETA_PRECISION)) + 1.0);
        int [] thetaFrequency = new int[domainSize];

        for (int k = 0; k < thetaFrequency.length; k++) {
            thetaFrequency[k] = 0;
        }

        return thetaFrequency;
    }

    protected static double convertIndexToTheta(double edge)
    {
        return (edge / Math.pow(10.0, TRI.THETA_PRECISION)) - Math.abs(TRI.MINIMUM_BOUNDARY);
    }

    protected static int convertThetaToIndex(double theta)
    {
        return (int) ((theta + Math.abs(TRI.MINIMUM_BOUNDARY)) * Math.pow(10.0, TRI.THETA_PRECISION));
    }
}
