package com.lz.mobilefollow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

/**recyclerview分页适配器*/
public abstract class BaseLoadMoreRecyclerAdapter<T extends Collection> extends RecyclerView.Adapter{

    /**是否有头部*/
	private boolean ifHasHeader=false;
	/**是否分页(默认不分页)*/
	private boolean ifHasPages=false;

	public Context context;
	/**数据源*/
	public T datas;

	/**上拉加载更多监听器*/
	private OnLoadMoreListener onLoadMoreListener;
	private MyRecyclerViewHolder holder;

	/**构造函数*/
	public BaseLoadMoreRecyclerAdapter(Context context, T datas){
		this.context=context;
		this.datas=datas;
	}
	/**是否有头部*/
	public void setIsHasHeader(boolean ifHasHeader){
		this.ifHasHeader=ifHasHeader;
	}
    /**是否分页*/
	public void setIsHasPages(boolean ifHasPages){
		this.ifHasPages=ifHasPages;
	}

	/**设置数据源*/
	public void setDatas(T datas){
		this.datas=datas;
	}

	/**获取数据源*/
	public T getDatas(){
		return this.datas;
	}
	/**设置上拉加载更多监听器*/
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
		this.onLoadMoreListener=onLoadMoreListener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view= LayoutInflater.from(context).inflate(viewType, parent, false);
		if (viewType == getHeaderView()) {
			holder=getHeaderViewHolder(view);
			return holder;
		} else if(viewType==getContentView()){
			holder=getViewHolder(view);
			return holder;
		}else{
			return new LoadMoreViewHolder(view);
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof MyRecyclerViewHolder) {
			if(ifHasHeader){//有头部需要加载判断头部
                if(position>0){
                    int indexPosition=position-1;
                    ((MyRecyclerViewHolder)holder).bindData(indexPosition);
                }else{
                        ((MyRecyclerViewHolder)holder).bindData(position);
                }
			}else{
                ((MyRecyclerViewHolder)holder).bindData(position);
            }
		} else if (holder instanceof LoadMoreViewHolder) {
			if(null!=onLoadMoreListener){
				onLoadMoreListener.onLoadMore();
			}
		}else{

		}
	}

	@Override
	public int getItemCount() {
		int size=datas==null?0:datas.size();
		if(ifHasHeader){//有头部
			size+=1;
		}
		if(ifHasPages){//有尾部（分页）
			size+=1;
		}
		return size;
	}

	@Override
	public int getItemViewType(int position) {
		if(ifHasHeader){//有头部
			if(position==0){
				return getHeaderView();
			}else{
				if(ifHasPages){//有尾部
					if(null!=datas&&(position==datas.size()+1)){
						return getLoadMoreView();
					}else{
						return getContentView();
					}
				}else{//没有尾部
                    return getContentView();
                }

			}
		}else {//没有头部
            if(ifHasPages){//有尾部
                if (null!=datas&&(position==datas.size())) {
                    return getLoadMoreView();
                } else {
                    return getContentView();
                }
            }else{//没有尾部
                return getContentView();
            }

		}
	}

	/**加载更多监听器*/
	public interface OnLoadMoreListener {
		void onLoadMore();
	}

	/**返回头部布局*/
	public abstract int getHeaderView();
	/**返回底部加载更多视图id*/
	public abstract int getLoadMoreView();
	/**返回item视图id*/
	public abstract int getContentView();
	/**返回头部视图适配器*/
	public abstract MyRecyclerViewHolder getHeaderViewHolder(View view);
	/**返回列表视图适配器*/
	public abstract MyRecyclerViewHolder getViewHolder(View view);
}

