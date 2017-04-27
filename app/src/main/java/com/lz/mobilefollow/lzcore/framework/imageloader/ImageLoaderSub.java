package com.lz.mobilefollow.lzcore.framework.imageloader;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoaderSub extends ImageLoaderAbs{
	
	private DisplayImageOptions defaultOptions;
	
	@Override
	protected synchronized DisplayImageOptions setOptions() {
		if(defaultOptions != null){
			return defaultOptions;
		}
		defaultOptions = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.ic_empty_stub)
//		.showImageForEmptyUri(R.drawable.ic_empty_stub)
//		.showImageOnFail(R.drawable.ic_empty_stub)
		.cacheInMemory(false).cacheOnDisk(true)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		return defaultOptions;
	}

	@Override
	public DisplayImageOptions setShowStubImage(int res) {
		defaultOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(res)
		.showImageForEmptyUri(res)
		.showImageOnFail(res)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.cacheInMemory(false).cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		return defaultOptions;
	}

	@Override
	protected DisplayImageOptions setRoundOptions(int round, int res) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(res)
				.showImageForEmptyUri(res)
				.showImageOnFail(res)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.resetViewBeforeLoading(true)
				.cacheInMemory(false)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true)
				.displayer(new RoundedBitmapDisplayer(round))
				.build();
				return options;
	}

}
