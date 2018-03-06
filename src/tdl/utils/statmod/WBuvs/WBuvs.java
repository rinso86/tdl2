package tdl.utils.statmod.WBuvs;

import java.util.ArrayList;

import tdl.model.Task;
import tdl.utils.statmod.StatMod;

public class WBuvs implements StatMod {
	
	private Task root;
	private DistanceMatrix dm;
	private Double globalMeanTimeNet;
	private Double globalMeanChildcountNet;

	@Override
	public void calculateModelParameters(Task rt) {
		root = rt;
		dm = TreeParser.getDistanceMatrix(root);
		globalMeanTimeNet = TreeParser.getGlobalMeanTimeNet(root);
		globalMeanChildcountNet = TreeParser.getGlobalMeanChildcountNet(root);
	}

	@Override
	public double estimateTimeToComplete(Task task) {
		
		if(task.isCompleted()) {
			return task.getSecondsActiveRecursive();
		}
		
		// Step 1: expected time net of the task alone
		Double expectedTimeNet = expectedTimeNet(task);
		
		
		// Step 2: expected time of the existing children
		Double expectedTimeExistingChildren = 0.0;
		for(Task child : task.getChildren()) {
			expectedTimeExistingChildren += estimateTimeToComplete(child);
		}
		
		// Step 3: expected time of the expected children yet to come
		Double nrChildrenExpected = expectedNrChildrenNet(task);
		Double nrAdditionalChildrenExpected = nrChildrenExpected - task.getChildCount();
		Double expectedTimeNewChild = expectedTimeNewChildGross(task);
		Double expectedTimeNewChildren = nrAdditionalChildrenExpected * expectedTimeNewChild;
		
		Double sum = expectedTimeNet + expectedTimeExistingChildren + expectedTimeNewChildren;
		
		return sum;
	}


	/**
	 * E[T_n(task)] | tree_0 = integrate_t0^oo t P(t) dt
	 * For an exponential  distribution, this yields: t0 + lambda
	 * @param task
	 * @return
	 */
	private Double expectedTimeNet(Task task) {
		
		if(task.isCompleted()) {
			return (double) task.getSecondsActive();
		}
		
		Double timeActive = (double) task.getSecondsActive();
		Double timeWeightedMean = weightedMeanTimeActive(task);
		
		Double sum = timeActive + timeWeightedMean;
		
		return sum;
	}

	private Double weightedMeanTimeActive(Task baseTask) {
		Double sum = 0.0;
		Double sumW = 0.0;
		ArrayList<Task> tasks = TreeParser.getTasksCompleted(root, new ArrayList<Task>());
		for(Task task : tasks) {
			Integer distance = dm.distance(baseTask, task);
			Double weight = 1.0/distance;
			sumW += weight;
			sum += weight * task.getSecondsActive();
		}
		
		Double mean = sum/sumW;
		
		return mean;
	}

	
	private Double expectedNrChildrenNet(Task task) {
		
		if(task.isCompleted()) {
			return (double) task.getChildCount();
		}
		
		Double nrChildrenCurrent = (double) task.getChildCount();
		Double nrChildrenWeightedMean = weightedMeanNrChildren(task);
		
		Double sum = nrChildrenCurrent + nrChildrenWeightedMean;
		
		return sum;
	}

	private Double weightedMeanNrChildren(Task baseTask) {
		Double sum = 0.0;
		Double sumW = 0.0;
		ArrayList<Task> tasks = TreeParser.getTasksCompleted(root, new ArrayList<Task>());
		for(Task task : tasks) {
			Integer distance = dm.distance(baseTask, task);
			Double weight = 1.0/distance;
			sumW += weight;
			sum += weight * task.getChildCount();
		}
		
		Double mean = sum/sumW;
		
		return mean;
	}
	

	private Double expectedTimeNewChildGross(Task parent) {
		
		if (parent.isCompleted()) {
			return 0.0;
		}
		
		// @TODO: this is using the global mean. That is not ideal. It would be better if we'd calculate this individually for any node. 
		
		// E[T_b] = E[T_n] + E[Ch_n] E[T_b(child)]
		//        = T_g + Ch_g ( T_g + Ch_g ( T_g + Ch_g ( T_g + Ch_g ( T_g + Ch_g ( ... ) ) ) ) )
		Double expectedTimeNewChildGross = globalMeanTimeNet * (1.0 / (1.0 - globalMeanChildcountNet) );
		
		return expectedTimeNewChildGross;
		
	}

}
