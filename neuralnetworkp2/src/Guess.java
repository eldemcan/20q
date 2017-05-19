
// TODO: Auto-generated Javadoc
/**
 * The Class Guess.
 */
public class Guess {

	double [] output=null;

	/**
	 * Instantiates a new guess.
	 *
	 * @param output the output
	 */
	Guess(double[] output){
		this.output=output;
	}

	/**
	 * Find max.
	 *
	 * @param path the path
	 * @return the int
	 */
	public int findMax(double[] path){
		double max = path[0];
		int max_index=0;
		for (int i = 0; i < path.length; i++) {
			if (path[i] > max && path[i]>Data.THRESHOlD) {
				max = path[i];
				max_index=i;
			}
		}
		return max_index;
	}

	/**
	 * Guess animal finds highest possible animal accordin to network values 
	 *
	 * @param before the before
	 * @return the string
	 */
	public String guessAnimal(boolean before){

		int index;
		String name = null ;
		if(before==true){
			int deleted_index=findMax(output);
			output[deleted_index]=-1;
			index=findMax(output);
			if(deleted_index==index){Data.over=true;};
			name=Data.concepts.get(index).name;
		}//if
		else {
			index=findMax(output);
			name=Data.concepts.get(index).name;
		}//else 
		return name;
	}//guess animal
}
