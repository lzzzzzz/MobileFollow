package com.lz.mobilefollow.activity.testdemo;

import android.widget.Button;

import com.lz.mobilefollow.R;
import com.lz.mobilefollow.lzcore.base.BaseActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainTestActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_test;
    }

    @Override
    protected void initData() {
        super.initData();
        //lambda表达式测试
        Button bt1=new Button(this);
        bt1.setText("测试");
//        bt1.setOnClickListener(View->System.out.println("测试lambda"));

//        new Thread(()->System.out.println());

    }




}
