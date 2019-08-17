package com.goldze.home.ui.adapter;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.goldze.base.entity.MoreTypeBean;
import com.goldze.home.R;
import com.goldze.home.ui.viewmodel.BannerViewModel;
import com.goldze.home.ui.viewmodel.CatalogItemViewModel;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.goldze.mvvmhabit.utils.KLog;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class RecyclerVarietyAdapter extends RecyclerView.Adapter {
    //定义三种常量  表示三种条目类型
    public static final int TYPE_BANNER = 0;
    public static final int TYPE_GRID = 1;
    public static final int TYPE_PRODUCT_LIST = 2;
    private List<MoreTypeBean> mData;
    private Context context;

    public RecyclerVarietyAdapter(Context context, List<MoreTypeBean> data) {
        this.mData = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        View view;
        if(viewType == TYPE_BANNER){
            view = LayoutInflater.from(context).inflate(R.layout.home_banner, parent, false);
            viewHolder = new BannerHolder(view);
        }else if (viewType == TYPE_GRID) {
            view = LayoutInflater.from(context).inflate(R.layout.home_grid, parent, false);
            viewHolder = new GridHolder(view);
        } else if (viewType == TYPE_PRODUCT_LIST) {
            view = LayoutInflater.from(context).inflate(R.layout.home_product_list, parent, false);
            viewHolder = new ProductListHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        KLog.e(position);
        switch (getItemViewType(position)) {
            case TYPE_BANNER:
                KLog.e("banner type");
                BannerHolder bannerHolder = (BannerHolder) holder;
                break;
            case TYPE_GRID:
                KLog.e("grid type");
                GridHolder gridHolder = (GridHolder) holder;
                break;
            case TYPE_PRODUCT_LIST:
                KLog.e("product type");
                ProductListHolder productListHolder = (ProductListHolder) holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //根据条件返回条目的类型
    @Override
    public int getItemViewType(int position) {
        if (mData.size() > 0) {
            return mData.get(position).type;
        }
        return super.getItemViewType(position);
    }

    /**
     * 创建三种ViewHolder
     */
    private class BannerHolder extends RecyclerView.ViewHolder {
        Banner banner;
        public String[] images = new String[] {
                "http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg",
                "http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg",
                "http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg",
                "http://img.zcool.cn/community/01fda356640b706ac725b2c8b99b08.jpg",
                "http://img.zcool.cn/community/01fd2756e142716ac72531cbf8bbbf.jpg",
                "http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg"};
        public BannerHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
            banner.setImages(Arrays.asList(images));
            banner.setImageLoader(new ImageLoader(){
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(context.getApplicationContext())
                            .load((String) path)
                            .into(imageView);
                }
            });
            banner.start();
        }
    }

    private class GridHolder extends RecyclerView.ViewHolder {

        public GridHolder(View itemView) {
            super(itemView);
            RecyclerView grids = itemView.findViewById(R.id.grids);
            GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
            //设置布局管理器
            grids.setLayoutManager(layoutManager);
            //设置Adapter;
            List<CatalogItemViewModel> data = new ArrayList<>();

        }
    }

    private class ProductListHolder extends RecyclerView.ViewHolder {

        public ProductListHolder(View itemView) {
            super(itemView);
        }
    }
}

