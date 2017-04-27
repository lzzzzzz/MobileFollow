package com.lz.mobilefollow.lzcore.framework.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * ImageLoader进行的简单的封装
 * <p/>
 * <pre>
 * 变动showImageOnLoading需求
 * </pre>
 * <p/>
 * <pre>
 * 使用 先获取instance
 * </pre>
 *
 * @author lk
 */
public abstract class ImageLoaderAbs {

    private static ImageLoaderAbs instance;

    public static ImageLoaderAbs getInstance() {
        synchronized (ImageLoaderAbs.class) {
            if (instance == null) {
                instance = new ImageLoaderSub();
            }
        }
        return instance;

    }

    /**
     * 自定义 options 直接显示 没有监听
     */
    public void displayImage(DisplayImageOptions options, String path,
                             ImageView imageView) {
        ImageLoader.getInstance().displayImage(path, imageView, options);
    }

    ;

    /**
     * 自定义 options 直接显示 监听
     */
    public void displayImage(DisplayImageOptions options, String path,
                             ImageView imageView, SimpleImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(path, imageView, setOptions(),
                listener);
    }

    ;

    /**
     * 自定义图片显示大小（不设置以xml里面定义的大小显示） 直接显示 没有监听
     */
    public void displayImageZoom(ImageSize size, String path,
                                 final ImageView imageView) {
        ImageLoader.getInstance().loadImage(path, size, setOptions(),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        imageView.setImageBitmap(loadedImage);
                    }
                });
    }

    ;

    /**
     * 自定义 options 直接显示 没有监听
     */
    public void imageZoom(ImageSize size, String path,
                          SimpleImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(path, size, listener);
    }

    ;

    /**
     * 自定义 image显示图片角度， 直接显示 没有监听
     */
    public void roundDisplayImage(String path, ImageView imageView, int subRes) {
        ImageLoader.getInstance().displayImage(path, imageView,
                setRoundOptions(180, subRes));
    }

    ;

    /**
     * 自定义 image显示图片角度 and 填充图片空间
     */
    public void roundDisplayImageSmall(int round, String path, ImageView imageView, int subRes) {
        imageView.setScaleType(ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(path, imageView,
                setRoundOptions(round, subRes));
    }

    ;

    /**
     * 直接显示 没有监听
     */
    public void displayImage(String path, ImageView imageView) {
        ImageLoader.getInstance().displayImage(path, imageView, setOptions());
    }

    ;

    /**
     * 自定义 options 直接显示 需要监听
     */
    public void displayImage(String path, ImageView imageView,
                             SimpleImageLoadingListener imageLoaderListener) {
        ImageLoader.getInstance().displayImage(path, imageView, setOptions());
    }

    ;

    /**
     * 自定义缩放ImageView
     */
    public void displayImageZoomSmall(String path, ImageView fakeImage) {
        // fakeImage.setLayoutParams(new RelativeLayout.LayoutParams(
        // targetImageSize.getWidth(), targetImageSize.getHeight()));
        fakeImage.setScaleType(ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(path, fakeImage, setOptions());
    }

    /**
     * 自定义 options 直接显示 有监听 有加载进度
     */
    public void displayImage(String path, ImageView fakeImage,
                             ImageLoadingListener loadingListener,
                             ImageLoadingProgressListener progressListener) {
        fakeImage.setScaleType(ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(path, fakeImage, setOptions(),
                loadingListener, progressListener);
    }

    protected abstract DisplayImageOptions setOptions();

    public abstract DisplayImageOptions setShowStubImage(int res);

    protected abstract DisplayImageOptions setRoundOptions(int round, int res);

    // 设置左边图片
    public void setDrawableLeft(final TextView v, String urlLeft, final int Dx, final int Dy) {
        ImageLoader.getInstance().loadImage(urlLeft,
                new SimpleImageLoadingListener() {
                    public void onLoadingComplete(String imageUri,
                                                  View view,
                                                  Bitmap loadedImage) {
                        Drawable drawable = new BitmapDrawable(loadedImage);
                        int x = Dx, y = Dy;
                        if (Dx == 0)
                            x = drawable.getMinimumWidth();
                        if (Dy == 0)
                            y = drawable.getMinimumHeight();
                        drawable.setBounds(0, 0, x, y);
                        v.setCompoundDrawables(drawable, null,
                                null, null); // imageView，你要显示的imageview控件对象，布局文件里面//配置的
                    }

                    ;
                });
    }

    // 设置上部边图片
    public void setDrawableTop(final TextView v, String urlTop, final int Dx, final int Dy) {
        ImageLoader.getInstance().loadImage(urlTop,
                new SimpleImageLoadingListener() {
                    public void onLoadingComplete(String imageUri,
                                                  View view,
                                                  Bitmap loadedImage) {
                        Drawable drawable = new BitmapDrawable(loadedImage);
                        int x = Dx, y = Dy;
                        if (x == 0)
                            x = drawable.getMinimumWidth();
                        if (y == 0)
                            y = drawable.getMinimumHeight();
                        drawable.setBounds(0, 0, x, y);
                        v.setCompoundDrawables(null, drawable,
                                null, null); // imageView，你要显示的imageview控件对象，布局文件里面//配置的
                    }

                    ;
                });
    }

    // 设置右边图片
    public void setDrawableRight(final TextView v, String urlRight, final int Dx, final int Dy) {
        ImageLoader.getInstance().loadImage(urlRight,
                new SimpleImageLoadingListener() {
                    public void onLoadingComplete(String imageUri,
                                                  View view,
                                                  Bitmap loadedImage) {
                        Drawable drawable = new BitmapDrawable(loadedImage);
                        int x = Dx, y = Dy;
                        if (Dx == 0)
                            x = drawable.getMinimumWidth();
                        if (Dy == 0)
                            y = drawable.getMinimumHeight();
                        drawable.setBounds(0, 0, x, y);
                        v.setCompoundDrawables(null, null,
                                drawable, null); // imageView，你要显示的imageview控件对象，布局文件里面//配置的
                    }

                    ;
                });
    }

    // 设置底部图片
    public void setDrawableBottom(final TextView v, String urlBottom, final int Dx, final int Dy) {
        ImageLoader.getInstance().loadImage(urlBottom,
                new SimpleImageLoadingListener() {
                    public void onLoadingComplete(String imageUri,
                                                  View view,
                                                  Bitmap loadedImage) {
                        Drawable drawable = new BitmapDrawable(loadedImage);
                        int x = Dx, y = Dy;
                        if (Dx == 0)
                            x = drawable.getMinimumWidth();
                        if (Dy == 0)
                            y = drawable.getMinimumHeight();
                        drawable.setBounds(0, 0, x, y);
                        v.setCompoundDrawables(null, null, null,
                                drawable); // imageView，你要显示的imageview控件对象，布局文件里面//配置的
                    }

                    ;
                });
    }
}
