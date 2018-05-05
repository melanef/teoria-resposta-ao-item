import java.lang.Integer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Exam
{
    protected ArrayList<Question> questions;
    protected ArrayList<Integer> question_ids;
    protected int questions_quantity;

    public Exam(int questions_quantity)
    {
        this.questions_quantity = questions_quantity;
        this.questions = new ArrayList<Question>(this.questions_quantity);
        this.question_ids = new ArrayList<Integer>(this.questions_quantity);
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
                this.question_ids.add(new Integer(i));
            }
        }

        while (this.questions.size() < this.questions_quantity) {
            Integer current_question_id = new Integer(
                ThreadLocalRandom.current().nextInt(0, library.size())
            );

            if (!this.question_ids.contains(current_question_id)) {
                this.question_ids.add(current_question_id);
                this.questions.add(library.getQuestion(current_question_id));
            }
        }
    }

    public double tryCandidate(Candidate candidate)
    {
        int score = 0;

        for (int i = 0; i < this.questions_quantity; i++) {
            Question current = this.questions.get(i);
            double chance = TRI.chance(candidate.getTheta(), current.getA(), current.getB());
            double event = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            if (event <= chance) {
                score++;
            }
        }

        return score / this.questions_quantity;
    }
}
