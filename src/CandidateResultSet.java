import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Boolean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CandidateResultSet
{
    /**
     * Constante: SEPARATOR (separador) -- caractere usado para separar os valores
     * nas linhas do arquivo.
     */
    public static final String SEPARATOR = " ";
    
    public static final String HIT_INDICATOR = "1";
    
    public static final double MINIMUM_BOUNDARY = -10.0;
    
    public static final double MAXIMUM_BOUNDARY = 10.0;
    
    public static final double DERIVATIVE_FACTOR = 0.00001;
    
    public static final double LEVEL_MARGIN = 0.00001;

    /**
     * Propriedade protegida: ArrayList<Boolean> results -- lista de resultados
     */
    protected List<Boolean> results;
    
    protected CandidateResultSet()
    {
        this.results = new ArrayList<Boolean>();
    }
    
    protected CandidateResultSet(int size)
    {
        this.results = new ArrayList<Boolean>(size);
    }
    
    protected CandidateResultSet(QuestionLibrary library)
    {
        
    }
    
    public void add(int index, Boolean result)
    {
        this.results.add(index, result);
    }
    
    public static List<CandidateResultSet> createFromFile(String filepath)
    {
        List<CandidateResultSet> resultSets = null;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            
            String line;
            String [] parts;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                parts = line.split(QuestionLibrary.SEPARATOR);
                
                if (resultSets == null) {
                    resultSets = new ArrayList<CandidateResultSet>(parts.length);
                    
                    for (int j = 0; j < parts.length; j++) {
                        resultSets.add(j, new CandidateResultSet());
                    }
                }
                
                for (int j = 0; j < parts.length; j++) {
                    CandidateResultSet current = (CandidateResultSet) resultSets.get(j);
                    current.add(i, new Boolean(parts[j].equals(CandidateResultSet.HIT_INDICATOR)));
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
    
    public double estimate(QuestionLibrary library) throws Exception
    {
        double minimumBoundary = CandidateResultSet.MINIMUM_BOUNDARY;
        double maximumBoundary = CandidateResultSet.MAXIMUM_BOUNDARY;
        double midpoint;
        
        double minimumBoundaryDerivative;
        double maximumBoundaryDerivative;
        double midpointDerivative;
        
        do {
            midpoint = (minimumBoundary + maximumBoundary) / 2;
            
            minimumBoundaryDerivative = this.logLikelihoodDerivative(library, minimumBoundary);
            maximumBoundaryDerivative = this.logLikelihoodDerivative(library, maximumBoundary);
            midpointDerivative = this.logLikelihoodDerivative(library, midpoint);
            
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
                throw new Exception("Erro: não há raiz entre os limites");
            }
        } while (Math.abs(midpointDerivative) > CandidateResultSet.LEVEL_MARGIN);
        
        return midpoint;
    }
    
    protected double logLikelihoodDerivative(QuestionLibrary library, double guessedTheta)
    {
        return 
            (
                logLikelihood(
                    library,
                    (guessedTheta + CandidateResultSet.DERIVATIVE_FACTOR)
                )
                - logLikelihood(library, guessedTheta)
            )
            / CandidateResultSet.DERIVATIVE_FACTOR;
    }
    
    protected double logLikelihood(QuestionLibrary library, double guessedTheta)
    {
        return Math.log(this.likelihood(library, guessedTheta));
    }
    
    protected double likelihood(QuestionLibrary library, double guessedTheta)
    {
        double likelihood = 1;
        int id = 0;
        Iterator<Boolean> resultsIterator = this.results.iterator();
        while (resultsIterator.hasNext()) {
            Boolean current = (Boolean) resultsIterator.next();
            double questionChance = TRI.chance(guessedTheta, library.getQuestion(id++));
            if (!current.booleanValue()) {
                questionChance = 1 - questionChance;
            }
            
            likelihood = likelihood * questionChance;
        }
        
        return likelihood;
    }
}