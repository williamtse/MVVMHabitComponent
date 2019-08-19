package com.goldze.home.ui.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.goldze.base.entity.CatelogEntity;
import com.goldze.home.R;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ItemViewModel;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class GridViewModel extends ItemViewModel {
    public GridViewModel(@NonNull BaseViewModel viewModel, List<CatelogEntity.ItemsEntity> catalogs) {
        super(viewModel);
        //分类下的子分类
        for (CatelogEntity.ItemsEntity catalog:catalogs) {
            observableList.add(new CatalogItemViewModel(viewModel, catalog));
        }
    }



    //给RecyclerView添加ObservableList
    public ObservableList<CatalogItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<CatalogItemViewModel> itemBinding = ItemBinding.of(com.goldze.home.BR.viewModel, R.layout.grid_catalog);
}
