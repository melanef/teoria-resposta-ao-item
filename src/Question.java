import java.lang.Cloneable;

public class Question implements Cloneable
{
	protected double a;
	protected double b;
	
	public Question(double a, double b)
	{
		this.a = a;
		this.b = b;
	}
	
	public double getA()
	{
		return this.a;
	}
	
	public double getB()
	{
		return this.b;
	}
	
    public double getCandidatesChance(Candidate candidate1, Candidate candidate2)
    {
        double chanceCandidate1 = TRI.chance(candidate1.getTheta(), this.getA(), this.getB());
        double chanceCandidate2 = TRI.chance(candidate2.getTheta(), this.getA(), this.getB());
        
        return chanceCandidate1 - chanceCandidate2;
    }
}
