package hr.fer.zemris.ann.net;

import java.util.ArrayList;
import java.util.Random;

public class Neuron {
	private ArrayList<Double> weights;
	private ArrayList<Double> deltaWeights;
	private ArrayList<Double> scalerWeights;
	private double delta;
	private int index;
	private double output;
	
	// postavi index na index, izlaz na nulu, delta na nulu
	// zatim postavi nasumicne tezine onoliko koliko ima izlaza
	public Neuron(int index, int numOfOutputs) {
		this.index = index;
		this.output = 0.;
		this.delta = 0.;
		this.weights = new ArrayList<Double>();
		this.deltaWeights = new ArrayList<Double>();
		this.scalerWeights = new ArrayList<Double>();
		
		long seed = System.currentTimeMillis();
		Random r = new Random(seed);
		for (int i = 0; i < numOfOutputs; i++) {
			this.weights.add(r.nextDouble());
			this.deltaWeights.add(0.);
			this.scalerWeights.add(r.nextDouble());
		}
	}
	
	public Neuron(int index, int numOfOutputs, double output) {
		this.index = index;
		this.output = output;
		this.delta = 0.;
		this.weights = new ArrayList<Double>();
		this.deltaWeights = new ArrayList<Double>();
		this.scalerWeights = new ArrayList<Double>();
		
		Random random = new Random();
		for (int i = 0; i < numOfOutputs; i++) {
			this.weights.add(random.nextDouble());
			this.deltaWeights.add(0.);
			this.scalerWeights.add(random.nextDouble());
		}
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public double getDelta() {
		return this.delta;
	}
	
	public void setDelta(double value) {
		this.delta = value;
	}
	
	public int getNumOfOutputs() {
		return weights.size();
	}
	
	public double getOutput() {
		return this.output;
	}
	
	public void setOutput(double value) {
		this.output = value;
	}
	
	public double getWeightAt(int index) {
		return this.weights.get(index);
	}
	
	public void setWeightAt(int index, double value) {
		this.weights.set(index, value);
	}
	
	public double getDeltaWeightAt(int index) {
		return this.deltaWeights.get(index);
	}
	
	public void setDeltaWeightAt(int index, double value) {
		this.deltaWeights.set(index, value);
	}

	public double getScalerAt(int index) {
		return this.scalerWeights.get(index);
	}
	
	public void setScalerAt(int index, double value) {
		this.scalerWeights.set(index, value);
	}
}
