package com.Legoing;


import com.Legoing.R;
import com.Legoing.ItemDef.ComponentItem;
import com.Legoing.ItemDef.ComponentList;
import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.LegoItemEvaluation;
import com.Legoing.ItemDef.LegoingUserEvaluation;
import com.Legoing.ItemDef.Set;
import com.Legoing.UserControls.ActionBar;
import com.Legoing.UserControls.Layout_Comment;
import com.Legoing.UserControls.PageView;
import com.Legoing.UserControls.UCtrl_AddToMyLego;
import com.Legoing.UserControls.UCtrl_LegoComponentListItem;
import com.Legoing.webimage.ImageRequest;
import com.Legoing.webimage.WebImageLoader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_LegoItemInfo extends Activity_Base {

	Context context;
	ActionBar actionBar;
	ImageView imageView_LegoItemPic;
	TextView /*textView_LegoItemName,*/ textView_ItemYear, textView_ItemCata, textView_PartsCount, textView_MinifigsCount;
	TextView textView_Tip;
	ImageButton imageButton_AddFavorite;
	ExpandableListView expandableListView_0;
	ProgressBar progressBar_LoadCompont, progressBar_LoadPic;
	RatingBar ratingBar;
	TextView textView_PageTitle_ComponentList, textView_PageTitle_Comment;
	PageView pageView;
	ListView listView_Comment;
	
	TreeViewAdapter_Components treeViewAdapter_ListComponent;
	Async_RequestData requestDate;
	ImageRequest.Observer imageObserver;
	Handler handlerUI;
	GestureDetector gestureDetector;
	OnGestureListener onGestureListener;
	static final int MSG_COMPONENTLIST_LOAD_SUCCESS = 0;
	static final int MSG_PIC_LOAD_SUCCESS = 1;
	static final int MSG_EVALUATION_LOAD_SUCCESS = 2;
	static final int MSG_COMPONENTLIST_LOAD_NOT_FOUND = -1;
	static final int MSG_COMPONENTLIST_LOAD_OTHER = -4;
	static final int MSG_ADD_COMMENT_SUCCESS = 3;

	LegoItem value;
	LegoItemEvaluation evaluation;
	boolean isCurBigImg = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lego_item_info);
		String keyString = getIntent().getExtras().getString(EXTRA_GOLBALKEY);
		value = (LegoItem)StaticOverall.fetchGlobalData(keyString);
		initialID();
		initial();

		textView_PageTitle_ComponentList.getBackground().setLevel(1);
		requestDate = new Async_RequestData();
		requestDate.execute(new Object());
       
        progressBar_LoadPic.setVisibility(View.VISIBLE);
        WebImageLoader.instance(context).requestImage(value.getImgLink(), imageObserver);
