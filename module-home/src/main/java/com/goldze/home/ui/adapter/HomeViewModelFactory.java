package com.goldze.home.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.goldze.base.base.Injection;
import com.goldze.base.data.CatalogRepository;
import com.goldze.home.ui.viewmodel.HomeViewModel;

/**
 * Created by goldze on 2019/3/26.
 */
public class HomeViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile HomeViewModelFactory INSTANCE;
    private final Application mApplication;
    private final CatalogRepository mRepository;

    public static HomeViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (HomeViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HomeViewModelFactory(application, Injection.provideCatalogRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HomeViewModelFactory(Application application, CatalogRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(mApplication, mRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
