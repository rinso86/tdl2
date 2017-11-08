package tdl.utils.statmod;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;

import tdl.model.Task;


/**
 * Bottom-Up Variable Structure Model
 * 
 * 
 */
public class Buvs implements StatMod {
	
	int treedepth;
	HashMap<Integer, Double> meanNetTimes = new HashMap<Integer, Double>();
	HashMap<Integer, Double> varNetTimes = new HashMap<Integer, Double>();
	HashMap<Integer, Double> meanChildCounts = new HashMap<Integer, Double>();
	double globalMeanNetTime;
	double globalVarNetTime;
	double globalMeanChildCount;
	HashMap<Integer, PoissonDistribution> poisDs = new HashMap<Integer, PoissonDistribution>();
	HashMap<Integer, GammaDistribution> gamDs = new HashMap<Integer, GammaDistribution>();

	@Override
	public void calculateModelParameters(Task root) {
		treedepth = getHeight(root);
		HashMap<Integer, ArrayList<Double>> netTimes = new HashMap<Integer, ArrayList<Double>>();
		HashMap<Integer, ArrayList<Integer>> childCounts = new HashMap<Integer, ArrayList<Integer>>();
		fillNetTimes(root, netTimes);
		fillChildCouns(root, childCounts);
		calcNetMeanTimes(netTimes);
		calcVarNetTimes(netTimes);
		calcMeanChildCounts(childCounts);
		globalMeanNetTime = averageMeanNetTime();
		globalMeanChildCount = averageMeanChildCount();
		globalVarNetTime = averageVarNetTime();
		calcPoisDistrs();
		calcGammaDistrs();
	}



	private Double getMeanNetTime(int depth) {
		Double mnt = meanNetTimes.get(depth);
		if(mnt == null) {
			mnt = globalMeanNetTime;
		}
		return mnt;
	}


	private Double getVarNetTime(int depth) {
		Double vnt = varNetTimes.get(depth);
		if(vnt == null) {
			vnt = globalVarNetTime;
		}
		return vnt;
	}


	private Double getMeanChildCount(int depth) {
		Double mcc = meanChildCounts.get(depth);
		if(mcc == null) {
			mcc = globalMeanChildCount;
		}
		return mcc;
	}


	private void calcGammaDistrs() {
		for(int d = 0; d < treedepth; d++) {
			Double mean = getMeanNetTime(d);
			Double var = varNetTimes.get(d);
			if(var == null) var = globalVarNetTime;
			double alpha = mean * mean / var;
			double beta = mean / var;
			double scale = 1 / beta;
			GammaDistribution gam = new GammaDistribution(alpha, scale);  
			gamDs.put(d, gam);
		}
	}



	private void calcPoisDistrs() {
		// TODO Auto-generated method stub
		
	}

	private double averageMeanChildCount() {
		double sum = 0;
		for(double c : meanChildCounts.values()) {
			sum += c;
		}
		double average = sum / meanChildCounts.size();
		return average;
	}

	private double averageMeanNetTime() {
		double sum = 0;
		for(double t : meanNetTimes.values()) {
			sum += t;
		}
		double average = sum / meanNetTimes.size();
		return average;
	}


	private double averageVarNetTime() {
		double sum = 0;
		for(double v : varNetTimes.values()) {
			sum += v;
		}
		double average = sum / varNetTimes.size();
		return average;
	}
	
	
	private void calcMeanChildCounts(HashMap<Integer, ArrayList<Integer>> childCounts) {
		for(Integer d : childCounts.keySet()) {
			double sum = 0;
			for(double c : childCounts.get(d)) {
				sum += c;
			}
			double meanCount = sum / childCounts.get(d).size();
			meanChildCounts.put(d, meanCount);
		}
	}

	private void calcNetMeanTimes(HashMap<Integer, ArrayList<Double>> netTimes) {
		for(Integer d : netTimes.keySet()) {
			double sum = 0;
			for(double t : netTimes.get(d)) {
				sum += t;
			}
			double meanTime = sum / netTimes.get(d).size();
			meanNetTimes.put(d, meanTime);
		}
	}

	

	private void calcVarNetTimes(HashMap<Integer, ArrayList<Double>> netTimes) {
		for(Integer d : netTimes.keySet()) {
			double mean = meanNetTimes.get(d);
			double sum = 0;
			for(double t : netTimes.get(d)) {
				sum += (mean - t)*(mean - t);
			}
			double varTime = sum / netTimes.get(d).size();
			varNetTimes.put(d, varTime);
		}
	}
	
