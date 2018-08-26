package hr.fer.zemris.ann.net;

import java.util.ArrayList;


public class Net {
	private ArrayList<Layer> layers;
	private double error;
	private double recentAverageError;
	private TrainingData data;
	
	private static final double eta = 0.15;
	
	public Net(TrainingData data) {
		layers = new ArrayList<Layer>();
		error = 0.;
		recentAverageError = 0.;
		this.data = data;
		
		// popunjavanje slojeva
		int i = 0;
		// popunjavam prvog
		layers.add(new Layer(data.getTopology().get(i), data.getTopology().get(i+1), false));
		i++;
		// dodavaj slojove do pretposljednjeg
		for (; i < data.getTopology().size()-1; i++) {
			layers.add(new Layer(data.getTopology().get(i), data.getTopology().get(i+1), true));
		}
		// dodaj posljednji sloj
		layers.add(new Layer(data.getTopology().get(i), 1, false));
		
	}
	
	public Net (TrainingData data, ArrayList<Double> weights) {
		layers = new ArrayList<Layer>();
		error = 0.;
		recentAverageError = 0.;
		this.data = data;
		
		// popunjavanje slojeva
		int i = 0;
		// popunjavam prvog
		layers.add(new Layer(data.getTopology().get(i), data.getTopology().get(i+1), false));
		i++;
		// dodavaj slojove do pretposljednjeg
		for (; i < data.getTopology().size()-1; i++) {
			layers.add(new Layer(data.getTopology().get(i), data.getTopology().get(i+1), true));
		}
		// dodaj posljednji sloj
		layers.add(new Layer(data.getTopology().get(i), 1, false));
		
		// popunjavam tezine
		i = 0;
		for (int j = 0; j < layers.size()-1; j++) {
			Layer layer = layers.get(j);
			for (int k = 0; k < layer.size(); k++) {
				Neuron n = layer.get(k);
				for (int l = 0; l < n.getNumOfOutputs(); l++) {
					n.setWeightAt(l, weights.get(i));
					i++;
				}
			}
		}
		// popunjavam scaler
		Layer layer = layers.get(0);
		for (int k = 0; k < layer.size(); k++) {
			Neuron n = layer.get(k);
			for (int l = 0; l < n.getNumOfOutputs(); l++) {
				n.setScalerAt(l, weights.get(i));
				i++;
			}
		}
	}
	
	public ArrayList<Layer> getLayers() {
		return this.layers;
	}
	
	public void setWeights(ArrayList<Double> weights) {
		
		// popunjavam tezine
		int i = 0;
		for (int j = 0; j < layers.size()-1; j++) {
			Layer layer = layers.get(j);
			for (int k = 0; k < layer.size(); k++) {
				Neuron n = layer.get(k);
				for (int l = 0; l < n.getNumOfOutputs(); l++) {
					n.setWeightAt(l, weights.get(i));
					i++;
					if (j == 0) {
						n.setScalerAt(l, weights.get(i));
						i++;
					}
				}
			}
		}
		
		/*
		// popunjavam scaler
		Layer layer = layers.get(0);
		for (int k = 0; k < layer.size(); k++) {
			Neuron n = layer.get(k);
			for (int l = 0; l < n.getNumOfOutputs(); l++) {
				
			}
		}
		*/
	}
	
	private void printAllWeights() {

		for (int j = 0; j < layers.size()-1; j++) {
			Layer layer = layers.get(j);
			for (int k = 0; k < layer.size(); k++) {
				Neuron n = layer.get(k);
				for (int l = 0; l < n.getNumOfOutputs(); l++) {
					System.out.println(n.getWeightAt(l));
					if (j == 0) {
						System.out.println(n.getScalerAt(l));
					}
				}
			}
		}
		System.out.println("");
	}
	
	public ArrayList<Integer> getTopology() {
		ArrayList<Integer> topology = new ArrayList<Integer>();
		for (Layer l : layers) {
			topology.add(l.size());
		}
		return topology;
	}
	
	public int getNumOfOutputs() {
		int numOfOutputs = 0;
		for (int i = 0; i < this.layers.size() - 1; i++) {
			Layer l = this.layers.get(i);
			numOfOutputs += l.size() * l.getNumOfOutputs();
		}
		Layer firstLayer = this.layers.get(0);
		numOfOutputs += firstLayer.size() * firstLayer.getNumOfOutputs();
		return numOfOutputs;
	}
	
