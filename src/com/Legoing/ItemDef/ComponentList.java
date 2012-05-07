package com.Legoing.ItemDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComponentList {

	public List<List<List<ComponentItem>>> getPlist() {
		return plist;
	}

	public void setPlist(List<List<List<ComponentItem>>> plist) {
		this.plist = plist;
		for (List<List<ComponentItem>> list0 : plist) {
			for (ComponentItem item : list0.get(0)) {
				item.type = LegoItem.ITEM_TYPE_SET;
				item.arrangeItem(LegoItem.ITEM_TYPE_SET);
			}
			for (ComponentItem item : list0.get(1)) {
				item.type = LegoItem.ITEM_TYPE_MINIFIG;
				item.arrangeItem(LegoItem.ITEM_TYPE_MINIFIG);
			}
			for (ComponentItem item : list0.get(2)) {
				item.type = LegoItem.ITEM_TYPE_PART;
				item.arrangeItem(LegoItem.ITEM_TYPE_PART);
			}
		}
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	/**
	 * 
	 * @param typ
	 *            Part or Minifig
	 * @return
	 */
	public List<ComponentItem> getAllItems(int typ) {
		List<ComponentItem> retItems = new LinkedList<ComponentItem>();
		if (plist != null) {
			for (List<List<ComponentItem>> list0 : plist) {
				switch (typ) {
				case LegoItem.ITEM_TYPE_PART:
					retItems.addAll(list0.get(2));
					break;
				case LegoItem.ITEM_TYPE_MINIFIG:
					retItems.addAll(list0.get(1));
				default:
					break;
				}
			}
		}

		return retItems;
	}

	public List<ComponentItem> getAllItemsIgnoreColor(int typ) {
		
		List<ComponentItem> retItems = new LinkedList<ComponentItem>();
		if (plist != null) {
			for (List<List<ComponentItem>> list0 : plist) {
				switch (typ) {
				case LegoItem.ITEM_TYPE_PART:
					retItems.addAll(list0.get(2));
					break;
				case LegoItem.ITEM_TYPE_MINIFIG:
					retItems.addAll(list0.get(1));
				default:
					break;
				}
			}
		}
		Map<String, ComponentItem> map = new HashMap<String, ComponentItem>();
		for (ComponentItem item : retItems) {
			String no = item.getItemInfo().getNo();
			if (map.containsKey(no)) {
				ComponentItem it = map.get(no);
				it.setQuantity(it.getQuantity()+item.getQuantity());
			}else {
				map.put(no, item);
			}
		}

		return new ArrayList<ComponentItem>(map.values());
	}
	

	/**
	 * 
	 * pList: array( 0 => array(0 => array(//set list), 1 => array(//part list),
	 * 2 => array(//minifig list)), 1 => array(0 => array(//set list), 1 =>
	 * array(//part list), 2 => array(//minifig list)), 2 => array(0 =>
	 * array(//set list), 1 => array(//part list), 2 => array(//minifig list)),
	 * 3 => array(0 => array(//set list), 1 => array(//part list), 2 =>
	 * array(//minifig list)) )
	 */
	public List<List<List<ComponentItem>>> plist;
	String sno;

}
