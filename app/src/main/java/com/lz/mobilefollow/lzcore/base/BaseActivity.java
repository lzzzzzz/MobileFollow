package com.lz.mobilefollow.lzcore.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lz.mobilefollow.R;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;
import com.lz.mobilefollow.lzcore.framework.manager.ActivityStack;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    protected Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ActivityStack.getInstance().pushActivity(this);
        eventBundle(getIntent().getExtras());
        initView();
        initData();

    }


    private Toolbar getToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }


    /**
     * 添加toolbar标题
     */
    public void setToolbarTitle(int resText) {
        setToolbarTitle(getString(resText));
    }


    /**
     * 添加toolbar返回按键
     */
    public void setToolbarNavigationIcon(int resId) {
        getToolbar().setNavigationIcon(resId);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeftMenu();
            }
        });
    }

    /**
     * 去除 ToolbarNavigationIcon
     */
    public void setToolbarNavigationIcon() {
        if (getToolbar() != null) {
            getToolbar().setNavigationIcon(null);
        }
    }


    /**
     * toolbar左边按钮点击事件
     */
    public void onClickLeftMenu() {
    }


    /**
     * 添加toolbar标题
     */
    public void setToolbarTitle(String textTitle) {
        getToolbar().setTitle(textTitle);
    }

    /**
     * 隐藏toolBar
     */
    protected void hideTitle() {
        if (getToolbar() == null) {
            return;
        }
        getToolbar().setVisibility(View.GONE);
    }

    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            LogUtils.d("Could not cast View to concrete class." + ex);
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(View view, int id) {
        return (T) view.findViewById(id);
    }


    /**
     * layout xml文件ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 接收上个界面传递过来的信息
     *
     * @param bundle
     */
    protected void eventBundle(Bundle bundle) {
    }

    /**
     * 控件初始化
     */
    protected void initView() {
    }


    /**
     * 初始化数据
     */
    protected void initData() {
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstance().popActivity(this);
    }

    /**
     * 跳转界面 并且finish掉当前activity
     *
     * @param intent
     */
    protected void SkipActivityFinish(Intent intent) {
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        onClickView(v.getId());
    }

    /**
     * 点击事件转换
     *
     * @param key
     */
    protected void onClickView(int key) {

    }

    /**
     * 隐藏输入键盘
     *
     * @param
     */
    public static void hideSortInput(View v) {
        InputMethodManager inputmanger = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
