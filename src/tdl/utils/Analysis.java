package tdl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import tdl.model.Task;


/**
 * 
 * We model the time it takes to complete a task as such: 
 * 
	E_0B = mean(0N)
	E_1B = mean(1N) + sum_children E_0B
	E_2B = mean(2N) + sum_children E_1B
	...
	
	where
	
	E: expected time
	N: netto
	B: brutto
	0: leaf
	1: first parent
	2: second parent 
	...
	
 *
 */
public class Analysis {

	
	private Double[] meansNetto;
	
	
	public Analysis() {}
	
	public void calculateModelParameters(Task root) {
		HashMap<Integer, ArrayList<Long>> data = extractData(root); 
		
		meansNetto = new Double[data.size()];
		
		for(int i = 0; i < data.size(); i++) {
			meansNetto[i] = meanTime(data.get(i));
		}
	}

	public double estimateTimeToComplete(Task tree) {
		int level = getDepth(tree);
		Double E_T_brutto = meansNetto[level];
		for(Task child : tree.getChildren()) {
			E_T_brutto += estimateTimeToComplete(child);
		}	
		return E_T_brutto;
	}

	private Double meanTime(ArrayList<Long> nettoTimes) {
		Double result = null;
		Long sum = 0L;
		for(Long nettoTime : nettoTimes) {
			sum += nettoTime;
		}
		Integer count = nettoTimes.size();
		if(count > 0) {
			result = new Double(sum) / new Double(count);
		}
		return result;
	}
	
	
	private HashMap<Integer, ArrayList<Long>> extractData(Task root) {
		
		// Setting up data structure
		Integer depth = getDepth(root);
		HashMap<Integer, ArrayList<Long>> data = new HashMap<Integer, ArrayList<Long>>();
		for(Integer i = 0; i < depth; i++) {
			data.put(i, new ArrayList<Long>());
		}
		
		// Filling up data structure
		fillLevelTimesMap(root, data);
		
		return data;
	}
	
	/*
	 *  HashMap<Integer, ArrayList<Integer>>
	 *  		depth    nettoTimes on this depth
	 */
	
	private HashMap<Integer, ArrayList<Long>> fillLevelTimesMap(Task root, HashMap<Integer, ArrayList<Long>> data) {
		
		Integer depth = getDepth(root);
		for(Task child : root.getChildren()) {
			if(child.isCompleted() && child.getSecondsActive() > 0) {
				data.get(depth - 1).add(child.getSecondsActive());				
			}
		}
		
		for(Task child : root.getChildren()) {
			fillLevelTimesMap(child, data);
		}
		
		return data;
	}

	private int getDepth(Task tree) {
		int depth = 1;
		int maxChildDepth = 0;
		for(Task task : tree.getChildren()) {
			int maxChildDepthCandidate = getDepth(task);
			maxChildDepth = Math.max(maxChildDepth, maxChildDepthCandidate);
		}
		return depth + maxChildDepth;
	}	
	
}
