package com.lz.mobilefollow.lzcore.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.lz.mobilefollow.lzcore.bridge.BridgeFactory;
import com.lz.mobilefollow.lzcore.bridge.BridgeLifeCycleSetKeeper;
import com.lz.mobilefollow.lzcore.bridge.Bridges;
import com.lz.mobilefollow.lzcore.bridge.cache.localstorage.LocalFileStorageManager;
import com.lz.mobilefollow.lzcore.util.ToastUtil;
import com.lz.mobilefollow.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <应用初始化> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-3-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BaseApplication extends Application
{
    /**
     * app实例
     */
    public static BaseApplication ebApplication = null;
    
    /**
     * 本地activity栈
     */
    public static List<Activity> activitys = new ArrayList<Activity>();
    
    /**
     * 当前avtivity名称
     */
    public static String currentActivityName = "";
    /**获取当前context*/
    private static Context context;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        context=getApplicationContext();
        initData();
    }
    public static Context getCurrentContext(){
        return context;
    }
    /**
     * 初始化数据
     */
    private void initData() {
        ebApplication = this;
        BridgeFactory.init(this);
        BridgeLifeCycleSetKeeper.getInstance().initOnApplicationCreate(this);
        LocalFileStorageManager manager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE);
        LocationUtils.getInstance().locationInit(this);
    }


    @Override
    public void onTerminate()
    {
        super.onTerminate();
        onDestory();
    }

    /**
     * 退出应用，清理内存
     */
    private void onDestory() {
        BridgeLifeCycleSetKeeper.getInstance().clearOnApplicationQuit();
        ToastUtil.destory();
    }


    /**
     * 
     * <添加> <功能详细描述>
     * 
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void addActivity(Activity activity)
    {
        activitys.add(activity);
    }
    
    /**
     * 
     * <删除>
     * <功能详细描述>
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void deleteActivity(Activity activity)
    {
        if (activity != null)
        {
            activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 一个获得当前进程的名字的方法
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
