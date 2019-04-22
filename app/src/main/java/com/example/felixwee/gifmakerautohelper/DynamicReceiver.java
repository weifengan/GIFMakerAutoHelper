package com.example.felixwee.gifmakerautohelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DynamicReceiver extends BroadcastReceiver {


    private  GifMakerService mService;
    public  void Init(GifMakerService service){
        mService=service;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String type=intent.getAction();


        switch (type){
            case GifMakerService.GIFMAKER_STATUS_CHANGED:
                mService.isRunning= intent.getBooleanExtra("status",false);
                Toast.makeText(context,"快手助手状态变化了: "+mService.isRunning,Toast.LENGTH_SHORT ).show();
                break;
                case GifMakerService.GIFMAKER_DO_SCROLL:
                    Toast.makeText(context,"進行滾動: ",Toast.LENGTH_SHORT ).show();
                    mService.MakeScrollAction();
                    break;
        }

    }
}
