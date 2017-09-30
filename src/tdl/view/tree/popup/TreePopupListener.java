package tdl.view.tree.popup;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;


public class TreePopupListener implements MouseListener {
	
	TreePopup tp;

	public TreePopupListener(TreePopup tp) {
		this.tp = tp;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
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
