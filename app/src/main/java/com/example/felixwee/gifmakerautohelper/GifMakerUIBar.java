package com.example.felixwee.gifmakerautohelper;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class GifMakerUIBar{

    private Context mContext;
    private WindowManager mWindowManager;
    private  ConstraintLayout mView;
    private  GifMakerService mService;  //服务

    //是否开启自动服务
    private  boolean isRunning=false;
    public ConstraintLayout getView(final Context context, WindowManager windowManager, GifMakerService service){
        mContext=context;
        mWindowManager=windowManager;
        mService=service;

        mView = (ConstraintLayout)LayoutInflater.from(context).inflate(R.layout.gifmaker_ui_layout, null);


        final ImageButton btngifmaker_auto=(ImageButton)mView.findViewById(R.id.gifmaker_auto);
        btngifmaker_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isRunning){
                    btngifmaker_auto.setImageDrawable(context.getResources().getDrawable(R.drawable.gifmaker_status_stop));
                }else {
                    btngifmaker_auto.setImageDrawable(context.getResources().getDrawable(R.drawable.gifmaker_status_running));
                }
                isRunning=!isRunning;
                Log.i("快手","点击胃");

                Intent intent = new Intent();
                intent.setAction(GifMakerService.GIFMAKER_STATUS_CHANGED);
                intent.putExtra("status",isRunning);
                mContext.sendBroadcast(intent);
            }
        });

        return mView;
    }

}
