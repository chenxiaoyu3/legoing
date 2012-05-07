package com.Legoing;

import com.Legoing.ItemDef.ImportResult;
import com.Legoing.UserControls.ActionBar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Import extends Activity_Base{
	
	private ActionBar actionBar;
	private EditText editText_Name, editText_Psd;
	private Button button_Confirm, button_Reset;
	private ProgressBar progressBar;
	private TextView textView_Suc_S, textView_Suc_M, textView_Suc_P;
	private TextView textView_Fail_S, textView_Fail_M, textView_Fail_P;
	private TextView textView_Repeat_S, textView_Repeat_M, textView_Repeat_P;
	private TextView textView_Website, textView_User;
	private View view_Input, view_Result;
	
	private ImportResult importResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO , Chen Xiaoyu Cxy, 2012-3-1 下午2:49:08
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.import_website);
		initialIDs();
		initial();
	}
	private void initialIDs()
	{
		this.actionBar = (ActionBar)findViewById(R.id.actionBar_import);
		this.editText_Name = (EditText)findViewById(R.id.editText_import_UName);
		this.editText_Psd = (EditText)findViewById(R.id.editText_import_Psd);
		this.button_Confirm = (Button)findViewById(R.id.button_import_confirm);
		this.button_Reset = (Button)findViewById(R.id.button_import_reset);
		this.progressBar = (ProgressBar)findViewById(R.id.progressBar_import);
		this.textView_Suc_S = (TextView)findViewById(R.id.textView_importResult_suc_S);
		this.textView_Suc_M = (TextView)findViewById(R.id.textView_importResult_suc_M);
		this.textView_Suc_P = (TextView)findViewById(R.id.textView_importResult_suc_P);
		this.textView_Fail_S = (TextView)findViewById(R.id.textView_importResult_fail_S);
		this.textView_Fail_M = (TextView)findViewById(R.id.textView_importResult_fail_M);
		this.textView_Fail_P = (TextView)findViewById(R.id.textView_importResult_fail_P);
		this.textView_Repeat_S = (TextView)findViewById(R.id.textView_importResult_repeat_S);
		this.textView_Repeat_M = (TextView)findViewById(R.id.textView_importResult_repeat_M);
		this.textView_Repeat_P = (TextView)findViewById(R.id.textView_importResult_repeat_P);
		this.textView_Website = (TextView)findViewById(R.id.textView_importResult_website);
		this.textView_User = (TextView)findViewById(R.id.textView_importResult_name);
		this.view_Input = findViewById(R.id.layout_import_login);
		this.view_Result = findViewById(R.id.layout_import_result);
	}
	private void initial()
	{
		this.button_Confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 4:41:54 PM
				if (StaticOverall.curUser == null) {
					Toast.makeText(
							Activity_Import.this,
							StaticOverall.getResources().getString(
									R.string.msg_LoginFirstPlease), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String name = editText_Name.getText().toString();
				String psd = editText_Psd.getText().toString();
				new Async_RequestImport().execute(new String[]{name,psd});
				
			}
		});
		actionBar.setTitle(getString(R.string.label_import_Title));
		this.textView_Website.setText(R.string.website_peeron);
		
	}
	private void setAsInputMode()
	{
		this.view_Result.setVisibility(View.INVISIBLE);
		this.view_Input.setVisibility(View.VISIBLE);
	}
	private void setAsResultMode()
	{
		this.view_Input.setVisibility(View.INVISIBLE);
		this.view_Result.setVisibility(View.VISIBLE);
	}
	 
	class Async_RequestImport extends AsyncTask<String, Object, Integer>
	{

		@Override
		protected void onPreExecute() {
			// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 4:39:40 PM
			button_Confirm.setEnabled(false);
			progressBar.setVisibility(View.VISIBLE);
			textView_User.setText(editText_Name.getText().toString());
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... params) {
			// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 2:42:03 PM
			ParamObj<ImportResult> paramObj = new DefaultParamObj<ImportResult>();
			String name = params[0];
			String psd = params[1];
			int ret = StaticOverall.netOperation.requestImport(name, psd, StaticOverall.curUser.getUserIndex(), paramObj);
			Activity_Import.this.importResult = paramObj.value;
			return ret;
		}
		@Override
		protected void onPostExecute(Integer result) {
			// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 2:52:31 PM
			
			if (result == 0) {
				setAsResultMode();
				textView_Suc_S.setText(String.valueOf(importResult.getNumSetSuc()));
				textView_Suc_P.setText(String.valueOf(importResult.getNumPartSuc()));
				textView_Suc_M.setText("0");
				int fail = importResult.getNumSetFail();
				if (fail > 0) {
					textView_Fail_S.setTextColor(R.color.red);
				}
				textView_Fail_S.setText(String.valueOf(fail));
				fail = importResult.getNumPartFail();
				if (fail > 0) {
					textView_Fail_P.setTextColor(R.color.red);
				}
				textView_Fail_P.setText(String.valueOf(fail));
				textView_Fail_M.setText("0");
				textView_Repeat_S.setText(String.valueOf(importResult.getNumSetRepeat()));
				textView_Repeat_P.setText(String.valueOf(importResult.getNumPartRepeat()));
				textView_Repeat_M.setText("0");
			}else if (result == -1) {
				Toast.makeText(Activity_Import.this, R.string.msg_WrongUserNameOrPsd, Toast.LENGTH_LONG).show();
				setAsInputMode();
			}else {
				setAsInputMode();
				StaticOverall.netOperation.handleReturnValue(result);
			}
			button_Confirm.setEnabled(true);
			progressBar.setVisibility(View.INVISIBLE);
			
			super.onPostExecute(result);
		}
		
	}

}
