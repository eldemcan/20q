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

import android.widget.AnalogClock;

/**
 * The Class AnimalNetwork.
 */
public class AnimalNetwork {

	NeuralNetwork animal_network;
	BackPropagation b_learning;
	TrainingSet<SupervisedTrainingElement> training_set;
	//parameters 
	public static final int MAXITERATIONS=1000;
	public static double LEARNINGRATE=0.4;
	public static final double MAXERROR=0.001;
	public double total_error =0.0;

	/**
	 * Instantiates a new animal network.
	 */
	public AnimalNetwork() {
		super();
		initializeNeurons();

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
		animal_network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,Data.INPUTUNITS,Data.HIDDENUNITS,Data.OUTPUTUNITS);
		animal_network.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
		animal_network.randomizeWeights();  //randomize weights 
		((LMS) animal_network.getLearningRule()).setMaxError(MAXERROR);//0-1
		((LMS) animal_network.getLearningRule()).setLearningRate(LEARNINGRATE);//0-1
		((LMS) animal_network.getLearningRule()).setMaxIterations(MAXITERATIONS);//0-1
		animal_network.setLearningRule(new BackPropagation());
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
	 * Sets the training set.
	 *
	 * @param input_neurons the input_neurons
	 * @param output_neurons the output_neurons
	 */
	public void setTrainingSet(int input_neurons,int output_neurons){
		training_set =new TrainingSet<SupervisedTrainingElement>(input_neurons,output_neurons);
	}

	/**
	 * Initialize training set from text.Reads input and output units and train network 
	 * accordingly 
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

				for(int i=0;i<num_input;i++){
					input.add((double)Integer.parseInt(data[i]));
					temp.path.add((double)Integer.parseInt(data[i]));
				}

				for(int i=num_input;i<num_output+num_input;i++){
					output.add((double)Integer.parseInt(data[i]));
					temp.outpath.add((double)Integer.parseInt(data[i]));
				}
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
	 * Learning with back propogation
	 */
	public void learn(){
		System.out.println("learning in progress...");
		animal_network.learn(training_set);
		total_error= ((LMS) animal_network.getLearningRule()).getTotalNetworkError();
		System.out.println("total error:"+(float)((LMS) animal_network.getLearningRule()).getTotalNetworkError());//0-1
		System.out.println("learning finished");
	}// learn 

	/**
	 * Ask network.Send user inputs to network to see which output unit is increasing 
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
	 * Counts line in file to determine initial input-ouput units dynamically 
	 *
	 * @param filename the filename
	 * @return the int
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
	 * Initialize number of neurons.dynamically from text file  
	 */
	public void initializeNeurons(){
		Data.INPUTUNITS=countLine(Data.ATTRIBUTEFILE);
		System.out.println("number of input units:"+Data.INPUTUNITS);
		Data.OUTPUTUNITS=countLine(Data.TRAININGFILE);
		Data.HIDDENUNITS=(int) Math.round((Data.INPUTUNITS+Data.OUTPUTUNITS)*(0.8)); //calculation if new hidden units 
		System.out.println("number of output unit:"+Data.OUTPUTUNITS);
		System.out.println("number of hidden units:"+Data.HIDDENUNITS);
	}//initialize values 

	/**
	 * Re train network after new concept or updating concept 
	 */
	public void reTrain(){
		// there is a problem with paramaters detected by verify class 
		// parameters need to be set again 
		if(Data.UPDATE == false && Data.RETRAIN == true){
			double temp_error_rate=total_error;
			double division_number = LEARNINGRATE - ((LEARNINGRATE/100)*50);
			System.out.println("i need to train myself again");
			System.out.println("randomazing weights");
			animal_network.randomizeWeights();  //randomize weights
			while(temp_error_rate>=total_error){
				LEARNINGRATE = (LEARNINGRATE+0.01)%division_number;
				((LMS) animal_network.getLearningRule()).setLearningRate(LEARNINGRATE);//0-1
				animal_network.setLearningRule(new BackPropagation());	
				learn();
			}//while
			SaveNetwork("animal_network.nnet"); // save network 
		}//if

		//training for new element
		else if(Data.UPDATE == true && Data.RETRAIN == true){
			System.out.println("parameters set according to new concept");			
			initializeNeurons();
			training_set = null ;
			animal_network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,Data.INPUTUNITS,Data.HIDDENUNITS,Data.OUTPUTUNITS);
			animal_network.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
			animal_network.randomizeWeights();  //randomize weights 
			((LMS) animal_network.getLearningRule()).setMaxError(MAXERROR);//0-1
			((LMS) animal_network.getLearningRule()).setLearningRate(LEARNINGRATE);//0-1
			((LMS) animal_network.getLearningRule()).setMaxIterations(MAXITERATIONS);//0-1
			animal_network.setLearningRule(new BackPropagation());
			setTrainingSet(Data.INPUTUNITS,Data.OUTPUTUNITS);
			initializeTrainingSetFromText(Data.TRAININGFILE);
			learn();
			SaveNetwork("animal_network.nnet"); // save network 
		}
	}//retrain 

}//end of class 