	public int getNumOfNeurons() {
		ArrayList<Integer> topology = getTopology();
		int numOfNeurons = 0;
		for (Integer i : topology) {
			numOfNeurons += i;
		}
		return numOfNeurons;
	}
	
	public void feedForward(Pattern pattern) {
		// korigiram output u ulaznom sloju
		Layer inputLayer = layers.get(0);
		for (int i = 0; i < 1; i++) {
			Neuron n = inputLayer.get(i);
			n.setOutput(pattern.getX());
			n = inputLayer.get(i+1);
			n.setOutput(pattern.getY());
		}
		
		// korigiram output u drugom sloju
		Layer secondLayer = layers.get(1);
		for (int i = 0; i < secondLayer.size()-1; i++) {
			Neuron n = secondLayer.get(i);
			
			double net = 0.;
			for (int j = 0; j < inputLayer.size(); j++) {
				net = net + (Math.abs(inputLayer.get(j).getOutput() - inputLayer.get(j).getWeightAt(n.getIndex())))/Math.abs(inputLayer.get(j).getScalerAt(n.getIndex()));
				//System.out.println(inputLayer.get(j).getWeightAt(n.getIndex()));
				//System.out.println(inputLayer.get(j).getScalerAt(n.getIndex()));
				if (i==0) {
					/*System.out.println(inputLayer.get(j).getOutput());
					System.out.println(inputLayer.get(j).getWeightAt(n.getIndex()));
					System.out.println(Math.abs(inputLayer.get(j).getOutput() - inputLayer.get(j).getWeightAt(n.getIndex())));
					System.out.println(Math.abs(inputLayer.get(j).getScalerAt(n.getIndex())));
					System.out.println();*/
				}
			}
			
			//System.out.println(net);
			n.setOutput(1./(1+net));
			//System.out.println(n.getOutput());
		}
		//System.out.println();
		
		// korigiram output u skrivenom i izlaznom sloju
		
		for (int i = 2; i < layers.size(); i++) {
			Layer currLayer = layers.get(i);
			Layer prevLayer = layers.get(i-1);
			int layerSize = (i != layers.size() - 1) ? currLayer.size() - 1 : currLayer.size();
			
			for (int j = 0; j < layerSize; j++) {
				Neuron currNeuron = currLayer.get(j);
				int currNeuronIndex = currNeuron.getIndex();
				
				double net = 0.;
				
				// w(i) * x(i)
				for (int k = 0; k < prevLayer.size(); k++) {
					//System.out.println(k);
					Neuron tempNeuron = prevLayer.get(k);
					net = net + tempNeuron.getOutput() * tempNeuron.getWeightAt(currNeuronIndex);
					//System.out.println(tempNeuron.getWeightAt(currNeuronIndex));
					
					/*
					if (j == 0) {
						
						System.out.println("UNUTARNJA PETLJA");
						System.out.println(tempNeuron.getOutput());
						System.out.println(tempNeuron.getWeightAt(currNeuronIndex));
						System.out.println("");
						
					}
					*/
					
				}
				
				//if (i == layers.size() - 2) System.out.println(net);
				//System.out.println("");
				// output = sigma(net);
				
				double output = activationFunction(net);
				//if (j==0) System.out.println("NET JE " + net);
				
				currNeuron.setOutput(output);
				/*
				if (i == layers.size() - 1) {
					
					System.out.println("PASS");
					System.out.println(output);
					System.out.println(currNeuron.getOutput());
					System.out.println("");
					
				}
				*/
				//System.out.println(currNeuron.getOutput());
			}
			//System.out.println("");
			//if (i == layers.size() - 1) System.out.println("FINIL SAN");
		}
	}
	
