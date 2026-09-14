package com.clover.sdk.v1.printer.job;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.clover.sdk.GenericClient;
import com.clover.sdk.GenericParcelable;
import com.clover.sdk.JSONifiable;
import com.clover.sdk.v1.printer.Category;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Print job for cash-event receipt printing.
 *
 * IMPORTANT:
 * - Keep class name and package exactly as-is for engine compatibility.
 * - Engine reflects fields/getters: cashEvent/getCashEvent, receiptVariant/getReceiptVariant,
 *   transactionId/getTransactionId.
 */
public class CashEventPrintJob extends PrintJob implements Parcelable {

  public static class Builder extends PrintJob.Builder {
    protected CashEventPayload cashEvent;
    protected String receiptVariant;
    protected String transactionId;

    public Builder cashEventPrintJob(CashEventPrintJob pj) {
      printJob(pj);
      this.cashEvent = pj.cashEvent;
      this.receiptVariant = pj.receiptVariant;
      this.transactionId = pj.transactionId;
      return this;
    }

    public Builder cashEvent(CashEventPayload cashEvent) {
      this.cashEvent = cashEvent;
      return this;
    }

    public Builder receiptVariant(String receiptVariant) {
      this.receiptVariant = receiptVariant;
      return this;
    }

    public Builder receiptVariant(@Nullable ReceiptVariant receiptVariant) {
      this.receiptVariant = receiptVariant == null ? null : receiptVariant.value();
      return this;
    }

    public Builder transactionId(@Nullable String transactionId) {
      this.transactionId = transactionId;
      return this;
    }

    @Override
    public Builder flag(int flag) {
      super.flag(flag);
      return this;
    }

    @Override
    public Builder includePrintGroups(boolean includePrintGroups) {
      super.includePrintGroups(includePrintGroups);
      return this;
    }

    @Override
    @Deprecated
    public Builder printToAny(boolean printToAny) {
      super.printToAny(printToAny);
      return this;
    }

    @Override
    public CashEventPrintJob build() {
      return new CashEventPrintJob(this);
    }
  }

  public enum ReceiptVariant {
    STARTING_BALANCE("STARTING_BALANCE"),
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    CASH_INSPECTION("CASH_INSPECTION");

    private final String value;

    ReceiptVariant(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }

    @Nullable
    public static ReceiptVariant fromValue(@Nullable String value) {
      if (value == null) {
        return null;
      }
      for (ReceiptVariant variant : values()) {
        if (variant.value.equals(value)) {
          return variant;
        }
      }
      return null;
    }

    @Override
    public String toString() {
      return value;
    }
  }


  public final CashEventPayload cashEvent;
  public final String receiptVariant;
  public final String transactionId;

  public CashEventPrintJob(
      CashEventPayload cashEvent,
      String receiptVariant,
      @Nullable String transactionId
  ) {
    this(new Builder()
        .cashEvent(cashEvent)
        .receiptVariant(receiptVariant)
        .transactionId(transactionId));
  }

  public CashEventPrintJob(
      CashEventPayload cashEvent,
      @Nullable ReceiptVariant receiptVariant,
      @Nullable String transactionId
  ) {
    this(new Builder()
        .cashEvent(cashEvent)
        .receiptVariant(receiptVariant)
        .transactionId(transactionId));
  }

  protected CashEventPrintJob(Builder builder) {
    super(builder);
    this.cashEvent = builder.cashEvent;
    this.receiptVariant = builder.receiptVariant;
    this.transactionId = builder.transactionId;
  }

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  protected CashEventPrintJob(Parcel in) {
    super(in);
    this.cashEvent = in.readParcelable(CashEventPayload.class.getClassLoader());
    this.receiptVariant = in.readString();
    this.transactionId = in.readString();
  }

  public CashEventPayload getCashEvent() {
    return cashEvent;
  }

  public String getReceiptVariant() {
    return receiptVariant;
  }

