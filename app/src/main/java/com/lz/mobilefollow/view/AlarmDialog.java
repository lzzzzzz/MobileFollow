package com.lz.mobilefollow.view;

import android.content.Context;
import android.os.Bundle;

import com.lz.mobilefollow.utils.MediaPlayUtils;

/**
 * Created by LZ on 2016/10/31.
 */

public class AlarmDialog extends android.support.v7.app.AlertDialog {
    public AlarmDialog(Context context) {
        super(context);
    }

    public AlarmDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        MediaPlayUtils.getInstance().play(this.getContext(),"water_flower.mp3");
    }

    @Override
    public void dismiss() {
        MediaPlayUtils.getInstance().destroy();
        super.dismiss();
    }
}
