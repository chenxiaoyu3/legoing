package com.Legoing.ItemDef;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ComponentItem 
{
	int colorIndex;
	//String pno;
	int quantity;
	Object itemDB;

	public Object getItemDB() {
		return itemDB;
	}
	public void setItemDB(Object itemDB) {
		this.itemDB = itemDB;
		
	}
	public int getColorIndex() {
		return colorIndex;
	}
	public void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
//	public String getPno() {
//		return pno;
//	}
//	public void setPno(String pno) {
//		this.pno = pno;
//	}
	public boolean equals(ComponentItem o) {
		// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 10:49:16 AM
		return itemInfo.getNo().equals(o.getItemInfo().getNo());
	}
	//--- not with server commu ---/////////
	int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

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
	
	public static ComponentItem getTest()
	{
		ComponentItem ret = new ComponentItem();
		ret.colorIndex = 1;
		//ret.pno = "Test component";
		ret.quantity = 1;
		return ret;
	}
	public ComponentItem()
	{
		
	}
	/**
	 * itemDB and itemInfo not clone
	 * @param item
	 */
	public ComponentItem(ComponentItem item) {
		// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 11:05:24 AM
		setColorIndex(item.colorIndex);
		setItemDB(item.itemDB);
		setQuantity(item.quantity);
		setType(item.type);
		setItemInfo(item.itemInfo);
	}
	
	@Override
	/**
	 * Note: just new place for quantity!
	 */
	public Object clone(){
		// TODO , Chen Xiaoyu Cxy, 2012-2-18 下午6:08:12
		ComponentItem item = new ComponentItem();
		item.setColorIndex(this.colorIndex);
		item.setItemDB(this.itemDB);
		item.setQuantity(this.quantity);
		item.setType(this.type);
		item.setItemInfo(this.itemInfo);
		return item;
	}
	public boolean equals(ComponentItem o, boolean ignoreColor) {
	    // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 13, 2012 10:19:27 AM
	    if (ignoreColor) {
            return itemInfo.getNo().equals(o.getItemInfo().getNo());
        }else {
            return itemInfo.getNo().equals(o.getItemInfo().getNo()) && colorIndex == o.getColorIndex();
        }
	}
	
//	int type = 0;
//	@Override
//	public int getTYPE() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return 0;
//	}
//	@Override
//	public String getNo() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return pno;
//	}
//	@Override
//	public void setNo(String no) {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		this.pno = no;
//	}
//	@Override
//	public String getName() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return "";
//	}
//	@Override
//	public void setName(String name) {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		
//	}
//	@Override
//	public String getType() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return StaticOverall.getResources().getStringArray(R.array.legoItem_types)[type];
//	}
//	@Override
//	public void setType(String type) {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		
//	}
//	@Override
//	public int getYearBegin() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return 0;
//	}
//	@Override
//	public void setYearBegin(int yearBegin) {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		
//	}
//	@Override
//	public int getYearEnd() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return 0;
//	}
//	@Override
//	public void setYearEnd(int yearEnd) {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		
//	}
//	@Override
//	public String getImgLink() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return "";
//	}
//	@Override
//	public void setImgLink(String imgLink) {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		
//	}
//	@Override
//	public String getCategory() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return "";
//	}
//	@Override
//	public void setCategory(String category) {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		
//	}
//	@Override
//	public String getComment() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return "";
//	}
//	@Override
//	public void setComment(String comment) {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		
//	}
//	@Override
//	public String getString_Year() {
//		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:26:17
//		return "";
//	}
//	
//	/**
//	 * make sure T implements the TI
//	 * @param ori
//	 * @return
//	 */
//	public static <TI,T> List<TI> ListTypeCase(List<T> ori)
//	{
//		List<TI> ret = new ArrayList<TI>(ori.size());
//		for (T t : ori) {
//			ret.add((TI)t);
//		}
//		return ret;
//	}

}
