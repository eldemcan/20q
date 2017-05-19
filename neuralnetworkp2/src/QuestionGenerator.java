import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * The Class QuestionGenerator.Generates questions according to attributes in text file 
 */
public class QuestionGenerator {
	
	BufferedReader reader = null ;
	QuestionNode node ;
	String line;
	
	/**
	 * Instantiates a new question generator.
	 */
	public QuestionGenerator( ) {
		try {
			reader = new BufferedReader(new FileReader(Data.ATTRIBUTEFILE));
			while ((line = reader.readLine()) != null){
				String question=GenerateQuestions(line);
				Data.complete_questions.add(question);								
			}//while
		} catch (FileNotFoundException e) {
			System.out.println("can t open file");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("can t read file");
			e.printStackTrace();
		}
			try {
				reader.close();
			} catch (IOException e) {
				System.out.println("could not close file");
				e.printStackTrace();
			}
	}//end 

	/**
	 * Generate questions.
	 *
	 * @param line the line
	 * @return the string
	 */
	public String GenerateQuestions(String line){
		String question = null;
		String [] input = line.split(" ");
		int question_index = Integer.parseInt(input[1]);
		question = Data.questions[question_index]+" "+input[0]+" ?";
		return question ;
	}//generate questions 
	
}
