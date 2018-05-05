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
    public static final int TRIES = 1000;

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

        TRI.I1(candidates, library);
        TRI.I2(candidates, library);
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

                writer.println(examLine.trim());
            }

            writer.close();
        }
        catch (IOException exception) {
            System.out.println("Erro na escrita do arquivo de saída I1: " + exception.getMessage());
        }
    }

    public static void I2(Candidate [] candidates, QuestionLibrary library)
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
}
