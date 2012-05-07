package com.Legoing.ItemDef;

import java.io.Serializable;

import com.Legoing.R;
import com.Legoing.StaticOverall;

public class LegoItem implements Serializable,ILegoItem
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2296456070968328321L;
	//corrosponding to the index of array : legoItem_types
	public static final int ITEM_TYPE_SET = 1;
	public static final int ITEM_TYPE_MINIFIG = 2;
	public static final int ITEM_TYPE_PART = 3;
	
	static int YEAR_UNKOWN = 0;
	static int YEAR_NOT_GOT = -1;
	
	static String[] itemTypes;
	
	String no;
	String name;
	String type;
	
	int yearBegin,yearEnd;	
	String imgLink;
	String category;
	String comment;
	public static LegoItem getTestItem()
	{
		LegoItem item = new LegoItem();
		item.no = "0000-0";
		item.name = "Test set";
		item.yearBegin = 1999;
		item.category = "testCata.testCata....";
		item.imgLink = "http://wandrae.com/test.jpg";
		return item;
	}
	public int getTYPE()
	{
		if (itemTypes == null) {
			itemTypes = StaticOverall.getResources().getStringArray(R.array.legoItem_types);
		}
		if (type.equals(itemTypes[ITEM_TYPE_SET])) {
			return ITEM_TYPE_SET;
		}else if(type.equals(itemTypes[ITEM_TYPE_PART]))
			return ITEM_TYPE_PART;
		else if(type.equals(itemTypes[ITEM_TYPE_MINIFIG]))
			return ITEM_TYPE_MINIFIG;
		else 
			return 0;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getYearBegin() {
		return yearBegin;
	}
	public void setYearBegin(int yearBegin) {
		this.yearBegin = yearBegin;
	}
	public int getYearEnd() {
		return yearEnd;
	}
	public void setYearEnd(int yearEnd) {
		this.yearEnd = yearEnd;
	}
	public String getImgLink() {
//		return IMAGE_NORMAL_ADDR_PREFIX + getNo() + ".jpg";
//		if (imgLink == null || imgLink.length() == 0) {
//			return IMAGE_NORMAL_ADDR_PREFIX + getNo() + ".jpg";
//		}else {
//			return imgLink;
//		}
		return imgLink;
	}
	public String getBigImgLink()
	{
	    return null;
	}
	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isYearUnkonw()
	{
		return yearBegin == YEAR_UNKOWN;
	}
	public boolean isYearUnInitialized()
	{
		return yearBegin == YEAR_NOT_GOT;
	}
	public String getString_Year()
	{
		if (isYearUnkonw()) {
			return "Unkonwn";
		}
		if(isYearUnInitialized())
		{
			return "";
		}
		return String.valueOf(yearBegin + " - " + yearEnd);
	}
	
	//Not with server's definde
	ComponentList componentList = null;
	public ComponentList getComponentList() {
		return componentList;
	}
	public void setComponentList(ComponentList componentList) {
		this.componentList = componentList;
	}
	public boolean isDetail()
	{
	    return false;
	}
}
