package com.example.test.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.test.test.myffmpeg.FFmpegActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<Bitmap> imgList;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private float mX;
    private float mY;
    private ScaleAndTranslateImageView scaleAndTranslateImageView;
    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView viewById = (TextView) findViewById(R.id.tv_jump);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
        CoverFlowView<MyCoverFlowAdapter> mCoverFlowView = (CoverFlowView<MyCoverFlowAdapter>) this.findViewById(R.id.coverflow);
        imgList = new ArrayList<>();
        imgList.add(BitmapFactory.decodeResource(this.getResources(), R.mipmap.a));
        imgList.add(BitmapFactory.decodeResource(this.getResources(), R.mipmap.b));
        imgList.add(BitmapFactory.decodeResource(this.getResources(), R.mipmap.c));
        imgList.add(BitmapFactory.decodeResource(this.getResources(), R.mipmap.d));
        imgList.add(BitmapFactory.decodeResource(this.getResources(), R.mipmap.d));

        MyCoverFlowAdapter adapter = new MyCoverFlowAdapter();
        mCoverFlowView.setAdapter(adapter);


        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        //mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
        //mSwipeRefreshWidget.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new MyAdapter());
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshWidget.setRefreshing(false);
                    }
                },2000);
            }
        });
        initScaleAndTranslateView();
        initSeeBar();
        initSurfaceView();
    }

    private void initSeeBar() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.seeBar);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("TAG", "onProgressChanged: "+fromUser );
                float v = progress * 1f / seekBar.getMax();
                if(scaleAndTranslateImageView!=null){
                    scaleAndTranslateImageView.setAlphas(1-v);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initScaleAndTranslateView() {
        scaleAndTranslateImageView = (ScaleAndTranslateImageView) findViewById(R.id.scaleAndTranslateImageView);
        final ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(scaleAndTranslateImageView,"xxx",1f,0.333f);
        objectAnimator.setDuration(10000);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f= (float) animation.getAnimatedValue();
                //scaleAndTranslateImageView.setParmars(f);
                scaleAndTranslateImageView.setAlphas(f);
//                scaleAndTranslateImageView.setScaleX(f);
//                scaleAndTranslateImageView.setScaleY(f);
//                Log.e("TAG", "run: x="+scaleAndTranslateImageView.getX()+", y="+scaleAndTranslateImageView.getY() );
//                scaleAndTranslateImageView.requestLayout();
//                Log.e("TAG", "run: x="+scaleAndTranslateImageView.getMeasuredWidth()+", y="+scaleAndTranslateImageView.getMeasuredHeight() );
                //scaleAndTranslateImageView.setX(mX);
                //scaleAndTranslateImageView.setY(mY);
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //scaleAndTranslateImageView.requestLayout();
                Log.e("TAG", "onAnimationEnd: "+ scaleAndTranslateImageView.getWidth()+", "+ scaleAndTranslateImageView.getHeight() );
            }
        });
        scaleAndTranslateImageView.post(new Runnable() {
            @Override
            public void run() {
//                mX = scaleAndTranslateImageView.getX();
//                mY = scaleAndTranslateImageView.getY();
//                Log.e("TAG", "run: x="+mX+", y="+mY +", "+scaleAndTranslateImageView.getPivotX()+", "+scaleAndTranslateImageView.getPivotY()+", "+scaleAndTranslateImageView.getWidth()+", "+scaleAndTranslateImageView.getHeight());
//                scaleAndTranslateImageView.setPivotX(0);
//                scaleAndTranslateImageView.setPivotY(0);
                //objectAnimator.start();
            }
        });
    }

    public class MyCoverFlowAdapter extends CoverFlowAdapter {
        public int getCount() {
            return 5;
        }

        public Bitmap getImage(int position) {
            return imgList.get(position);
        }
    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(new TextView(getApplicationContext()));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(position+"啦啦啦");
        }

        @Override
        public int getItemCount() {
            return 30;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv=(TextView)itemView;
            }
        }
    }
    private void initSurfaceView(){
        mSurfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        holder = mSurfaceView.getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
               /* mMediaPlayer.prepareAsync();
                //mMediaPlayer.setDisplay(holder);
                mMediaPlayer.setSurface(holder.getSurface());*/
               play();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }
    private void play() {
        if(mMediaPlayer==null) {
            MyMediaPlayer myMediaPlayer = new MyMediaPlayer();
            mMediaPlayer = myMediaPlayer.getmMediaPlayer();
        }
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource("http://www.xiwang.com/task/resourceview?id=462&token=BSZHgiovoprjhuz7ujjwo89ek5y3zy2stjg7");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //VideoView
        //mediaPlayer1.setDataSource("/mnt/sdcard/通话录音/1.mp4");
        // 把视频输出到SurfaceView上
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();
        /*try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //mMediaPlayer.start();
    }

}
