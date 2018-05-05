import java.util.Comparator;

public class QuestionComparator implements Comparator<Question>
{
    protected Candidate best;
    protected Candidate worst;

    public QuestionComparator(Candidate best, Candidate worst)
    {
        this.best = best;
        this.worst = worst;
    }

    public int compare(Question q1, Question q2)
    {
        double q1Difference = q1.getCandidatesChanceDifference(this.best, this.worst);
        double q2Difference = q2.getCandidatesChanceDifference(this.best, this.worst);

        /*
        System.out.println("Q1: " + q1.getId() + " - Chance Difference: " + q1Difference);
        System.out.println("Q2: " + q2.getId() + " - Chance Difference: " + q2Difference);
        */

        return q1Difference < q2Difference ? 1 : -1;
    }
}
