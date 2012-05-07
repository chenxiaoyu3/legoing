package com.Legoing.ItemDef;

import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class UserItem {
	String itemNo;
	String itemName;
	int quantity;
	int colorIndex;
	Object itemDB;
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getColorIndex() {
		return colorIndex;
	}
	public void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}
	public Object getItemDB() {
		return itemDB;
	}
	public void setItemDB(Object itemDB) {
		this.itemDB = itemDB;
	}
	
	////not commu with server
	String type;
	LegoItem itemInfo;
	public void arrangeItem(int typ)
	{
		String string = ((JSONObject)itemDB).toJSONString();
		try {
			switch (typ) {
			case LegoItem.ITEM_TYPE_SET:
				itemInfo = JSON.parseObject(string,Set.class);
				break;
			case LegoItem.ITEM_TYPE_PART:
				itemInfo = JSON.parseObject(string,Part.class);
				break;
			case LegoItem.ITEM_TYPE_MINIFIG:
				itemInfo = JSON.parseObject(string,Minifig.class);
				break;

			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("arrange", e.toString());
		}
		
		
	}
	public LegoItem getItemInfo() {
		return (LegoItem)itemInfo;
	}
	public void setItemInfo(LegoItem itemInfo) {
		this.itemInfo = itemInfo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
//	View view;
//	public View getView() {
//		return view;
//	}
//	public void setView(View view) {
//		this.view = view;
//	}
	boolean isChecked;
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean checked) {
		this.isChecked = checked;
	}
	
	
	
}