  @Nullable
  public ReceiptVariant getReceiptVariantEnum() {
    return ReceiptVariant.fromValue(receiptVariant);
  }

  @Nullable
  public String getTransactionId() {
    return transactionId;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(cashEvent, flags);
    dest.writeString(receiptVariant);
    dest.writeString(transactionId);
  }

  public static final Creator<CashEventPrintJob> CREATOR = new Creator<CashEventPrintJob>() {
    @Override
    public CashEventPrintJob createFromParcel(Parcel in) {
      return new CashEventPrintJob(in);
    }

    @Override
    public CashEventPrintJob[] newArray(int size) {
      return new CashEventPrintJob[size];
    }
  };

  /**
   * Payload object intentionally shaped for engine parsing.
   * Engine can parse from getJSONObject() and/or direct getters.
   */
  public static class CashReconciliation extends GenericParcelable implements com.clover.sdk.v3.Validator, com.clover.sdk.JSONifiable {
    private enum CacheKey implements com.clover.sdk.ExtractionStrategyEnum {
      startingCashBalance(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      cashSales(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      cashDeposits(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      cashWithdrawals(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      cashRefunds(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      expectedCashBalance(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      cashCount(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      cashDifference(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      expectedNonCashBalance(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      nonCashCount(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      nonCashDifference(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      totalDifference(com.clover.sdk.extractors.BasicExtractionStrategy.instance(Long.class)),
      comment(com.clover.sdk.extractors.BasicExtractionStrategy.instance(String.class));

      private final com.clover.sdk.extractors.ExtractionStrategy extractionStrategy;

      CacheKey(com.clover.sdk.extractors.ExtractionStrategy extractionStrategy) {
        this.extractionStrategy = extractionStrategy;
      }

      @Override
      public com.clover.sdk.extractors.ExtractionStrategy getExtractionStrategy() {
        return extractionStrategy;
      }
    }

    private final GenericClient<CashReconciliation> genClient;

    public CashReconciliation() {
      genClient = new GenericClient<CashReconciliation>(this);
    }

    protected CashReconciliation(boolean noInit) {
      genClient = null;
    }

    public CashReconciliation(String json) throws IllegalArgumentException {
      this();
      genClient.initJsonObject(json);
    }

    public CashReconciliation(JSONObject jsonObject) {
      this();
      genClient.setJsonObject(jsonObject);
    }

    public CashReconciliation(CashReconciliation src) {
      this();
      if (src.genClient.getJsonObject() != null) {
        genClient.setJsonObject(com.clover.sdk.v3.JsonHelper.deepCopy(src.genClient.getJSONObject()));
      }
    }

    @Override
    public void validate() throws JSONException {
      // Intentionally no field-level constraints for this payload object.
    }

    public static class Builder {
      protected Long startingCashBalance;
      protected Long cashSales;
      protected Long cashDeposits;
      protected Long cashWithdrawals;
      protected Long cashRefunds;
      protected Long expectedCashBalance;
      protected Long cashCount;
      protected Long cashDifference;
      protected Long expectedNonCashBalance;
      protected Long nonCashCount;
      protected Long nonCashDifference;
      protected Long totalDifference;
      protected String comment;

      public Builder cashReconciliation(CashReconciliation reconciliation) {
        this.startingCashBalance = reconciliation.getStartingCashBalance();
        this.cashSales = reconciliation.getCashSales();
        this.cashDeposits = reconciliation.getCashDeposits();
        this.cashWithdrawals = reconciliation.getCashWithdrawals();
        this.cashRefunds = reconciliation.getCashRefunds();
        this.expectedCashBalance = reconciliation.getExpectedCashBalance();
        this.cashCount = reconciliation.getCashCount();
        this.cashDifference = reconciliation.getCashDifference();
        this.expectedNonCashBalance = reconciliation.getExpectedNonCashBalance();
        this.nonCashCount = reconciliation.getNonCashCount();
        this.nonCashDifference = reconciliation.getNonCashDifference();
        this.totalDifference = reconciliation.getTotalDifference();
        this.comment = reconciliation.getComment();
        return this;
      }

      public Builder startingCashBalance(@Nullable Long startingCashBalance) {
        this.startingCashBalance = startingCashBalance;
        return this;
      }

      public Builder cashSales(@Nullable Long cashSales) {
        this.cashSales = cashSales;
        return this;
      }

      public Builder cashDeposits(@Nullable Long cashDeposits) {
        this.cashDeposits = cashDeposits;
        return this;
      }

      public Builder cashWithdrawals(@Nullable Long cashWithdrawals) {
        this.cashWithdrawals = cashWithdrawals;
        return this;
      }

      public Builder cashRefunds(@Nullable Long cashRefunds) {
        this.cashRefunds = cashRefunds;
        return this;
      }

      public Builder expectedCashBalance(@Nullable Long expectedCashBalance) {
        this.expectedCashBalance = expectedCashBalance;
        return this;
      }

      public Builder cashCount(@Nullable Long cashCount) {
        this.cashCount = cashCount;
        return this;
      }

      public Builder cashDifference(@Nullable Long cashDifference) {
        this.cashDifference = cashDifference;
        return this;
      }

      public Builder expectedNonCashBalance(@Nullable Long expectedNonCashBalance) {
        this.expectedNonCashBalance = expectedNonCashBalance;
        return this;
      }

      public Builder nonCashCount(@Nullable Long nonCashCount) {
        this.nonCashCount = nonCashCount;
        return this;
      }

      public Builder nonCashDifference(@Nullable Long nonCashDifference) {
        this.nonCashDifference = nonCashDifference;
        return this;
      }

      public Builder totalDifference(@Nullable Long totalDifference) {
        this.totalDifference = totalDifference;
        return this;
      }

      public Builder comment(@Nullable String comment) {
        this.comment = comment;
        return this;
      }

      public CashReconciliation build() {
        return new CashReconciliation(
            startingCashBalance,
            cashSales,
            cashDeposits,
            cashWithdrawals,
            cashRefunds,
            expectedCashBalance,
            cashCount,
            cashDifference,
            expectedNonCashBalance,
            nonCashCount,
            nonCashDifference,
            totalDifference,
            comment);
      }
    }


    public CashReconciliation(
        @Nullable Long startingCashBalance,
        @Nullable Long cashSales,
        @Nullable Long cashDeposits,
        @Nullable Long cashWithdrawals,
        @Nullable Long cashRefunds,
        @Nullable Long expectedCashBalance,
        @Nullable Long cashCount,
        @Nullable Long cashDifference,
        @Nullable Long expectedNonCashBalance,
        @Nullable Long nonCashCount,
        @Nullable Long nonCashDifference,
        @Nullable Long totalDifference,
        @Nullable String comment
    ) {
      this();
      setStartingCashBalance(startingCashBalance);
      setCashSales(cashSales);
      setCashDeposits(cashDeposits);
      setCashWithdrawals(cashWithdrawals);
      setCashRefunds(cashRefunds);
      setExpectedCashBalance(expectedCashBalance);
      setCashCount(cashCount);
      setCashDifference(cashDifference);
      setExpectedNonCashBalance(expectedNonCashBalance);
      setNonCashCount(nonCashCount);
      setNonCashDifference(nonCashDifference);
      setTotalDifference(totalDifference);
      setComment(comment);
      resetChangeLog();
    }

    @Nullable
    public Long getStartingCashBalance() {
      return genClient.cacheGet(CacheKey.startingCashBalance);
    }

    @Nullable
    public Long getCashSales() {
      return genClient.cacheGet(CacheKey.cashSales);
    }

    @Nullable
    public Long getCashDeposits() {
      return genClient.cacheGet(CacheKey.cashDeposits);
    }

    @Nullable
    public Long getCashWithdrawals() {
      return genClient.cacheGet(CacheKey.cashWithdrawals);
    }

    @Nullable
    public Long getCashRefunds() {
      return genClient.cacheGet(CacheKey.cashRefunds);
    }

    @Nullable
    public Long getExpectedCashBalance() {
      return genClient.cacheGet(CacheKey.expectedCashBalance);
    }

    @Nullable
    public Long getCashCount() {
      return genClient.cacheGet(CacheKey.cashCount);
    }

    @Nullable
    public Long getCashDifference() {
      return genClient.cacheGet(CacheKey.cashDifference);
    }

    @Nullable
    public Long getExpectedNonCashBalance() {
      return genClient.cacheGet(CacheKey.expectedNonCashBalance);
    }

    @Nullable
    public Long getNonCashCount() {
      return genClient.cacheGet(CacheKey.nonCashCount);
    }

    @Nullable
    public Long getNonCashDifference() {
      return genClient.cacheGet(CacheKey.nonCashDifference);
    }

    @Nullable
    public Long getTotalDifference() {
      return genClient.cacheGet(CacheKey.totalDifference);
    }

    @Nullable
    public String getComment() {
      return genClient.cacheGet(CacheKey.comment);
    }

    public JSONObject getJSONObject() {
      return genClient.getJSONObject();
    }

    public CashReconciliation setStartingCashBalance(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.startingCashBalance);
    }

    public CashReconciliation setCashSales(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.cashSales);
    }

    public CashReconciliation setCashDeposits(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.cashDeposits);
    }

    public CashReconciliation setCashWithdrawals(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.cashWithdrawals);
    }

    public CashReconciliation setCashRefunds(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.cashRefunds);
    }

    public CashReconciliation setExpectedCashBalance(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.expectedCashBalance);
    }

    public CashReconciliation setCashCount(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.cashCount);
    }

    public CashReconciliation setCashDifference(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.cashDifference);
    }

    public CashReconciliation setExpectedNonCashBalance(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.expectedNonCashBalance);
    }

    public CashReconciliation setNonCashCount(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.nonCashCount);
    }

