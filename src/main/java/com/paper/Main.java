package com.paper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.paper.util.A4Paper;
import com.paper.util.ImageUtil;

public class Main {

	public static void main(String[] args) {
		OutputStream os1 = null;
		OutputStream os2 = null;
		OutputStream os3 = null;
		try {
			
			// 方式一. 文件路径直接调用
			byte[] imgStr3 = ImageUtil.imageMergeToBytesBasePath("C:\\img\\1.png", "C:\\img\\2.png");
			os1 = new FileOutputStream("C:\\img\\3.jpg");
			os1.write(imgStr3);
			
			// 方式二. 输入流的形式调用
			InputStream is1 = new FileInputStream(new File("C:\\img\\1.png"));
			InputStream is2 = new FileInputStream(new File("C:\\img\\2.png"));
			byte[] imgStr4 = ImageUtil.imageMergeToBytesBaseIO(is1, is2);
			os2 = new FileOutputStream("C:\\img\\4.jpg");
			os2.write(imgStr4);
			
			// 方式三. 打印到A4纸
			byte[] imgStr5 = A4Paper.compileImage("C:\\img\\1.png", "C:\\img\\2.png");
			os3 = new FileOutputStream("C:\\img\\5.jpg");
			os3.write(imgStr5);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os1 != null) {
				try {
					os1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (os2 != null) {
				try {
					os2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (os3 != null) {
				try {
					os3.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
