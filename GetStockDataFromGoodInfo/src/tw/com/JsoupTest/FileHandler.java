package tw.com.JsoupTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileHandler {
	File source = null;
	File dirFold = null;
	static File oldFold = null;
	static File newFold = null;
	File desFile = null;

	public static void moveFile(String newFoldStr, String oldFoldStr) {
		newFold = new File(newFoldStr);
		// dirFold = new File(dirsFold);
		oldFold = new File(oldFoldStr);
		if (oldFold.isDirectory() && newFold.isDirectory()) {
			File[] fileList = oldFold.listFiles();
			for (File f : fileList) {
				boolean move = f.renameTo(newFold);
				System.out.println("移動結果: " + move);
			}

		}
	}

	/**
	 * 刪除檔
	 * 
	 * @param filePathAndNameString
	 *            檔路徑及名稱 如c:/fqf.txt
	 * @param fileContentString
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();

		} catch (Exception e) {
			System.out.println("刪除檔操作出錯");
			e.printStackTrace();

		}

	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 檔存在時
				InputStream inStream = new FileInputStream(oldPath);// 讀入原檔
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 位元組數 檔案大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("複製單個檔操作出錯");
			e.printStackTrace();

		}

	}
	public static void main(String[] args) {
		// moveFile("D:\\Temp","D:\\Stock");
		//"D:/Stock/test_
		copyFile("D:/Stock", "D:/Temp");
//		delFile("D:\\Stock");
	}
//	複製單個檔操作出錯
//	java.io.FileNotFoundException: D:\Stock (存取被拒。)
//		at java.io.FileInputStream.open(Native Method)
//		at java.io.FileInputStream.<init>(FileInputStream.java:146)
//		at java.io.FileInputStream.<init>(FileInputStream.java:101)
//		at tw.com.JsoupTest.FileHandler.copyFile(FileHandler.java:58)
//		at tw.com.JsoupTest.FileHandler.main(FileHandler.java:79)
}