    public CashReconciliation setNonCashDifference(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.nonCashDifference);
    }

    public CashReconciliation setTotalDifference(@Nullable Long value) {
      return genClient.setOther(value, CacheKey.totalDifference);
    }

    public CashReconciliation setComment(@Nullable String value) {
      return genClient.setOther(value, CacheKey.comment);
    }

    public boolean containsChanges() {
      return genClient.containsChanges();
    }

    public void resetChangeLog() {
      genClient.resetChangeLog();
    }

    public CashReconciliation copyChanges() {
      CashReconciliation copy = new CashReconciliation();
      copy.mergeChanges(this);
      copy.resetChangeLog();
      return copy;
    }

    public void mergeChanges(CashReconciliation src) {
      if (src.genClient.getChangeLog() != null) {
        genClient.mergeChanges(new CashReconciliation(src).getJSONObject(), src.genClient);
      }
    }

    @Override
    protected GenericClient getGenericClient() {
      return genClient;
    }

    public static final Parcelable.Creator<CashReconciliation> CREATOR = new Parcelable.Creator<CashReconciliation>() {
      @Override
      public CashReconciliation createFromParcel(Parcel in) {
        CashReconciliation instance = new CashReconciliation(com.clover.sdk.v3.JsonParcelHelper.ObjectWrapper.CREATOR.createFromParcel(in).unwrap());
        instance.genClient.setBundle(in.readBundle(getClass().getClassLoader()));
        instance.genClient.setChangeLog(in.readBundle());
        return instance;
      }

      @Override
      public CashReconciliation[] newArray(int size) {
        return new CashReconciliation[size];
      }
    };

