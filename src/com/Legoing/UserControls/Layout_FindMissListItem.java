package com.Legoing.UserControls;

import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ItemDef.ComponentItem;
import com.Legoing.ItemDef.ComponentItemCompare;
import com.Legoing.ItemDef.LegoItem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Layout_FindMissListItem extends LinearLayout{

	private Context mContext;
	private ComponentItem mComponentItem;
	private TextView mTextView_ID, mTextView_Name, mTextView_quantComp, mTextView_Miss;
	private ImageView mImageView_Color;
	public Layout_FindMissListItem(Context context) {
		super(context);
		// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 10:31:24 AM
		LayoutInflater.from(context).inflate(R.layout.find_miss_component_listitem, this, true);
		this.mContext = context;
		initialIDs();
	}
	private void initialIDs()
	{
		mTextView_ID = (TextView)findViewById(R.id.textView_findMissComponentListItem_itemID);
		mTextView_Name = (TextView)findViewById(R.id.textView_findMissComponentListItem_ItemName);
		mTextView_quantComp = (TextView)findViewById(R.id.textView_findMissComponentListItem_quantitiyCompare);
		mTextView_Miss = (TextView)findViewById(R.id.textView_findMissComponentListItem_quantitiyMiss);
		mImageView_Color = (ImageView)findViewById(R.id.imageView_findMissComponentListItem_color);
	}
	public void setInfo(ComponentItemCompare compare)
	{
		setItem(compare);
		setQuantity_total(compare.getQtyTotal());
	}
	private void setItem(ComponentItem item)
	{
		this.mComponentItem = item;
		this.mTextView_ID.setText(item.getItemInfo().getNo());
		this.mTextView_Name.setText(item.getItemInfo().getName());
		int cor = StaticOverall.legoColors.getValue(item.getColorIndex(), true);
		mImageView_Color.setImageDrawable(new ColorDrawable(cor));
	}
	private void setQuantity_total(int num)
	{
		int qty_this, qty_total, qty_miss;
		qty_this = mComponentItem.getQuantity();
		qty_total = num;
		qty_miss = qty_this - qty_total;
		if (qty_miss < 0) {
			qty_miss = 0;
		}
		this.mTextView_quantComp.setText(qty_this + "/" + qty_total);
		this.mTextView_Miss.setText(String.valueOf(qty_miss));
		if (qty_miss > 0) {
			this.mTextView_Miss.setTextColor(Color.RED);
		}else {
			this.mTextView_Miss.setTextColor(Color.GREEN);
		}
	}
	public LegoItem getItemInfo()
	{
	    return mComponentItem.getItemInfo();
	}

}
