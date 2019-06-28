import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Boolean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResultSet
{
    /**
     * Propriedade protegida: ArrayList<Boolean> results -- lista de resultados
     */
    protected List<Boolean> results;

    protected double estimatedTheta;
    protected boolean isThetaEstimated;
    
    protected double score;
    protected boolean isScoreCalculated;
    
    protected ResultSet()
    {
        this.isThetaEstimated = false;
        this.isScoreCalculated = false;
        this.results = new ArrayList<Boolean>();
    }
    
    protected ResultSet(int size)
    {
        this.isThetaEstimated = false;
        this.isScoreCalculated = false;
        this.results = new ArrayList<Boolean>(size);
    }
    
    protected ResultSet(Exam exam)
    {
        this.isThetaEstimated = false;
        this.isScoreCalculated = false;
    }
    
    public void add(int index, Boolean result)
    {
        this.results.add(index, result);
    }
    
    public static List<ResultSet> createFromFile(String filepath)
    {
        List<ResultSet> resultSets = null;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            
            String line;
            String [] parts;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                parts = line.split(TRI.SEPARATOR);
                
                if (resultSets == null) {
                    resultSets = new ArrayList<ResultSet>(parts.length);
                    
                    for (int j = 0; j < parts.length; j++) {
                        resultSets.add(j, new ResultSet());
                    }
                }
                
                for (int j = 0; j < parts.length; j++) {
                    ResultSet current = (ResultSet) resultSets.get(j);
                    current.add(i, Boolean.valueOf(parts[j].equals(TRI.HIT_INDICATOR)));
                }
                
                i++;
            }
        }
        catch(FileNotFoundException exception) {
            System.out.println("Arquivo de respostas não encontrado");
        }
        catch (IOException exception) {
            System.out.println("Erro na leitura do arquivo");
        }
        
        return resultSets;
    }
    
    public double getScore()
    {
        if (this.isScoreCalculated) {
            return this.score;
        }

        double score = 0;
        Iterator<Boolean> resultsIterator = this.results.iterator();
        while (resultsIterator.hasNext()) {
            Boolean current = (Boolean) resultsIterator.next();
            if (current.booleanValue()) {
                score++;
            }
        }

        this.isScoreCalculated = true;
        this.score = score;

        return score;
    }
    
    public double estimate(Exam exam)
    {
        if (this.isThetaEstimated) {
            return this.estimatedTheta;
        }

        double minimumBoundary = TRI.MINIMUM_BOUNDARY;
        double maximumBoundary = TRI.MAXIMUM_BOUNDARY;
        double midpoint;
        
        double minimumBoundaryDerivative;
        double maximumBoundaryDerivative;
        double midpointDerivative;
        
        do {
            midpoint = (minimumBoundary + maximumBoundary) / 2;
            
            minimumBoundaryDerivative = this.logLikelihoodDerivative(exam, minimumBoundary);
            maximumBoundaryDerivative = this.logLikelihoodDerivative(exam, maximumBoundary);
            midpointDerivative = this.logLikelihoodDerivative(exam, midpoint);
            
            if (
                (minimumBoundaryDerivative > 0 && midpointDerivative < 0)
                || (minimumBoundaryDerivative < 0 && midpointDerivative > 0)
            ) {
                maximumBoundary = midpoint;
            } else if (
                (maximumBoundaryDerivative > 0 && midpointDerivative < 0)
                || (maximumBoundaryDerivative < 0 && midpointDerivative > 0)
            ) {
                minimumBoundary = midpoint;
            } else {
                minimumBoundaryDerivative = Math.abs(minimumBoundaryDerivative);
                maximumBoundaryDerivative = Math.abs(maximumBoundaryDerivative);
                
                if (minimumBoundaryDerivative < maximumBoundaryDerivative) {
                    return minimumBoundary;
                } else {
                    return maximumBoundary;
                }
            }
        } while (Math.abs(midpointDerivative) > TRI.LEVEL_MARGIN);
        
        this.isThetaEstimated = true;
        this.estimatedTheta = midpoint;

        return midpoint;
    }
    
    protected double logLikelihoodDerivative(Exam exam, double guessedTheta)
    {
        return 
            (
                logLikelihood(
                    exam,
                    (guessedTheta + TRI.DERIVATIVE_FACTOR)
                )
                - logLikelihood(exam, guessedTheta)
            )
            / TRI.DERIVATIVE_FACTOR;
    }
    
    protected double logLikelihood(Exam exam, double guessedTheta)
    {
        return Math.log(this.likelihood(exam, guessedTheta));
    }
    
    protected double likelihood(Exam exam, double guessedTheta)
    {
        double likelihood = 1;
        int i = 0;
        Iterator<Boolean> resultsIterator = this.results.iterator();
        while (resultsIterator.hasNext()) {
            Boolean current = (Boolean) resultsIterator.next();
            Question currentQuestion = exam.getQuestion(i++);
            
            double questionChance = TRI.chance(guessedTheta, currentQuestion);
            
            if (!current.booleanValue()) {
                questionChance = 1 - questionChance;
            }
        
            likelihood = likelihood * questionChance;
        }
        
        return likelihood;
    }
}