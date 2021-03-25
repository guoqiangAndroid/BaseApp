package com.yang.base.base.mvp.pai;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIService {
    /**
     * 我的评价
     */
    @POST("toutiao/index")
    Flowable<ResponseBody> loadUs(@Query("type")  String type,
                                  @Query("key")String key);
}