//        actionBar.postDelayed(new Runnable() {
//            
//            @Override
//            public void run() {
//                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 7, 2012 9:30:52 PM
//                openOptionsMenu();
//            }
//        }, 50000);
        
	}
	@Override
	protected void onNewIntent(Intent intent) {
	    // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 17, 2012 3:47:16 PM
	   
	    super.onNewIntent(intent);
	}

	void initialID() {
	    actionBar = (ActionBar)findViewById(R.id.actionBar_itemInfo);
		imageView_LegoItemPic = (ImageView) findViewById(R.id.imageView_ItemPic);
//		textView_LegoItemName = (TextView) findViewById(R.id.textView_itemInfo_ItemName);
		textView_ItemYear = (TextView) findViewById(R.id.textView_itemInfo_Year);
		textView_ItemCata = (TextView) findViewById(R.id.textView_itemInfo_Catalog);
		textView_PartsCount= (TextView) findViewById(R.id.textView_itemInfo_Parts);
		textView_MinifigsCount = (TextView) findViewById(R.id.textView_itemInfo_Minifigs);
		imageButton_AddFavorite = (ImageButton) findViewById(R.id.imageButton_ItemInfo_AddFavorite);
		expandableListView_0 = (ExpandableListView) findViewById(R.id.expandableListView_ItemInfo_0);
		progressBar_LoadCompont = (ProgressBar)findViewById(R.id.progressBar_legoItemInfo_loadComponent);
		progressBar_LoadPic = (ProgressBar)findViewById(R.id.progressBar_legoItemInfo_loadPic);
		textView_Tip = (TextView)findViewById(R.id.textView_legoItemInfo_tip);
		ratingBar = (RatingBar)findViewById(R.id.ratingBar_legoItemInfo);
		textView_PageTitle_ComponentList = (TextView)findViewById(R.id.layout_legoItemInfo_pageComponent);
		textView_PageTitle_Comment = (TextView)findViewById(R.id.layout_legoItemInfo_pageComment);
		pageView = (PageView)findViewById(R.id.pageView_legoItemInfo);
		listView_Comment = (ListView)findViewById(R.id.listView_legoItemInfo_comment);
		
	}

	OnClickListener itemPicClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO , Chen Xiaoyu Cxy, 2011-11-9 1:25:30

			// Toast toast = Toast.makeText(Activity_LegoItemInfo.this,
			// legoItem.id, Toast.LENGTH_SHORT);
			// toast.show();
			// Matrix mt = imageView_LegoItemPic.getImageMatrix();
			// mt = new Matrix(mt);
			// mt.postScale(3, 3);
			// // imageView_LegoItemPic.setImageMatrix(mt);
			// // imageView_LegoItemPic.invalidate();
			//
			// Bitmap bm =
			// BitmapFactory.decodeResource(Activity_LegoItemInfo.this.getResources(),
			// R.drawable.lego_set);
			// Bitmap resizeBitmap = Bitmap.createBitmap(bm, 0, 0,
			// bm.getWidth(), bm.getHeight(), mt, true);
			// imageView_LegoItemPic.setImageBitmap(resizeBitmap);
		    
		    
		    String urlStr = "";
		    if (isCurBigImg) {
                urlStr = value.getImgLink();
            }else {
                urlStr = value.getBigImgLink();
            }
		    
		    imageView_LegoItemPic.setClickable(false);
		    progressBar_LoadPic.setVisibility(View.VISIBLE);
		    WebImageLoader.instance(context).requestImage(urlStr, imageObserver);
		}
	};
	OnClickListener addFavoriteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// _TODO , Chen Xiaoyu Cxy, 2011-11-25 8:09:23
		    showAddToMylegoDialog();
		}
	};

	void initial() {
	    String name = value.getName();
	    SpannableStringBuilder builder = new SpannableStringBuilder(name);
	    builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_theme)),
	            0, name.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
	    actionBar.setTitle(builder);
		textView_ItemYear.setText(value.getString_Year());
		textView_ItemCata.setText(value.getCategory());
		
		imageView_LegoItemPic.setOnClickListener(itemPicClickListener);
		imageButton_AddFavorite.setOnClickListener(addFavoriteListener);
		textView_PageTitle_ComponentList.setText(R.string.itemInfo_Detail);
		textView_PageTitle_Comment.setText(R.string.itemInfo_Comment);
		if (value.getTYPE() == LegoItem.ITEM_TYPE_SET) {
			textView_PartsCount.setText(String.valueOf( ((Set)value).getPartQuantity() ) );
			textView_MinifigsCount.setText(String.valueOf( ((Set)value).getMinifigQuantity() ) );
		}

		treeViewAdapter_ListComponent = new TreeViewAdapter_Components(this);
		expandableListView_0.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 5:50:46 PM
                if (v instanceof UCtrl_LegoComponentListItem) {
                    UCtrl_LegoComponentListItem layout = (UCtrl_LegoComponentListItem)v;
                    ComponentItem comp = layout.getValue();
                    LegoItem item = comp.getItemInfo();
                    Activity_LegoItemInfo.this.startLegoItemInfoActivity(item);
                }
                
                return false;
            }
        });
        imageObserver = new ImageRequest.Observer() {
                    
            @Override
            public void onImageLoaded(String url, Bitmap bitmap) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 8:26:10 PM
                isCurBigImg = !isCurBigImg;
                imageView_LegoItemPic.setImageBitmap(bitmap);
                imageView_LegoItemPic.setClickable(true);
                progressBar_LoadPic.setVisibility(View.INVISIBLE);
            }
            
            @Override
            public void onImageLoadFailed(String url) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 8:26:10 PM
                imageView_LegoItemPic.setClickable(true);
                progressBar_LoadPic.setVisibility(View.INVISIBLE);
            }
        };
        pageView.setOnPageChangeListener(new PageView.OnPageViewChangeListener() {
            
            @Override
            public void onStopTracking(PageView pagedView) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:14:52 PM
                
            }
            
            @Override
            public void onStartTracking(PageView pagedView) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:14:52 PM
                
            }
            
            @Override
            public void onScroll(int offsetX) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:14:52 PM
                
            }
            
            @Override
            public void onPageChanged(PageView pagedView, int previousPage, int newPage) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:14:52 PM
                textView_PageTitle_Comment.getBackground().setLevel(newPage);
                textView_PageTitle_ComponentList.getBackground().setLevel((newPage+1)%2);
            }
            
            @Override
            public void onPageChangePreview(int newPage) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:14:52 PM
                
            }
        });
        textView_PageTitle_ComponentList.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:06:56 PM
                pageView.smoothScrollToPage(0);
                textView_PageTitle_Comment.getBackground().setLevel(0);
                textView_PageTitle_ComponentList.getBackground().setLevel(1);
            }
        });
        textView_PageTitle_Comment.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:11:10 PM
                pageView.smoothScrollToPage(1);
                textView_PageTitle_Comment.getBackground().setLevel(1);
                textView_PageTitle_ComponentList.getBackground().setLevel(0);
            }
        });

	}
	
	@Override
	public void onAttachedToWindow() {
	    // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 7, 2012 9:36:08 PM
	    super.onAttachedToWindow();
	    openOptionsMenu();
	}

	private void showAddToMylegoDialog()
	{
	    if (!isSignedIn()) {
            actionBar.doSignInOrOff();
            return;
        }
	    AlertDialog.Builder aDialogBuilder = new AlertDialog.Builder(
                context);
	    UCtrl_AddToMyLego addToMyLego = new UCtrl_AddToMyLego(context);
        addToMyLego.setValue(value);
        aDialogBuilder.setView(addToMyLego);
        AlertDialog dialog = aDialogBuilder.create();
        addToMyLego.setOuterDialog(dialog);
        dialog.show();
	}
	
	private void showAddCommentDialog()
	{
	    if (!isSignedIn()) {
            actionBar.doSignInOrOff();
            return;
        }
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    final View view = LayoutInflater.from(context).inflate(R.layout.add_comment, null);
        builder.setView(view);
	    builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 7, 2012 8:09:51 PM
                RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar_addComment);
                EditText editText = (EditText)view.findViewById(R.id.editText_addComment);
                float score = ratingBar.getRating();
                new Async_AddComment((int)(score * 20), editText.getText().toString()).execute(0);
            }
        });
	    builder.setNegativeButton(R.string.CANCLE, null);
	    
	    builder.show();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // _TODO , Chen Xiaoyu Cxy, 2012-3-1 下午2:46:43
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 1:58:01 PM
	    switch (item.getItemId()) {
            case R.id.menuItem_findMiss:
                startFindMissActivity(value);
                break;
            case R.id.menuItem_addToMyLego:
                showAddToMylegoDialog();
                break;
            case R.id.menuItem_comment:
                showAddCommentDialog();
                break;
            default:
                break;
        }
	    return super.onOptionsItemSelected(item);
	}
	
	class Async_RequestData extends AsyncTask<Object, Object, Object>
	{

	    @Override
	    protected void onPreExecute() {
	        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 1:34:58 PM
	        progressBar_LoadCompont.setVisibility(View.VISIBLE);
	        super.onPreExecute();
	    }
	    /**
	     * param: isLoadPic, 
	     */
        @Override
        protected Object doInBackground(Object... params) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 1:17:46 PM
           
            if (!value.isDetail()) {
                publishProgress(MSG_COMPONENTLIST_LOAD_NOT_FOUND);
                return null;
            }
            
            ParamObj<LegoItemEvaluation> param = new DefaultParamObj<LegoItemEvaluation>();
            int ret = StaticOverall.netOperation.requestEvalutation(value.getNo(), param);
            switch (ret) {
                case 0:
                    publishProgress(MSG_EVALUATION_LOAD_SUCCESS,param.value);
                    break;
                case -1:
                    break;
                default:
                    publishProgress(MSG_NET_UTILS_OTHER,ret);
                    break;
                }
            ParamObj<ComponentList> paramObj = new DefaultParamObj<ComponentList>();
            ret = StaticOverall.netOperation.getComponentList(
                    value.getNo(), value.getType(), paramObj);
            switch (ret) {
            case 0:
                value.setComponentList(paramObj.value);
                publishProgress(MSG_COMPONENTLIST_LOAD_SUCCESS);
                break;
            case -1:
                publishProgress(MSG_COMPONENTLIST_LOAD_NOT_FOUND);
                break;
            default:
                publishProgress(MSG_NET_UTILS_OTHER,ret);
                break;
            }
            return null;
        }
        
        @Override
        protected void onProgressUpdate(Object... values) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 1:30:45 PM
            int msg = (Integer)values[0];
            switch (msg) {
                case MSG_COMPONENTLIST_LOAD_SUCCESS:
                    treeViewAdapter_ListComponent.setList(value
                            .getComponentList());
                    expandableListView_0
                            .setAdapter(treeViewAdapter_ListComponent);
                    break;
                case MSG_COMPONENTLIST_LOAD_NOT_FOUND:
                    textView_Tip.setText(R.string.msg_ComponentsNotFound);
                    Toast.makeText(Activity_LegoItemInfo.this,R.string.msg_ComponentsNotFound
                            , Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PIC_LOAD_SUCCESS:
                    progressBar_LoadPic.setVisibility(View.INVISIBLE);
                    imageView_LegoItemPic.setImageBitmap((Bitmap)values[1]);
                    break;
                case MSG_EVALUATION_LOAD_SUCCESS:
                    ratingBar.setVisibility(View.VISIBLE);
                    evaluation = (LegoItemEvaluation)values[1];
                    ratingBar.setRating((float)((float)evaluation.getItemTotalScore()/100.0*5));
                    listView_Comment.setAdapter(new AdapterComment());
                    break;
                default:
                    StaticOverall.netOperation.handleReturnValue((Integer)values[1]);
                    break;
            }
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Object result) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 1:34:16 PM
            progressBar_LoadCompont.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
        }
	    
	}
	
	class Async_AddComment extends AsyncTask<Object, Integer, Object>{

	    int score;
	    String desc;
	    public Async_AddComment(int score, String desc) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 7, 2012 8:03:27 PM
	        this.score = score;
	        this.desc = desc;
        }
        @Override
        protected Object doInBackground(Object... params) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 7, 2012 8:03:15 PM
            int serverRet = StaticOverall.netOperation.requestAddComment(value.getNo(), StaticOverall.curUser.getUserIndex(), score, desc);
            publishProgress(serverRet);
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 7, 2012 9:09:24 PM
            switch (values[0]) {
                case 0:
                    Toast.makeText(context, R.string.itemInfo_addCommentSuccess, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    StaticOverall.netOperation.handleReturnValue(values[0]);
                    break;
            }
            super.onProgressUpdate(values);
        }
	    
	}
	
	
	class AdapterComment extends BaseAdapter{

        @Override
        public int getCount() {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:59:18 PM
            return evaluation.getItemComment().size();
        }

        @Override
        public Object getItem(int position) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:59:18 PM
            return null;
        }

        @Override
        public long getItemId(int position) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:59:18 PM
            return 0;
        }
        @Override
        public boolean isEnabled(int position) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 3:11:18 PM
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:59:18 PM
            Layout_Comment layout = null;
            LegoingUserEvaluation eva = Activity_LegoItemInfo.this.evaluation.getItemComment().get(position);
            if (convertView == null) {
                layout = new Layout_Comment(context);
                layout.setValue(eva);
            }else {
                layout = (Layout_Comment)convertView;
                layout.setValue(eva);
            }
            return layout;
        }
	    
	}
	
