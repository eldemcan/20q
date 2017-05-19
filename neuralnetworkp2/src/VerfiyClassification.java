import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Class VerfiyClassification.Verify input,output patterns and update insert text file 
 */
public class VerfiyClassification {

	/**
	 * Verify path.
	 *
	 * @param path the path
	 * @param concept the concept
	 */
	public void verifyPath(String path,String concept){
		int found_index = searchConcept(concept);
		//if concept is already exsists there might be something wrong with parameters
		if(found_index!=-1){
			if(fixWrongPath(found_index,path)){ // we need to re train outputs 
				Data.RETRAIN =true;			
			}//if
		}//if
		else{
			int found_input_index=findInput(path);
			
			if (found_input_index!=-1){
				System.out.println("need to update my table");
				Data.concepts.get(found_input_index).name=concept;
				updateFile();
			}//if 

			else{

				System.out.println("i need to learn this concept");
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(Data.TRAININGFILE));
					for(int i=0;i<Data.concepts.size();i++){
						Concept temp = Data.concepts.get(i);
						temp.outpath.add(0.0);

						for(int j=0;j<temp.path.size();j++){
							double g=temp.path.get(j);
							writer.write((int)g+" ");
						}//for
						for(int m=0;m<temp.outpath.size();m++){
							double g= temp.outpath.get(m);
							writer.write((int)g+" ");
						}//for
						writer.write(temp.name+"\n"); //write name
					}//for concepts 

					String new_concept=path;
					for(int i=0;i<Data.OUTPUTUNITS;i++){
						new_concept=new_concept+" 0";
					}//for
					new_concept=new_concept+" 1 "+concept;
					System.out.println("this line going to be added to table");
					System.out.println(new_concept);
					writer.write(new_concept);
					writer.close();
					Data.OUTPUTUNITS=Data.OUTPUTUNITS+1; // increase number of output units
					Data.HIDDENUNITS=(int) Math.round((Data.INPUTUNITS+Data.OUTPUTUNITS)*(0.8)); //calculation if new hidden units 
					Data.UPDATE=true;
					Data.RETRAIN=true;
				} catch (IOException e) {
					System.out.println("file can't open");
					e.printStackTrace();
				}
			}// else 
		}//else
	}

	/**
	 * Search concept.with given name
	 *
	 * @param name the name
	 * @return the int
	 */
	public int searchConcept(String name){

		int found_index = -1 ;

		for(int i=0;i<Data.concepts.size();i++){
			Concept temp =Data.concepts.get(i);

			if(temp.name.equalsIgnoreCase(name)){
				found_index = i ;
				System.out.println("concept is already exsists");
			}
		}
		return found_index ;
	}//end of method 

	/**
	 * Fix wrong path.checks whether classification is correct or not
	 *
	 * @param concept_index the concept_index
	 * @param path the path
	 * @return true, if successful
	 */
	public boolean fixWrongPath(int concept_index,String path){

		Concept temp = Data.concepts.get(concept_index);
		path=path.replace(" ",""); // delete empty spaces 
		ArrayList<Double> converted = new ArrayList<Double>();
		boolean path_mistake = true ;
		System.out.println("verfiying paths");

		for(int i=0;i<temp.path.size();i++){
			int compare =(int) (temp.path.get(i)*1);
			if(compare!=Integer.parseInt(""+path.charAt(i))){
				System.out.println("mistake in classification,are you sure you have right information about object?");
				path_mistake = false ;
				break;
			}//if
		}
		return path_mistake;
	}// find wrong index 

	/**
	 * Find input.
	 *
	 * @param path the path
	 * @return the int
	 */
	public int findInput(String path){

		int input_index=-1;

		for(int i=0;i<Data.concepts.size();i++){
			Concept temp = Data.concepts.get(i);
			String compare_s="";
			for(int m=0;m<temp.path.size();m++){
				compare_s=compare_s+" "+(temp.path.get(m).intValue());
			}//for
			if(path.equals(compare_s.trim())==true){
				input_index=i;		
			}//if
		}//for
		return input_index;
	}//find input

	/**
	 * Update text file.with changed data
	 */
	public void updateFile(){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(Data.TRAININGFILE));

			for(int i=0;i<Data.concepts.size();i++){
				Concept temp = Data.concepts.get(i);

				for(int j=0;j<temp.path.size();j++){
					double g=temp.path.get(j);
					writer.write((int)g+" ");
				}//for
				for(int m=0;m<temp.outpath.size();m++){
					double g= temp.outpath.get(m);
					writer.write((int)g+" ");
				}//for
				writer.write(temp.name+"\n"); //write name
			}//for concepts 
			writer.close();
		}
		catch (IOException e) {
			System.out.println("file can't open");
			e.printStackTrace();
		}
	} // update file 

}
