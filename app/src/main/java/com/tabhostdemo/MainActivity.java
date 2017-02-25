package com.tabhostdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.flContentContainer)
    FrameLayout mFlContentContainer;
    @BindView(R.id.fragmentTabHost)
    FragmentTabHost mFragmentTabHost;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();

    }
    //初始化TabHost
    private void init() {
        //最基本初始化
        mFragmentTabHost.setup(this,getSupportFragmentManager(),R.id.flContentContainer);

           /*--------------- 循环的方式创建TabSpec_数组循环---------------*/
        String[] mainTitleArr = {"综合", "动弹", "", "发现", "我"};

       // 初始化TabHost.TabSpec以及Fragment
        TabHost.TabSpec tabSpec1 = mFragmentTabHost.newTabSpec("tag1");
        //创建indicatorView1
        View indicatorView1 = View.inflate(this,R.layout.inflate_indicatorview,null);


    }
}
