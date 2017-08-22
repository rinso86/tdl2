package tdl2.view.upcoming;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import tdl2.model.Task;

@SuppressWarnings("serial")
public class TaskListRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object element, int index, boolean selected, boolean focus) {
		JLabel comp =  (JLabel) super.getListCellRendererComponent(list, element, index, selected, focus);
		if(element instanceof Task) {
			Task task = (Task) element;
			String dl = ".......";
			if(task.getDeadline() != null) {
				dl = task.getDeadline().toString();
			}
			String labelText = dl + " -- " + task.getTitle();
			comp.setText(labelText);
		}
		return comp;
	}
}
