import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Double;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;

/**
 * Repositório de questões.
 * O repositório é construido através de um arquivo que contém os dados de todas
 * as questões.
 *
 * A classe espera que o arquivo seja formatado da seguinte maneira:
 * Cada linha representa uma questão e contém dois dados numéricos decimais (
 * separados por ponto) que correspondem aos parâmetros de dificuldade da questão.
 *
 * Cada linha lida do arquivo é interpretada e os dados extraídos são usados para
 * criar nova instância da classe Question (que representa a Questão). A nova
 * instância é depositada na lista de questões (o repositório propriamente dito
 * --implementado através de uma ArrayList).
 */
public class QuestionLibrary
{
    /**
     * Propriedade protegida: ArrayList<Question> library -- lista de questões.
     */
    protected ArrayList<Question> library;

    /**
     * Construtor protegido. A operação de construção do objeto da classe deve
     * ocorrer através do método createFromFile (que implementa uma factory).
     * Cria uma instância de ArrayList para Question.
     *
     * @return  QuestionLibrary
     */
    protected QuestionLibrary()
    {
        this.library = new ArrayList<Question>();
    }

    /**
     * Método estático. Factory usada para criação de instâncias da classe.
     * Recebe um caminho de arquivo como parâmetro, abre o arquivo para leitura,
     * lê linha-a-linha, instancia objetos da classe Question e os armazena na
     * library.
     *
     * @param   String  filepath    Caminho do arquivo a ser usado na construção
     *
     * @return  QuestionLibrary
     */
    public static QuestionLibrary createFromFile(String filepath)
    {
        QuestionLibrary newLibrary = new QuestionLibrary();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));

            String line;
            String [] parts;
            int id = 0;
            while ((line = reader.readLine()) != null) {
                if (line.matches(TRI.PATTERN)) {
                    parts = line.split(TRI.SEPARATOR);

                    Double a = Double.valueOf(parts[0]);
                    Double b = Double.valueOf(parts[1]);

                    newLibrary.add(new Question(id++, a.doubleValue(), b.doubleValue()));
                }
            }
        }
        catch(FileNotFoundException exception) {
            System.out.println("Arquivo de questões não encontrado");
        }
        catch (IOException exception) {
            System.out.println("Erro na leitura do arquivo");
        }

        return newLibrary;
    }

    /**
     * Método getter para uma questão da lista indicada pelo seu ID.
     *
     * @param   int     id  ID numérico (índice) da questão a ser obtida
     *
     * @return  Question
     */
    public Question getQuestion(int id)
    {
        Question question = (Question) this.library.get(id);
        return question.getClone();
    }

    /**
     * Sobrecarga do método getQuestion usando a classe Integer como parâmetro
     * ao invés do primitivo correspondente.
     *
     * @param   Integer     id  ID numérico (índice) da questão a ser obtida
     *
     * @return  Question
     */
    public Question getQuestion(Integer id)
    {
        return this.getQuestion(id.intValue());
    }

    /**
     * Proxy para o método size da library. Retorna o tamanho do repositório (a
     * quantidade de questões disponíveis).
     *
     * @return  int
     */
    public int size()
    {
        return this.library.size();
    }

    /**
     * Proxy para o método add da library. Adiciona o objeto passado como
     * parâmetro à library.
     *
     * @param   Question    question    Questão a ser inclusa na lista
     *
     * @return  void
     */
    public void add(Question question)
    {
        this.library.add(question);
    }

    /**
     * Getter seguro (usando clonagem) para a lista de questões.
     *
     * @return  ArrayList<Question>
     */
    public ArrayList<Question> getQuestions()
    {
        ArrayList<Question> cloneList = new ArrayList<Question>(this.library.size());
        for (int i = 0; i < this.library.size(); i++) {
            cloneList.add(this.getQuestion(i));
        }

        return cloneList;
    }
}
