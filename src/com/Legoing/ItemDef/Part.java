package com.Legoing.ItemDef;

import com.Legoing.IHaveImgLink;

public class Part extends LegoItem implements IHaveImgLink
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -461260512902871283L;
//	public boolean isDetail() {
//		return detail;
//	}
//
//	public void setDetail(boolean detail) {
//		this.detail = detail;
//	}

	public int getAppearSet() {
		return appearSet;
	}

	public void setAppearSet(int appearSet) {
		this.appearSet = appearSet;
	}

	public int getAppearPart() {
		return appearPart;
	}

	public void setAppearPart(int appearPart) {
		this.appearPart = appearPart;
	}

	public int getAppearMinifig() {
		return appearMinifig;
	}

	public void setAppearMinifig(int appearMinifig) {
		this.appearMinifig = appearMinifig;
	}

	public boolean detail; // true if this part has component
	@Override
	public boolean isDetail() {
		return detail;
	}

	public void setDetail(boolean detail) {
		this.detail = detail;
	}

	public int appearSet;
	public int appearPart;
	public int appearMinifig;
	static final String IMAGE_BIG_URL = "http://www.bricklink.com/PL/*.jpg";
	@Override
	public String getImageLinks() {
		// TODO , Chen Xiaoyu Cxy, 2011-11-23  5:14:06
		return imgLink;
	}
	public String getBigImgLink()
    {
        return IMAGE_BIG_URL.replace("*", getNo());
    }

}
