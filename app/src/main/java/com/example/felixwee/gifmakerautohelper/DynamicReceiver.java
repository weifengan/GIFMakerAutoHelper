package com.example.felixwee.gifmakerautohelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DynamicReceiver extends BroadcastReceiver {


    private  GifMakerService mService;
    public  void Init(GifMakerService service){
        mService=service;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

         mService.isRunning= intent.getBooleanExtra("status",false);
        Toast.makeText(context,"快手助手状态变化了: "+mService.isRunning,Toast.LENGTH_SHORT ).show();
    }
}
