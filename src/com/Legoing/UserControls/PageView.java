package com.Legoing.UserControls;


import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class PageView extends LinearLayout {

	private static final String LOG_TAG = PageView.class.getSimpleName();

    public interface OnPageViewChangeListener {

        void onPageChanged(PageView pagedView, int previousPage, int newPage);
        void onStartTracking(PageView pagedView);
        void onStopTracking(PageView pagedView);
        void onScroll(int offsetX);
        void onPageChangePreview(int newPage);
        
    }

    private static final int INVALID_PAGE = -1;
    private static final int MINIMUM_PAGE_CHANGE_VELOCITY = 500;
    private static final int VELOCITY_UNITS = 1000;
    private static final int FRAME_RATE = 1000 / 60;

    private final Handler mHandler = new Handler();

    private int mPageCount;
    private int mCurrentPage;
    private int mPreviewPage=-1;
    private int mTargetPage = INVALID_PAGE;
    private boolean mTouchScrollable = true;

    private int mPagingTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mPageSlop;

    private boolean mIsBeingDragged;

    private int mOffsetX = -1;
    private int mStartMotionX;
    private int mStartOffsetX;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private OnPageViewChangeListener mOnPageChangeListener;


//    private SparseArray<View> mActiveViews = new SparseArray<View>();
//    private Queue<View> mRecycler = new LinkedList<View>();

    public PageView(Context context) {
        this(context, null);
    }

    public PageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initPagedView();
        mGestureDetector = new GestureDetector(new YscrollDetector());
    }

    private void initPagedView() {

        final Context context = getContext();

        mScroller = new Scroller(context, new DecelerateInterpolator());

        final ViewConfiguration conf = ViewConfiguration.get(context);
        // getScaledPagingTouchSlop() only available in API Level 8
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
        mMaximumVelocity = conf.getScaledMaximumFlingVelocity();

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mMinimumVelocity = (int) (metrics.density * MINIMUM_PAGE_CHANGE_VELOCITY + 0.5f);
    }
    
    public void setTouchScrollable(boolean flag){
    	this.mTouchScrollable = flag;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	mPageCount = this.getChildCount();
    	for(int i=0;i<mPageCount;i++){
    		this.getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
    	}
    	
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	super.onLayout(changed, l, t, r, b);
//    	if(changed && this.getChildCount() != mPageCount || true){
	    	int width = r-l;
	    	int height = b-t;
	    	log("layout call ..... "+this.mCurrentPage+" height = "+height);
	        for(int i=0;i<mPageCount;i++){
	        	this.getChildAt(i).layout(l+(i-mCurrentPage)*width, 0, r+(i-mCurrentPage)*width, height);
	        }
//    	}
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPageSlop = (int) (w * 0.5);
        mOffsetX = getOffsetForPage(mCurrentPage);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

    	if (mGestureDetector.onTouchEvent(ev)) {
			// srcoll y
			return super.onInterceptTouchEvent(ev);
		}else {
			// srcoll x
			
		}
    	if(!mTouchScrollable){
    		return super.onInterceptTouchEvent(ev);
    	}
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true;
        }

        final int x = (int) ev.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartMotionX = x;
                mIsBeingDragged = !mScroller.isFinished();
                if (mIsBeingDragged) {
                    mScroller.forceFinished(true);
                    mHandler.removeCallbacks(mScrollerRunnable);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(x - mStartMotionX);
                if (xDiff > mPagingTouchSlop) {
                    mIsBeingDragged = true;
                    performStartTracking(x);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /*
                 * Release the drag
                 */
                mIsBeingDragged = false;
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	
    	if(!mTouchScrollable){
    		return super.onTouchEvent(ev);
    	}
        final int action = ev.getAction();
        final int x = (int) ev.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                    mHandler.removeCallbacks(mScrollerRunnable);
                }
                performStartTracking(x);
                break;

            case MotionEvent.ACTION_MOVE:
                // Scroll to follow the motion event
                final int newOffset = mStartOffsetX - (mStartMotionX - x);
                if (newOffset > 0 || newOffset < getOffsetForPage(mPageCount - 1)) {
                    mStartOffsetX = mOffsetX;
                    mStartMotionX = x;
                } else {
                    setOffsetX(newOffset);
                } 
                
//                performPageChangePreview(getActualCurrentPage() + changePage(x,false));
                
                

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                setOffsetX(mStartOffsetX - (mStartMotionX - x));

//                int direction = 0;
//
//                final int slop = mStartMotionX - x;
//                if (Math.abs(slop) > mPageSlop) {
//                    direction = (slop > 0) ? 1 : -1;
//                } else {
//                    mVelocityTracker.computeCurrentVelocity(VELOCITY_UNITS, mMaximumVelocity);
//                    final int initialVelocity = (int) mVelocityTracker.getXVelocity();
//                    if (Math.abs(initialVelocity) > mMinimumVelocity) {
//                        direction = (initialVelocity > 0) ? -1 : 1;
//                    }
//                }

                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onStopTracking(this);
                }

                smoothScrollToPage(getActualCurrentPage() + changePage(x,true));

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }

        return true;
    }
    
    private int changePage(int x,boolean byVelocity)
    {
    	int direction = 0;

        final int slop = mStartMotionX - x;
        if (Math.abs(slop) > mPageSlop) {
            direction = (slop > 0) ? 1 : -1;
        } else if (byVelocity){
            mVelocityTracker.computeCurrentVelocity(VELOCITY_UNITS, mMaximumVelocity);
            final int initialVelocity = (int) mVelocityTracker.getXVelocity();
            if (Math.abs(initialVelocity) > mMinimumVelocity) {
                direction = (initialVelocity > 0) ? -1 : 1;
            }
        }
        
        
        return direction;
    }

    public void setOnPageChangeListener(OnPageViewChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    private int getActualCurrentPage() {
        return mTargetPage != INVALID_PAGE ? mTargetPage : mCurrentPage;
    }

    public void smoothScrollToPage(int page) {
        scrollToPage(page, true);
    }

    public void smoothScrollToNext() {
        smoothScrollToPage(getActualCurrentPage() + 1);
    }

    public void smoothScrollToPrevious() {
        smoothScrollToPage(getActualCurrentPage() - 1);
    }

    public void scrollToPage(int page) {
        scrollToPage(page, false);
    }

    public void scrollToNext() {
        scrollToPage(getActualCurrentPage() + 1);
    }

    public void scrollToPrevious() {
        scrollToPage(getActualCurrentPage() - 1);
    }

    private void scrollToPage(int page, boolean animated) {

        page = Math.max(0, Math.min(page, mPageCount - 1));

        final int targetOffset = getOffsetForPage(page);

        final int dx = targetOffset - mOffsetX;
        if (dx == 0) {
            performPageChange(page);
            return;
        }

        if (animated) {
            mTargetPage = page;
            mScroller.startScroll(mOffsetX, 0, dx, 0);
            mHandler.post(mScrollerRunnable);
        } else {
            setOffsetX(targetOffset);
            performPageChange(page);
        }
    }

    private void setOffsetX(int offsetX) {
    	
        if (offsetX == mOffsetX) {
            return;
        }
        
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onScroll(offsetX);
        }
        
        final int leftAndRightOffset = offsetX - mOffsetX;
        for(int i=0;i<this.getChildCount();i++){
        	this.getChildAt(i).offsetLeftAndRight(leftAndRightOffset);
        }

        mOffsetX = offsetX;
        invalidate();
    }

    private int getOffsetForPage(int page) {
        return -(page * getWidth());
    }

    private int getPageForOffset(int offset) {
        return -offset / getWidth();
    }

    private void performStartTracking(int startMotionX) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onStartTracking(this);
        }
        mStartMotionX = startMotionX;
        mStartOffsetX = mOffsetX;
    }

    private void performPageChange(int newPage) {
        if (mCurrentPage != newPage) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageChanged(this, mCurrentPage, newPage);
            }
            mCurrentPage = newPage;
            mPreviewPage = -1;
        }
    }
    
    private void performPageChangePreview(int newPage)
    {
    	if (mPreviewPage==-1)
    		mPreviewPage=mCurrentPage;
    	if (mPreviewPage != newPage) {
	    	if (mOnPageChangeListener != null) {
	            mOnPageChangeListener.onPageChangePreview(newPage);
	        }
	    	mPreviewPage = newPage;
    	}
    }

    static class SavedState extends BaseSavedState {

        int currentPage;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentPage);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.currentPage = mCurrentPage;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mCurrentPage = ss.currentPage;
    }


    private Runnable mScrollerRunnable = new Runnable() {
        @Override
        public void run() {
            final Scroller scroller = mScroller;
            if (!scroller.isFinished()) {
                scroller.computeScrollOffset();
                setOffsetX(scroller.getCurrX());
                mHandler.postDelayed(this, FRAME_RATE);
            } else {
                performPageChange(mTargetPage);
            }
        }
    };
    
    GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
	class YscrollDetector extends SimpleOnGestureListener
	{
		// true if scrolling Y 
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Mar 30, 2012 10:10:32 AM
			try {
				String ll = "dX="+distanceX + " dY="+distanceY;
				float disY = Math.abs(distanceY);
				float disX = Math.abs(distanceX);
				if (disY - disX > 0 ) {
					ll = "YYY s " + ll;
					//log(ll);
					return true;
				}else {
					ll = "X scroll " + ll;
					//log(ll);
					return false;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}
    
    
    private static final String TAG = "PageView";
    private static final void log(String text){
    	Log.v(TAG, text);
    }
	

}
