import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * TRI.
 * Pontro de entrada do programa com método main, métodos estáticos para cada um
 * dos itens pedidos na definição da parte 1 e o método estático chance que
 * calcula a probabilidade de um candidato acertar uma questão (dados o parâmetro
 * de habilidade do candidato e os parâmetros discriminatório e de dificuldade da
 * questão).
 */
public class TRI {
    /**
     * Constante: QUESTIONS_PATH -- indica o endereço do arquivo que contém as
     * informações das questões.
     */
    public static final String QUESTIONS_PATH = "../data/questoes.txt";

    /**
     * Constante: I1_OUTPUT_PATH -- indica o endereço do arquivo de saída que
     * deve ser gerado para o item 1.
     */
    public static final String I1_OUTPUT_PATH = "../outputs/I1.txt";

    /**
     * Constante: I2_OUTPUT_PATH -- indica o endereço do arquivo de saída que
     * deve ser gerado para o item 2.
     */
    public static final String I2_OUTPUT_PATH = "../outputs/I2.txt";

    /**
     * Constante: I3_OUTPUT_PATH -- indica o endereço do arquivo de saída que
     * deve ser gerado para o item 3.
     */
    public static final String I3_OUTPUT_PATH = "../outputs/I3.txt";

    /**
     * Constante: TRIES -- indica a quantidade de simulações devem ser executadas
     * para cada item.
     */
    public static final double TRIES = 1000000;

    /**
     * Constante: TRUST_DEGREE -- indica o grau de confiança exigido para o item 3.
     */
    public static final double TRUST_DEGREE = 0.1;

    /**
     * Método estático. Cálculo da probabilidade de um candidato acertar uma
     * questão, dados seu parâmetro de habilidade e os parâmetros de
     * discriminação e dificuldade da questão.
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
     * Método estático. Ponto de entrada do programa. Aqui instanciamos nosso
     * repositório de questões indicando o arquivo de questões a ser usado,
     * instanciamos também nossos 5 candidatos amostrais para a parte 1 e,
     * finalmente, invocamos os métodos estáticos de cada item.
     *
     * @param   String[]    args    Parâmetro padrão de argumentos da linha de
     * comando. Descartado nesse caso.
     *
     * @return  void
     */
    public static void main(String [] args)
    {
        QuestionLibrary library = QuestionLibrary.createFromFile(QUESTIONS_PATH);
        Candidate [] candidates = new Candidate[5];
        candidates[0] = new Candidate(-1.0);
        candidates[1] = new Candidate(-0.5);
        candidates[2] = new Candidate(0.0);
        candidates[3] = new Candidate(0.5);
        candidates[4] = new Candidate(1.0);

        TRI.I1(candidates, library);
        TRI.I2(candidates, library);
        TRI.I3(candidates, library);
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
    protected static void I1(Candidate [] candidates, QuestionLibrary library)
    {
        try {
            PrintWriter writer = new PrintWriter(I1_OUTPUT_PATH, "UTF-8");

            Exam [] exams = new Exam[4];
            exams[0] = new Exam(10, library);
            exams[1] = new Exam(20, library);
            exams[2] = new Exam(50, library);
            exams[3] = new Exam(100, library);

            for (int i = 0; i < 4; i++) {
                String examLine = "";
                for (int j = 0; j < 4; j++) {
                    double timesBestIsBetter = 0;
                    for (double k = 0; k < TRIES; k++) {
                        double bestCandidateScore = exams[i].tryCandidate(candidates[4]);
                        double currentCandidateScore = exams[i].tryCandidate(candidates[j]);

                        if (bestCandidateScore > currentCandidateScore) {
                            timesBestIsBetter++;
                        }
                    }

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
    protected static void I2(Candidate [] candidates, QuestionLibrary library)
    {
        try {
            PrintWriter writer = new PrintWriter(I2_OUTPUT_PATH, "UTF-8");

            ArrayList<Question> questions = library.getQuestions();
            Collections.sort(questions, new QuestionComparator(candidates[4], candidates[3]));

            Exam [] exams = new Exam[3];
            exams[0] = new Exam(10);
            exams[1] = new Exam(20);
            exams[2] = new Exam(50);

            for (int i = 0; i < 3; i++) {
                String examQuestionsLine = "";
                String examLine = "";

                /**
                 * Cria as provas com as questões que mais favorecem o aluno 5 em detrimento do aluno 4
                 */
                for (int j = 0; j < exams[i].size(); j++) {
                    Question currentQuestion = questions.get(j);
                    examQuestionsLine = examQuestionsLine + currentQuestion.getId() + " ";
                    exams[i].addQuestion(currentQuestion);
                }

                /**
                 * Aplica as provas recém criadas comparando a chance do aluno 5 ser melhor do que cada um dos outros 4 alunos
                 */
                for (int j = 0; j < 4; j++) {
                    double timesBestIsBetter = 0;
                    for (int k = 0; k < TRIES; k++) {
                        double bestCandidateScore = exams[i].tryCandidate(candidates[4]);
                        double currentCandidateScore = exams[i].tryCandidate(candidates[j]);

                        if (bestCandidateScore > currentCandidateScore) {
                            timesBestIsBetter++;
                        }
                    }

                    double probability = timesBestIsBetter / TRIES;
                    examLine = examLine + probability + " ";
                }

                writer.println(examQuestionsLine.trim());
                writer.println(examLine.trim());
            }

            writer.close();
        }
        catch (IOException exception) {
            System.out.println("Erro na escrita do arquivo de saída I1: " + exception.getMessage());
        }
    }

    /**
     * Item 3.
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
    protected static void I3(Candidate [] candidates, QuestionLibrary library)
    {
        try {
            PrintWriter writer = new PrintWriter(I3_OUTPUT_PATH, "UTF-8");

            Exam [] exams = new Exam[4];
            exams[0] = new Exam(10, library);
            exams[1] = new Exam(20, library);
            exams[2] = new Exam(50, library);
            exams[3] = new Exam(100, library);

            for (int i = 0; i < exams.length; i++) {
                String examLine = "";

                for (int j = 0; j < candidates.length; j++) {
                    int [] scoreFrequency = new int[exams[i].size() + 1];

                    for (int k = 0; k < scoreFrequency.length; k++) {
                        scoreFrequency[k] = 0;
                    }

                    for (int k = 0; k < TRIES; k++) {
                        double relativeScore = exams[i].tryCandidate(candidates[j]);
                        int score = (int) Math.floor(relativeScore * exams[i].size());
                        scoreFrequency[score]++;
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
            System.out.println("Erro na escrita do arquivo de saída I3: " + exception.getMessage());
        }
    }
}
