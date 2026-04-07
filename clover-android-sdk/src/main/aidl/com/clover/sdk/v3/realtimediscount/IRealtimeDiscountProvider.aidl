package com.clover.sdk.v3.realtimediscount;

import com.clover.sdk.v3.realtimediscount.DiscountRequest;
import com.clover.sdk.v3.realtimediscount.DiscountResponse;
import com.clover.sdk.v3.realtimediscount.CapturedDiscount;
import com.clover.sdk.v3.realtimediscount.IDiscountCallback;

/**
 * <p><strong>The Realtime Discount/Realtime Promotion system computes discounts during the transaction flow
 * based on the card type, specific bin ranges, or wallet type. If an application is available that
 * provides the required discount by card type and permitted by the Clover echo-system, the payment
 * flow can bind to that third-party APK to apply custom discounts.
 *
 * The name "Realtime Discount" refers to the fact that these calculations occur instantly once the
 * card details are read within the live payment flow.</strong>
 *
 * IRealtimeDiscountProvider is the core interface implemented by third-party services
 * to provide a Real-Time Discount (RTD) capability during a Clover checkout flow.
 *
 * <p><strong>Significance:</strong>
 * This interface allows external partners (e.g., in regions like Argentina) to dynamically
 * interpret a transaction's details—such as the card BIN, order amount, and entry mode—and
 * apply an instant financial discount or rebate before the payment is fully authorized.
 *
 * <p><strong>Example Usage:</strong>
 * A third-party provider implements an Android Service that exposes this AIDL interface:
 * <pre>
 *   public class MyDiscountService extends Service {
 *       private final IRealtimeDiscountProvider.Stub mBinder = new IRealtimeDiscountProvider.Stub() {
 *           \@Override
 *           public void getDiscount(DiscountRequest request, IDiscountCallback callback) {
 *               // 1. Analyze request (e.g., amount, cardDetails.first6)
 *               // 2. Calculate discount
 *               // 3. Return via callback.onResult(new DiscountResponse(...))
 *           }
 *           // ... implement other methods
 *       };
 *       \@Override
 *       public IBinder onBind(Intent intent) { return mBinder; }
 *   }
 * </pre>
 * The Clover Payment system binds to this service automatically if it possesses the
 * Clover `PAYMENTS_R` permission and matches the filtering criteria.
 *
 * <p><strong>System Interaction:</strong>
 * The `RealTimeDiscountEngine` securely establishes a connection to the chosen provider and calls
 * {@link #getDiscount()} prior to sending the auth request. If the transaction requires a partial
 * approval flow, the engine will subsequently call {@link #onPartialApproval()}. Finally, when
 * the transaction officially concludes, {@link #onDiscountFinalized()} is called for reconciliation.
 */
interface IRealtimeDiscountProvider {
    /**
     * Request a discount for a pending transaction.
     *
     * @param request The discount request containing transaction details like amount, order ID, and merchant info.
     * @param callback The callback to receive the discount response asynchronously.
     */
    void getDiscount(in DiscountRequest request, in IDiscountCallback callback);

    /**
     * Notify the provider that a discount has been finalized and the transaction completed.
     *
     * @param capturedDiscount The details of the finalized discount and transaction.
     */
    void onDiscountFinalized(in CapturedDiscount capturedDiscount);

    /**
     * Handle a partial approval scenario where the authorized amount is less than the requested amount.
     * The provider may need to recalculate the discount based on the new amount.
     *
     * @param capturedDiscount The details of the partially approved transaction.
     * @param callback The callback to receive the updated discount response asynchronously.
     */
    void onPartialApproval(in CapturedDiscount capturedDiscount, in IDiscountCallback callback);
}
