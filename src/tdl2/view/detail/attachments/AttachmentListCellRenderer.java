package tdl2.view.detail.attachments;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import tdl2.controller.Controller;
import tdl2.model.Task;

public class AttachmentListCellRenderer extends DefaultListCellRenderer{

	private Controller controller;

	public AttachmentListCellRenderer(Controller controller) {
		this.controller = controller;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		File renderedFile = (File) value;
		Task currentTask = controller.getCurrentTask();
		ArrayList<File> lowestLevelFiles = currentTask.getAttachments();
		
		if(! lowestLevelFiles.contains(renderedFile)) {
			label.setForeground(Color.GRAY);
		}
		
		return label;
	}

}
