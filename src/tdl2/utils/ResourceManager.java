package tdl2.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceManager {

	private File resourceDir;
	
	public ResourceManager() {
		resourceDir = new File("resources");
		if(! resourceDir.exists() ) {
			System.out.println("Resources nicht gefunden. Ordner wird erzeugt.");
			resourceDir.mkdir();
		}
	}
	
	public File getResourceDir() {
		return resourceDir;
	}

	
	public File saveToResources(File sourceFile) throws IOException {
		File savedFile = new File(resourceDir, sourceFile.getName());
		copyFileUsingStream(sourceFile, savedFile);
		return savedFile;
	}

	public void deleteFileFromResources(File file) {
		file.delete();
	}
	
	private void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
}
