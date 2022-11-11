package com.paper.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageUtil {
	
	/**
     * 基于文件路径进行图像合并, 并用bytes数组输出
     * @param prosPath 第一张图片路径
     * @param consPath 第二张图片路径
     * @return byte[] 图片流
     */
	public static byte[] imageMergeToBytesBasePath(String prosPath, String consPath) {
		try {
			// 读取待合并的文件
			BufferedImage prosImg = ImageIO.read(new File(prosPath));
			BufferedImage consImg = ImageIO.read(new File(consPath));
			return imageMergeToBytes(prosImg, consImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] imageMergeToBytesBaseIO(InputStream is1, InputStream is2) {
		try {
			BufferedImage prosImg = ImageIO.read(is1);
			BufferedImage consImg = ImageIO.read(is2);
			return imageMergeToBytes(prosImg, consImg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static byte[] imageMergeToBytes(BufferedImage prosImg, BufferedImage consImg) {
		byte[] bytes = null;
		try {
		    // 图片压缩
			prosImg = resize(prosImg, 1000, 1000, true);
			consImg = resize(consImg, 1000, 1000, true);
			
			// 新图片压缩
			BufferedImage destImg = mergeImage(prosImg, consImg, false);
			destImg = resize(destImg, 1000, 1000, false);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(destImg, "jpg", baos);
			bytes = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 压缩图片
	 * @param source 原图
	 * @param targetW 宽度压缩
	 * @param targetH 高度压缩
	 * @param isRotate 调整宽和高
	 * @return BufferedImage 图片
	 */
	 private static BufferedImage resize(BufferedImage source, int targetW, int targetH, boolean isRotate) {
		 // targetW，targetH分别表示目标长和宽
		 int type = source.getType();
		 BufferedImage target = null;
		 int width = source.getWidth();
		 int height = source.getHeight();
		 
	     // 图片宽度小于高度 需要 则调整 宽高 值
		 if (width < height && isRotate) {
			 width = height;
			 height = source.getWidth();
		 }
		 
		 double sx = (double)targetW / width;
		 double sy = (double)targetH / height;
		 
		 // 这里想实现在targetW，targetH范围内实现等比缩放
		 if (sx > sy) {
			 sx = sy;
			 targetW = (int) (sx * source.getWidth());
		 } else {
			 sy = sx;
			 targetH = (int) (sy * source.getHeight());
		 }
		 
		 if (type == BufferedImage.TYPE_CUSTOM) {
			 ColorModel cm = source.getColorModel();
			 WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			 boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			 target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		 } else {
			 target = new BufferedImage(targetW, targetH, type);
		 }
		 
		 Graphics2D g = target.createGraphics();
		 g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		 g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		 g.dispose();
		 return target;
	 }
	 
	 /**
      * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
      * mergeImage方法不做判断，自己判断。
      * @param img1 待合并的第一张图
      * @param img2 带合并的第二张图
      * @param isHorizontal 为true时表示水平方向合并，为false时表示垂直方向合并
      * @return 返回合并后的BufferedImage对象
      */
	 private static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2, boolean isHorizontal) {
		 int w1 = img1.getWidth();
		 int h1 = img1.getHeight();
		 int w2 = img2.getWidth();
		 int h2 = img2.getHeight();
		 
	     // 从图片中读取RGB, 逐行扫描图像中各个像素的RGB到数组中
		 int[] imageArrayOne = new int[w1 * h1];
		 imageArrayOne = img1.getRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
		 int[] imageArrayTwo = new int[w2 * h2];
		 imageArrayTwo = img2.getRGB(0, 0, w2, h2, imageArrayTwo, 0, w2);
		 
		 // 生成新图片
		 BufferedImage destImage = null;
		 if (isHorizontal) {
			 // 水平方向合并
			 destImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_BGR);
			 // 设置上半部分或左半部分的RGB
			 destImage.setRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
			 // 设置下半部分的RGB
			 destImage.setRGB(w1, 0, w2, h2, imageArrayTwo, 0, w2);
		 } else {
			 // 垂直方向合并
			 destImage = new BufferedImage(w1, h1 + h2, BufferedImage.TYPE_INT_BGR);
			 // 设置上半部分或左半部分的RGB
			 destImage.setRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
			 // 设置下半部分的RGB
			 destImage.setRGB(0, h1, w2, h2, imageArrayTwo, 0, w2);
		 }
		 return destImage;
	 }
	 
	 
}
