package hr.fer.zemris.ga;

import java.util.ArrayList;
import java.util.Random;

public class Chromosome {
	private ArrayList<Double> values;
	private double fitness;
	
	public Chromosome(int numOfValues) {
		this.values = new ArrayList<Double>();
		
		Random random = new Random();
		for (int i = 0; i < numOfValues; i++) {
			this.values.add(4. * random.nextDouble() - 2.);
		}
		this.fitness = 0.;
	}
	
	public Chromosome(ArrayList<Double> vals) {
		this.values = vals;
		this.fitness = 0.;
	}
	
	//public double calculateFitness(double x, double y) {
	//	return Math.sin(values[0] + values[1] * x) + values[2] * Math.cos(x * (values[3] + y)) * (1/(1 + Math.exp(Math.pow(x-values[4], 2))));
	//}
	
	public double getFitness() {
		return this.fitness;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public ArrayList<Double> getValues() {
		return this.values;
	}
	
	public void setValue(int i, double value) {
		this.values.set(i, value);
	}
	
	public int getValSize() {
		return this.values.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chromosome other = (Chromosome) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	
	
}
