package com.clover.sdk.v3.retrofit;

import com.clover.sdk.v3.base.Address;
import com.clover.sdk.v3.merchant.Merchant;
import com.clover.sdk.v3.order.Order;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.clover.sdk.v3.retrofit.SdkSame.assertMerchantsSame;
import static com.clover.sdk.v3.retrofit.SdkSame.assertOrderSame;

@RunWith(RobolectricTestRunner.class)
public class JsonifiableResposeConverterTest {
  private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8");

  @Test
  public void testOrderConversion() throws IOException {

    final Order order = new Order();
    order.setId("RXKWT747REWXY");
    order.setNote("This is an order note");
    order.setState("open");

    ResponseBody body = ResponseBody.create(MEDIA_TYPE_JSON, order.getJSONObject().toString());

    JSONifiableResponseConverter rc = new JSONifiableResponseConverter(Order.class);
    final Order convertedOrder = (Order) rc.convert(body);

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

    ResponseBody body = ResponseBody.create(MEDIA_TYPE_JSON, merchant.getJSONObject().toString());

    JSONifiableResponseConverter rc = new JSONifiableResponseConverter(Merchant.class);
    final Merchant convertedMerchant = (Merchant) rc.convert(body);

    assertMerchantsSame(merchant, convertedMerchant);
  }
}
