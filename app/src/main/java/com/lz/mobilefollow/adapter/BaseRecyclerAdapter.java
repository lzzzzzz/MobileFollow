package com.lz.mobilefollow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

/**recyclerview适配器*/
public abstract class BaseRecyclerAdapter<T extends Collection> extends RecyclerView.Adapter{

    /**是否有头部*/
	private boolean ifHasHeader=false;
	/**是否有尾部*/
	private boolean ifHasFooter=false;

	public Context context;
	/**数据源*/
	public T datas;
	private MyRecyclerViewHolder holder;

	/**构造函数*/
	public BaseRecyclerAdapter(Context context, T datas){
		this.context=context;
		this.datas=datas;
	}
	/**是否有头部*/
	public void setIsHasHeader(boolean ifHasHeader){
		this.ifHasHeader=ifHasHeader;
	}
    /**是否分页*/
	public void setIsHasFooter(boolean ifHasFooter){
		this.ifHasFooter=ifHasFooter;
	}

	/**设置数据源*/
	public void setDatas(T datas){
		this.datas=datas;
	}

	/**获取数据源*/
	public T getDatas(){
		return this.datas;
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
			holder=getFooterViewHolder(view);
			return holder;
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof MyRecyclerViewHolder) {
			int type=((MyRecyclerViewHolder)holder).getHolderType();
			if(type==((MyRecyclerViewHolder)holder).HOLDER_TYPE_HEADER){//头
				((MyRecyclerViewHolder)holder).bindData(position);
			}else if(type==((MyRecyclerViewHolder)holder).HOLDER_TYPE_CONTENT){//item
				if(ifHasHeader){
					int indexPosition=position-1;
					((MyRecyclerViewHolder)holder).bindData(indexPosition);
				}else{
					((MyRecyclerViewHolder)holder).bindData(position);
				}
			}else if(type==((MyRecyclerViewHolder)holder).HOLDER_TYPE_FOOTER){//尾
				((MyRecyclerViewHolder)holder).bindData(position);
			}
		}
	}

	@Override
	public int getItemCount() {
		int size=(datas==null?0:datas.size());
		if(ifHasHeader){//有头部
			size+=1;
		}
		if(ifHasFooter){//有尾部（分页）
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
				if(ifHasFooter){//有尾部
					int posi=(datas==null?0:datas.size());
					if((position==posi+1)){
						return getFooterView();
					}else{
						return getContentView();
					}
				}else{//没有尾部
                    return getContentView();
                }

			}
		}else {//没有头部
            if(ifHasFooter){//有尾部
				int posi=(datas==null?0:datas.size());
                if (position==posi) {
                    return getFooterView();
                } else {
                    return getContentView();
                }
            }else{//没有尾部
                return getContentView();
            }

		}
	}

	/**返回头部布局*/
	public abstract int getHeaderView();
	/**返回底部加载更多视图id*/
	public abstract int getFooterView();
	/**返回item视图id*/
	public abstract int getContentView();
	/**返回头部视图适配器*/
	public abstract MyRecyclerViewHolder getHeaderViewHolder(View view);
	/**返回列表视图适配器*/
	public abstract MyRecyclerViewHolder getViewHolder(View view);
	/**返回列表视图适配器*/
	public abstract MyRecyclerViewHolder getFooterViewHolder(View view);
}

