/**
 * The Class QuestionNode.For desicion tree structure 
 */
public class QuestionNode {

	String attribute_name = "";
	String question = "" ;
	QuestionNode parent=null;
	QuestionNode left_question;
	QuestionNode right_question;
	
	/**
	 * Instantiates a new question node.
	 *
	 * @param name the name
	 */
	public QuestionNode(String name) {
		attribute_name = name;
	}
	
	/**
	 * Adds the left.
	 *
	 * @param temp the temp
	 */
	public void addLeft(QuestionNode temp){
		left_question = temp ;
	}
	
	/**
	 * Adds the right.
	 *
	 * @param temp the temp
	 */
	public void addRight(QuestionNode temp){
		right_question = temp ;
	}

	/**
	 * Instantiates a new question node.
	 */
	public QuestionNode() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
