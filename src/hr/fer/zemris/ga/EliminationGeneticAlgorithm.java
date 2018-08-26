package hr.fer.zemris.ga;

import java.util.ArrayList;
import java.util.Random;

import hr.fer.zemris.ann.net.Net;
import hr.fer.zemris.ann.net.TrainingData;

public class EliminationGeneticAlgorithm {
	
	private Net net;
	private int populationSize;
	private int maxIter;
	private double epsilon;
	private TrainingData data;
	private boolean elitism;
	private Population population;
	private final int M = 10;
	private final double pMutate = 0.01;
	private final double vMutate = 0.9;
	private final double sigma1 = 1.;
	private final double sigma2 = 1.;
	
	private int bestIndex;
	
	public EliminationGeneticAlgorithm(int populationSize, TrainingData data, int maxIter, double epsilon, boolean elitism) {
		this.net = new Net(data);
		this.populationSize = populationSize;
		this.maxIter = maxIter;
		this.epsilon = epsilon;
		this.elitism = elitism;
		this.population = new Population(populationSize, net.getNumOfOutputs());
		this.data = data;
		
		
		this.bestIndex = 0;
	}
	
	public Net run() {
		// TODO Auto-generated method stub
		int i = 0;
		evaluate();
		do {
			print(i);
			select();
			crossOver();
			mutate();
			evaluate();
			//printAllWeights();
			i += 1;
		} while (i < this.maxIter && epsilon < this.population.get(getBestIndex()).getFitness());
		print(i);
		this.net.setWeights(this.population.get(getBestIndex()).getValues());
		return this.net;
	}

