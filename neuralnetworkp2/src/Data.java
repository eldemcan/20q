import java.util.ArrayList;


/**
 * The Class Data.Where settings and global variables hold .
 */
public class Data {
	public static String [] questions = {"Is it","Lives","Does it","Have"};
	public static ArrayList<String> complete_questions = new ArrayList<String>();
    public static int SIZE =questions.length;
    public static ArrayList<Concept> concepts = new ArrayList<Concept>();
    public static final String TRAININGFILE = "animal_training.txt";
    public static final String ATTRIBUTEFILE = "attributes.txt";
    public static boolean over;
    public static boolean correct;
    public static int INPUTUNITS;
    public static int OUTPUTUNITS;
    public static int HIDDENUNITS;
    public static boolean LEARN =true ;
    public static boolean RETRAIN;
    public static boolean UPDATE;
    public static double THRESHOlD=0.5;
}
