package tdl.view.tree.popup;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import tdl.view.tree.TreeView;


@SuppressWarnings("serial")
public class TreePopup extends JPopupMenu {

	@SuppressWarnings("unused")
	private TreeView treeView; 
	private JMenuItem addSubtaskItem;
	private JMenuItem completeTaskItem;
	private JMenuItem reactivateTaskItem;
	private JMenuItem deleteTaskItem;
	
	public TreePopup(TreeView treeView) {
		this.treeView = treeView;
		
		addSubtaskItem = new JMenuItem("Add subtask");
		addSubtaskItem.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				treeView.onPopupAddSubtaskRequested(e);
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
				treeView.onPopupTaskCompleteRequested(e);
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
				treeView.onPopupReactivateTaskRequested(e);
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
				treeView.onPopupDeleteTaskRequested(e);
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

}