//	class Request_Image extends AsyncTask<String, Object, Object>
//	{
//
//	    @Override
//	    protected void onPreExecute() {
//	        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 8:04:05 PM
//	        progressBar_LoadPic.setVisibility(View.VISIBLE);
//	        super.onPreExecute();
//	    }
//        @Override
//        protected Object doInBackground(String... params) {
//            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 8:02:07 PM
//            
//            try {
//                Bitmap bit = StaticOverall.imageMagager.getNormalImage(value);
//                if (bit != null) {
//                    publishProgress(MSG_PIC_LOAD_SUCCESS,bit);
//                    
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        @Override
//        protected void onProgressUpdate(Object... values) {
//            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 8:03:09 PM
//            int msg = (Integer)values[0];
//            switch (msg) {
//                case MSG_PIC_LOAD_SUCCESS:
//                    progressBar_LoadPic.setVisibility(View.INVISIBLE);
//                    imageView_LegoItemPic.setImageBitmap((Bitmap)values[1]);
//                    break;
//                default:
//                    StaticOverall.netOperation.handleReturnValue((Integer)values[1]);
//                    break;
//            }
//            super.onProgressUpdate(values);
//        }
//        @Override
//        protected void onPostExecute(Object result) {
//            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 8:03:43 PM
//            progressBar_LoadPic.setVisibility(View.INVISIBLE);
//            super.onPostExecute(result);
//        }
//        @Override
//        protected void onCancelled() {
//            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 8:04:24 PM
//            progressBar_LoadPic.setVisibility(View.INVISIBLE);
//            super.onCancelled();
//        }
//	    
//	}
	
	
}
