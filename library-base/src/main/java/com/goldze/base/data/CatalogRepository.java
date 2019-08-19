package com.goldze.base.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.goldze.base.data.source.HttpDataSource;
import com.goldze.base.data.source.LocalDataSource;
import com.goldze.base.entity.CatelogEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 * Created by goldze on 2019/3/26.
 */
public class CatalogRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static CatalogRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private CatalogRepository(@NonNull HttpDataSource httpDataSource,
                              @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static CatalogRepository getInstance(HttpDataSource httpDataSource,
                                                LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (CatalogRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CatalogRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<Object> login() {
        return mHttpDataSource.login();
    }


    @Override
    public Observable<BaseResponse<CatelogEntity>> getCategories() {
        return mHttpDataSource.getCategories();
    }

    @Override
    public Observable<BaseResponse<CatelogEntity>> getSubCategories(int parentId) {
        return mHttpDataSource.getSubCategories(parentId);
    }



    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }
}
