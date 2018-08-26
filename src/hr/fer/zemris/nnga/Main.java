package hr.fer.zemris.nnga;

import java.util.ArrayList;

import hr.fer.zemris.ann.net.Layer;
import hr.fer.zemris.ann.net.Net;
import hr.fer.zemris.ann.net.Neuron;
import hr.fer.zemris.ga.EliminationGeneticAlgorithm;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataParser parser = new DataParser("zad7-dataset.txt");
		/*
		Net net = new Net(parser.getTrainingData());
		net.feedForward(parser.getTrainingData().getX().get(0));
		System.out.println(net.getResults()[0]);
		System.out.println(net.getResults()[1]);
		System.out.println(net.getResults()[2]);
		*/
		EliminationGeneticAlgorithm eGA = new EliminationGeneticAlgorithm(50, parser.getTrainingData(), 20000, 1e-7, true);
		Net net = eGA.run();
		net.test();
		ArrayList<Layer> layers = net.getLayers();
		Layer firstLayer = layers.get(0);
		System.out.println("LAYER 0");
		for (int j = 0; j < firstLayer.size(); j++) {
			Neuron n = firstLayer.get(j);
			System.out.println("NEURON " + j);
			for (int k = 0; k < n.getNumOfOutputs(); k++) {
				System.out.println("w" + k + " = " + n.getWeightAt(k) + ";\ts" + k + " = " + n.getScalerAt(k));
			}
		}
		for (int i = 1; i < layers.size()-1; i++) {
			System.out.println("LAYER " + i);
			for (int j = 0; j < firstLayer.size(); j++) {
				Neuron n = layers.get(i).get(j);
				System.out.println("NEURON " + j);
				for (int k = 0; k < n.getNumOfOutputs(); k++) {
					System.out.println("w" + k + " = " + n.getWeightAt(k));
				}
			}
		}
	}

}
