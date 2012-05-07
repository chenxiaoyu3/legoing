package com.Legoing.ItemDef;

public interface ILegoItem
{
	public int getTYPE();
	public String getNo();
	public void setNo(String no);
	public String getName();
	public void setName(String name);
	public String getType() ;
	public void setType(String type);
	public int getYearBegin();
	public void setYearBegin(int yearBegin);
	public int getYearEnd();
	public void setYearEnd(int yearEnd);
	public String getImgLink();
	public void setImgLink(String imgLink);
	public String getCategory();
	public void setCategory(String category);
	public String getComment();
	public void setComment(String comment);
	
	public String getString_Year();
	
}
