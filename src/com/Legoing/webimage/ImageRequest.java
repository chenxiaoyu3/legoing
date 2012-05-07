package com.Legoing.webimage;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;

public class ImageRequest {
	public static final int REQUEST_SIZE_ORIGINAL = -1;

	public static interface Observer {
		public void onImageLoaded(String url, Bitmap bitmap);

		public void onImageLoadFailed(String url);
	}

	public String mUrl = "";
	public int expectHeight = REQUEST_SIZE_ORIGINAL;
	public int expectWidth = REQUEST_SIZE_ORIGINAL;
	
	private WeakReference<Observer> mObserverRef = null;

	public ImageRequest(String url, Observer obs) {
		mUrl = url;
		mObserverRef = new WeakReference<Observer>(obs);
	}

	public ImageRequest(String url, int width, int height, Observer obs) {
		mUrl = url;
		expectWidth = width;
		expectHeight = height;
		mObserverRef = new WeakReference<Observer>(obs);
	}

	public String encodeKey() {
		return expectWidth + "," + expectHeight + "," + mUrl;
	}

	public boolean isSuiteWith(ImageRequest another) {
		if (!mUrl.equals(another.mUrl)) {
			return false;
		}
		if (expectWidth == REQUEST_SIZE_ORIGINAL
				&& expectHeight == REQUEST_SIZE_ORIGINAL) {
			return true;
		}
		return expectWidth >= another.expectWidth
				&& expectHeight >= another.expectHeight;
	}

	public Observer getObserver() {
		return mObserverRef.get();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (o instanceof ImageRequest) {
			final ImageRequest r = ((ImageRequest) o);
			boolean equal = r.mUrl.equals(mUrl)
					&& r.expectHeight == expectHeight
					&& r.expectWidth == expectWidth;
			if (!equal) {
				return false;
			} else {
				Observer myObserver = mObserverRef.get();
				Observer otherObserver = r.getObserver();
				if (myObserver == otherObserver) {
					return true;
				} else if (myObserver == null || otherObserver == null) {
					return false;
				} else {
					return myObserver.equals(otherObserver);
				}
			}
		}
		return false;
	}
}