	private void fillChildCouns(Task root, HashMap<Integer, ArrayList<Integer>> childCounts) {
		int depth = getDepth(root);
		int chCount = root.getChildren().size();
		if(childCounts.get(depth) != null) {
			childCounts.get(depth).add(chCount);			
		}else {
			childCounts.put(depth, new ArrayList<Integer>());
			childCounts.get(depth).add(chCount);
		}
		for(Task child : root.getChildren()) {
			fillChildCouns(child, childCounts);
		}
	}

	private void fillNetTimes(Task root, HashMap<Integer, ArrayList<Double>> netTimes) {
		int depth = getDepth(root);
		if(root.isCompleted()) {
			double netTime = root.getSecondsActive();
			if(netTimes.get(depth) != null) {
				netTimes.get(depth).add(netTime);			
			}else {
				netTimes.put(depth, new ArrayList<Double>());
				netTimes.get(depth).add(netTime);
			}			
		}
		for(Task child : root.getChildren()) {
			fillNetTimes(child, netTimes);
		}
	}

	@Override
	public double estimateTimeToComplete(Task tree) {
		double expctTime = 0;
		
		if(tree.isCompleted()) {
			return tree.getSecondsActiveRecursive();
		}
		
		// factor 1: net time of base
		int depth = getDepth(tree);
		long secsActive = tree.getSecondsActive();
		expctTime += getEstimateMeanNetTime(depth, secsActive);
		
		// factor 2: estimated gross time of children
		for(Task child : tree.getChildren()) {
			expctTime += estimateTimeToComplete(child);
		}
		
		// factor 3: expected number of additional children
		int k0 = tree.getChildren().size();
		double lambda = meanChildCounts.get(depth);
		double additChildren = expChldCond(lambda, k0) - k0;
		double expTimeNewChild = expTimeNewChild(depth);
		expctTime += additChildren * expTimeNewChild;
		
		return expctTime;
	}



	/**
	 * Returns the distance from the tree root
	 * 
	 * @param tree
	 * @return
	 */
	private int getDepth(Task tree) {
		int depth = 0;
		Task parent = tree.getParent();
		while(parent != null) {
			depth++;
			parent = parent.getParent();
		}
		return depth;
	}


	/**
	 * Returns the distance to the furthest leaf
	 * 
	 * @param root
	 * @return
	 */
	private int getHeight(Task root) {
		int height = 1;
		int maxChHeight = 0;
		for(Task child : root.getChildren()) {
			int chHeight = getHeight(child);
			if(chHeight > maxChHeight) {
				maxChHeight = chHeight;
			}
		}
		height += maxChHeight;
		return height;
	}
	
	private double factorial(int k) {
		double out = 1;
		while(k > 0) {
			out *= k;
			k--;
		}
		return out;
	}

	
	/**
	 * Estimates the number of hits in a Poisson process, given that we already have k0 hits.
	 * 
	 * @param lambda
	 * @param k0
	 * @return
	 */
	private double expChldCond(double lambda, int k0) {
		double sum1 = 0;
		double sum2 = 0;
		for(int i = 0; i < k0 -1; i++) {
			sum1 += i * Math.pow(lambda, i) / factorial(i);
			sum2 += Math.pow(lambda, i) / factorial(i);
		}
		double a = lambda - Math.exp(-lambda) * sum1;
		double b = 1 - Math.exp(-lambda) * sum2;
		return a / b;
	}
	
	
	/**
	 * Returns the expected gross time for a completely new childnode
	 * 
	 * @param depth
	 * @return
	 */
	private double expTimeNewChild(int depth) {
		double expTime = 0;
		int maxDepth = meanNetTimes.size();
		expTime +=  getEstimateMeanNetTime(depth, 0);
		double chCount = 1;
		for(int d = depth; d < maxDepth; d++) {
			expTime += chCount * getEstimateMeanNetTime(d, 0);
			chCount *= meanChildCounts.get(d);
		}
		return expTime;
	}
	

	private double getEstimateMeanNetTime(int depth, long secsActive) {
		
		GammaDistribution gam = gamDs.get(depth);
		
		double alpha = gam.getAlpha();
		double gt0 = gam.probability(secsActive);
		double a = gt0 * secsActive * secsActive;
		double b = alpha + 1;
		double fac = a / b;
		
		double expct = gam.getNumericalMean();
		double prbCuml = gam.cumulativeProbability(secsActive);
		
		return (expct - fac) / (1 - prbCuml);
	}
	
}
