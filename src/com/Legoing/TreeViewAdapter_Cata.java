package com.Legoing;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.Legoing.ItemDef.UserItem;
import com.Legoing.UserControls.Layout_ExpandableListTitle;

public class TreeViewAdapter_Cata extends BaseExpandableListAdapter {

	Context context;
	// List<String> groupsName;
	List<ListViewAdapter_forCataItem> children;

	public TreeViewAdapter_Cata(Context context,
			List<ListViewAdapter_forCataItem> children) {
		// TODO , Cxy, 2012-1-1 ����3:20:30
		this.context = context;
		// this.groupsName = groups;
		this.children = children;
		
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����3:19:46
		return children.get(groupPosition).getItem(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����3:19:46
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����3:19:46
		return children.get(groupPosition).getView(childPosition, convertView,
				parent);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����3:19:46
		return children.get(groupPosition).getCount();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����3:19:46
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����3:19:46
		return children.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����3:19:46
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 pm 3:19:46
		Layout_ExpandableListTitle view = new Layout_ExpandableListTitle(
				context);
		// view.setTitleText(groupsName.get(groupPosition));
		view.setTitleText(((UserItem)children.get(groupPosition).getItem(0)).getItemInfo().getCategory());
		// ((Layout_TitleLevel_2)arg1).setTitleText2(String.valueOf(sets.size()));
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 119:46
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 3:19:46
		return true;
	}

	public static TreeViewAdapter_Cata getAdapterFromItems(Context context, List<UserItem> userItems)
	{
		Map<String, List<UserItem>> map = new HashMap<String, List<UserItem>>();
		for (UserItem item : userItems) {
			String cata = item.getItemInfo().getCategory();
			if (!map.keySet().contains(cata)) {
				List<UserItem> right = new LinkedList<UserItem>();
				right.add(item);
				map.put(cata, right);
			}else {
				map.get(cata).add(item);
			}
		}
		
		List<ListViewAdapter_forCataItem> alList = new LinkedList<ListViewAdapter_forCataItem>();
		Set<Entry<String, List<UserItem>>> sets =  map.entrySet();
		Iterator<Entry<String, List<UserItem>>> iterator = sets.iterator();
		while (iterator.hasNext()) {
			ListViewAdapter_forCataItem adapter_forCataItem = 
					new ListViewAdapter_forCataItem(context, ((Entry<String, List<UserItem>>)iterator.next()).getValue());
			alList.add(adapter_forCataItem);
		}
		TreeViewAdapter_Cata cata = new TreeViewAdapter_Cata(context, alList);
		return cata;
	}

	public static int AtGroups(List<String> group, String str) {
		for (int i = 0; i < group.size(); ++i) {
			if (group.get(i).equalsIgnoreCase(str)) {
				return i;
			}
		}
		return -1;
	}

}
