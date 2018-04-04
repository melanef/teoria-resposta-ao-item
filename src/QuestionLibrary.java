import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Double;
import java.lang.String;
import java.util.ArrayList;

public class QuestionLibrary
{
	public static final String PATTERN = "[0-9\.]+ [0-9\.]+";

	protected ArrayList<Question> library;
	
	protected QuestionLibrary()
	{
		this.library = new ArrayList<Question>();
	}
	
	public QuestionLibrary createFromFile(String filepath)
	{
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		QuestionLibrary newLibrary = new QuestionLibrary();
		
		String line;
		String [] parts;
		while (line = reader.readLine()) {
			if (line.matches(QuestionLibrary.PATTERN)) {
				parts = line.split(QuestionLibrary.PATTERN);
				
				Double a = new Double(parts[0]);
				Double b = new Double(parts[1]);
				
				newLibrary.add(new Question(a.doubleValue(), b.doubleValue()));
			}
		}
		
		return newLibrary;
	}
}
