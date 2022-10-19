package com.clover.sdk.v3.retrofit;

import com.clover.sdk.v3.base.Address;
import com.clover.sdk.v3.merchant.Merchant;
import com.clover.sdk.v3.order.Order;

import okhttp3.RequestBody;
import okio.Buffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.clover.sdk.v3.retrofit.SdkSame.assertMerchantsSame;
import static com.clover.sdk.v3.retrofit.SdkSame.assertOrderSame;

@RunWith(RobolectricTestRunner.class)
public class JsonifiableRequestConverterTest {
  @Test
  public void testOrderConversion() throws IOException {

    final Order order = new Order();
    order.setId("RXKWT747REWXY");
    order.setNote("This is an order note");
    order.setState("open");

    JSONifiableRequestConverter rc = new JSONifiableRequestConverter(Order.class);
    final RequestBody body = rc.convert(order);
    final Buffer buffer = new Buffer();
    body.writeTo(buffer);
    final String content = buffer.readString(StandardCharsets.UTF_8);
    final Order convertedOrder = new Order(content);

    assertOrderSame(order, convertedOrder);
  }

  @Test
  public void testMerchantConversion() throws IOException {
    final Merchant merchant = new Merchant();
    merchant.setId("RXKWT747REWXY");
    final Address address = new Address();
    address.setAddress1("415 N Mathilda Ave.");
    address.setCity("Sunnyvale");
    address.setState("CA");
    address.setZip("94085");
    merchant.setAddress(address);

    JSONifiableRequestConverter rc = new JSONifiableRequestConverter(Merchant.class);
    final RequestBody body = rc.convert(merchant);
    final Buffer buffer = new Buffer();
    body.writeTo(buffer);
    final String content = buffer.readString(StandardCharsets.UTF_8);
    final Merchant convertedMerchant = new Merchant(content);

    assertMerchantsSame(merchant, convertedMerchant);

  }
}
