import java.lang.Math;
import java.lang.String;

public class TRI {
    public static final String QUESTIONS_PATH = "../data/questoes.txt";

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
	}
}