	private void printAllWeights() {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.population.getSize(); i++) {
			Chromosome ch = this.population.get(i);
			System.out.println(ch.getValues());
		}
		System.out.println("");
	}

	private void print(int i) {
		// TODO Auto-generated method stub
		double bestFitness = population.get(0).getFitness();
		for (int j = 1; j < population.getSize(); j++) {
			if (population.get(j).getFitness() < bestFitness) {
				bestFitness = population.get(j).getFitness();
			}
		}
		System.out.println("U generaciji " + i + " pronađeno je najbolje rješenje sa srednjim kvadratnim odstupanjem " + bestFitness);
	}

	private void evaluate() {
		// TODO Auto-generated method stub
		for (int j = 0; j < population.getSize(); j++) {
			
			//System.out.println("Checkpoint: " + population.get(j).getValues().size());
			//System.out.println(population.getSize() + "---" + j);
			
			this.net.setWeights(population.get(j).getValues());
			//Net net = new Net(data, population.get(j).getValues());
			double fitness = this.net.train()/this.data.getSize();
			
			population.get(j).setFitness(fitness);
		}
	}

	private void mutate() {
		// TODO Auto-generated method stub
		Random random = new Random();
		for (int i = 0; i < population.getSize(); i++) {
			Chromosome ch = population.get(i);
			/*
			if (i == this.bestIndex) {
				System.out.println("Prije mutacije najbolji iznosi " + population.get(this.bestIndex).getFitness());
				System.out.println("Prije mutacije najbolji iznosi " + (new Net(data, ch.getValues())).train()/data.getSize());
			}
			*/
			for (int j = 0; j < ch.getValSize(); j++) {
				//System.out.println("Prije mutacije:");
				//System.out.println((new Net(this.data, ch.getValues())).train());
				if (random.nextDouble() < pMutate) {
					//System.out.println("Mutirao");
					if (random.nextDouble() < vMutate) {
						ch.setValue(j, ch.getValues().get(j) + random.nextGaussian() * sigma1);
					}
					else {
						ch.setValue(j, random.nextGaussian() * sigma2);
					}
				}
				//System.out.println("Nakon mutacije:");
				//System.out.println((new Net(this.data, ch.getValues())).train());
			}
			/*
			if (i == this.bestIndex) {
				System.out.println("Nakon mutacije najbolji iznosi " + population.get(this.bestIndex).getFitness());
				System.out.println("Nakon mutacije najbolji iznosi " + (new Net(data, ch.getValues())).train()/data.getSize());
			}
			*/
		}
	}

	private void crossOver() {
		// TODO Auto-generated method stub
		Random random = new Random();
		this.bestIndex = getBestIndex();
		ArrayList<Chromosome> newChromosomes = new ArrayList<Chromosome>();
		for (int i = 0; i < M; i++) {
			
			int indexOfChromosomeToBeSelected = 0;
			
			// selecting first chromosome
			//indexOfChromosomeToBeSelected = random.nextInt(population.getSize());
			//Chromosome ch1 = population.get(indexOfChromosomeToBeSelected);
			Chromosome ch1 = tournament();
			//System.out.println(ch1.getFitness());
			// selecting second chromosome
			//indexOfChromosomeToBeSelected = random.nextInt(population.getSize());
			//Chromosome ch2 = population.get(indexOfChromosomeToBeSelected);
			Chromosome ch2 = tournament();
			//System.out.println(ch2.getFitness());
			
			if (ch1.equals(ch2)) {
				i--;
				continue;
			}
			
			// choosing mating option
			// option = 0 --> uniform
			// option = 1 --> single-point crossover
			// option = 2 --> two-point crossover
			int option = random.nextInt(3);
			Chromosome baby = null;
			
			ArrayList<Double> vals = new ArrayList<Double>();
			
			if (option == 0) {
				for (int j = 0; j < ch1.getValSize(); j++) {
					//vals.add(0.5 * ch1.getValues().get(j) + (1-0.5) * ch2.getValues().get(j));
					
					boolean firstChromosome = random.nextBoolean();
					if (firstChromosome) {
						vals.add(ch1.getValues().get(j));
					}
					else {
						vals.add(ch2.getValues().get(j));
					}
					
				}
			}
			else if (option == 1) {
				// j oznacava redni broj tezine
				int j = 0;
				// topologija
				ArrayList<Integer> topology = this.net.getTopology();
				// k je pomocna varijabla koja govori koji kromosom je na redu
				for (int k = 0; k < topology.get(0); k++) {
					boolean firstChromosome = random.nextBoolean();
					if (firstChromosome) {
						// l mi je pomocna varijabla koja trci po tezinama
						for (int l = 0; l < 2 * (topology.get(1) - 1); l++) {
							vals.add(ch1.getValues().get(j));
							//System.out.println(j);
							j++;
						}
						//System.out.println("");
					}
					else {
						// l mi je pomocna varijabla koja trci po tezinama
						for (int l = 0; l < 2 * (topology.get(1) - 1); l++) {
							vals.add(ch2.getValues().get(j));
							//System.out.println(j);
							j++;
						}
						//System.out.println("");
					}
				}
				//System.out.println("TOPOLOGY SIZE " + topology.size());
				//System.out.println("TOPOLOGY LOOKS LIKE " + topology);
				// k je pomocna varijabla koja trci po sloju
				for (int k = 1; k < topology.size() - 2; k++) {
					//System.out.println("SAD SAN NA ELEMENTE " + topology.get(k));
					// l je pomocna varijabla koja trci po neuronima
					for (int l = 0; l < topology.get(k); l++) {
						//System.out.println(l);
						boolean firstChromosome = random.nextBoolean();
						if (firstChromosome) {
							// l mi je pomocna varijabla koja trci po tezinama
							for (int m = 0; m < topology.get(k+1) - 1; m++) {
								vals.add(ch1.getValues().get(j));
								//System.out.println(j);
								j++;
							}
							//System.out.println("");
						}
						else {
							// l mi je pomocna varijabla koja trci po tezinama
							for (int m = 0; m < topology.get(k+1) - 1; m++) {
								vals.add(ch2.getValues().get(j));
								//System.out.println(j);
								j++;
							}
							//System.out.println("");
						}
					}
				}
				//System.out.println("FINIL SAN");
				for (int k = 0; k < topology.get(topology.size()-2); k++) {
					boolean firstChromosome = random.nextBoolean();
					if (firstChromosome) {
						// l mi je pomocna varijabla koja trci po tezinama
						for (int l = 0; l < topology.get(topology.size()-1); l++) {
							vals.add(ch1.getValues().get(j));
							//System.out.println(j);
							j++;
						}
						//System.out.println("");
					}
					else {
						// l mi je pomocna varijabla koja trci po tezinama
						for (int l = 0; l < topology.get(topology.size()-1); l++) {
							vals.add(ch2.getValues().get(j));
							//System.out.println(j);
							j++;
						}
						//System.out.println("");
					}
				}
			}
			/*
				ArrayList<Integer> topology = this.net.getTopology();
				boolean firstChromosome = random.nextBoolean();
				if (firstChromosome) {
					vals.add(ch1.getValues().get(j));
				}
				else {
					vals.add(ch2.getValues().get(j));
				}
				/*
				double percentage = 0.3;
				if (j < ch1.getValSize() * percentage) {
					vals.add(ch1.getValues().get(j));
				}
				else {
					vals.add(ch2.getValues().get(j));
				}
				
			}
			*/
			else if (option == 2) {
				for (int j = 0; j < ch1.getValSize(); j++) {
					vals.add(0.5 * ch1.getValues().get(j) + (1-0.5) * ch2.getValues().get(j));
				}
				
				/*
				double percentage1 = 0.15;
				double percentage2 = 0.45;
				if (j < ch1.getValSize() * percentage1 || j > ch1.getValSize() * percentage2) {
					vals.add(ch1.getValues().get(j));
				}
				else {
					vals.add(ch2.getValues().get(j));
				}
				*/
			}
			else {
				System.out.println("Something went wrong.");
			}
			
			baby = new Chromosome(vals);
			//baby.setFitness((new Net(this.data, vals)).train()/this.data.getSize());
			
			if (population.contains(baby)) {
				i--;
				continue;
			}
			newChromosomes.add(baby);
			//System.out.println("best je " + population.get(this.bestIndex).getFitness() + ", a ja dodajem " + (new Net(this.data, vals)).train()/this.data.getSize());
		}
		this.population.addAll(newChromosomes);
	}

	private Chromosome tournament() {
		// TODO Auto-generated method stub
		Random random = new Random();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int index = random.nextInt(population.getSize());
		indices.add(index);
		Chromosome ch = population.get(index);
		for (int i = 0; i < 4; i++) {
			index = random.nextInt(population.getSize());
			if (indices.contains(index)) {
				i--;
				continue;
			}
			indices.add(index);
			Chromosome tempCh = population.get(index);
			if (tempCh.getFitness() < ch.getFitness()) {
				ch = tempCh;
			}
		}
		return ch;
	}

	private void select() {
		// TODO Auto-generated method stub
		for(int k = 0; k < M; k++) {
			int indexBest = getBestIndex();
			double[] probability = getRouletteWheel();
			Random random = new Random();
			double probabilityK = random.nextDouble();
			int indexToBeRemoved = 0;
			for (indexToBeRemoved = 0; probabilityK > probability[indexToBeRemoved]; indexToBeRemoved++) {
			}
			if (elitism && indexBest == indexToBeRemoved) {
				k--;
				continue;
			}
			//System.out.println("best je " + population.get(indexBest).getFitness() + ", a ja brisem " + population.get(indexToBeRemoved).getFitness());
			population.remove(indexToBeRemoved);
		}
	}

	private int getBestIndex() {
		// TODO Auto-generated method stub
		int bestIndex = 0;
		double bestFitness = population.get(0).getFitness();
		for (int j = 1; j < population.getSize(); j++) {
			if (population.get(j).getFitness() < bestFitness) {
				bestIndex = j;
				bestFitness = population.get(j).getFitness();
			}
		}
		return bestIndex;
	}

	private double[] getRouletteWheel() {
		// TODO Auto-generated method stub
		double ukupanFitness = getUkupanFitness();
		double[] probability = new double[population.getSize()];
		probability[0] = population.get(0).getFitness()/getUkupanFitness();
		for (int j = 1; j < population.getSize(); j++) {
			probability[j] = probability[j-1] + population.get(j).getFitness() / ukupanFitness;
		}
		return probability;
	}

	private double getUkupanFitness() {
		// TODO Auto-generated method stub
		double ukupanFitness = 0.;
		for (int i = 0; i < population.getSize(); i++) {
			ukupanFitness += population.get(i).getFitness();
		}
		return ukupanFitness;
	}
	
}
