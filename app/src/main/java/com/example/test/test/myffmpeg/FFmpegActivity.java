package com.example.test.test.myffmpeg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.test.test.R;

public class FFmpegActivity extends AppCompatActivity {

    private TextView textView;
    private TextView tv_abi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg);
        textView = (TextView) findViewById(R.id.tv);
        tv_abi = (TextView) findViewById(R.id.tv_abi);
        //int i = HellowJni.avcodec_find_decoder(13);
        HellowJni hellowJni = new HellowJni();
        textView.setText(hellowJni.configurationinfo());
        tv_abi.setText(JniUtils.getAndroidCpuType()+"\n"+hellowJni.getAndroidCpuType());
    }

}
