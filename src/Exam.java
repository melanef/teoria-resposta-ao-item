import java.lang.Integer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Exam
{
    protected ArrayList<Question> questions;
    protected int questions_quantity;

    public Exam(int questions_quantity)
    {
        this.questions_quantity = questions_quantity;
        this.questions = new ArrayList<Question>(this.questions_quantity);
    }

    public Exam(int questions_quantity, QuestionLibrary library) {
        this(questions_quantity);
        this.fillQuestions(library);
    }

    public void fillQuestions(QuestionLibrary library)
    {
        if (this.questions.size() == this.questions_quantity) {
            for (int i = 0; i < this.questions_quantity; i++) {
                this.questions.add(library.getQuestion(new Integer(i)));
            }
        }

        ArrayList<Integer> question_ids = new ArrayList<Integer>(this.questions_quantity);

        while (this.questions.size() < this.questions_quantity) {
            Integer current_question_id = new Integer(
                ThreadLocalRandom.current().nextInt(0, library.size())
            );

            if (!question_ids.contains(current_question_id)) {
                question_ids.add(current_question_id);
                this.questions.add(library.getQuestion(current_question_id));
            }
        }
    }

    public void addQuestion(Question question)
    {
        this.questions.add(question.getClone());
    }

    public int size()
    {
        return this.questions_quantity;
    }

    public double tryCandidate(Candidate candidate)
    {
        double score = 0;

        for (int i = 0; i < this.questions_quantity; i++) {
            Question current = this.questions.get(i);
            double chance = TRI.chance(candidate.getTheta(), current.getA(), current.getB());
            //System.out.println("Chance: " + chance);
            double event = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            //System.out.println("Event: " + event);
            if (event <= chance) {
                score++;
            }
        }

        //System.out.println("Score: " + score);

        return score / this.questions_quantity;
    }
}
