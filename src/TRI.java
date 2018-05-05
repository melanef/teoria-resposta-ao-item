import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math;
import java.lang.String;

public class TRI {
    public static final String QUESTIONS_PATH = "../data/questoes.txt";
    public static final String I1_OUTPUT_PATH = "../outputs/I1.txt";
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

        Exam [] exams = new Exam[4];
        exams[0] = new Exam(10, library);
        exams[1] = new Exam(20, library);
        exams[2] = new Exam(50, library);
        exams[3] = new Exam(100, library);

        TRI.I1(candidates, exams);
    }

    protected static void I1(Candidate [] candidates, Exam [] exams)
    {
        try {
            PrintWriter writer = new PrintWriter(I1_OUTPUT_PATH, "UTF-8");

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
}