    public static final JSONifiable.Creator<CashReconciliation> JSON_CREATOR = new JSONifiable.Creator<CashReconciliation>() {
      @Override
      public Class<CashReconciliation> getCreatedClass() {
        return CashReconciliation.class;
      }

      @Override
      public CashReconciliation create(JSONObject jsonObject) {
        return new CashReconciliation(jsonObject);
      }
    };
  }

  public static class CashEventPayload implements Parcelable {
    private static final String BUNDLE_TYPE = "type";
    private static final String BUNDLE_AMOUNT_CHANGE = "amountChange";
    private static final String BUNDLE_TIMESTAMP = "timestamp";
    private static final String BUNDLE_NOTE = "note";
    private static final String BUNDLE_EMPLOYEE = "employee";
    private static final String BUNDLE_DEVICE_ID = "deviceId";
    private static final String BUNDLE_RECONCILIATION = "reconciliation";
    private static final String BUNDLE_ID = "id";
    private static final String BUNDLE_EXPECTED_BALANCE = "expectedBalance";

    public static class Builder {
      protected String type;
      protected Long amountChange;
      protected Long timestamp;
      protected String note;
      protected String employee;
      protected String deviceId;

      protected CashReconciliation reconciliation;
      protected String id;
      protected Long expectedBalance;

