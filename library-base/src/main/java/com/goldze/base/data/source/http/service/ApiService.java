package com.goldze.base.data.source.http.service;

import com.goldze.base.entity.CatelogEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by goldze on 2017/6/15.
 */

public interface ApiService {
//    @GET("action/apiv2/home_banner?catalog=1")
//    Observable<BaseResponse<DemoEntity>> demoGet();
//
//    @FormUrlEncoded
//    @POST("action/apiv2/home_banner")
//    Observable<BaseResponse<DemoEntity>> demoPost(@Field("catalog") String catalog);

    @GET("/mock/categories")
    Observable<BaseResponse<CatelogEntity>> getCategories();

    @GET("/mock/categories/{parentId}/subs")
    Observable<BaseResponse<CatelogEntity>> getSubCategories(@Path("parentId") int parentId);
}
