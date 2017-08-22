package tdl2.controller.detailcontroller;

import java.io.File;
import java.util.ArrayList;

import tdl2.controller.Controller;
import tdl2.utils.FileDrop.Listener;

public class MyAttachmentListener implements Listener {

	private Controller controller;

	public MyAttachmentListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void filesDropped(File[] files) {
		System.out.println("Files dropped: " + files);
		ArrayList<File> fileList = new ArrayList<File>();
		for(int i = 0; i < files.length; i++) {
			fileList.add(files[i]);
			System.out.println("Added file " + files[i].getName());
		}
		controller.getTreeView().getCurrentNode().getTask().addAttachments(fileList);
	}

}
