package com.clover.sdk.v3.retrofit;

import com.clover.sdk.JSONifiable;
import com.clover.sdk.v3.base.Address;
import com.clover.sdk.v3.merchant.Merchant;
import com.clover.sdk.v3.order.Order;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

import static com.clover.sdk.v3.retrofit.SdkSame.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class SdkConverterFactoryTest {
  private static Retrofit mockRetrofit(MockCall c) {
    OkHttpClient httpClient = mock(OkHttpClient.class);
    when(httpClient.newCall(any(Request.class))).thenReturn(c);
    return new Retrofit.Builder()
        .baseUrl("https://example.com")
        .addConverterFactory(new SdkConverterFactory())
        .client(httpClient)
        .build();
  }

  private static Retrofit mockRetrofit(JSONifiable jsonifiable) {
    return mockRetrofit(new SdkCall(jsonifiable));
  }

  private static Retrofit mockRetrofit(String s) {
    return mockRetrofit(new StringCall(s));
  }

  @Test
  public void testConvertOrder() throws IOException {
    Order order = new Order();
    order.setId("RXKWT747REWXY");
    order.setNote("Hello, order.");
    order.setState("open");

    Retrofit retrofit = mockRetrofit(order);
    Api api = retrofit.create(Api.class);
    Call<Order> createOrder = api.createOrder("RXKWT747REWXY", order);
    assertNotNull(createOrder);
    Response<Order> createOrderResponse = createOrder.execute();
    assertNotNull(createOrderResponse);

    Order responseOrder = createOrderResponse.body();
    assertOrderSame(order, responseOrder);
  }

  @Test
  public void testUpdateMerchant() throws IOException {
    final Merchant merchant = new Merchant();
    merchant.setId("RXKWT747REWXY");

    Retrofit retrofit = mockRetrofit(merchant);
    Api api = retrofit.create(Api.class);
    Call<Merchant> getMerchant = api.updateMerchant(merchant);
    assertNotNull(getMerchant);
    Response<Merchant> getMerchantResponse = getMerchant.execute();
    assertNotNull(getMerchantResponse);

    assertMerchantsSame(merchant, getMerchantResponse.body());
  }

  @Test
  public void testUpdateMerchant_withAddress() throws IOException {
    final Merchant merchant = new Merchant();
    merchant.setId("RXKWT747REWXY");
    final Address address = new Address();
    address.setAddress1("415 N Mathilda Ave.");
    address.setCity("Sunnyvale");
    address.setState("CA");
    address.setZip("94085");
    merchant.setAddress(address);

    Retrofit retrofit = mockRetrofit(merchant);
    Api api = retrofit.create(Api.class);
    Call<Merchant> getMerchant = api.updateMerchant(merchant);
    assertNotNull(getMerchant);
    Response<Merchant> getMerchantResponse = getMerchant.execute();
    assertNotNull(getMerchantResponse);

    assertMerchantsSame(merchant, getMerchantResponse.body());
  }

  @Test
  public void testUpdateMerchant_emptyResponse() throws IOException {
    final Merchant merchant = new Merchant();
    merchant.setId("RXKWT747REWXY");

    Retrofit retrofit = mockRetrofit("");
    Api api = retrofit.create(Api.class);
    Call<Merchant> getMerchant = api.updateMerchant(merchant);
    assertNotNull(getMerchant);
    Response<Merchant> getMerchantResponse = getMerchant.execute();
    assertNotNull(getMerchantResponse);

    final Merchant responseMerchant = getMerchantResponse.body();
    assertNotNull(responseMerchant);
    assertNotEquals(merchant, responseMerchant);
    assertNull(responseMerchant.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMerchant_nullRequest() throws IOException {
    Retrofit retrofit = mockRetrofit("");
    Api api = retrofit.create(Api.class);
    Call<Merchant> getMerchant = api.updateMerchant(null);
    assertNotNull(getMerchant);
    getMerchant.execute();
  }

  @Test(expected = IOException.class)
  public void testUpdateMerchant_invalidResponse() throws IOException {
    final Merchant merchant = new Merchant();
    merchant.setId("RXKWT747REWXY");

    Retrofit retrofit = mockRetrofit("!THIS IS NOT JSON!");
    Api api = retrofit.create(Api.class);
    Call<Merchant> getMerchant = api.updateMerchant(merchant);
    assertNotNull(getMerchant);
    getMerchant.execute();
  }

}
