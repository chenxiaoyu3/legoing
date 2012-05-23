package com.Legoing.UserControls;

import com.Legoing.Activity_Base;
import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.Model.UserSignStateChangedListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActionBar extends RelativeLayout implements UserSignStateChangedListener{

    Resources mRes;
	Context mContext;
	TextView mTextView_Title;
	TextView mTextView_Account;
	Drawable mDrawable_Offline, mDrawable_OnLine;
	ImageView mImageView_Title;
	AlertDialog mAlertDialog;
	public ActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 5:44:56 PM
		LayoutInflater.from(context).inflate(R.layout.actionbar, this,true);
		this.mContext = context;
		this.mRes = context.getResources();
		this.mTextView_Title = (TextView)findViewById(R.id.textView_actionBar_title);
		this.mTextView_Account = (TextView)findViewById(R.id.textView_actionbar_account);
		this.mImageView_Title = (ImageView)findViewById(R.id.imageView_actionBar_title);
		this.mDrawable_Offline = mRes.getDrawable(R.drawable.account_offline);
		this.mDrawable_Offline.setBounds(0, 0, mDrawable_Offline.getIntrinsicWidth(), mDrawable_Offline.getIntrinsicHeight());
		this.mDrawable_OnLine = mRes.getDrawable(R.drawable.account_online2);
		mDrawable_OnLine.setBounds(0,0,mDrawable_OnLine.getIntrinsicWidth(),mDrawable_OnLine.getIntrinsicHeight());
		
		//获取自定义的属性
		TypedArray at = context.obtainStyledAttributes(attrs, R.styleable.ActionBar, 0,0);
		String defTitle = at.getString(R.styleable.ActionBar_titleText);
		if (defTitle != null) {
            setTitle(defTitle);
        }
		initial();
	}
	
	private void initial()
	{
	    mTextView_Account.setOnClickListener(mOnClickListener_Signin);
	    if (StaticOverall.curUser == null) {
            setOffLine();
        }else {
            setOnLine();
        }
	    ((Activity_Base)mContext).addUserSignChangedListener(this);
	}
	private void setOnLine()
	{
	    mTextView_Account.setCompoundDrawables(mDrawable_OnLine, null, null, null);
	    mTextView_Account.setTextColor(mRes.getColor(R.color.greenyellow));
	    mTextView_Account.setText("");
	    
	}
	private void setOffLine()
	{
	    mTextView_Account.setCompoundDrawables(mDrawable_Offline, null, null, null);
	    mTextView_Account.setTextColor(mRes.getColor(R.color.darkgray));
	    mTextView_Account.setText(R.string.actionbar_login);
	}
	public void setTitle(CharSequence text)
	{
	    mImageView_Title.setImageDrawable(null);
		mTextView_Title.setText(text);
	}
	public void setTitle(Drawable drawable)
	{
	    mImageView_Title.setImageDrawable(drawable);
	    mTextView_Title.setText("");
	}
	
	private View.OnClickListener mOnClickListener_Signin = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 8:52:19 PM
            doSignInOrOff();
        }
    };
    
    private View.OnClickListener mClickListener_SigninOK = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 2:47:55 PM
            mAlertDialog.dismiss();
            if (StaticOverall.curUser == null) {
                ((Activity_Base)mContext).notifySign(false);
            }else {
                ((Activity_Base)mContext).notifySign(true);
            }

        }
    };
    @Override
    public void onUserSignStateChanged(boolean signed) {
        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 4:50:04 PM
        if (!signed) {
            setOffLine();
        }else {
            setOnLine();
        }
    }
    
    public void doSignInOrOff()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (StaticOverall.curUser == null) {
            Layout_Signin layout= new Layout_Signin(mContext);
            layout.setOKListener(mClickListener_SigninOK);
            builder.setView(layout);
        }else {
            builder.setTitle(R.string.msg_LogOff);
            builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 4:35:31 PM
                    StaticOverall.curUser = null;
                    setOffLine();
                    ((Activity_Base)mContext).notifySign(false);
                    mAlertDialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.CANCLE, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 4:36:45 PM
                    mAlertDialog.dismiss();
                }
            });
        }
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

}
