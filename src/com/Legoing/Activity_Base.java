package com.Legoing;

import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.LegoingUser;
import com.Legoing.ItemDef.UserItems;
import com.Legoing.Model.UserSignStateChangedListener;
import com.Legoing.zxing.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Activity_Base extends Activity{
    
    protected static final int MSG_NET_UTILS_OTHER = -10;

    static List<UserSignStateChangedListener> mUserSignStateChangedListeners;
    
    public static void init()
    {
        mUserSignStateChangedListeners = new LinkedList<UserSignStateChangedListener>();
    }
    public static final String EXTRA_GOLBALKEY = "EXTRA_GOLBALKEY";

    public int doSignIn(String name, String psd)
    {
        ParamObj<LegoingUser> retValue = new DefaultParamObj<LegoingUser>();
        int ret = StaticOverall.netOperation.userVerify(name, psd, retValue);
        switch (ret) {
            case 0:
                
                ParamObj<UserItems> paramObj = new DefaultParamObj<UserItems>();
                ret = StaticOverall.netOperation.getUserItems(retValue.value.getUserIndex(), paramObj);
                if (ret == 0) {
                    retValue.value.setItems(paramObj.value);
                    StaticOverall.curUser = retValue.value;
                }
                break;
            case -1:
                Toast.makeText(Activity_Base.this, R.string.msg_WrongUserNameOrPsd, Toast.LENGTH_LONG).show();
                break;
            default:
                StaticOverall.netOperation.handleReturnValue(ret);
                break;
            }
        return ret;
    }
    public void doSignOff()
    {
        StaticOverall.curUser = null;
    }
    public boolean isSignedIn(){
        return !(StaticOverall.curUser == null);
    }
    public void notifySign(boolean sign)
    {
        for (UserSignStateChangedListener listener : mUserSignStateChangedListeners) {
            if (listener != null) {
                listener.onUserSignStateChanged(sign);
            }
        }
    }
    public void addUserSignChangedListener(UserSignStateChangedListener listener)
    {
        mUserSignStateChangedListeners.add(listener);
    }
    
    public void startBarcodeScan()
    {
        Intent intent = new Intent(Activity_Base.this, CaptureActivity.class);
        startActivityForResult(intent, REQ_BARCODESCAN);
    }
    
    public void startLegoItemInfoActivity( LegoItem item)
    {
        String timestamp = String.valueOf( new Date().getTime() );
        Intent i = new Intent(this,Activity_LegoItemInfo.class);
        i.putExtra(Activity_LegoItemInfo.EXTRA_GOLBALKEY, timestamp);
        StaticOverall.pushGlobalData(timestamp, item);
        startActivity(i);
    }
    public void startFindMissActivity(LegoItem item)
    {
        Intent intent = new Intent(this, Activity_FindMiss.class);
        String timestamp = String.valueOf( new Date().getTime() );
        intent.putExtra(Activity_LegoItemInfo.EXTRA_GOLBALKEY, timestamp);
        StaticOverall.pushGlobalData(timestamp, item);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 24, 2012 12:50:34 PM
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void shutdownConfirm() {
        AlertDialog.Builder aDialogBuilder = new AlertDialog.Builder(this);
        aDialogBuilder.setIcon(R.drawable.lego_1);
        aDialogBuilder.setTitle(R.string.Confirm);
        aDialogBuilder.setMessage(R.string.msg_Exit);
        aDialogBuilder.setPositiveButton(R.string.YES,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // _TODO , Chen Xiaoyu Cxy, 2012-1-3  9:00:32
                        ThreadsMgr.abortAllThreads();
                        StaticOverall.shutdown();                   
                        }
                });
        aDialogBuilder.setNegativeButton(R.string.CANCLE,null);
        aDialogBuilder.show();
    }
    public static final int REQ_BARCODESCAN = 1;
}
