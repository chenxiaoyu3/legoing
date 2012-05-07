
package com.Legoing.UserControls;

import com.Legoing.Activity_Base;
import com.Legoing.DefaultParamObj;
import com.Legoing.ParamObj;
import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ItemDef.LegoingUser;
import com.Legoing.Model.UserSignStateChangedListener;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Layout_Signin extends LinearLayout{

    Context mContext;

    EditText mEditText_Name, mEditText_Psd;

    Button mButton_Confirm, mButton_Cancle;
    View mView_Input, mView_Loading;

    private View.OnClickListener mOnClickListener_OK/*, OnClickListener_Cancle*/;

    public Layout_Signin(Context context) {
        super(context);
        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 9:04:30 PM
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.signin_layout, this, true);
        mEditText_Name = (EditText)findViewById(R.id.editText_signin_uName);
        mEditText_Psd = (EditText)findViewById(R.id.editText_signin_uPsd);
        mButton_Confirm = (Button)findViewById(R.id.button_signin_confirm);
        mButton_Cancle = (Button)findViewById(R.id.button_signin_cancle);
        mView_Input = findViewById(R.id.layout_signin_input);
        mView_Loading = findViewById(R.id.layout_signin_loading);
        this.mButton_Confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 11, 2012
                // 9:13:30 PM
                String name = mEditText_Name.getText().toString();
                if (name.trim().length() == 0) {
                    Toast.makeText(mContext, R.string.msg_EnterUserNamePlease, Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String psd = mEditText_Psd.getText().toString();
                if (psd.trim().length() == 0) {
                    Toast.makeText(mContext, R.string.msg_EnterUserPsdPlease, Toast.LENGTH_SHORT).show();
                    return;
                }
                new Async_RequestSignin().execute(name,psd);

            }
        });
        this.mButton_Cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 11, 2012
                // 9:14:57 PM
                if (mOnClickListener_OK != null) {
                    mOnClickListener_OK.onClick(null);
                }                
            }
        });
    }

    public void setOKListener(View.OnClickListener onClickListener) {
        this.mOnClickListener_OK = onClickListener;
    }

//    public void setCancleListener(View.OnClickListener onClickListener) {
//        this.mOnClickListener_Cancle = onClickListener;
//    }
    
    class Async_RequestSignin extends AsyncTask<String, Object, Object>
    {

        @Override
        protected void onPreExecute() {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 9:19:41 PM
            mView_Input.setVisibility(View.INVISIBLE);
            mView_Loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(String... params) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 9:12:53 PM
//            ParamObj<LegoingUser> paramObj = new DefaultParamObj<LegoingUser>();
//            int signRet = StaticOverall.netOperation.userVerify(params[0], params[1], paramObj);
//            if (signRet == 0) {
//                StaticOverall.curUser = paramObj.value;
//            }
            int ret = ((Activity_Base)mContext).doSignIn(params[0], params[1]);
            return ret;
        }
        @Override
        protected void onPostExecute(Object result) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 20, 2012 9:20:24 PM
            int res = (Integer)result;
            if (res == 0) {
                if (mOnClickListener_OK != null) {
                    mOnClickListener_OK.onClick(null);
                }
            }else if (res == -1) {
                Toast.makeText(mContext, R.string.msg_WrongUserNameOrPsd, Toast.LENGTH_LONG).show();
            }else {
                StaticOverall.netOperation.handleReturnValue(res);
                if (mOnClickListener_OK != null) {
                    mOnClickListener_OK.onClick(null);
                }
            }
            mView_Input.setVisibility(View.VISIBLE);
            mView_Loading.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
        }
    }

}
