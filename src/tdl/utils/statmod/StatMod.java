package tdl.utils.statmod;



import java.util.HashMap;

import tdl.model.Task;

public interface StatMod {

	
	public void calculateModelParameters(Task root);

	public double estimateTimeToComplete(Task tree);
	
}
