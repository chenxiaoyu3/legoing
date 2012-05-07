package com.Legoing.ItemDef;

import com.Legoing.IHaveImgLink;

public class Set extends LegoItem implements IHaveImgLink
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7727843812863919899L;
	static final String IMAGE_BIG_URL = "http://www.bricklink.com/SL/*.jpg";
	public Set() {
		
	}
//	public static String getName_SetID()
//	{
//		return "SetID";
//	}
//	public static String getName_SetIDSecond()
//	{
//		return "SetIDSecond";
//	}
//	public static String getName_SetName()
//	{
//		return "SetName";
//	}
//	public static String getName_PartsNum()
//	{
//		return "PartsNum";
//	}
//	public static String getName_YearReleased()
//	{
//		return "YearReleased";
//	}
//	
//	public List<Integer> getPartsList_InnerID() {
//		return PartsList_InnerID;
//	}
//	public void setPartsList_InnerID(List<Integer> partsList_InnerID) {
//		PartsList_InnerID = partsList_InnerID;
//	}
	
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
//	int id;
	
//	int setIDSecond;
	
	int setQuantity;
	int partQuantity;
	int minifigQuantity;
	
	public static Set getDefaultSet()
	{
		Set set = new Set();
		set.category = "Foo";
		set.comment = "Commenttt";
		return set;
	}
	
	
	public int getSetQuantity() {
		return setQuantity;
	}
	public void setSetQuantity(int setQuantity) {
		this.setQuantity = setQuantity;
	}
	public int getPartQuantity() {
		return partQuantity;
	}
	public void setPartQuantity(int partQuantity) {
		this.partQuantity = partQuantity;
	}
	public int getMinifigQuantity() {
		return minifigQuantity;
	}
	public void setMinifigQuantity(int minifigQuantity) {
		this.minifigQuantity = minifigQuantity;
	}
	
	@Override
	public String getImageLinks() {
		// TODO , Chen Xiaoyu Cxy, 2011-11-23 ����5:13:37
		return imgLink;
	}
	
	@Override
	public boolean isDetail() {
	    // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 26, 2012 1:25:33 PM
	    return true;
	}
	public String getBigImgLink()
    {
        return IMAGE_BIG_URL.replace("*", getNo());
    }
	
	
	
//	List<Integer> PartsList_InnerID;
//	float Weight;
//	float Length;
//	float Width;
//	float Height;
}