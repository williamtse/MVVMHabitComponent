package com.goldze.home.ui.viewmodel;

import android.support.annotation.NonNull;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ItemViewModel;

public class ProductTagItemViewModel extends ItemViewModel {
    public String text;
    public ProductTagItemViewModel(@NonNull BaseViewModel viewModel, String text) {
        super(viewModel);
        this.text = text;
    }
}
