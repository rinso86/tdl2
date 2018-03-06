package tdl.utils.statmod;

import tdl.model.Task;

/**
 * Weighted version of the Buvs Model
 * @author Langbein_M
 *
 */
public class WBuvs implements StatMod {
	
	private WorkHours wh;
	
	public WBuvs() {
		this.wh = new WorkHours();
	}

	@Override
	public void calculateModelParameters(Task root) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double estimateTimeToComplete(Task tree) {
		Double expctTime = 0.0;
		
		if(tree.isCompleted()) {
			return tree.getSecondsActiveRecursive();
		}

		
		
		return 0;
	}

}
