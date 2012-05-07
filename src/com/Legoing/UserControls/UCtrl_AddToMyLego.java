package com.Legoing.UserControls;

import com.Legoing.Activity_MainTab;
import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.Set;
import com.Legoing.NetOperation.NetOperation;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class UCtrl_AddToMyLego extends LinearLayout {

	Context context;
	LegoItem value;
	Button button_Yes, button_No;
	EditText editText_Quantity, editText_Color;
	Handler handler_ColorPicked;
	AlertDialog dialog;
	View view_Color;

	public UCtrl_AddToMyLego(Context context) {
		super(context);
		// _TODO , Cxy, 2012-1-2 pm 3:47:29
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.add_to_my_lego, this);
		initialID();
		button_Yes.setOnClickListener(onClickListener_Yes);
		button_No.setOnClickListener(onClickListener_Cancle);
		editText_Color.setOnFocusChangeListener(onFocusChangeListener_ColorPick);
		editText_Color.setOnClickListener(onClickListener_ColorPick);
		handler_ColorPicked = new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO , Cxy, 2011-11-25 pm 3:12:20
				switch (msg.what) {
				case 1:
					editText_Color.setText(StaticOverall.legoColors.toString(UCtrl_ColorPicker.selectedColor));
					break;
				}
				super.handleMessage(msg);
			}
		};

	}

	public void initialID() {
		this.button_No = (Button) findViewById(R.id.button_AddToMyLego_No);
		this.button_Yes = (Button) findViewById(R.id.button_AddToMyLego_Yes);
		this.editText_Quantity = (EditText) findViewById(R.id.editText_AddToMyLego_Quantity);
		this.editText_Color = (EditText) findViewById(R.id.editText_AddToMyLego_Color);
		this.view_Color = findViewById(R.id.layout_AddToMyLego_color);
	}

	public void setValue(LegoItem item) {
		this.value = item;
		if (this.value instanceof Set) {
		    this.view_Color.setVisibility(View.VISIBLE);
        }
	}

	public void setOuterDialog(AlertDialog dialog) {
		this.dialog = dialog;
	}

	View.OnClickListener onClickListener_Yes = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO , Chen Xiaoyu Cxy, 2012-1-2 ����4:18:25
			addToMyLego();
		}
	};
	
	View.OnClickListener onClickListener_Cancle = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO , Chen Xiaoyu Cxy, 2012-1-2 ����4:18:25
			dialog.dismiss();
		}
	};
	View.OnClickListener onClickListener_ColorPick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO , Chen Xiaoyu Cxy, 2012-2-15 下午4:18:15
			AlertDialog.Builder aDialogBuilder = new AlertDialog.Builder(
					context);
			UCtrl_ColorPicker colorPicker = new UCtrl_ColorPicker(context, handler_ColorPicked);
			aDialogBuilder.setView(colorPicker);
			AlertDialog dialog = aDialogBuilder.create();
			colorPicker.setOuterDialog(dialog);
  			dialog.show();
		}
	};
	View.OnFocusChangeListener onFocusChangeListener_ColorPick = new View.OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO , Chen Xiaoyu Cxy, 2012-2-15 下午4:10:41
			if (hasFocus) {
				onClickListener_ColorPick.onClick(null);
			}
			
		}
	};
	

	void addToMyLego() {
		if (StaticOverall.curUser == null) {
			Toast.makeText(
					context,
					StaticOverall.getResources().getString(
							R.string.msg_LoginFirstPlease), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		int quantity = 0;
		int color = UCtrl_ColorPicker.selectedColor;
		try {
			quantity = new Integer(editText_Quantity.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(
					context,
					StaticOverall.getResources().getString(
							R.string.msg_EnterQuantity), Toast.LENGTH_SHORT)
					.show();
		}
		
		int ret = StaticOverall.netOperation.addUserItem(
				StaticOverall.curUser.getUserIndex(), value.getType(),
				value.getNo(), quantity, color);
		switch (ret) {
		case 0:
			Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
//			outHandler.sendEmptyMessage(Layout_MyLegoing.MSG_REFRESH_USERSITEMS);
			((Activity_MainTab)StaticOverall.getMainActivity()).getTab_myLego().needRefreshUserItems();
			dialog.dismiss();
			break;
		case -1:
			Toast.makeText(context, "Item exists!", Toast.LENGTH_SHORT).show();
			//outHandler.sendEmptyMessage(Layout_MyLegoing.MSG_REFRESH_USERSITEMS);
			dialog.dismiss();
			break;
		case NetOperation.CODE_NET_FAILED:
			Toast.makeText(
					context,
					StaticOverall.getResources().getString(
							R.string.msg_Error_ServerConnectFail),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(context, "Unkown Ret " + ret, Toast.LENGTH_SHORT).show();
			break;
		}
		return;
	}

}
