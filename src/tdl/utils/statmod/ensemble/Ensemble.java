package tdl.utils.statmod.ensemble;

import java.util.ArrayList;

import tdl.model.Task;
import tdl.utils.statmod.StatMod;

public class Ensemble implements StatMod {

	private ArrayList<StatMod> models;

	public Ensemble(ArrayList<StatMod> models) {
		this.models = models;
	}

	@Override
	public void calculateModelParameters(Task root) {
		for(StatMod model : models) {
			model.calculateModelParameters(root);
		}
	}

	@Override
	public double estimateTimeToComplete(Task tree) {
		double[] predictions = new double[models.size()];
		for(int i = 0; i<models.size(); i++) {
			predictions[i] = models.get(i).estimateTimeToComplete(tree);
		}
		double mean = calcMean(predictions);
		return mean;
	}
	
	private double calcMean(double[] data) {
		if(data.length == 0) return 0;
		
		double sum = 0;
		for(double el : data) {
			sum += el;
		}
		return sum / data.length;
	}

}
