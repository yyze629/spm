package com.fh.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;



/**
 * 二维码工具类
 * 
 * @Title: QRCodeUtils.java
 * @Package com.yinyang.gooagoo.common
 * @author yinyang@dhgate.com
 * @date 2016年5月14日 上午11:13:19
 * @version V1.0
 */
public class QRCodeUtils
{
    private final static int SIZE = 160;
    private final static int REAL_SIZE = 128;
    
    /**
     * 生成二维码字节流
     * 
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] getImageByte(String content) throws Exception
    {

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try
        {
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, REAL_SIZE, REAL_SIZE);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
            ImageIO.write(bufferedImage, "bmp", byteArray);
            return byteArray.toByteArray();
        }
        catch (Exception e)
        {
            throw new Exception("【QRCodeUtils-getImageByte】生成二维码异常, content=" + content, e);
        }
    }

    /**
     * 生成二维码字节流
     * 
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] getImageByteWithCut(String content) throws Exception
    {

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try
        {
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, SIZE, SIZE);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
            ImageIO.write(bufferedImage, "bmp", byteArray);
            return cut(byteArray.toByteArray(), "bmp", (SIZE - REAL_SIZE) / 2, (SIZE - REAL_SIZE) / 2, REAL_SIZE, REAL_SIZE);
        }
        catch (Exception e)
        {
            throw new Exception("【QRCodeUtils-getImageByte】生成二维码异常,content=" + content, e);
        }
    }

    /**
     * 生成二维码文件
     * 
     * @param content
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] getImageFile(String content, String path) throws Exception
    {

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try
        {
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, SIZE, SIZE);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
            ImageIO.write(bufferedImage, "bmp", new File(path));
        }
        catch (Exception e)
        {
            throw new Exception("【QRCodeUtils-getImageFile】生成二维码异常, content=" + content + ",path=" + path, e);
        }
        return byteArray.toByteArray();
    }
    
	/**
	 * 解析二维码中包含的内容
	 * @param imgPath 二维码图片路径
	 * @return
	 */
	public static String decoderQRCodeFile(String imgPath) {
		// QRCode 二维码图片的文件
		//String filePath = "D://zxing.png";
		String url = null;
		BufferedImage image;
		try {
			image = ImageIO.read(new File(imgPath));
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
			JSONObject content = JSONObject.parseObject(result.getText());
			System.out.println("图片中内容：  ");
			System.out.println("author： " + content.getString("author"));
			System.out.println("zxing：  " + content.getString("zxing"));
			System.out.println("图片中格式：  ");
			System.out.println("encode： " + result.getBarcodeFormat());
			url = content.getString("zxing");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return url;
	}
    
	public static byte[] cut(byte[] inBytes, String lastdir, int x, int y, int width, int height) throws Exception
    {
        ImageInputStream iis = null;
        InputStream inputstream = null;
        try
        {
            inputstream = new ByteArrayInputStream(inBytes);

            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(lastdir);
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(inputstream);
            reader.setInput(iis, true);

            ImageReadParam param = reader.getDefaultReadParam();

            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);

            BufferedImage bi = reader.read(0, param);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bi, lastdir, bos);
            return bos.toByteArray();
        }
        catch (IOException e)
        {
            throw new Exception("【PicUtil-cut】IO异常 inBytes.length=" + inBytes.length + ",lastdir=" + lastdir + ",x=" + x + ",y=" + y + ",width=" + width + ",height=" + height,e);
        }
        finally
        {
        	if(inputstream!=null){
        		inputstream.close();
        	}
        	if(iis!=null){
        		iis.close();
        	}
        }

    }
	
    public static void main(String[] arg) throws Exception
    {
        FileUtil.getFileFromBytes(getImageByte("http://caipiao.taobao.com/?spm=1.1000386.221827.9.A9jHID&ad_id=&am_id=&cm_id=&pm_id="), "d:/444.bmp");
    }

}
