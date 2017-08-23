package tdl2.view.detail.attachments;

import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractListModel;


@SuppressWarnings("serial")
public class AttachmentListModel extends AbstractListModel<File> {
	
	private ArrayList<File> files;

	public AttachmentListModel(ArrayList<File> files) {
		this.files = files;
	}

	@Override
	public File getElementAt(int index) {
		return files.get(index);
	}

	@Override
	public int getSize() {
		return files.size();
	}
	
	public void setData(ArrayList<File> newfiles) {
		if(newfiles != null) {			
			this.files = newfiles;
			refreshView();
		} 
	}

	public void refreshView() {
		if(this.getSize() > 0) {
			fireContentsChanged(this, 0, this.getSize());			
		}
	}
	
}