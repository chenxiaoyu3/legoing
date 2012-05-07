package com.Legoing.webimage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
//TODO may be a cycle recycle is better?
public class WebImageCache {
	private static final String TAG = "WebImageCache";
	private static final int HARD_CACHE_CAPACITY = 4 * 1024 * 1024;
	private static final String CACHE_DIR = "ImageChache";
	private File mCacheDir = null;
	private LinkedHashMap<String, BitmapSample> mHardCache = new HardCacheLinkedMap(
			HARD_CACHE_CAPACITY / 2, 0.75f, true);
	private ConcurrentHashMap<String, SoftReference<BitmapSample>> mSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<BitmapSample>>();

	private static WebImageCache mInstance = null;

	private static final long DISK_CACHE_SIZE = 10 * 1024 * 1024;
	private static final int CLEAR_PERCENT = 50;

	public static WebImageCache instance(Context context) {
		if (mInstance == null) {
			mInstance = new WebImageCache(context);
		}
		return mInstance;
	}

	private WebImageCache(Context context) {
		mCacheDir = new File(context.getCacheDir(), CACHE_DIR);
		if (!mCacheDir.exists() && !mCacheDir.mkdir()) {
			throw new RuntimeException("Cannot create Cache Dir: "
					+ mCacheDir.getAbsolutePath());
		}
	}

