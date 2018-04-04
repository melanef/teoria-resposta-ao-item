import java.lang.Math;
import java.lang.String;

public class TRI {
	public static final String QUESTIONS_PATH = "../data/questoes.txt";

	static double chance(double theta, double ai, double bi)
	{
		return Math.pow(Math.E, ai * (theta - bi)) / 1.0 + Math.pow(Math.E, ai * (theta - bi));
	}
	
	public static void main(String [] args)
	{
		QuestionLibrary library = QuestionLibrary.createFromFile(QUESTIONS_PATH);
		Exams [] exams = Exam[4];
		exam[0] = new Exam(10, library);
		exam[0] = new Exam(20, library);
		exam[0] = new Exam(50, library);
		exam[0] = new Exam(100, library);
	}
}
