package com.Legoing;

public class CommuObj {
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int ret;
	public Object value;
	public String info;
	
	public static final int RET_PARAM_NOT_COMPLETE = -1;
	public static final int RET_PARAM_NOT_FIT = -2;
	public static final int RET_NO_RESULT = -3;
	public static final int RET_SUCCESS = 0;
	

}
