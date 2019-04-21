/**
 *    用于实现快手自动关注，双击点赞，自动评论服务
 *
 */
package com.example.felixwee.gifmakerautohelper;

import android.accessibilityservice.AccessibilityService;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GifMakerService extends AccessibilityService {
    private static WindowManager mWindowManager;
    private  static WindowManager.LayoutParams wmParams;
    private  static ConstraintLayout mView;
    private  DynamicReceiver mReceiver;


    public   boolean isRunning=false;
    public  static String GIFMAKER_STATUS_CHANGED="GIFMAKER_STATUS_CHANGED";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i("快手","快手Auto助手服务已经链接");

        CreateUI();

        RegistReceiver();



    }

    private  void RegistReceiver(){

        Log.i("快手","注册消息");
        IntentFilter filter=new IntentFilter();
        filter.addAction(GIFMAKER_STATUS_CHANGED);
        mReceiver=new DynamicReceiver();
        mReceiver.Init(this);
        registerReceiver(mReceiver,filter);
    }

    ///创建UI
    private  void CreateUI(){
        Log.i("快手","创建快手助手UI");

        //取得系统窗体
        mWindowManager=(WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
        //船舰窗口样式
        wmParams=new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //设置窗体焦点及触摸
        wmParams.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置显示模式
        wmParams .format=PixelFormat.RGBA_8888;

        //设定方式
        wmParams.gravity = Gravity.TOP | Gravity.RIGHT;

        //设置窗口和高度
        wmParams.width=WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height=WindowManager.LayoutParams.WRAP_CONTENT;

        //
        GifMakerUIBar gmui=new GifMakerUIBar();
        mView=gmui.getView(getApplicationContext(),mWindowManager,this);
        //添加View
        mWindowManager.addView(mView, wmParams);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("快手","快手Auto助手服务已经创建成功!");
    }

    private AccessibilityNodeInfo root=null;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if(!isRunning) return;
        if(event.getPackageName().equals("com.smile.gifmaker")){
            //获取root

                root=getRootInActiveWindow();
                 //Log.i("快手","状态改变"+root.getClassName().toString());
                 MakeAddFriend(root);
                //MakeAHeart(root);

        }else {
            Log.i("快手","root相同");
        }




    }

    private  AccessibilityNodeInfo currentfollowNode;
    public  void MakeAddFriend(AccessibilityNodeInfo root){

        //查找播放页关注按钮
        List<AccessibilityNodeInfo> nodes=root.findAccessibilityNodeInfosByViewId("com.smile.gifmaker:id/follow");
        //判断是否可以找到关注按钮
        if(nodes!=null && nodes.size()>0){
            //找到了关注按钮
            final AccessibilityNodeInfo folowBtn=nodes.get(0);
            if(folowBtn.isClickable()){
                Log.i("kuaishou","开始模拟关注.....");

                if(currentfollowNode!=folowBtn) {
                    currentfollowNode = folowBtn;

                    //开始模拟点击
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            folowBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Log.i("kuaishou", "关注按钮被点击了");
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1000);//3秒后执行TimeTask的run方法
                }
            }
        }
    }


    /*模拟点赞*/
    private  void MakeAHeart(AccessibilityNodeInfo root){

        //查找播放页关注按钮
        List<AccessibilityNodeInfo> nodes=root.findAccessibilityNodeInfosByViewId("com.smile.gifmaker:id/like_layout");

        //判断场景中有点赞
        if(nodes!=null && nodes.size()>1){
            //取出点赞Like按钮
            final AccessibilityNodeInfo likeBtn = nodes.get(0);
            likeBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.e("快手","点赞完成");
        }

        //
    }

    @Override
    public void onInterrupt() {
        Log.i("快手","快手Auto助手服务正在被打断..");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("快手","快手Auto助手服务正在被销毁..");
        mWindowManager.removeView(mView);
        unregisterReceiver(mReceiver);
    }
}
