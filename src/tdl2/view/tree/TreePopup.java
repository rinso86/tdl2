package tdl2.view.tree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import tdl2.controller.Controller;
import tdl2.controller.treecontroller.TaskNode;


@SuppressWarnings("serial")
public class TreePopup extends JPopupMenu{

	private Controller controller; 
	private TaskNode currentnode;
	private JMenuItem addSubtaskItem;
	private JMenuItem completeTaskItem;
	private JMenuItem reactivateTaskItem;
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
		
		completeTaskItem = new JMenuItem("Task completed");
		completeTaskItem.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				controller.completeTask(currentnode);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
		});
		add(completeTaskItem);
		
		reactivateTaskItem = new JMenuItem("Reactivate task");
		reactivateTaskItem.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				controller.reactivateTask(currentnode);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
		});
		add(reactivateTaskItem);
		
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
