package com.Legoing.webimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
//TODO caculate self's size and pass to request
public class WebImageView extends ImageView implements ImageRequest.Observer {
	private String mImageUrl = "";
	private Drawable mDefaultDrawable = null;
	private Drawable mFailedDrawable = null;
	private boolean mAutoDownLoadImage = true;

	public static final int INITIAL = 0;
	public static final int DOWNLOADING = 1;
	public static final int FAILED = 2;
	public static final int SUCCEEDED = 3;

	private int mLoadingState = INITIAL;

	public WebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WebImageView(Context context) {
		super(context);
	}

	public void setImageAutoDownload(boolean auto) {
		mAutoDownLoadImage = auto;
	}

	public boolean isImageAutoDownload() {
		return mAutoDownLoadImage;
	}

	public int getDownloadingState() {
		return mLoadingState;
	}

	public boolean isImageDownloading() {
		return mLoadingState == DOWNLOADING;
	}

	public void setDefaultImageDrawable(Drawable defaultDrawable) {
		mDefaultDrawable = defaultDrawable;
		if (mLoadingState == INITIAL) {
			setImageDrawable(mDefaultDrawable);
		}
	}

	public void setFailedImageDrawable(Drawable failedDrawable) {
		mFailedDrawable = failedDrawable;
		if (mLoadingState == FAILED) {
			setImageDrawable(mFailedDrawable);
		}
	}

	public void setWebImageUrl(String url) {
		if (mImageUrl.equals(url)) {
			return;
		}
		mLoadingState = INITIAL;
		mImageUrl = url;
		if (mImageUrl == null) {
			mImageUrl = "";
		}
		setImageDrawable(mDefaultDrawable);
		if (mAutoDownLoadImage && !mImageUrl.equals("")) {
			startFetchImage();
		}
	}

	public void startFetchImage() {
		if (mImageUrl == null || mImageUrl.equals("")) {
			throw new RuntimeException("Imageurl is Empty");
		}
		mLoadingState = DOWNLOADING;
		WebImageLoader.instance(getContext()).requestImage(mImageUrl, this);
	}

	@Override
	public void onImageLoaded(String url, Bitmap bitmap) {
		if (url.equals(mImageUrl)) {
			mLoadingState = SUCCEEDED;
			setImageBitmap(bitmap);
		}
	}

	@Override
	public void onImageLoadFailed(String url) {
		if (url.equals(mImageUrl)) {
			mLoadingState = FAILED;
			setImageDrawable(mFailedDrawable);
		}
	}
}