      public Builder cashEventPayload(CashEventPayload payload) {
        this.type = payload.type;
        this.amountChange = payload.amountChange;
        this.timestamp = payload.timestamp;
        this.note = payload.note;
        this.employee = payload.employee;
        this.deviceId = payload.deviceId;
        this.reconciliation = payload.reconciliation;
        this.id = payload.id;
        this.expectedBalance = payload.expectedBalance;
        return this;
      }

      public Builder type(@Nullable String type) {
        this.type = type;
        return this;
      }

      public Builder amountChange(@Nullable Long amountChange) {
        this.amountChange = amountChange;
        return this;
      }

      public Builder timestamp(@Nullable Long timestamp) {
        this.timestamp = timestamp;
        return this;
      }

      public Builder note(@Nullable String note) {
        this.note = note;
        return this;
      }

      public Builder employee(@Nullable String employee) {
        this.employee = employee;
        return this;
      }

      public Builder deviceId(@Nullable String deviceId) {
        this.deviceId = deviceId;
        return this;
      }

      public Builder reconciliation(@Nullable CashReconciliation reconciliation) {
        this.reconciliation = reconciliation;
        return this;
      }

      public Builder id(@Nullable String id) {
        this.id = id;
        return this;
      }

      public Builder expectedBalance(@Nullable Long expectedBalance) {
        this.expectedBalance = expectedBalance;
        return this;
      }

      public CashEventPayload build() {
        return new CashEventPayload(
            type,
            amountChange,
            timestamp,
            note,
            employee,
            deviceId,
            reconciliation,
            id,
            expectedBalance);
      }
    }

    public final String type;
    public final Long amountChange;
    public final Long timestamp;
    public final String note;
    public final String employee;
    public final String deviceId;
    public final CashReconciliation reconciliation;
    public final String id;
    public final Long expectedBalance;

    public CashEventPayload(
        @Nullable String type,
        @Nullable Long amountChange,
        @Nullable Long timestamp,
        @Nullable String note,
        @Nullable String employee,
        @Nullable String deviceId,
        @Nullable CashReconciliation reconciliation,
        @Nullable String id,
        @Nullable Long expectedBalance
    ) {
      this.type = type;
      this.amountChange = amountChange;
      this.timestamp = timestamp;
      this.note = note;
      this.employee = employee;
      this.deviceId = deviceId;
      this.reconciliation = reconciliation;
      this.id = id;
      this.expectedBalance = expectedBalance;
    }

