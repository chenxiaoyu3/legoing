package com.Legoing.ItemDef;


public class Minifig extends LegoItem{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8809190028931905058L;
	static final String IMAGE_BIG_URL = "http://www.bricklink.com/ML/*.jpg";
	boolean detail;
	int appearSet;
	int appearMinifig;
	
	@Override
	public boolean isDetail() {
		return detail;
	}
	public void setDetail(boolean detail) {
		this.detail = detail;
	}
	public int getAppearSet() {
		return appearSet;
	}
	public void setAppearSet(int appearSet) {
		this.appearSet = appearSet;
	}
	public int getAppearMinifig() {
		return appearMinifig;
	}
	public void setAppearMinifig(int appearMinifig) {
		this.appearMinifig = appearMinifig;
	}
	public String getBigImgLink()
    {
        return IMAGE_BIG_URL.replace("*", getNo());
    }
}
