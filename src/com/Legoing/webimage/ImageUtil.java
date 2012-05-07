package com.Legoing.webimage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageUtil {
	private static final String TAG = "ImageUtil";

	private static final boolean DEBUG = true;

	/**
	 * get image size
	 * 
	 * @param is
	 *            - InputStream
	 * @param options
	 *            as output (outWidht and outHeight)
	 * @return True if succeeded, False else.
	 */
	public static boolean getImageSize(InputStream is,
			BitmapFactory.Options options) {
		if (DEBUG) {
			Log.d(TAG, "getImageSize");
		}

		boolean succeeded = false;
		if (is != null && options != null) {
			// get size of decoded image, then calculate sample size
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, options);
			if (options.outWidth > 0 && options.outHeight > 0) {
				if (DEBUG)
					Log.d(TAG, "outWidth = [" + options.outWidth
							+ "], outHeight = [" + options.outHeight + "]");
				succeeded = true;
			}
		}
		if (DEBUG) {
			Log.v(TAG, "getImageSize/out - [" + succeeded + "]");
		}
		return succeeded;
	}

	public static Bitmap getBitmap(File file, BitmapFactory.Options options) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			return getBitmap(is, options);
		} catch (Exception e) {
			if(is!=null){
				try {
					is.close();
				} catch (IOException e1) {
					is = null;
				}
			}
			return null;
		}
	}

	public static Bitmap getBitmap(File file, BitmapFactory.Options options, int expectWidht, int expectHeight) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			return getBitmap(is, options, expectWidht, expectHeight);
		} catch (Exception e) {
			if(is!=null){
				try {
					is.close();
				} catch (IOException e1) {
					is = null;
				}
			}
			return null;
		}
	}
	/**
	 * get Bitmap object
	 * 
	 * @param is
	 *            - InputStream
	 * @param options
	 *            - options as input
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap getBitmap(InputStream is, BitmapFactory.Options options)
			throws OutOfMemoryError {
		return getBitmap(is, options, options.outWidth, options.outHeight);
	}

	public static Bitmap getBitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	public static Bitmap getBitmap(InputStream is,
			BitmapFactory.Options options, int expectWidth, int expectHeight) {

		options.inJustDecodeBounds = false;
		options.inSampleSize = computeSampleSize(options.outWidth,
				options.outHeight, expectWidth, expectHeight);
		return BitmapFactory.decodeStream(is, null, options);
	}

	/**
	 * calculate the sample size
	 * 
	 * @param outWidth
	 * @param outHeight
	 * @param expectWidth
	 * @param expectHeight
	 * @return
	 */
	public static int computeSampleSize(int outWidth, int outHeight,
			int expectWidth, int expectHeight) {
		if (DEBUG) {
			Log.d(TAG, "calculateSampleSize()" + "<in>" + ": outWidth = "
					+ outWidth + ": outHeight = " + outHeight
					+ ": expectWidth = " + expectWidth + ": expectHeight = "
					+ expectHeight);
		}

		int initialSize = computeInitialSampleSize(outWidth, outHeight,
				expectWidth, expectHeight);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		if (DEBUG) {
			Log.d(TAG, "calculateSampleSize()" + "<out>" + ": roundedSize = "
					+ roundedSize);
		}

		return roundedSize;
	}

	/**
	 * calculate the initial sample size
	 * 
	 * @param outWidth
	 * @param outHeight
	 * @param expectWidth
	 * @param expectHeight
	 * @return
	 */
	private static int computeInitialSampleSize(int outWidth, int outHeight,
			int expectWidth, int expectHeight) {
		if (DEBUG) {
			Log.d(TAG, "computeInitialSampleSize()" + "<in>" + ": outWidth = "
					+ outWidth + ": outHeight = " + outHeight
					+ ": expectWidth = " + expectWidth + ": expectHeight = "
					+ expectHeight);
		}

		double w = outWidth;
		double h = outHeight;
		int res = (int) Math.min(w / expectWidth, h / expectHeight);
		Log.d(TAG, "computeInitialSampleSize() result:" + res);
		return res;
	}
}