    protected CashEventPayload(Parcel in) {
      Bundle bundle = in.readBundle(getClass().getClassLoader());
      if (bundle == null) {
        this.type = null;
        this.amountChange = null;
        this.timestamp = null;
        this.note = null;
        this.employee = null;
        this.deviceId = null;
        this.reconciliation = null;
        this.id = null;
        this.expectedBalance = null;
        return;
      }

      this.type = bundle.getString(BUNDLE_TYPE);
      this.amountChange = bundle.containsKey(BUNDLE_AMOUNT_CHANGE)
          ? bundle.getLong(BUNDLE_AMOUNT_CHANGE)
          : null;
      this.timestamp = bundle.containsKey(BUNDLE_TIMESTAMP)
          ? bundle.getLong(BUNDLE_TIMESTAMP)
          : null;
      this.note = bundle.getString(BUNDLE_NOTE);
      this.employee = bundle.getString(BUNDLE_EMPLOYEE);
      this.deviceId = bundle.getString(BUNDLE_DEVICE_ID);
      this.reconciliation = bundle.getParcelable(BUNDLE_RECONCILIATION);
      this.id = bundle.getString(BUNDLE_ID);
      this.expectedBalance = bundle.containsKey(BUNDLE_EXPECTED_BALANCE)
          ? bundle.getLong(BUNDLE_EXPECTED_BALANCE)
          : null;
    }

    @Nullable
    public String getType() {
      return type;
    }

    @Nullable
    public Long getAmountChange() {
      return amountChange;
    }

    @Nullable
    public Long getTimestamp() {
      return timestamp;
    }

    @Nullable
    public String getNote() {
      return note;
    }

    @Nullable
    public String getEmployee() {
      return employee;
    }

    @Nullable
    public String getDeviceId() {
      return deviceId;
    }

    @Nullable
    public CashReconciliation getReconciliation() {
      return reconciliation;
    }

    @Nullable
    public String getId() {
      return id;
    }

    @Nullable
    public Long getExpectedBalance() {
      return expectedBalance;
    }

    /**
     * Engine will read this via reflection when available.
     */
    public JSONObject getJSONObject() {
      JSONObject obj = new JSONObject();
      try {
        if (type != null) obj.put("type", type);
        if (amountChange != null) obj.put("amountChange", amountChange);
        if (timestamp != null) obj.put("timestamp", timestamp);
        if (note != null) obj.put("note", note);
        if (id != null) obj.put("id", id);
        if (expectedBalance != null) obj.put("expectedBalance", expectedBalance);

        if (employee != null) obj.put("employee", employee);

        if (deviceId != null) {
          obj.put("deviceId", deviceId);
        }

        CashReconciliation rec = getReconciliation();
        if (rec != null) {
          obj.put("reconciliation", rec.getJSONObject());
        }
      } catch (JSONException ignored) {
        return null;
      }
      return obj;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      Bundle bundle = new Bundle();
      bundle.putString(BUNDLE_TYPE, type);
      if (amountChange != null) {
        bundle.putLong(BUNDLE_AMOUNT_CHANGE, amountChange);
      }
      if (timestamp != null) {
        bundle.putLong(BUNDLE_TIMESTAMP, timestamp);
      }
      bundle.putString(BUNDLE_NOTE, note);
      bundle.putString(BUNDLE_EMPLOYEE, employee);
      bundle.putString(BUNDLE_DEVICE_ID, deviceId);
      bundle.putParcelable(BUNDLE_RECONCILIATION, reconciliation);
      bundle.putString(BUNDLE_ID, id);
      if (expectedBalance != null) {
        bundle.putLong(BUNDLE_EXPECTED_BALANCE, expectedBalance);
      }
      dest.writeBundle(bundle);
    }

    @Override
    public int describeContents() {
      return 0;
    }

    public static final Creator<CashEventPayload> CREATOR = new Creator<CashEventPayload>() {
      @Override
      public CashEventPayload createFromParcel(Parcel in) {
        return new CashEventPayload(in);
      }

      @Override
      public CashEventPayload[] newArray(int size) {
        return new CashEventPayload[size];
      }
    };
  }
}