	public boolean putImageFile(String url, InputStream inputStream) {
		if (hasDiskCache(url)) {
			return true;
		}
		clearDiskCacheWhenSpaceRaiseto(CLEAR_PERCENT, DISK_CACHE_SIZE);
		String fileName = urlToFileName(url);
		File cacheFile = new File(mCacheDir, fileName);
		try {
			if (!cacheFile.createNewFile()) {
				Log.e(TAG, "already exist!" + url);
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "create Cache File failed!" + url);
			return false;
		}
		FileOutputStream os = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			os = new FileOutputStream(cacheFile);
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(os);

			byte[] bytes = new byte[1024];
			int count = 0;
			while ((count = bis.read(bytes)) != -1) {
				bos.write(bytes, 0, count);
			}
			bis.close();
			bos.close();
			os.close();
			return true;
		} catch (FileNotFoundException e) {
			Log.d(TAG, "file not Found Exception: " + url);
			return false;
		} catch (IOException e) {

			try {
				if (bos != null) {
					bos.close();
				}
				if (os != null) {
					os.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			os = null;
			bis = null;
			bos = null;
			Log.d(TAG, "io Exception: " + url);
			return false;
		}
	}

	public Bitmap getMemCacheBitmap(String url) {
		if (mHardCache.containsKey(url)) {
			BitmapSample sample = mHardCache.get(url);
			if (!sample.isSample) {
				return sample.bitmap;
			} else {
				mHardCache.remove(url);
			}
		}
		if (mSoftBitmapCache.containsKey(url)) {
			SoftReference<BitmapSample> ref = mSoftBitmapCache.get(url);
			BitmapSample sample = ref.get();
			if (sample == null || sample.isSample) {
				mSoftBitmapCache.remove(url);
				return null;
			} else {
				mHardCache.put(url, sample);
				return sample.bitmap;
			}
		}
		return null;
	}

	public Bitmap getMemCacheBitmap(String url, int expectWidth,
			int expectHeight) {

		if (expectWidth == ImageRequest.REQUEST_SIZE_ORIGINAL
				|| expectHeight == ImageRequest.REQUEST_SIZE_ORIGINAL) {
			return getMemCacheBitmap(url);
		}

		if (mHardCache.containsKey(url)) {
			BitmapSample sample = mHardCache.get(url);
			if (!sample.isSample || sample.width() >= expectWidth
					&& sample.height() >= expectHeight) {
				return sample.bitmap;
			} else {
				mHardCache.remove(url);
			}
		}

		if (mSoftBitmapCache.containsKey(url)) {
			SoftReference<BitmapSample> ref = mSoftBitmapCache.get(url);
			BitmapSample sample = ref.get();
			if (sample == null) {
				mSoftBitmapCache.remove(url);
				return null;
			} else {
				if (!sample.isSample || sample.width() >= expectWidth
						&& sample.height() >= expectHeight) {
					return sample.bitmap;
				} else {
					mSoftBitmapCache.remove(url);
					return null;
				}
			}
		}
		return null;
	}

	public Bitmap getDiskCacheBitmap(String url, int expectWidth,
			int expectHeight) {
		if (!hasDiskCache(url)) {
			return null;
		}
		File image = new File(mCacheDir, urlToFileName(url));
		InputStream is = null;
		boolean succeeded = false;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		boolean original = expectHeight == ImageRequest.REQUEST_SIZE_ORIGINAL
				|| expectHeight == ImageRequest.REQUEST_SIZE_ORIGINAL;
		try {
			is = new FileInputStream(image);
			succeeded = ImageUtil.getImageSize(is, opt);
			if (succeeded) {
				Bitmap bitmap = null;
				if (!original) {
					bitmap = ImageUtil.getBitmap(image, opt, expectWidth,
							expectHeight);
				} else {
					bitmap = ImageUtil.getBitmap(image, opt);
				}

				if (bitmap != null) {
					if (original) {
						mHardCache.put(url, new BitmapSample(bitmap, false));
					} else {
						mHardCache.put(url, new BitmapSample(bitmap, true));
					}
					return bitmap;
				} else {
					return null;
				}
			} else {
				Log.d(TAG, "not succeeded");
				return null;
			}
		} catch (FileNotFoundException e) {
			succeeded = false;
			Log.d(TAG, "File Not found");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					Log.e(TAG, "File close Exception" + e.getMessage());
				}
			}
		}
		return null;
	}

	public Bitmap getDiskCacheBitmap(String url) {
		if (!hasDiskCache(url)) {
			return null;
		}
		return getDiskCacheBitmap(url, ImageRequest.REQUEST_SIZE_ORIGINAL,
				ImageRequest.REQUEST_SIZE_ORIGINAL);
	}

	public boolean hasDiskCache(String url) {
		return new File(mCacheDir, urlToFileName(url)).exists();
	}

	private String urlToFileName(String url) {
		return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
	}

	private static class BitmapSample {
		public boolean isSample = true;
		public Bitmap bitmap;

		public BitmapSample(Bitmap bit, boolean sample) {
			isSample = sample;
			bitmap = bit;
		}

		public int height() {
			return bitmap.getHeight();
		}

		public int width() {
			return bitmap.getWidth();
		}
	}

	private void clearDiskCacheWhenSpaceRaiseto(int percent, long sizeInBytes) {
		File[] cacheFiles = mCacheDir.listFiles();
		long totalSize = 0;
		for (int i = 0; i < cacheFiles.length; ++i) {
			totalSize = cacheFiles[i].length();
		}
		if (totalSize >= sizeInBytes) {
			clearDiskCache(percent);
		}
	}

	private void clearDiskCache(int percent) {
		if (percent < 0) {
			percent = 0;
		} else if (percent > 100) {
			percent = 100;
		}
		File[] cacheFiles = mCacheDir.listFiles();
		Arrays.sort(cacheFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return (int) (f1.lastModified() - f2.lastModified());
			}
		});
		final int clearSize = (int) (cacheFiles.length * percent / 100.0f);
		for (int i = 0; i < clearSize; ++i) {
			cacheFiles[i].delete();
		}
	}

	private class HardCacheLinkedMap extends
			LinkedHashMap<String, BitmapSample> {
		private static final long serialVersionUID = 1L;

		public HardCacheLinkedMap(int i, float f, boolean b) {
			super(i, f, b);
		}

		protected synchronized boolean removeEldestEntry(
				java.util.Map.Entry<String, BitmapSample> eldest) {
			int totalsize = 0;
			Collection<BitmapSample> samples = values();
			for (BitmapSample sample : samples) {
				if (sample.bitmap != null) {
					final int width = sample.bitmap.getWidth();
					final int height = sample.bitmap.getHeight();
					totalsize += width * height * 4;
				}
			}
			if (totalsize > HARD_CACHE_CAPACITY) {
				mSoftBitmapCache.put(eldest.getKey(),
						new SoftReference<BitmapSample>(eldest.getValue()));
				return true;
			} else {
				return false;
			}
		}
	}
}
