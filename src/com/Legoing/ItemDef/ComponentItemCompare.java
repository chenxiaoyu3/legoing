package com.Legoing.ItemDef;

public class ComponentItemCompare extends ComponentItem{

	int qtyTotal;

	public ComponentItemCompare(ComponentItem item)
	{
		super(item);
	}
	public int getQtyTotal() {
		return qtyTotal;
	}

	public void setQtyTotal(int qtyTotal) {
		this.qtyTotal = qtyTotal;
	}
	
	
}
