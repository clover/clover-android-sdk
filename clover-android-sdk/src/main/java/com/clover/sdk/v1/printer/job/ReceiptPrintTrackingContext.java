package com.clover.sdk.v1.printer.job;

import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;
import com.clover.sdk.JSONifiable;
import com.clover.sdk.v3.JsonHelper;
import com.clover.sdk.v3.JsonParcelHelper;
import com.clover.sdk.v3.Validator;
import com.clover.sdk.v3.payments.ReceiptTrackingDetails;
import org.json.JSONObject;

/** Per-print data used to report a successful payment receipt print. */
public final class ReceiptPrintTrackingContext extends GenericParcelable
    implements Validator, JSONifiable {

  public static final class Builder {
    private ReceiptTrackingScope scope;
    private String orderId;
    private String paymentId;
    private ReceiptTrackingDetails receiptDetails;

    public Builder scope(ReceiptTrackingScope scope) {
      this.scope = scope;
      return this;
    }

    public Builder orderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder paymentId(String paymentId) {
      this.paymentId = paymentId;
      return this;
    }

    public Builder receiptDetails(ReceiptTrackingDetails receiptDetails) {
      this.receiptDetails = receiptDetails;
      return this;
    }

    public ReceiptPrintTrackingContext build() {
      validateTarget(scope, orderId, paymentId, true);
      ReceiptPrintTrackingContext context = new ReceiptPrintTrackingContext();
      context.genClient.setOther(scope, CacheKey.scope);
      context.genClient.setOther(orderId, CacheKey.orderId);
      context.genClient.setOther(paymentId, CacheKey.paymentId);
      context.genClient.setRecord(receiptDetails, CacheKey.receiptDetails);
      context.genClient.resetChangeLog();
      return context;
    }
  }

  public ReceiptTrackingScope getScope() {
    return genClient.cacheGet(CacheKey.scope);
  }

  public String getOrderId() {
    return genClient.cacheGet(CacheKey.orderId);
  }

  public String getPaymentId() {
    return genClient.cacheGet(CacheKey.paymentId);
  }

  public ReceiptTrackingDetails getReceiptDetails() {
    return genClient.cacheGet(CacheKey.receiptDetails);
  }

  private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
    scope(com.clover.sdk.extractors.EnumExtractionStrategy.instance(ReceiptTrackingScope.class)),
    orderId(com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    paymentId(com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class)),
    receiptDetails(com.clover.sdk.extractors.RecordExtractionStrategy.instance(
        ReceiptTrackingDetails.JSON_CREATOR));

    private final com.clover.sdk.extractors.ExtractionStrategy extractionStrategy;

    CacheKey(com.clover.sdk.extractors.ExtractionStrategy extractionStrategy) {
      this.extractionStrategy = extractionStrategy;
    }

    @Override
    public com.clover.sdk.extractors.ExtractionStrategy getExtractionStrategy() {
      return extractionStrategy;
    }
  }

  private final GenericClient<ReceiptPrintTrackingContext> genClient;

  public ReceiptPrintTrackingContext() {
    genClient = new GenericClient<>(this);
  }

  public ReceiptPrintTrackingContext(String json) throws IllegalArgumentException {
    this();
    genClient.initJsonObject(json);
  }

  public ReceiptPrintTrackingContext(JSONObject jsonObject) {
    this();
    genClient.setJsonObject(jsonObject);
  }

  public ReceiptPrintTrackingContext(ReceiptPrintTrackingContext source) {
    this();
    if (source.genClient.getJsonObject() != null) {
      genClient.setJsonObject(JsonHelper.deepCopy(source.genClient.getJSONObject()));
    }
  }

  @Override
  protected GenericClient getGenericClient() {
    return genClient;
  }

  @Override
  public JSONObject getJSONObject() {
    return genClient.getJSONObject();
  }

  @Override
  public void validate() {
    validateTarget(getScope(), getOrderId(), getPaymentId(), false);
    if (getReceiptDetails() != null) {
      getReceiptDetails().validate();
    }
  }

  private static void validateTarget(
      ReceiptTrackingScope scope, String orderId, String paymentId, boolean builder) {
    if (scope == null) {
      failValidation("Receipt tracking scope is required", builder);
    }
    if (orderId == null || orderId.isEmpty()) {
      failValidation("Receipt tracking orderId is required", builder);
    }
    if (scope == ReceiptTrackingScope.ORDER && paymentId != null) {
      failValidation("Order-scoped receipt tracking must omit paymentId", builder);
    }
    if (scope == ReceiptTrackingScope.PAYMENT && (paymentId == null || paymentId.isEmpty())) {
      failValidation("Payment-scoped receipt tracking requires paymentId", builder);
    }
  }

  private static void failValidation(String message, boolean builder) {
    if (builder) {
      throw new IllegalStateException(message);
    }
    throw new IllegalArgumentException(message);
  }

  public static final Parcelable.Creator<ReceiptPrintTrackingContext> CREATOR =
      new Parcelable.Creator<ReceiptPrintTrackingContext>() {
        @Override
        public ReceiptPrintTrackingContext createFromParcel(Parcel in) {
          ReceiptPrintTrackingContext instance = new ReceiptPrintTrackingContext(
              JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
          instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
          instance.genClient.setChangeLog(in.readBundle());
          return instance;
        }

        @Override
        public ReceiptPrintTrackingContext[] newArray(int size) {
          return new ReceiptPrintTrackingContext[size];
        }
      };

  public static final JSONifiable.Creator<ReceiptPrintTrackingContext> JSON_CREATOR =
      new JSONifiable.Creator<ReceiptPrintTrackingContext>() {
        @Override
        public Class<ReceiptPrintTrackingContext> getCreatedClass() {
          return ReceiptPrintTrackingContext.class;
        }

        @Override
        public ReceiptPrintTrackingContext create(JSONObject jsonObject) {
          return new ReceiptPrintTrackingContext(jsonObject);
        }
      };
}
