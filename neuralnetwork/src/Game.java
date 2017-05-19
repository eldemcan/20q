import java.io.File;
import java.util.Scanner;


public class Game {

	/**
	 * The main method.
	 *
	 * @author 120014670
	 */
	public static void main(String[] args) {

		File f = new File("animal_network.nnet"); //check previous file
		AnimalNetwork animal =null ;


		while(Data.continue_game){
			
			//clean values begining of loop
			Data.questions.clear();
			Data.concepts.clear();
			
			//if network did not trained before 
			if(f.exists()==false){
				animal = new AnimalNetwork(Data.INPUTUNITS,Data.HIDDENUNITS,Data.OUTPUTUNITS); // create network
				animal.setTrainingSet(Data.INPUTUNITS,Data.OUTPUTUNITS);
				animal.initializeTrainingSetFromText(Data.TRAININGFILE);
				if(Data.LEARN==true){// to see difference between training and non training
					animal.learn();} 
				animal.SaveNetwork("animal_network.nnet");
			}

			//if network trained before 
			else if(f.exists()==true){ 
				animal = new AnimalNetwork();
				animal.loadNetwork("animal_network.nnet");
				animal.setTrainingSet(Data.INPUTUNITS,Data.OUTPUTUNITS);
				animal.initializeTrainingSetFromText(Data.TRAININGFILE);
			}// else if 


			Scanner input =new Scanner(System.in);
			String askpath=""; // user input creates a path
			
			// ask initialized question by sequence 
			for(int i =0;i<Data.questions.size();i++){

				System.out.println(Data.questions.get(i)+" (y/n)"); // ask question 
				String answer = input.nextLine();

				if(answer.equalsIgnoreCase("y")){
					askpath=askpath+"1 ";
				} //if

				else if(answer.equalsIgnoreCase("n")){
					askpath=askpath+"0 ";
				} //else if

			}//for 
			//send inputs to network 
			double[] path=animal.askNetwork(askpath.trim());

			//make a guess 
			Guess guess = new Guess(path);
			System.out.println("Is it "+guess.guessAnimal(false)+ "?");
			String answer = input.nextLine();

			//finish if user says yes 
			if(answer.equalsIgnoreCase("y")){
				Data.over=true;
				Data.correct = true ;
				Data.continue_game= true;
				System.out.println("Thanks for playing");
				System.out.println("do you want to play more? (y/n)");
				String temp=input.nextLine();
				if(temp.equalsIgnoreCase("y")){
					Data.continue_game = true ;
				}

				else{
					Data.continue_game = false ;
				}

			}//if 

			//let computer guess few times 
			else{  //computer could not guess right answer
				int step=0;
				while (step!=2 && Data.over==false){
					String guessed_name=guess.guessAnimal(true);
					if(Data.over==true){break;}
					System.out.println("Is it "+guessed_name+ "?");
					answer = input .nextLine();
					if(answer.equalsIgnoreCase("y")){ Data.over=true; Data.correct=true; Data.continue_game=false; System.out.println("Thanks for playing");}
					step++;
				}//while
				System.out.println("do you want to play more? (y/n)");
				String temp=input.nextLine();
				if(temp.equalsIgnoreCase("y")){
					Data.continue_game = true ;
				}

				else{
					Data.continue_game = false ;
				}

			}//else 
		} // game loop
	}// main
}// end of class 
