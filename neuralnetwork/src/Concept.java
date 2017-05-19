import java.util.ArrayList;
import java.util.Arrays;


public class Concept {
	String name=""; // name of animal 
	ArrayList<Double> path=new ArrayList<Double>() ; // input path for that concept 
	ArrayList<Double> outpath = new ArrayList<Double>(); // output path for that concept
	public Concept() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Concept [name=" + name + ", path=" + path.toString()
				+ "]";
	}
}//concept
