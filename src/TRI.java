import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TRI {
    public static final String QUESTIONS_PATH = "../data/questoes.txt";
    public static final String I1_OUTPUT_PATH = "../outputs/I1.txt";
    public static final String I2_OUTPUT_PATH = "../outputs/I2.txt";
    public static final String I31_OUTPUT_PATH = "../outputs/I3-1.txt";
    public static final String I32_OUTPUT_PATH = "../outputs/I3-2.txt";
    public static final double TRIES = 100000;
    //public static final double TRIES = 1000000;
    public static final double TRUST_DEGREE = 0.1; 

    public static double chance(double theta, double a, double b)
    {
        return (Math.pow(Math.E, a * (theta - b)) / (1.0 + Math.pow(Math.E, (a * (theta - b)))));
    }

    public static void main(String [] args)
    {
        QuestionLibrary library = QuestionLibrary.createFromFile(QUESTIONS_PATH);
        Candidate [] candidates = new Candidate[5];
        candidates[0] = new Candidate(-1.0);
        candidates[1] = new Candidate(-0.5);
        candidates[2] = new Candidate(0.0);
        candidates[3] = new Candidate(0.5);
        candidates[4] = new Candidate(1.0);

        //TRI.I1(candidates, library);
        //TRI.I2(candidates, library);
        TRI.I31(candidates, library);
        TRI.I32(candidates, library);
    }

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

    protected static void I2(Candidate [] candidates, QuestionLibrary library)
    {
        try {
            PrintWriter writer = new PrintWriter(I2_OUTPUT_PATH, "UTF-8");

            ArrayList<Question> questions = library.getQuestions();
            Collections.sort(questions, new QuestionComparator(candidates[4], candidates[3]));

            /*
            for (int i = 0; i < questions.size(); i++) {
                System.out.println("ID: " + questions.get(i).getId() + " - Chance Difference: " + questions.get(i).getCandidatesChanceDifference(candidates[4], candidates[3]));
            }
            */

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
    
    protected static void I31(Candidate [] candidates, QuestionLibrary library)
    {
        try {
            PrintWriter writer = new PrintWriter(I31_OUTPUT_PATH, "UTF-8");

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
                    
                    int mostFrequent = 0;
                    for (int k = 0; k < scoreFrequency.length; k++) {
                        //System.out.println(k + ": " + scoreFrequency[k]);
                        
                        if (scoreFrequency[k] > scoreFrequency[mostFrequent]) {
                            mostFrequent = k;
                        }
                    }
                    
                    //System.out.println("mostFrequent: " + mostFrequent);
                    
                    double expectedVolume = (1.0 - TRUST_DEGREE) * TRIES;
                    double currentVolume = scoreFrequency[mostFrequent];
                    
                    //System.out.println("currentVolume: " + currentVolume + " - expectedVolume: " + expectedVolume);
                    
                    int leftEdge = mostFrequent;
                    int leftLimit = 0;
                    int rightEdge = mostFrequent;
                    int rightLimit = scoreFrequency.length - 1;
                    while (currentVolume < expectedVolume) {
                        int leftVolume = 0;
                        for (int k = leftLimit; k < leftEdge; k++) {
                            leftVolume += scoreFrequency[k];
                        }
                        
                        int rightVolume = 0;
                        for (int k = rightEdge + 1; k < rightLimit; k++) {
                            rightVolume += scoreFrequency[k];
                        }
                        
                        if (leftVolume > rightVolume) {
                            leftEdge--;
                            currentVolume += scoreFrequency[leftEdge];
                        } else {
                            rightEdge++;
                            currentVolume += scoreFrequency[rightEdge]; 
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
    
    protected static void I32(Candidate [] candidates, QuestionLibrary library)
    {
        try {
            PrintWriter writer = new PrintWriter(I32_OUTPUT_PATH, "UTF-8");

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
