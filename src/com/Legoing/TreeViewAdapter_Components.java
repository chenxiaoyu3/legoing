package com.Legoing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.Legoing.ItemDef.ComponentItem;
import com.Legoing.ItemDef.ComponentList;
import com.Legoing.ItemDef.IComponentItem;
import com.Legoing.UserControls.Layout_ExpandableListTitle;
import com.Legoing.UserControls.UCtrl_LegoComponentListItem;
import com.Legoing.UserControls.UCtrl_LegoListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class TreeViewAdapter_Components extends BaseExpandableListAdapter {

	Context context;
	LayoutInflater inflater;
	String[] groupNames;
	ComponentList list;
	List<ListViewAdapter_forComponents_Groups> components;

	ListViewAdapter_forComponents_Groups group0,group1,group2,group3;
	List<ListViewAdapter_forComponents_Groups> groups;
	public TreeViewAdapter_Components(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		groupNames = StaticOverall.getResources().getStringArray(
				R.array.array_componentList_GroupName);
	}

	public void setList(ComponentList list) {
		this.list = list;
		components = new ArrayList<ListViewAdapter_forComponents_Groups>(4);
		groups = new ArrayList<ListViewAdapter_forComponents_Groups>(4);
		List<List<ComponentItem>> li0 = list.getPlist().get(0);

		List<ComponentItem> eaSet = li0.get(0);
		List<ComponentItem> eaMini =li0.get(1);
		List<ComponentItem> eaPart = li0.get(2);
		group0 = new ListViewAdapter_forComponents_Groups(
				context, eaSet, eaMini, eaPart);
		groups.add(group0);
		
		li0 = list.getPlist().get(1);
		eaSet = li0.get(0);
		eaMini =li0.get(1);
		eaPart = li0.get(2);
		group1 = new ListViewAdapter_forComponents_Groups(
				context, eaSet, eaMini, eaPart);
		groups.add(group1);
		
		li0 = list.getPlist().get(2);
		eaSet = li0.get(0);
		eaMini =li0.get(1);
		eaPart = li0.get(2);
		group2 = new ListViewAdapter_forComponents_Groups(
				context, eaSet, eaMini, eaPart);
		groups.add(group2);
		
		li0 = list.getPlist().get(3);
		eaSet = li0.get(0);
		eaMini =li0.get(1);
		eaPart = li0.get(2);
		group3 = new ListViewAdapter_forComponents_Groups(
				context, eaSet, eaMini, eaPart);
		groups.add(group3);
	}


	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		// View view = inflater.inflate(R.layout.list_item, null);
		//View view = new UCtrl_LegoListItem(context, null);
		// ((UCtrl_LegoListItem)view).setValue(cur);
		// Expand
		//UCtrl_LegoComponentListItem view2 = new UCtrl_LegoComponentListItem(context, null);
		//view2.setValue(componentItem)
		
		return groups.get(groupPosition).getView(childPosition, convertView, parent);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		
		return groups.get(groupPosition).getCount();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		return 4;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		Layout_ExpandableListTitle view = new Layout_ExpandableListTitle(
				context);
		if (groupPosition < 4) {
			view.setTitleText(groupNames[groupPosition]);
		}
		// ((Layout_TitleLevel_2)arg1).setTitleText("Set");
		// ((Layout_TitleLevel_2)arg1).setTitleText2(String.valueOf(sets.size()));
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����5:44:37
		return true;
	}

}
