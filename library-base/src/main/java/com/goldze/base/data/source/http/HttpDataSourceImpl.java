package com.goldze.base.data.source.http;


import com.goldze.base.data.source.HttpDataSource;
import com.goldze.base.data.source.http.service.ApiService;
import com.goldze.base.entity.CatelogEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private ApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<Object> login() {
        return Observable.just(new Object()).delay(3, TimeUnit.SECONDS); //延迟3秒
    }

    @Override
    public Observable<BaseResponse<CatelogEntity>> getCategories() {
        KLog.e("HttpDataSourceImpl.getCategories");
        return apiService.getCategories();
    }
    @Override
    public Observable<BaseResponse<CatelogEntity>> getSubCategories(int parentId){
        return apiService.getSubCategories(parentId);
    }
}
