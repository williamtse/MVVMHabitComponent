package com.goldze.home.ui.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.goldze.base.entity.ProductEntity;
import com.goldze.home.R;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ItemViewModel;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ProductListViewModel extends ItemViewModel {
    public ProductListViewModel(@NonNull BaseViewModel viewModel) {
        super(viewModel);
        for(int j = 0; j < 10; j++) {
            ProductEntity product = new ProductEntity();
            product.setId(1);
            product.setMainImg("https://img10.360buyimg.com/mobilecms/s250x250_jfs/t21766/109/531020260/338336/409b289f/5b0fcd66N06279b1b.jpg");
            product.setCatalogId(1);
            product.setOriginPrice(2099);
            product.setPrice(1999);
            product.setTotalSale(82000);
            observableProductList.add(new ProductItemViewModel(viewModel, product));
        }
    }

    //给RecyclerView添加ObservableList
    public ObservableList<ProductItemViewModel> observableProductList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<ProductItemViewModel> productItemBinding = ItemBinding.of(com.goldze.home.BR.viewModel, R.layout.grid_product);
}
