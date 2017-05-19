import java.io.File;
import java.util.Scanner;

import android.widget.AnalogClock;

public class Game {

	/**
	 * The main method
	 *
	 * @author Can Eldem
	 */
	public static void main(String[] args) {

		File f = new File("animal_network.nnet");
		AnimalNetwork animal =null ;
		VerfiyClassification verify = null ;
		String askpath="";
		new QuestionGenerator();
		QuestionTree tryy = new QuestionTree();
		

		// if network file is not exsists create file and train network 
		if(f.exists()==false){
			animal = new AnimalNetwork(Data.INPUTUNITS,Data.HIDDENUNITS,Data.OUTPUTUNITS); // create network
			animal.setTrainingSet(Data.INPUTUNITS,Data.OUTPUTUNITS);
			animal.initializeTrainingSetFromText(Data.TRAININGFILE);
			if(Data.LEARN==true){// to see difference between training and non training
				animal.learn();}
			animal.SaveNetwork("animal_network.nnet");
		}// if
		// load network from file 
		else if(f.exists()==true){ 
			animal = new AnimalNetwork();
			animal.loadNetwork("animal_network.nnet");
			animal.setTrainingSet(Data.INPUTUNITS,Data.OUTPUTUNITS);
			animal.initializeTrainingSetFromText(Data.TRAININGFILE);
		}// else if 


		Scanner input =new Scanner(System.in);
		// THIS IS FOR DESICION TREE!!!
		//	for(int i =0 ; i<tryy.questions.size();i++){  
		for(int i =0;i<Data.complete_questions.size();i++){
			//	System.out.println(tryy.questions.get(i).question+" (y/n)"); // ask question 
			System.out.println(Data.complete_questions.get(i)+" (y/n)"); // ask question
			String answer = input.nextLine();
			if(answer.equalsIgnoreCase("y")){
				askpath=askpath+"1 ";
			} //if
			else if(answer.equalsIgnoreCase("n")){
				askpath=askpath+"0 ";
			} //else if
		}//for 

		double[] path=animal.askNetwork(askpath.trim());
		
//		System.out.println("network values;");
//		for(int h=0;h<path.length;h++){
//			System.out.println(path[h]);	
//		}
		// guessing after getting input from user
		System.out.println("i am guessing");
		Guess guess = new Guess(path);
		System.out.println("Is it "+guess.guessAnimal(false)+ "?");
		String answer = input.nextLine();

		if(answer.equalsIgnoreCase("y")){
			Data.over=true;
			Data.correct = true ;
			System.out.println("thanks for playing game!");
		}//if 

		//guess 3 times in case project does not figure out which animal is it
		else{  //computer could not guess right answer
			int step=0;
			while (step!=2 && Data.over==false){
				String guessed_name=guess.guessAnimal(true);
				if(Data.over==true){break;}
				System.out.println("Is it "+guessed_name+ "?");
				answer = input .nextLine();
				if(answer.equalsIgnoreCase("y")){ Data.over=true; Data.correct=true; System.out.println("thanks for playing");}
				else if(answer.equalsIgnoreCase("n")){Data.over =false; Data.correct =false;} 
				step++;
			}//while
		}//else

		//if(Data.over==false && Data.correct==false){ //program could not guess correctly 
		if(Data.correct==false){ //program could not guess correctly
			System.out.println("I gave up,could you tell me what is it?");
			answer = input.nextLine();
			double[] output_neurons=animal.askNetwork(askpath.trim());
			verify= new VerfiyClassification();
			verify.verifyPath(askpath.trim(),answer);
			animal.reTrain();
			System.out.println("done");
		}
	}
}
