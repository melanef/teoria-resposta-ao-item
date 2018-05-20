/**
 * Candidato.
 * O Candidato é representado por um valor numérico que indica o parâmetro de
 * habilidade dele.
 */
public class Candidate
{
    /**
     * Propriedade protegida: double theta -- parâmetro de habilidade do candidato
     */
    protected double theta;

    /**
     * Construtor. A operação de construção consiste em atribuir o parâmetro
     * apenas.
     *
     * @param   double  theta   Parâmetro de habilidade
     *
     * @return  Candidate
     */
    public Candidate(double theta)
    {
        this.theta = theta;
    }

    /**
     * Método getter para o parâmetro de habilidade.
     *
     * @return  double
     */
    public double getTheta()
    {
        return this.theta;
    }
}
