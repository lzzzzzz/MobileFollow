package com.lz.mobilefollow.activity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lz.mobilefollow.R;
import com.lz.mobilefollow.activity.testdemo.MediaControlerActivity;
import com.lz.mobilefollow.lzcore.base.BaseActivity;
import com.lz.mobilefollow.lzcore.constant.Constant;
import com.lz.mobilefollow.lzcore.constant.MessageConfig;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;
import com.lz.mobilefollow.service.FollowMoService;
import com.lz.mobilefollow.utils.WebSocketFactory;
import com.lz.mobilefollow.lzcore.util.SharedPreferenceUtils;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox rb_if_connect;//设置是否连接
    private TextView tv_connect_status;//显示连接状态

    private Toolbar toolbar;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void initView() {
        setToolbarTitle("Mobile-follow");
        rb_if_connect= (CheckBox) this.findViewById(R.id.rb_if_connect);
        tv_connect_status= (TextView) this.findViewById(R.id.tv_connect_status);
//        toolbar = (Toolbar) this.findViewById(R.id.id_toolbar);
//        toolbar.setTitle("Mobile-follow");

        boolean ifLogin= (boolean) SharedPreferenceUtils.getSPBoolean(this, Constant.SHARED_KEY_IF_LOGIN);
        if(!ifLogin){
            SharedPreferenceUtils.setSPBoolean(this, Constant.SHARED_KEY_IS_CONNECT,false);//没有登录禁止连接
//            startActivity(new Intent(this,LoginActivity.class));//测试关闭
            return;
        }
        if(ifLogin){
            boolean isConnect= (boolean) SharedPreferenceUtils.getSPBoolean(this, Constant.SHARED_KEY_IS_CONNECT);
            if(isConnect){
                rb_if_connect.setChecked(true);
            }else{
                rb_if_connect.setChecked(false);
            }
            rb_if_connect.setOnCheckedChangeListener(this);

            //定位
           /* String serviceString = Context.LOCATION_SERVICE;
            LocationManager locationManager = (LocationManager) getSystemService(serviceString);

            String provider = LocationManager.GPS_PROVIDER;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            getLocationInfo(location);
            locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);*/

            Intent intent = new Intent(this, FollowMoService.class);
            startService(intent);


        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        boolean ifLogin= (boolean) SharedPreferenceUtils.getSPBoolean(this, Constant.SHARED_KEY_IF_LOGIN);
        if(!ifLogin){
            rb_if_connect.setChecked(false);
            SharedPreferenceUtils.setSPBoolean(this, Constant.SHARED_KEY_IS_CONNECT,false);//没有登录禁止连接
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }
        if(isChecked){//连接
            WebSocketFactory.getInstance().setIfConnect(true);
            WebSocketFactory.getInstance().beginConnect(MainActivity.this);
            tv_connect_status.setText("连接开启");
        }else{//断开连接
            WebSocketFactory.getInstance().closeClient(MainActivity.this);
            tv_connect_status.setText("连接关闭");
        }
    }

    /**检查是否登录*/
    public void checkLogin(){
        boolean ifLogin= (boolean) SharedPreferenceUtils.getSPBoolean(this, Constant.SHARED_KEY_IF_LOGIN);
        if(!ifLogin){
            SharedPreferenceUtils.setSPBoolean(this, Constant.SHARED_KEY_IS_CONNECT,false);//没有登录禁止连接
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }
    }

    /**定位监听*/
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            getLocationInfo(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            getLocationInfo(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
            getLocationInfo(null);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void getLocationInfo(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();//维度
            double longitude = location.getLongitude();//经度
            LogUtils.d("---------------longitude&latitude:"+longitude+"&"+latitude);
            if(longitude<=0&&latitude<=0){
                return;
            }
            SharedPreferenceUtils.setSPString(this, MessageConfig.SP_LOCATION_LONGITUDE, String.valueOf(longitude));
            SharedPreferenceUtils.setSPString(this, MessageConfig.SP_LOCATION_LATITUDE, String.valueOf(latitude));
        }else{
            LogUtils.d("No location found");
        }
        LogUtils.d("Your current position is not vilide");
    }

    public void toTest(View view) {
        startActivity(new Intent(this, MediaControlerActivity.class));
    }
}
