package tdl.view.tree.popup;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import tdl.view.tree.TaskNode;
import tdl.view.tree.TreeView;


public class TreePopupListener implements MouseListener {
	
	TreePopup tp;
	private TreeView tv;

	public TreePopupListener(TreePopup tp, TreeView tv) {
		this.tp = tp;
		this.tv = tv;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			TaskNode tn = tv.getNodeForEvent(e);
			tp.setClickedNode(tn);
			tp.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
