package hr.fer.zemris.ga;

import java.util.ArrayList;

public class Population {

	private ArrayList<Chromosome> individuals;
	
	public Population() {
		this.individuals = new ArrayList<Chromosome>();
	}
	
	public Population(int populationSize, int numOfValues) {
		individuals = new ArrayList<Chromosome>();
		for (int i = 0; i < populationSize; i++) {
			individuals.add(new Chromosome(numOfValues));
		}
	}
	
	public void add(Chromosome chromosome) {
		individuals.add(chromosome);
	}
	
	public boolean remove(Chromosome chromosome) {
		if (individuals.indexOf(chromosome) != -1) {
			individuals.remove(individuals.indexOf(chromosome));
			return true;
		}
		return false;
	}
	
	public boolean remove(int index) {
		if (individuals.size() > index && index >= 0) {
			individuals.remove(index);
			return true;
		}
		return false;
	}
	
	public Chromosome get(int i) {
		return individuals.get(i);
	}
	
	public boolean contains (Chromosome ch) {
		return this.individuals.contains(ch);
	}
	
	public int getSize() {
		return this.individuals.size();
	}
	
	public void addAll(ArrayList<Chromosome> chs) {
		this.individuals.addAll(chs);
	}
}
