package com.Legoing.webimage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class WebImageLoader {
	private static final String TAG = "WebImageLoader";
	private static WebImageLoader mInstance = null;
	private Context mContext = null;
	private HashMap<String, List<ImageRequest>> mRequests = new HashMap<String, List<ImageRequest>>();
	private HashSet<ImageRequest> mDecodingRequests = new HashSet<ImageRequest>();
	private HashSet<String> mDownLoadingUrls = new HashSet<String>();
	private HttpWrapper mHttpWraper = null;

	public static WebImageLoader instance(Context context) {
		if (mInstance == null) {
			mInstance = new WebImageLoader(context);
		}
		return mInstance;
	}

	private WebImageLoader(Context context) {
		mContext = context.getApplicationContext();
	}

	public void requestImage(String url, ImageRequest.Observer obs) {
		if (obs == null) {
			throw new RuntimeException("Observer Param Cannot be empty");
		}
		WebImageCache cache = WebImageCache.instance(mContext);
		Bitmap bitmap = cache.getMemCacheBitmap(url);
		if (bitmap != null) {
			obs.onImageLoaded(url, bitmap);
		} else {
			requestImage(new ImageRequest(url, obs));
		}
	}

	public void setHttpWrapper(HttpWrapper wrapper) {
		mHttpWraper = wrapper;
	}

	public void requestImage(ImageRequest request) {
		WebImageCache cache = WebImageCache.instance(mContext);
		Bitmap bitmap = cache.getMemCacheBitmap(request.mUrl,
				request.expectWidth, request.expectHeight);// check memcache
															// first
		if (bitmap != null) {
			ImageRequest.Observer obs = request.getObserver();
			if (obs != null) {
				obs.onImageLoaded(request.mUrl, bitmap);
			}
		} else {
			if (!isRequestProcessing(request)) {
				addRequest(request.mUrl, request);
				startRequest(request);
			}
		}
	}

	private void startRequest(ImageRequest request) {
		WebImageCache cache = WebImageCache.instance(mContext);
		if (cache.hasDiskCache(request.mUrl)) {
			startFetchingImageFromDisk(request);
		} else if (!isImageDownloading(request.mUrl)) {
			startDownloadingImage(request.mUrl);
		} else {
			// do nothing
		}
	}

	public void requestImage(String url, int expectWidth, int expectHeight,
			ImageRequest.Observer obs) {
		ImageRequest request = new ImageRequest(url, expectWidth, expectHeight,
				obs);
		requestImage(request);
	}

	public boolean isRequestProcessing(ImageRequest req) {

		List<ImageRequest> reqs = mRequests.get(req.mUrl);
		if (reqs == null || reqs.size() == 0) {
			return false;
		} else {
			return reqs.contains(req);
		}
	}

	public boolean isImageDownloading(String url) {
		return mDownLoadingUrls.contains(url);
	}

	private void startDownloadingImage(String url) {
		mDownLoadingUrls.add(url);
		if (mHttpWraper == null) {
			mHttpWraper = new DefaultHttpWrapper();
		}
		ImageDownloadThreadPool.execute(new ImageDownloadRunnable(url));
	}

	private void addRequest(String url, ImageRequest req) {
		List<ImageRequest> reqs = mRequests.get(url);
		if (reqs == null) {
			reqs = new ArrayList<ImageRequest>();
			mRequests.put(url, reqs);
		}
		reqs.add(req);
	}

	private void imageDownloadedToCache(String url) {
		mDownLoadingUrls.remove(url);
		removeUnAvailableRequests(url);
		startFetchingImageFromDisk(url);
	}

	private void startFetchingImageFromDisk(String url) {
		List<ImageRequest> reqs = mRequests.get(url);
		if (reqs != null && reqs.size() > 0) {
			for (ImageRequest req : reqs) {
				startFetchingImageFromDisk(req);
			}
		} else {
			// donothing
		}
	}

	private void startFetchingImageFromDisk(ImageRequest req) {
		if (isDecoding(req)) {
			return;
		}
		mDecodingRequests.add(req);
		mDecodePoolExecutor.execute(new ImageDecodeRunnable(req));
	}

	private void removeUnAvailableRequests(String url) {
		List<ImageRequest> reqs = mRequests.get(url);
		if (reqs != null && reqs.size() > 0) {
			List<ImageRequest> tobeRemove = new ArrayList<ImageRequest>();
			for (ImageRequest req : reqs) {
				if (req.getObserver() == null) {
					tobeRemove.add(req);
				}
			}
			removeRequests(tobeRemove);
		}
	}

	private void imageDecodedFromDisk(ImageRequest req, Bitmap bitmap) {
		if (bitmap == null) {
			notifyImageLoadFailed(req.mUrl);
		} else {
			notifyImageLoaded(req, bitmap);
		}
	}

	private void notifyImageLoaded(ImageRequest request, Bitmap bitmap) {
		List<ImageRequest> requests = mRequests.get(request.mUrl);
		if (requests == null || requests.size() == 0) {
			return;
		}
		List<ImageRequest> tobeRemove = new ArrayList<ImageRequest>();
		for (ImageRequest req : requests) {
			if (request.isSuiteWith(req)) {
				tobeRemove.add(req);
				if (req.getObserver() != null) {
					req.getObserver().onImageLoaded(request.mUrl, bitmap);
				} else {
				}
			} else {
				if (req.getObserver() == null) {
					tobeRemove.add(req);
				}
			}
		}
		removeRequests(tobeRemove);
	}

	private void notifyImageLoadFailed(String url) {
		List<ImageRequest> requests = mRequests.get(url);
		if (requests == null || requests.size() == 0) {
			return;
		}
		for (ImageRequest req : requests) {
			ImageRequest.Observer ob = req.getObserver();
			if (ob != null) {
				ob.onImageLoadFailed(url);
			}
		}
		removeAllRequests(url);
	}

	private void removeRequests(List<ImageRequest> reqs) {
		for (ImageRequest req : reqs) {
			mRequests.remove(req);
		}
	}

	private void removeAllRequests(String url) {
		mRequests.remove(url);
	}

	public boolean isDownLoading(String url) {
		return mDownLoadingUrls.contains(url);
	}

	public boolean isDecoding(ImageRequest request) {
		for (ImageRequest req : mDecodingRequests) {
			if (req.isSuiteWith(request)) {
				return true;
			}
		}
		return false;
	}

	private class ImageDecodeRunnable implements Runnable{
		private final ImageRequest mRequest;
		public  ImageDecodeRunnable(ImageRequest req){
			mRequest = req;
		};
		public void run() {
			WebImageCache cache = WebImageCache.instance(mContext);
			Bitmap bitmap = cache.getDiskCacheBitmap(mRequest.mUrl, mRequest.expectWidth,
					mRequest.expectHeight);
			MessageHolder holder = new MessageHolder();
			holder.mBitmap = bitmap;
			holder.mRequest = mRequest;
			Message msg = mHandler.obtainMessage();
			msg.what = MSG_DECODE_COMPLETE;
			msg.obj = holder;
			mHandler.sendMessage(msg);
		}
		
	}
	private class ImageDownloadRunnable implements Runnable{
		private final String mUrl;
		public  ImageDownloadRunnable(String url){
			mUrl = url;
		}
		public void run() {
			WebImageCache cache = WebImageCache.instance(mContext);
			try {
				InputStream is = mHttpWraper.getInputStream(mUrl);
				if(is==null){
					Message msg = mHandler.obtainMessage();
					msg.what = MSG_DOWNLOAD_FAILED;
					msg.obj = mUrl;
					mHandler.sendMessage(msg);
					return;
				}
				if (!cache.putImageFile(mUrl, is)) {
					if (is != null) {
						is.close();
					}
					Message msg = mHandler.obtainMessage();
					msg.what = MSG_DOWNLOAD_FAILED;
					msg.obj = mUrl;
					mHandler.sendMessage(msg);
					return;
				} else {
					is.close();
					Message msg = mHandler.obtainMessage();
					msg.what = MSG_DOWNLOAD_SUCCESS;
					msg.obj = mUrl;
					mHandler.sendMessage(msg);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_DOWNLOAD_FAILED;
				msg.obj = mUrl;
				mHandler.sendMessage(msg);
			}	
		}
	}
	
	//for msg transfer
	private static final int MSG_DOWNLOAD_SUCCESS = 0;
	private static final int MSG_DOWNLOAD_FAILED = 1;
	private static final int MSG_DECODE_COMPLETE = 2;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_DOWNLOAD_SUCCESS: {
				String url = (String) msg.obj;
				imageDownloadedToCache(url);
				break;
			}
			case MSG_DOWNLOAD_FAILED: {
				String url = (String) msg.obj;
				notifyImageLoadFailed(url);
				break;
			}
			case MSG_DECODE_COMPLETE: {
				MessageHolder holder = (MessageHolder) msg.obj;
				imageDecodedFromDisk(holder.mRequest, holder.mBitmap);
				break;
			}
			default:
				break;
			}
		}

	};
	private static class MessageHolder {
		public ImageRequest mRequest;
		public Bitmap mBitmap;
	}
	
	private ExecutorService mDecodePoolExecutor = Executors.newSingleThreadExecutor();
}
