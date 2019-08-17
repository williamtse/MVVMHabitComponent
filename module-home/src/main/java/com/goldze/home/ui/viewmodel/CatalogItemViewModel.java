package com.goldze.home.ui.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;


import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ItemViewModel;

public class CatalogItemViewModel  extends ItemViewModel {
    public ObservableField<String> text = new ObservableField<>();

    public CatalogItemViewModel(@NonNull BaseViewModel viewModel, String text) {
        super(viewModel);
        this.text.set(text);
    }

}
