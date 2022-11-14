package com.paper.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

public class A4Paper {

	private static float widthRate = 2.0f;
	
	private static float heightRate = 2.97f;
	
	public static byte[] compileImage(
			String firstImgPath, 
			String secondImgPath) {
		byte[] bytes = null;
		try {
			// 待合并的图片
			BufferedImage img = ImageIO.read(new FileInputStream(firstImgPath));
			BufferedImage img2 = ImageIO.read(new FileInputStream(secondImgPath));
			
			// 图片宽度
			int widthCut = img.getWidth();
			
			// 计算A4纸张倍率
			int scale = calA4Scale(widthCut);
			
			// 设置A4纸大小
			int width = (int)(widthRate * scale);
			int height = (int)(heightRate * scale);
			
			// 生成A4面板
			BufferedImage image = new BufferedImage(width, height, BufferedImage.SCALE_DEFAULT);
			Graphics graphics = image.getGraphics();
			
			float cut_proportion_paper = 1.75f;
			int heightCut = (int)(widthCut/cut_proportion_paper);
			
			// A4纸渲染
			graphics.setColor(Color.WHITE);
			graphics.fillRect(1, 0, width, height);
			
			// 计算开始打印坐标
			int posPaperX = width / 2 - widthCut / 2;
			int posPaperY = height / 2 - heightCut;
			
			// 打印图片
			graphics.drawImage(img, posPaperX, posPaperY, widthCut, heightCut, null);
			graphics.drawImage(img2, posPaperX, posPaperY + heightCut, widthCut, heightCut, null);
			graphics.dispose();
			
			// ImageIO.write(image, "jpg", new File(outputPath));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			bytes = baos.toByteArray();
			baos.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	private static int calA4Scale(int width) {
		if (width == 0) {
			return 0;
		}
		
		return (int) Math.pow((double)10, (int)Math.log10(width));
	}
}
