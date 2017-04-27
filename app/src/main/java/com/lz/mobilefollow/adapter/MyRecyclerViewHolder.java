package com.lz.mobilefollow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**recyclerview item管理器*/
 public class MyRecyclerViewHolder extends RecyclerView.ViewHolder{
	/**此holder是头部管理器*/
	public static final int HOLDER_TYPE_HEADER=0x01;
	/**此holder是item管理器管理器*/
	public static final int HOLDER_TYPE_CONTENT=0x02;
	/**此holder是尾部管理器*/
	public static final int HOLDER_TYPE_FOOTER=0x02;
	/**item根部局*/
	private View rootView;

	public MyRecyclerViewHolder(View itemView) {
		super(itemView);
		this.rootView=itemView;
		bindView(rootView);
	}
	/**返回管理器类型（默认为item布局管理器）*/
	public int getHolderType(){
		return HOLDER_TYPE_CONTENT;
	}
	/**视图管理器*/
	public void bindView(View itemView){}
	/**数据管理*/
	public void bindData(int position){}
}