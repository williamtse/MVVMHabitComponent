package com.goldze.home.ui.viewmodel;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.goldze.base.data.CatalogRepository;
import com.goldze.base.entity.CatelogEntity;
import com.goldze.home.R;
import com.goldze.home.BR;
import com.goldze.home.ui.adapter.ViewPagerBindingAdapter;
import com.youth.banner.Banner;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2018/6/21.
 */

public class HomeViewModel extends BaseViewModel<CatalogRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();
    public HomeViewModel(@NonNull Application application, CatalogRepository repository) {
        super(application, repository);
    }

    public void addPage() {
        model.getCategories()
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider())) //请求与View周期同步（过度期，尽量少使用）
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog("正在请求...");
                    }
                })
                .subscribe(new Consumer<BaseResponse<CatelogEntity>>() {
                    @Override
                    public void accept(BaseResponse<CatelogEntity> response) throws Exception {
                        //清除列表
                        items.clear();
                        //请求成功
                        KLog.e("加载分类成功");
                        KLog.e(response.getResult());
                        if (response.isOk()) {
                            for (CatelogEntity.ItemsEntity entity : response.getResult().getItems()) {
                                ViewPagerItemViewModel itemViewModel = new ViewPagerItemViewModel(HomeViewModel.this, entity);
                                //双向绑定动态添加Item
                                items.add(itemViewModel);
                            }
                            dismissDialog();
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

    //给ViewPager添加ObservableList
    public ObservableList<ViewPagerItemViewModel> items = new ObservableArrayList<>();
    //给ViewPager添加ItemBinding
    public ItemBinding<ViewPagerItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_viewpager);
    //给ViewPager添加PageTitle
    public final BindingViewPagerAdapter.PageTitles<ViewPagerItemViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<ViewPagerItemViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, ViewPagerItemViewModel item) {
            return item.text;
        }
    };
    //给ViewPager添加Adpter，请使用自定义的Adapter继承BindingViewPagerAdapter，重写onBindBinding方法
    public final ViewPagerBindingAdapter adapter = new ViewPagerBindingAdapter();
    //ViewPager切换监听
    public BindingCommand<Integer> onPageSelectedCommand = new BindingCommand<>(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer index) {
            //ToastUtils.showShort("ViewPager切换：" + index);
        }
    });
}
