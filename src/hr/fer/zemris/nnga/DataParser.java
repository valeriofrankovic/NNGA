package hr.fer.zemris.nnga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import hr.fer.zemris.ann.net.Dot;
import hr.fer.zemris.ann.net.TrainingData;

public class DataParser {
	
	private TrainingData trainingData;

	public DataParser(String filename) {
		this.trainingData = new TrainingData();
		
		File file = new File(filename);
		BufferedReader reader = null;
		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;

		    while ((text = reader.readLine()) != null) {
		        String[] parts = text.split("\t");
		        double x0 = Double.parseDouble(parts[0]);
		        double x1 = Double.parseDouble(parts[1]);
		        Dot dot = new Dot(x0, x1);
		        int[] y_ = new int[3];
		        y_[0] = Integer.parseInt(parts[2]);
		        y_[1] = Integer.parseInt(parts[3]);
		        y_[2] = Integer.parseInt(parts[4]);
		        this.trainingData.add(dot, y_);
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
	}
	
	public TrainingData getTrainingData() {
		return this.trainingData;
	}
}
