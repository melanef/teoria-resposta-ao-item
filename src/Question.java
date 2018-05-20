import java.lang.Cloneable;

/**
 * Questão.
 * A questão é representada por dois valores numéricos que indicam o parâmetro de
 * discriminação e o parâmetro de dificuldade da questão.
 */
public class Question implements Cloneable
{
    /**
     * Propriedade protegida: int id -- identificador numérico (índice) da questão
     */
    protected int id;

    /**
     * Propriedade protegida: double a -- parâmetro de discriminação da questão
     */
    protected double a;

    /**
     * Propriedade protegida: double b -- parâmetro de dificuldade da questão
     */
    protected double b;

    /**
     * Construtor. A operação de construção consiste em atribuir os parâmetros
     * apenas.
     *
     * @param   int     id  Identificador numérico
     * @param   double  a   Parâmetro de discriminação
     * @param   double  b   Parâmetro de dificuldade
     *
     * @return  Question
     */
    public Question(int id, double a, double b)
    {
        this.id = id;
        this.a = a;
        this.b = b;
    }

    /**
     * Método getter para o identificador numérico.
     *
     * @return  int
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Método getter para o parâmetro de discriminação.
     *
     * @return  double
     */
    public double getA()
    {
        return this.a;
    }

    /**
     * Método getter para o parâmetro de dificuldade.
     *
     * @return  double
     */
    public double getB()
    {
        return this.b;
    }

    /**
     * Método público auxiliar usado para calcular a diferença na probabilidade
     * de acerto de dois candidatos dados como parâmetro.
     *
     * Este método é utilizado na comparação de questões para identificar
     * questões que favoreçam um candidato específico em detrimento de outro.
     *
     * Este cálculo é dado comparando a chance de acerto de cada candidato de
     * acordo com a fórmula implementada no método estático chance da classe TRI.
     *
     * @param   Candidate   candidate1  Candidato a ser considerado favorito
     * @param   Candidate   candidate2  Candidato a ser comparado
     *
     * @return  double
     */
    public double getCandidatesChanceDifference(Candidate candidate1, Candidate candidate2)
    {
        double chanceCandidate1 = TRI.chance(candidate1.getTheta(), this.getA(), this.getB());
        double chanceCandidate2 = TRI.chance(candidate2.getTheta(), this.getA(), this.getB());

        return chanceCandidate1 - chanceCandidate2;
    }

    /**
     * Método de clonagem. Permite a cópia segura de questões para preservar o
     * objeto original e protegê-lo contra manipulações indesejadas.
     *
     * @return  Question
     */
    public Question getClone()
    {
        try {
            return (Question) this.clone();
        }
        catch (CloneNotSupportedException exception) {
            System.out.println("Clonagem não suportada na classe Questão");
            return null;
        }
    }
}
