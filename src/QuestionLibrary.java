import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Double;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;

public class QuestionLibrary
{
    public static final String PATTERN = "[0-9\\.]+ [0-9\\.]+";
    public static final String SEPARATOR = " ";

    protected ArrayList<Question> library;

    protected QuestionLibrary()
    {
        this.library = new ArrayList<Question>();
    }

    public static QuestionLibrary createFromFile(String filepath)
    {
        QuestionLibrary newLibrary = new QuestionLibrary();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));

            String line;
            String [] parts;
            int id = 0;
            while ((line = reader.readLine()) != null) {
                if (line.matches(QuestionLibrary.PATTERN)) {
                    parts = line.split(QuestionLibrary.SEPARATOR);

                    Double a = new Double(parts[0]);
                    Double b = new Double(parts[1]);

                    newLibrary.add(new Question(id++, a.doubleValue(), b.doubleValue()));
                }
            }
        }
        catch(FileNotFoundException exception) {
            System.out.println("Arquivo de questões não encontrado");
        }
        catch (IOException exception) {
            System.out.println("Erro na leitura do arquivo");
        }

        return newLibrary;
    }

    public Question getQuestion(int id)
    {
        Question question = (Question) this.library.get(id);
        return question.getClone();
    }

    public Question getQuestion(Integer id)
    {
        return this.getQuestion(id.intValue());
    }

    public int size()
    {
        return this.library.size();
    }

    public void add(Question question)
    {
        this.library.add(question);
    }

    public ArrayList<Question> getQuestions()
    {
        ArrayList<Question> cloneList = new ArrayList<Question>(this.library.size());
        for (int i = 0; i < this.library.size(); i++) {
            cloneList.add(this.getQuestion(i));
        }

        return cloneList;
    }
}
