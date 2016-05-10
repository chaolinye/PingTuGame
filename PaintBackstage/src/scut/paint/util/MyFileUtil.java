package scut.paint.util;

import java.io.*;

public class MyFileUtil {
	public static File createNewFile(File file)throws IOException {
		if (!file.exists()) {
			mkdir(file.getParentFile());
				file.createNewFile();
		}
		return file;
	}

	public static void mkdir(File dir){
		if (!dir.getParentFile().exists()) {
			mkdir(dir.getParentFile());
		}
		dir.mkdir();
	}
	public static void main(String[] args)throws IOException{
		MyFileUtil.createNewFile(new File("d:/tmp/a.txt"));
	}
}
