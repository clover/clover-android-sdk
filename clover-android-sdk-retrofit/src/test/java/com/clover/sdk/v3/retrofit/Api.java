package com.clover.sdk.v3.retrofit;

import com.clover.sdk.v3.merchant.Merchant;
import com.clover.sdk.v3.order.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * A fake {@link retrofit2.Retrofit} API for testing {@link SdkConverterFactory}.
 * These never actually hit and server / endpoint.
 */
public interface Api {
  @POST("/v3/merchants/{mid}/orders")
  Call<Order> createOrder(@Path("mid") String mid, @Body Order order);

  @POST("/v3/merchants")
  Call<Merchant> updateMerchant(@Body Merchant merchant);
}
