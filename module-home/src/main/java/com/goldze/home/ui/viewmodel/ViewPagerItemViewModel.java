package com.goldze.home.ui.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.goldze.base.data.CatalogRepository;
import com.goldze.base.entity.CatelogEntity;
import com.goldze.base.entity.ProductEntity;
import com.goldze.home.BR;
import com.goldze.home.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.binding.viewadapter.swiperefresh.ViewAdapter;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;
import okhttp3.internal.Internal;

/**
 * Created by goldze on 2018/7/18.
 */

public class ViewPagerItemViewModel extends ItemViewModel<HomeViewModel>  {
    public String text;
    public int page=0;
    private final CatalogRepository model;
    private List<CatelogEntity.ItemsEntity> catalogs = new ArrayList<>();
    private final int categoryId;
    public ObservableField<Integer> refreshStatus = new ObservableField<>();
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        public SingleLiveEvent finishRefreshing = new SingleLiveEvent<>();
        //上拉加载完成
        public SingleLiveEvent finishLoadmore = new SingleLiveEvent<>();

    }

    private static class RefreshStatus{
        public static final int LOAD_MORE = 1;
        public static final int REFRESH = 2;
        public static final int REFRESH_ING = 3;
    }

    /**
     * 刷新
     *
     * @param smartRefreshLayout
     * @param bindingCommand
     */
    @BindingAdapter(value = {"onSmartRefreshCommend"}, requireAll = false)
    public static void refresh(SmartRefreshLayout smartRefreshLayout, final BindingCommand bindingCommand) {
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (bindingCommand != null) {
                    bindingCommand.execute(RefreshStatus.LOAD_MORE);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (bindingCommand != null) {
                    bindingCommand.execute(RefreshStatus.REFRESH);
                    refreshLayout.finishRefresh();
                }
            }
        });
    }

    public BindingCommand<Integer> refreslistener = new BindingCommand<Integer>(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer integer) {
            refreshStatus.set(RefreshStatus.REFRESH_ING);
            if (integer == RefreshStatus.LOAD_MORE) {
                page++;

            } else if (integer == RefreshStatus.REFRESH) {
                page = 1;
                onLoad();
            }
        }
    });

    private void loadMoreProducts(){

    }

    private void onLoad(){
        observableList.clear();
        observableList.add(new BannerViewModel(viewModel));
        getSubCategories(this.categoryId);
    }

    public ViewPagerItemViewModel(@NonNull final HomeViewModel viewModel, CatalogRepository catalogRepository,
                                  CatelogEntity.ItemsEntity parent) {
        super(viewModel);
        this.text = parent.getTitle();
        this.model = catalogRepository;
        this.categoryId = parent.getId();
        this.refreshStatus.set(RefreshStatus.REFRESH_ING);
        onLoad();
    }

    private void getSubCategories(int parentId)
    {
        model.getSubCategories(parentId)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .subscribe(new Consumer<BaseResponse<CatelogEntity>>() {
                    @Override
                    public void accept(BaseResponse<CatelogEntity> response) throws Exception {
                        //清除列表
                        catalogs.clear();
                        //请求成功
                        KLog.e("加载分类成功");
                        KLog.e(response.getResult());
                        if (response.isOk()) {
                            for (CatelogEntity.ItemsEntity entity : response.getResult().getItems()) {
                                //双向绑定动态添加Item
                                catalogs.add(entity);
                            }
                            observableList.add(new GridViewModel(viewModel, catalogs));
                            observableList.add(new ProductListViewModel(viewModel));
                            refreshStatus.set(RefreshStatus.REFRESH_ING);
                        } else {
                            //code错误时也可以定义Observable回调到View层去处理
                            ToastUtils.showShort("数据错误");
                        }
                    }
                }, new Consumer<ResponseThrowable>() {
                    @Override
                    public void accept(ResponseThrowable throwable) throws Exception {
                        KLog.e("加载分类error");
                        KLog.e(throwable.getMessage());
                    }
                });
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
