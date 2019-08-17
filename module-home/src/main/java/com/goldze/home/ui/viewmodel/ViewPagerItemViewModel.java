package com.goldze.home.ui.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.goldze.base.entity.CatelogEntity;
import com.goldze.base.entity.MoreTypeBean;
import com.goldze.base.entity.ProductEntity;
import com.goldze.home.BR;
import com.goldze.home.R;
import com.goldze.home.ui.adapter.RecyclerVarietyAdapter;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by goldze on 2018/7/18.
 */

public class ViewPagerItemViewModel extends ItemViewModel<HomeViewModel> {
    public String text;
    public List<ProductEntity> products;
    private RecyclerView mRecy;

    public ViewPagerItemViewModel(@NonNull final HomeViewModel viewModel, CatelogEntity.ItemsEntity catalog) {
        super(viewModel);
        this.text = catalog.getTitle();

        int catalogId = catalog.getId();
        observableList.add(new BannerViewModel(viewModel));
        observableList.add(new GridViewModel(viewModel));
        observableList.add(new ProductListViewModel(viewModel));
    }

    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView, ItemBinding<T> itemBinding, List<T> items,
                                      BindingRecyclerViewAdapter<T> adapter,
                                      BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
                                      BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory) {
        KLog.e("set Adapter");
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = new BindingRecyclerViewAdapter<>();
            } else {
                adapter = oldAdapter;
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        adapter.setItemBinding(itemBinding);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            KLog.e("oldAdapter != adapter");
            recyclerView.setAdapter(adapter);
        }
    }

    public RecyclerView.LayoutManager getLayoutManager(){
        return new LinearLayoutManager(viewModel.getApplication());
    }

    //给RecyclerView添加ObservableList
    public ObservableList<ItemViewModel> observableList = new ObservableArrayList<>();

    public final OnItemBind onItemBind = new OnItemBind() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, Object item) {
            int layout=0;
            switch (position){
                case 0:
                    layout = R.layout.home_banner;
                    break;
                case 1:
                    layout = R.layout.home_grid;
                    break;
                case 2:
                    layout = R.layout.home_product_list;
                    break;
            }
            itemBinding.set(BR.viewModel, layout);
        }
    };

}
