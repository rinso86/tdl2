package tdl2.view.tree;


import java.awt.Color;
import java.awt.Component;
import java.util.Date;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import tdl2.controller.treecontroller.TaskNode;


@SuppressWarnings("serial")
public class TaskNodeRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Component component =  super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		if(value instanceof TaskNode) {
			TaskNode tn = (TaskNode) value;
			if(tn.getTask().isCompleted()) {
				component.setForeground(Color.darkGray);
			} else {
				Date deadline = tn.getDeadline();
				if(deadline != null) {
					Date now = new Date();
					long diff = deadline.getTime() - now.getTime();
					float diffDays =  diff / (24* 1000 * 60 * 60);
					if (diff <= 0) {
						component.setForeground(Color.red);
					} else if (diffDays <= 1) {
						component.setForeground(Color.orange);
					} else if (diffDays <= 3) {
						component.setForeground(Color.yellow);
					}					
				}
			}
		}
		return component;
	}

}
