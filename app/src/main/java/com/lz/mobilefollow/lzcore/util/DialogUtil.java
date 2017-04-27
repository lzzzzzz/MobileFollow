package com.lz.mobilefollow.lzcore.util;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.lz.mobilefollow.R;
import com.lz.mobilefollow.view.AlarmDialog;

/**
 * 
 * <弹出框公共类>
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class DialogUtil
{
    /**弹出提醒dialog*/
    public static void showAlarmDialog(Context context, String message){
        final AlarmDialog ad=new AlarmDialog(context);
        ad.setTitle(context.getResources().getText(R.string.text_dialog_alarm_str_title));
        if(!TextUtils.isEmpty(message)){
            ad.setMessage(message);
        }else{
            ad.setMessage(context.getResources().getText(R.string.text_dialog_alarm_str_content));
        }
        ad.setIcon(R.mipmap.ic_launcher);
        ad.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.dismiss();
            }
        });
        ad.show();
    }
}
