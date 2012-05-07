package com.Legoing.ItemDef;

import java.util.List;

import com.Legoing.R;
import com.Legoing.StaticOverall;
/**
 * used only for server communication
 * @author Cxy
 *
 */
public class UserItems {
	int userIndex;
	List<List<UserItem>> plist;
	public List<List<UserItem>> getPlist() {
		return plist;
	}
	public void setPlist(List<List<UserItem>> plist) {
		this.plist = plist;
		String[] typesStrings = StaticOverall.getResources().getStringArray(R.array.legoItem_types);
		for (UserItem item : plist.get(0)) {
			item.type = typesStrings[1];
			item.arrangeItem(LegoItem.ITEM_TYPE_SET);
		}
		for (UserItem item : plist.get(1)) {
			item.type = typesStrings[2];
			item.arrangeItem(LegoItem.ITEM_TYPE_MINIFIG);
		}
		for (UserItem item : plist.get(2)) {
			item.type = typesStrings[3];
			item.arrangeItem(LegoItem.ITEM_TYPE_PART);
		}
	}
	public int getUserIndex() {
		return userIndex;
	}
	public void setUserIndex(int userIndex) {
		this.userIndex = userIndex;
	}

	public void addUserItem(UserItem item)
	{
	    if (item.itemInfo instanceof Set) {
            plist.get(0).add(item);
        }else if (item.itemInfo instanceof Minifig) {
            plist.get(1).add(item);
        }else {
            plist.get(2).add(item);
        }
	}
	////not commu with server
}

