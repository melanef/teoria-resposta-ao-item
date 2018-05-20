import java.util.Comparator;

/**
 * Comparador de Questões.
 * Para definirmos questões que favoreçam um determinado candidato em detrimento
 * de outro (item 2), consideramos que é possível comparar questões de acordo
 * com a chance de acerto de cada candidato para cada questão.
 *
 * Questões com maior diferença nas chances de acerto de um candidato em relação
 * ao outro podem ser mais eficientes para favorecer o candidato favorito.
 *
 * O método implementa a interface Comparator para que possa ser usado como
 * parâmetro para a invocação do método estático sort da classe Collections.
 */
public class QuestionComparator implements Comparator<Question>
{
    /**
     * Propriedade protegida: Candidate best -- candidato considerado favorito
     */
    protected Candidate best;

    /**
     * Propriedade protegida: Candidate worst -- candidato usado na comparação
     */
    protected Candidate worst;

    /**
     * Construtor. A operação de construção consiste apenas em definir as
     * propriedades com os parâmetros.
     *
     * @param   Candidate   best    Candidato considerado favortido
     * @param   Candidate   worst   Candidato usado na comparação
     *
     * @return  QuestionComparator
     */
    public QuestionComparator(Candidate best, Candidate worst)
    {
        this.best = best;
        this.worst = worst;
    }

    /**
     * Método compare. Implementação necessária para atender ao requisito da
     * interface Comparator.
     *
     * Dadas duas questões, a ordenação é feita colocando a questão com maior
     * diferença na chance dos candidatos favorito e comparado na frente. Para
     * isso, obtemos a diferença de chance de cada questão e comparamos. A maior
     * gera retorno -1 (que indica para o ordenador que este objeto deve vir
     * antes); caso contrário, gera retorno 1 (que indica para o ordenador que
     * este objeto deve vir depois).
     *
     * @param   Question    q1  Questão a ser comparada
     * @param   Question    q2  Questão a ser comparada
     *
     * @return  int
     */
    public int compare(Question q1, Question q2)
    {
        double q1Difference = q1.getCandidatesChanceDifference(this.best, this.worst);
        double q2Difference = q2.getCandidatesChanceDifference(this.best, this.worst);

        return q1Difference < q2Difference ? 1 : -1;
    }
}
