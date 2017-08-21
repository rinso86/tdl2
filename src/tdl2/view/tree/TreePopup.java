package tdl2.view.tree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import tdl2.controller.Controller;
import tdl2.controller.TaskNode;


@SuppressWarnings("serial")
public class TreePopup extends JPopupMenu{

	private Controller controller; 
	private TaskNode currentnode;
	private JMenuItem addSubtaskItem;
	private JMenuItem deleteTaskItem;
	
	public TreePopup(Controller c) {
		this.controller = c;
		this.currentnode = null;
		
		addSubtaskItem = new JMenuItem("Add subtask");
		addSubtaskItem.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				controller.addNewSubtask(currentnode);
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		add(addSubtaskItem);
		
		deleteTaskItem = new JMenuItem("Delete task");
		deleteTaskItem.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed (MouseEvent e) {
				controller.deleteTask(currentnode);
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
		});
		add(deleteTaskItem);
	}
	
	public TaskNode getCurrentnode() {
		return currentnode;
	}

	public void setCurrentnode(TaskNode currentnode) {
		this.currentnode = currentnode;
	}

}
