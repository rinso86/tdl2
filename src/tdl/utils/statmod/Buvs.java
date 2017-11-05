package tdl.utils.statmod;

import java.util.HashMap;

import tdl.model.Task;


/**
 * Bottom-Up Variable Structure Model
 * 
 * 
 */
public class Buvs implements StatMod {
	
	HashMap<Integer, Double> meanNetTimes = new HashMap<Integer, Double>();
	HashMap<Integer, Double> meanChildCounts = new HashMap<Integer, Double>();

	@Override
	public void calculateModelParameters(Task root) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double estimateTimeToComplete(Task tree) {
		double expctTime = 0;
		
		// factor 1: net time of base
		int depth = getDepth(tree);
		expctTime += meanNetTimes.get(depth);
		
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
		// TODO Auto-generated method stub
		return 0;
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
		expTime += meanNetTimes.get(depth);
		double chCount = 1;
		for(int d = depth; d < maxDepth; d++) {
			expTime += chCount * meanNetTimes.get(d);
			chCount *= meanChildCounts.get(d);
		}
		return expTime;
	}
}
