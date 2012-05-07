package com.Legoing.ItemDef;

import com.Legoing.R;
import com.Legoing.StaticOverall;

import android.content.res.Resources;

public class LegoColors 
{
	static final int COVER = 0xFF000000;
	String[] colors;
	int[] values;
	public LegoColors(Resources res)
	{
		colors = res.getStringArray(R.array.array_item_colors);
		values = res.getIntArray(R.array.array_colors_values);
	}
	public String toString(int colorIndex)
	{
		return colors[colorIndex];
	}
	//0x000000
	public int getValue(int colorIndex)
	{
		return values[colorIndex];
	}
	public int size()
	{
		return colors.length;
	}
	/**
	 * 
	 * @param colorIndex
	 * @param withTransparent if true, return 0xff000000
	 * @return
	 */
	public int getValue(int colorIndex, boolean withTransparent)
	{
		int colorValue = getValue(colorIndex);
		if (withTransparent) {
			colorValue = COVER | colorValue;
		}
		return colorValue;
	}

}
