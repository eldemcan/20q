import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Class QuestionTree.Is a decision tree to ask more specific questions about specific animal .
 */
public class QuestionTree {

	ArrayList<QuestionNode> questions = new ArrayList<QuestionNode>() ;
	BufferedReader reader = null ;
	QuestionNode node = new QuestionNode();
	QuestionNode left_node  ;
	QuestionNode right_node ;
	String line;

	/**
	 * Instantiates a new question tree.
	 */
	public QuestionTree() {
		try {
			//initialize  tree from text file 
			reader = new BufferedReader(new FileReader(Data.ATTRIBUTEFILE));

			while ((line = reader.readLine()) != null){
				String question=QuestionGenerator(line);
				String[] input = line.split(" ");
				node.attribute_name = input[0] ;
				node.question=question;
				if(node.left_question==null){
					String t_line=reader.readLine();
					if(t_line!=null){
						String t_q = QuestionGenerator(t_line);
						String [] t_input = t_q.split(" ");
						left_node = new QuestionNode(t_q);
						left_node.attribute_name = t_input[0];
						left_node.question = t_q ;
						left_node.parent =  node ;
						node.addLeft(left_node);
					}
				} // if

				else if(node.right_question ==null){
					String t_line=reader.readLine();
					if(t_line!=null){
						String t_q = QuestionGenerator(t_line);
						String [] t_input = t_q.split(" ");
						right_node = new QuestionNode(t_q);
						right_node.attribute_name = t_input[0];
						right_node.question = t_q ;
						right_node.parent =  node ;
						node.addRight(right_node) ;
					}
				} // else if
				questions.add(node);
				node = left_node;
			}//while
		} catch (FileNotFoundException e) {
			System.out.println("can t open file");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("can t read file");
			e.printStackTrace();
		}
	}

	/**
	 * Question generator.
	 *
	 * @param line the line
	 * @return the string
	 */
	public String QuestionGenerator(String line){
		String question = null;
		String [] input = line.split(" ");
		int question_index = Integer.parseInt(input[1]);
		question = Data.questions[question_index]+" "+input[0]+" ?";
		return question ;
	}

}