	// option 0 batch, option 1 stochastic, option 2 mini batch
	public void backpropagation(Classes classes, int option) {
		
		int i = this.layers.size() - 1;
		Layer zadnjiLayer = this.layers.get(i);
		
		double tempError = 0.;
		for (int k = 0; k < zadnjiLayer.size(); k++) {
			Neuron n = zadnjiLayer.get(k);
			tempError = tempError + Math.pow((classes.getY()[k] - n.getOutput()), 2);
			double delta = n.getOutput() * (1-n.getOutput()) * (classes.getY()[k] - n.getOutput());
			n.setDelta(delta);
		}
		tempError = tempError/zadnjiLayer.size();
		error += tempError;
		
		for (--i; i >= 0; i--) {
			Layer currLayer = this.layers.get(i);
			Layer succLayer = this.layers.get(i+1);
			int numOfOutputs = currLayer.getNumOfOutputs();
			// iteriraj po svim neuronima
			for (int k = 0; k < currLayer.size(); k++) {
				Neuron n = currLayer.get(k);
				double sumOfDeltaTimesW = 0.;
				// iteriraj po svim tezinama i namesti in delta
				for (int j = 0; j < numOfOutputs; j++) {
					sumOfDeltaTimesW = sumOfDeltaTimesW + succLayer.get(j).getDelta() * n.getWeightAt(j);
					if (option == 0 || option == 2) {
						double newDeltaWeight = n.getDeltaWeightAt(j) + succLayer.get(j).getDelta() * n.getOutput();
						n.setDeltaWeightAt(j, newDeltaWeight);
					}
					if (option == 1) {
						double newWeight = n.getWeightAt(j) + eta * succLayer.get(j).getDelta() * n.getOutput();
						n.setWeightAt(j, newWeight); // ovo je dobro za stochastic
					}
				}
				double delta = n.getOutput() * (1 - n.getOutput()) * sumOfDeltaTimesW;
				n.setDelta(delta);
			}
		}
	}
	
	private double activationFunction(double net) {
		return (1./(1+Math.exp(-1 * net)));
	}
	
	public double[] getResults() {
		Layer outputLayer = layers.get(layers.size()-1);
		double[] results = new double[outputLayer.size()];
		for (int i = 0; i < outputLayer.size(); i++) {
			results[i] = outputLayer.get(i).getOutput();
		}
		return results;
	}
	
	private void refreshWeights() {
		for (Layer l : layers) {
			for (int i = 0 ; i < l.size(); i++) {
				Neuron n = l.get(i);
				for (int k = 0; k < n.getNumOfOutputs(); k++) {
					n.setWeightAt(k, n.getWeightAt(k) + eta * n.getDeltaWeightAt(k));
					n.setDeltaWeightAt(k, 0.);
				}
			}
		}
	}
	
	public double getAndResetError() {
		double tempError = this.error;
		this.error = 0;
		return tempError;
	}
	
	public double train() {
		//printAllWeights();
		//System.out.println("");
		ArrayList<Pattern> patterns = data.getX();
		ArrayList<Classes> classes = data.getY();
		for (int i = 0; i < patterns.size(); i++) {
			feedForward(patterns.get(i));
			//System.out.println("ONE PASS");
			calcError(classes.get(i));
		}
		//System.exit(0);
		//System.out.println("TOTAL PASS");
		//System.out.println("");
		double err = getAndResetError();
		//System.out.println("Greska iznosi: " + err);
		//System.out.println("Srednja greska iznosi: " + err/patterns.size());
		//System.out.println("");
		return err;
	}
	
	public double test() {
		//printAllWeights();
		//System.out.println("");
		ArrayList<Pattern> patterns = data.getX();
		ArrayList<Classes> classes = data.getY();
		for (int i = 0; i < patterns.size(); i++) {
			feedForward(patterns.get(i));
			//System.out.println("ONE PASS");
			double[] res = getResults();
			System.out.println(patterns.get(i).getX() + "\t" + patterns.get(i).getY() + "\t" + Math.round(res[0]) + "\t" + Math.round(res[1]) + "\t" + Math.round(res[2]));
		}
		//System.exit(0);
		//System.out.println("TOTAL PASS");
		//System.out.println("");
		double err = getAndResetError();
		//System.out.println("Greska iznosi: " + err);
		//System.out.println("Srednja greska iznosi: " + err/patterns.size());
		//System.out.println("");
		return err;
	}

	private void calcError(Classes classes) {
		// TODO Auto-generated method stub
		int i = this.layers.size() - 1;
		Layer zadnjiLayer = this.layers.get(i);
		
		double tempError = 0.;
		//System.out.println("CLASSESS ERROR CALCULATED");
		for (int k = 0; k < zadnjiLayer.size(); k++) {
			Neuron n = zadnjiLayer.get(k);
			tempError = tempError + Math.pow((classes.getY()[k] - n.getOutput()), 2);
			//System.out.println(classes.getY()[k]);
			//System.out.println(n.getOutput());
			//System.out.println("");
			
		}
		//System.out.println(tempError);
		tempError = tempError/zadnjiLayer.size();
		this.error += tempError;	
	}
	
}
