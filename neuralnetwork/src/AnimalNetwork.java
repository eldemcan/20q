import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.MomentumBackpropagation;



/**
 * @author Can Eldem
 */
public class AnimalNetwork {

	NeuralNetwork animal_network;
	BackPropagation b_learning;
	TrainingSet<SupervisedTrainingElement> training_set;
	public static final int MAXITERATIONS=2000;
	public static double LEARNINGRATE=0.5;
	public static final double MAXERROR=0.001;
	public double total_error =0.0;

	/**
	 * Instantiates a new animal network.
	 */
	public AnimalNetwork() {
		super();
		initializeNeurons(); // initialize neurons numbers from text file 
		initializeQuestions(); // initialize questions from text file 
	}// constructor 


	/**
	 * Instantiates a new animal network.
	 *
	 * @param input the input
	 * @param hidden the hidden
	 * @param output the output
	 */
	public AnimalNetwork(int input,int hidden,int output) {
		super();
		System.out.println("network is created");
		initializeNeurons();
		initializeQuestions();
		animal_network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,Data.INPUTUNITS,Data.HIDDENUNITS,Data.OUTPUTUNITS);
		animal_network.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
		animal_network.randomizeWeights();  //randomize weights 
		// set parameters 
		((LMS) animal_network.getLearningRule()).setMaxError(MAXERROR);//0-1 
		((LMS) animal_network.getLearningRule()).setLearningRate(LEARNINGRATE);//0-1
		((LMS) animal_network.getLearningRule()).setMaxIterations(MAXITERATIONS);//0-1
		MomentumBackpropagation backpropogation = new MomentumBackpropagation(); // define momentum
		backpropogation.setMomentum(0.7); // set momentum
		animal_network.setLearningRule(backpropogation); 
	}// end of constructor 

	/**
	 * Save network.
	 *
	 * @param name the name
	 */
	public void SaveNetwork(String name){
		animal_network.save(name);
		System.out.println("save is completed");
	}

	/**
	 * Load network.
	 *
	 * @param name the name
	 */
	public void loadNetwork(String name){
		animal_network=NeuralNetwork.load(name);
		System.out.println("load is completed");
	}

	/**
	 * Sets the training size 
	 *
	 * @param input_neurons the input_neurons
	 * @param output_neurons the output_neurons
	 */
	public void setTrainingSet(int input_neurons,int output_neurons){
		training_set =new TrainingSet<SupervisedTrainingElement>(input_neurons,output_neurons);
	}

	/**
	 * Initialize training set from text.This method reads training set from file and trains network accordingly 
	 *
	 * @param filename the filename
	 */
	public void initializeTrainingSetFromText(String filename){
		try {

			BufferedReader reader = new BufferedReader(new FileReader(filename));

			String line =null ;
			int num_input = training_set.getInputSize() ;
			int num_output = training_set.getOutputSize() ;

			while ((line = reader.readLine()) != null){
				Concept temp = new Concept();

				String[] data=line.split(" ");
				ArrayList<Double>input = new ArrayList<Double>();
				ArrayList<Double>output = new ArrayList<Double>();
					
				//read inputs for an animal 
				for(int i=0;i<num_input;i++){
					input.add((double)Integer.parseInt(data[i]));
					temp.path.add((double)Integer.parseInt(data[i]));
				}
				 //read expected outputs 
				for(int i=num_input;i<num_output+num_input;i++){
					output.add((double)Integer.parseInt(data[i]));
					temp.outpath.add((double)Integer.parseInt(data[i]));
				}
				//read name of concept
				for(int i =num_input+num_output;i<data.length;i++){
					temp.name=temp.name+data[i];
				}
				
				Data.concepts.add(temp);
				training_set.addElement(new SupervisedTrainingElement(input,output));
			}//while 

			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("can't read text file for training set");
		} catch (IOException e) {
			System.out.println("can't read file");
			e.printStackTrace();
		}

	}//initializeTrainingSetFromText

	/**
	 * Learn.
	 */
	public void learn(){
		
		System.out.println("learning in progress...");
		animal_network.learn(training_set);
		total_error= ((LMS) animal_network.getLearningRule()).getTotalNetworkError();
		System.out.println("total error:"+(float)((LMS) animal_network.getLearningRule()).getTotalNetworkError());//0-1
		System.out.println("learning finished");
	}// learn 

	/**
	 * Ask network.
	 *
	 * @param input the input
	 * @return the double[]
	 */
	public double[] askNetwork(String input){

		String [] temp = input.split(" ");
		double [] dataset = new double[temp.length] ;

		for(int i=0;i<temp.length;i++){
			dataset[i]=Double.parseDouble(temp[i]);
		} // end of for

		animal_network.setInput(dataset);
		animal_network.calculate();

		double [] path = animal_network.getOutput();
		return path;
	}//ask network

	/**
	 * Count line metho counts to assign input and output neurons dynamically from text file
	 *
	 * @param filename the filename
	 * @return number of lines in file
	 */
	public int countLine(String filename){
		String line = null;
		BufferedReader reader;
		int counter = 0 ;
		try {
			reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null){
				counter ++ ;
			}//while
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("file could not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("could not read file");
			e.printStackTrace();
		}
		return counter;
	} 

	/**
	 * Initialize neurons.
	 */
	public void initializeNeurons(){
		Data.INPUTUNITS=countLine(Data.QUESTIONFILE);
		System.out.println("number of input units:"+Data.INPUTUNITS);
		Data.OUTPUTUNITS=countLine(Data.TRAININGFILE);
		Data.HIDDENUNITS = (int)Math.round(Math.log(Data.INPUTUNITS)/Math.log(2));
		System.out.println("number of output unit:"+Data.OUTPUTUNITS);
		System.out.println("number of hidden units:"+Data.HIDDENUNITS);
	}//initialize values 

	/**
	 * Initialize questions.
	 */
	public void initializeQuestions(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Data.QUESTIONFILE));
			String line ="";
			while ((line = reader.readLine()) != null){
				Data.questions.add(line);
			}//while 
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("can t open file");
			e.printStackTrace();
		}//catch 
		catch (IOException e) {
			System.out.println("can t read file");
			e.printStackTrace();
		}
	}// initializeQuestions

}//end of class 
