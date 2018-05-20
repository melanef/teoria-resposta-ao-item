import java.lang.Integer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

/**
 * Prova.
 * Uma prova consiste numa coleção de questões (representada por uma ArrayList).
 * A prova pode ser construida manualmente (escolhendo questão a questão) ou
 * automaticamente (o construtor escolhe questões do repositório dado como
 * parâmetro aleatoriamente).
 *
 * A prova é usada também para avaliar um aluno/candidato (ou seja, submeter um
 * aluno a cada uma de suas questões e retornar a avaliação deste aluno).
 */
public class Exam
{
    /**
     * Propriedade protegida: ArrayList<Question> questions -- lista de questões.
     */
    protected ArrayList<Question> questions;

    /**
     * Propriedade protegida: int questions_quantity -- quantidade de questões
     * esperada para essa prova.
     */
    protected int questions_quantity;

    /**
     * Construtor. A operação de construção consiste em definir a quantidade de
     * questões que essa prova suporta e instanciar o objeto de ArrayList para a
     * lista de questões com o tamanho definido.
     *
     * @param   int     questions_quantity  Quantidade de questões prevista para
     * a prova
     *
     * @return  Exam
     */
    public Exam(int questions_quantity)
    {
        this.questions_quantity = questions_quantity;
        this.questions = new ArrayList<Question>(this.questions_quantity);
    }

    /**
     * Construtor sobrecarregado. Usando este construtor, invocamos também o
     * método fillQuestions, que preenche a prova com questões escolhidas
     * aleatoriamente do repositório de questões fornecido como parâmetro.
     *
     * @param   int             questions_quantity  Quantidade de questões
     * prevista para a prova
     * @param   QuestionLibrary library             Repositório de questões
     * usado para o preenchimento automático
     *
     * @return  Exam
     */
    public Exam(int questions_quantity, QuestionLibrary library) {
        this(questions_quantity);
        this.fillQuestions(library);
    }

    /**
     * Método público para popular uma prova com as questões de um repositório
     * aleatóriamente.
     * No caso especial de a prova ser composta pela mesma quantidade de questões
     * que o repositório, apenas copiamos todas as questões para a prova.
     *
     * @param   QuestionLibrary library             Repositório de questões
     * usado para o preenchimento automático
     *
     * @return  void
     */
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

    /**
     * Método público para incluir uma questão na prova.
     * Usado para popular uma prova manualmente.
     *
     * @param   Question    question    Questão a ser inclusa na prova
     *
     * @return  void
     */
    public void addQuestion(Question question)
    {
        if (this.questions.size() < this.questions_quantity) {
            this.questions.add(question.getClone());
        }
    }

    /**
     * Método getter para a quantidade de questões prevista para a prova.
     *
     * @return  int
     */
    public int size()
    {
        return this.questions_quantity;
    }

    /**
     * Método público para avaliar um dado candidato.
     * Aplica todas as questões da prova para o candidato. Cada questão fornece,
     * para aquele candidato, uma chance de acerto (através do cálculo de
     * probabilidade da classe TRI). Esta chance é comparada com um número
     * aleatório entre 0 e 1. Se a chance for maior do que o número aleatório
     * obtido, consideramos que o candidato acertou a questão, e portanto,
     * incrementamos sua pontuação. O resultado da avaliação é a relação entre a
     * pontuação e a quantidade de questões da prova.
     *
     * @param   Candidate   candidate   Candidato a ser avaliado
     *
     * @return  double
     */
    public double tryCandidate(Candidate candidate)
    {
        double score = 0;

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
