package com.Legoing.ItemDef;

public class ImportResult {
	int numSetSuc, numSetRepeat, numSetFail;	
	int numPartSuc, numPartRepeat, numPartFail;
	public int getNumSetSuc() {
		return numSetSuc;
	}
	public void setNumSetSuc(int numSetSuc) {
		this.numSetSuc = numSetSuc;
	}
	public int getNumSetRepeat() {
		return numSetRepeat;
	}
	public void setNumSetRepeat(int numSetRepeat) {
		this.numSetRepeat = numSetRepeat;
	}
	public int getNumSetFail() {
		return numSetFail;
	}
	public void setNumSetFail(int numSetFail) {
		this.numSetFail = numSetFail;
	}
	public int getNumPartSuc() {
		return numPartSuc;
	}
	public void setNumPartSuc(int numPartSuc) {
		this.numPartSuc = numPartSuc;
	}
	public int getNumPartRepeat() {
		return numPartRepeat;
	}
	public void setNumPartRepeat(int numPartRepeat) {
		this.numPartRepeat = numPartRepeat;
	}
	public int getNumPartFail() {
		return numPartFail;
	}
	public void setNumPartFail(int numPartFail) {
		this.numPartFail = numPartFail;
	}
	
	//not cummu with server
	public int ret;

}
