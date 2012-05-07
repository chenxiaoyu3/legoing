package com.Legoing;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

import com.Legoing.ItemDef.LegoItem;
import com.Legoing.NetOperation.NetOperation;
import com.ant.liao.GifAction;
import com.ant.liao.GifDecoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class ImageMagager {
	static final String DIR_IMAGE_NORMAL = "Images_Normal";
	static final String DIR_IMAGE_THUMBNAIL = "Images_Thumbnail";

	private final int BUF_SIZE_IMAGE = 1024 * 500;

	NetOperation netOperation;

	public ImageMagager(NetOperation netOperation) {
		this.netOperation = netOperation;
	}

	private String toFileName(String itemNo, int type) {
		// String re = "";
		// switch (type) {
		// case TYPE_MINIFIG:
		// re = "MINIFIG";
		// break;
		// case TYPE_PART:
		// re = "PART";
		// break;
		// case TYPE_SET:
		// re = "SET";
		// break;
		// default:
		// re = "ERR";
		// break;
		// }
		return type + "_" + itemNo;
	}

	/**
	 * Chen Xiaoyu get a normal image, if not exist in memory, it will download
	 * from net
	 * 
	 * @param itemNo
	 * @param type
	 * @param imgLink
	 * @return null if failed
	 */
	public Bitmap getNormalImage(String itemNo, int type, IHaveImgLink imgLink) {
		Bitmap bitmap = openNormalImage(itemNo, type);
		if (bitmap == null) {
			Log.i(this.toString(), "Not at local, try download from net");
			try {
				byte[] data = netOperation.downloadBytes(imgLink
						.getImageLinks());
				
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				if (bitmap != null) {
					saveNormalImage(bitmap, itemNo, type);
				}
				//java.awt.
				return bitmap;
			} catch (MalformedURLException e) {
				// TODO , Cxy, Chen Xiaoyu, 2011-11-24 下午4:01:27
				e.printStackTrace();
				return null;
			}
		} else
			return bitmap;
		 
	}
	GifDecoder decoder;
	public Bitmap getNormalImage(LegoItem item) {
		if (item == null || item.getImgLink() == null || item.getImgLink().length() == 0) {
			return null;
		}
		Bitmap bitmap = openNormalImage(item.getNo(), item.getTYPE());
		if (bitmap == null) {
			Log.i(this.toString(), "Not at local, try download from net");
			try {
				byte[] data = netOperation.downloadBytes(item.getImgLink());
					
				if (data != null) {
					
					if (item.getImgLink().endsWith("gif")) {
						decoder = new GifDecoder(data, new GifAction() {
							
							@Override
							public void parseOk(boolean parseStatus, int frameIndex) {
								// TODO , Chen Xiaoyu Cxy, 2011-12-29 下午9:59:50
							}
						});
						decoder.run();
						bitmap = decoder.getImage();					
					}else {
						bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					}
					if (bitmap != null) {
						saveNormalImage(bitmap, item.getNo(), item.getTYPE());
					}
				}
			} catch (MalformedURLException e) {
				// TODO , Cxy, Chen Xiaoyu, 2011-11-24 下午4:01:27
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public void saveNormalImage(Bitmap bitmap, String itemNo, int type) {
		File normalDir = StaticOverall.getMainActivity().getDir(
				DIR_IMAGE_NORMAL, Context.MODE_PRIVATE);

		String fileName = toFileName(itemNo, type);
		File file = new File(normalDir.getAbsolutePath() + "/" + fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();

		} catch (Exception e) {
			// TODO , Cxy, 2011-11-14 下午2:26:23
			e.printStackTrace();
		}
	}

	public void clearImageFiles() {
		File normalDir = StaticOverall.getMainActivity().getDir(
				DIR_IMAGE_NORMAL, Context.MODE_PRIVATE);
		// normalDir.
		// normalDir.delete();
		delFolder(normalDir.getAbsolutePath());
	}

	/**
	 * 
	 * @param itemNo
	 * @param type
	 * @return null if 404
	 * @author Cxy Chen Xiaoyu
	 * 
	 */
	public Bitmap openNormalImage(String itemNo, int type) {
		File normalDir = StaticOverall.getMainActivity().getDir(
				DIR_IMAGE_NORMAL, Context.MODE_PRIVATE);
		String fileName = toFileName(itemNo, type);
		File file = new File(normalDir.getAbsolutePath() +"/"+ fileName);
		try {
			if (!file.exists()) {
				return null;
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[BUF_SIZE_IMAGE];
			int cnt = fis.read(buffer);
			Bitmap retBitmap = BitmapFactory.decodeByteArray(buffer, 0, cnt);
			return retBitmap;
		} catch (Exception e) {
			// TODO , Cxy, Chen Xiaoyu, 2011-11-22 下午8:55:04
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			boolean succ = myFilePath.delete(); // 删除空文件夹
			Log.e("Del Image", String.valueOf(succ));

		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();

		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

}
