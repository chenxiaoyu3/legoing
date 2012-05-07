package com.Legoing.UserControls;

import com.Legoing.R;
import com.Legoing.ItemDef.LegoingUserEvaluation;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Layout_Comment extends LinearLayout{

    Context mContext;
    TextView mTextView_Score, mTextView_Name, mTextView_Content;
    LegoingUserEvaluation mValue;
    public Layout_Comment(Context context) {
        super(context);
        mContext = context;
        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 2:54:00 PM
        LayoutInflater.from(context).inflate(R.layout.user_comment, this, true);
        mTextView_Score = (TextView)findViewById(R.id.textView_comment_score);
        mTextView_Name = (TextView)findViewById(R.id.textView_comment_name);
        mTextView_Content = (TextView)findViewById(R.id.textView_comment_content);
    }
    public void setValue(LegoingUserEvaluation ev)
    {
        this.mValue = ev;
        mTextView_Content.setText(ev.getItemDes());
        mTextView_Name.setText(ev.getUser());
        mTextView_Score.setText(String.valueOf(ev.getItemScore()));
    }

}
