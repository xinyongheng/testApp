package activity.cqdsj.cn.study_videoplay;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import activity.cqdsj.cn.study_videoplay.view.MediaController;
import activity.cqdsj.cn.study_videoplay.view.MyVideoView;

import static activity.cqdsj.cn.study_videoplay.LoadingSpeed.UPDATE_LOADING_SPEED;
import static java.lang.System.currentTimeMillis;

public class MainActivity extends Activity {

    private RelativeLayout vlayout ;
    private MyVideoView mVideoView ;

    //视频加载布局
    private LinearLayout loadingLayout ;

    //加载动画
    private ImageView loading ;
    //加载进度
    private TextView tvLoading ;

    private Button fullPlay ;

    private int clickNum = 0 ;

    //此视频特别流畅
//    private static final String url = "http://mgcdn.migucloud.com/vi0/ftp/miguread/CLOUD1000119556/56/video57267_56_keep.MP4?duration=62&owner=198&quality=9&timestamp=20170330094023&title=video57267_56.mp4&vid=2GBJSAnkd9tHlB3raM9DOw&para1=yyy&para2=xxx" ;

    //此视频特别卡，方便用于测试
    private static final String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4" ;
//    private static final String url = "http://yyekt.oss-cn-qingdao.aliyuncs.com/video/theory-1-2.mp4" ;
    private static class MyHandler1 extends activity.cqdsj.cn.study_videoplay.MyHandler{
        public MyHandler1(MainActivity o) {
            super(o);
        }

