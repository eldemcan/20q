/**
	 * This class created make a guess in case program is not able to guess correctly 
	 *
	 * @author Can Eldem
	 */
public class Guess {

	double [] output=null;

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
			if (path[i] > max && path[i]>0.5) {
				max = path[i];
				max_index=i;
			}
		}
		return max_index;
	}

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
