package hr.fer.zemris.ann.net;

import java.util.ArrayList;

public class Layer {

	private ArrayList<Neuron> layer;
	
	public Layer(int numOfNeurons, int numOfOutputs, boolean b) {
		this.layer = new ArrayList<Neuron>();
		int i = 0;
		for (; i < numOfNeurons; i++) {
			this.layer.add(new Neuron(i, numOfOutputs));
		}
		if (b) {
			Neuron bias = new Neuron(i, numOfOutputs, 1);
			bias.setOutput(1.);
			this.layer.add(bias);
		}
	}
	
	public int size() {
		return this.layer.size();
	}
	
	public int getNumOfOutputs() {
		return this.layer.get(0).getNumOfOutputs();
	}
	
	public Neuron get(int index) {
		return layer.get(index);
	}
	
}