        @Override
        public MainActivity getT() {
            return (MainActivity) super.getT();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == UPDATE_LOADING_SPEED){

                if(isEffective()&&getT().loadingLayout != null && (getT().loading.getVisibility()== View.VISIBLE)){
                    String loadSpeed = "玩命加载中.."+(String)msg.obj;
                    getT().tvLoading.setText(loadSpeed);
                }
            }
        }
    }
    private Handler handler = new MyHandler1(this);
    private LoadingSpeed mLoadSpeed;
    private MediaController mMediaController;
    private long mCTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        fullPlay = (Button) findViewById(R.id.bt_full_play) ;
        mVideoView = (MyVideoView) findViewById(R.id.mvv_vidoe) ;
        vlayout = (RelativeLayout) findViewById(R.id.rl_video_layout) ;

        mMediaController = new MediaController(this);


        /**
         * 双击监听
         */
        vlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLandScreen()){
                    clickNum = 0 ;
                }else{
                    clickNum++ ;
                    if(clickNum==1){
                        //获取当前时间
                        mCTime = currentTimeMillis();
                    }else{
                        clickNum = 0 ;
                        long dis = System.currentTimeMillis() - mCTime ;
                        if(dis<2000){
                            if(isLandScreen()){
                                // 当前为横屏播放，切换为竖屏播放
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            }else{
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            }
                        }
                    }
                }
            }
        });

        if(isLandScreen()){
            mVideoView.setMediaController(mMediaController);
            loadVideo(0,0,0,0, true) ;
        }else{
            loadVideo(0,0,320,240, false) ;
        }
    }
    private void loadVideo(int left, int top, int with, int height, boolean isHor) {
        if(mVideoView.isPlaying()){
            mVideoView.stopPlayback();
        }
        int l = dip2px(left) ;
        int t = dip2px(top) ;
        int w = dip2px(with) ;
        int h = dip2px(height) ;
        RelativeLayout.LayoutParams params ;
        if(isHor){
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT) ;
        }else{
            params = new RelativeLayout.LayoutParams(w, h) ;
        }

        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        vlayout.setLayoutParams(params);
        //显示进度加载
        showLoading() ;
        //播放视频
        mVideoView.setVideoPath(url);
        mLoadSpeed = new LoadingSpeed(handler);
        mLoadSpeed.start();
        mVideoView.setOnInfoListener(new MyOnInfoListener());
    }

    /**
     * 视频播放器OnInfo监听
     */
    private class MyOnInfoListener implements MediaPlayer.OnInfoListener{

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            handleOnInfo(mp, what) ;
            return true;
        }
    }

    /**
     * 处理播放信息
     * @param mp
     * @param what
     */
    private void handleOnInfo(MediaPlayer mp, int what){
        switch (what){
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:  //视频开始播放第一帧

                //隐藏进度加载
                hideLoading() ;

                //开启自定义检测卡顿,Ps记得注释掉VideoView自带监听
                //start();
                break ;

            // 这是VideoView自带监听，使用的话，代开注释，Ps：记得把自定义监听的start()方法注释掉
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:   //开始缓冲(视频卡顿)
                System.out.println("视频卡顿，开始缓冲...");
                //显示进度加载
                showLoading();
                break ;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:    //缓冲结束(视频可以播放)
                //隐藏进度加载
                System.out.println("缓冲结束...");
                hideLoading();
                break ;

        }
        //隐藏进度条
        mMediaController.hide();
    }

    /**
     * 显示视频加载布局
     */
    private void showLoading(){
        if(loadingLayout != null){
            loadingLayout.setVisibility(View.VISIBLE);
            ((AnimationDrawable)(loading.getBackground())).start() ;
        }else{
            //实例化加载控件
            loadingLayout = new LinearLayout(this) ;
            loading = new ImageView(this) ;
            tvLoading = new TextView(this) ;

            //初始化加载布局
            loadingLayout.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT) ;
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            loadingLayout.setLayoutParams(layoutParams);

            //初始化加载动画
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT) ;
            lparams.gravity = Gravity.CENTER_HORIZONTAL ;
            loading.setLayoutParams(lparams);
            loading.setBackgroundResource(R.drawable.loading);

            //初始化加载进度
            LinearLayout.LayoutParams tvlparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT) ;
            tvlparams.gravity = Gravity.CENTER_HORIZONTAL ;
            tvLoading.setLayoutParams(tvlparams);
            tvLoading.setTextColor(Color.WHITE);
            tvLoading.setText("玩命加载中...0.0Kb/s");

            loadingLayout.addView(loading);
            loadingLayout.addView(tvLoading);

            vlayout.addView(loadingLayout);
            ((AnimationDrawable)(loading.getBackground())).start() ;
        }
    }

    /**
     *  用于缩放加载动画
     * @param scale
     * @return
     */
    private Point scaleLoadingImage(int scale){
        //获取图片大小
        BitmapFactory.Options options = new BitmapFactory.Options() ;
        //设置预加载
        options.inJustDecodeBounds = true ;
        BitmapFactory.decodeResource(getResources(),R.drawable.gif_loading1,options);
        Point point = new Point() ;
        point.x = options.outWidth*scale ;
        point.y = options.outHeight*scale ;
        return point ;
    }

    private void hideLoading(){
        if(loadingLayout.getVisibility() == View.VISIBLE){
            ((AnimationDrawable)(loading.getBackground())).stop();
            loading.clearAnimation();
            AlphaAnimation alpha = new AlphaAnimation(1,0) ;
            alpha.setDuration(500);
            alpha.setFillAfter(true);
            alpha.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    loading.clearAnimation();
                    loadingLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            loading.setAnimation(alpha);
            alpha.start();
        }
    }

    /**
     * 横屏播放
     * @param v
     */
    public void switchHor(View v){
        //判断当前是否为横屏
        Configuration configuration = getResources().getConfiguration();
        if(isLandScreen()){
            // 当前为横屏播放，切换为竖屏播放
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            loadVideo(0,0,400,300, false) ;
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            loadVideo(0,0,0,0,true);
        }
    }

    private boolean isLandScreen(){
        //判断当前是否为横屏
        Configuration configuration = getResources().getConfiguration();
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            return true ;
        }
        return false ;
    }

    //定义1s前播放位置
    private long lastPosition ;
    private Timer mTimer ;

    /**
     * 开启卡顿检测
     */
    private void start(){
        mTimer = new Timer() ;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                    long dis = getVideoViewDelay() ;
                    //如果正常播放，dis应该等于1000ms，如果小于1000ms,则有卡顿的嫌疑，小于500ms基本可以断定卡顿严重
                    if(dis<500){
                        //视频卡顿，-------做一些卡顿处理(比如：显示加载动画，或者当前下载速度)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showLoading();
                            }
                        });
                    }else{
                        //视频卡顿结束(判断加载动画是否显示，如果显示则隐藏)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                hideLoading();
                            }
                        }) ;
                    }
                }
            };

        //开启检测前，先获取一次进度(主要是为了给lastPosition赋值)
        getVideoViewDelay() ;
        //1s之后开启检测
        mTimer.schedule(task, 1000, 1000) ;
    }

    /**
     * [currentPosition 获取延时]
     * @type {[type]}
     */
    private long getVideoViewDelay(){
        //获取当前播放位置
        long currentPosition = mVideoView.getCurrentPosition() ;
        //计算指定时间间隔实际播放的进度
        long dis = currentPosition - lastPosition ;

        lastPosition = currentPosition ;
        return dis ;
    }

    private int dip2px(float dipValue){
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer != null){
            mTimer.cancel();
        }

        if(mLoadSpeed != null){
            mLoadSpeed.cancel();
        }
        if(mVideoView != null){
            mVideoView.stopPlayback();
            mVideoView = null ;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isLandScreen() && keyCode == KeyEvent.KEYCODE_BACK){
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            startActivity(new Intent(this,JKActivity.class));
            return true ;
        }

        return super.onKeyDown(keyCode, event);
    }
}
