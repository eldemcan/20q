import java.util.ArrayList;


public class Data {
	
	public static ArrayList<String> questions = new ArrayList<String>();
    public static ArrayList<Concept> concepts = new ArrayList<Concept>();
    public static final String TRAININGFILE = "animal_training.txt"; 
    public static final String QUESTIONFILE ="questions.txt";
    public static boolean over =false ;
    public static boolean correct = false ;
    public static int INPUTUNITS;
    public static int OUTPUTUNITS;
    public static int HIDDENUNITS;
    public static final boolean LEARN = true ;
    public static boolean continue_game=true;
}
