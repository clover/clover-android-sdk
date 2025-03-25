package com.clover.sdk.v3.payments.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import com.clover.sdk.v1.Intents;

import java.util.List;
import java.util.Set;

/**
 * Use the ReadCardRequestIntentBuilder to build a request to request a read card operation
 */
public class ReadCardRequestIntentBuilder extends BaseIntentBuilder {
    private CardOptions cardOptions = null;

    /**
     *
     * @param cardOptions
     * @return
     */
    public ReadCardRequestIntentBuilder cardOptions(CardOptions cardOptions) {
        this.cardOptions = cardOptions;
        return this;
    }

    public Intent build(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be populated with a non null value");
        }

        Intent i = super.build(context);
        i.setComponent(new ComponentName("com.clover.payment.builder.pay", "com.clover.payment.builder.pay.handler.ReadCardRequestHandler"));

        if (cardOptions != null) {
            if (cardOptions.cardEntryMethods != null) {
                i.putExtra(Intents.EXTRA_CARD_ENTRY_METHODS, RequestIntentBuilderUtils.convert(cardOptions.cardEntryMethods));
            }
        }

        return i;
    }

    /**
     * Builds an Intent to trigger remote-pay to process the read card request. This is done WITHOUT a UI
     * on the MFD, allowing an integrator to provide a custom UI if desired on the MFD.
     * @param context
     * @return Intent to be used with RemotePayemntsAPIConnector for a CFD ONLY read card request.
     */
    public Intent buildRemoteServiceIntent(Context context) {
        Intent i = build(context);
        i.setComponent(new ComponentName(LOCAL_PAY_DISPLAY_PACKAGE, LOCAL_PAY_DISPLAY_PACKAGE + ".pos.DualDisplayRemoteDeviceStateService"));
        i.setAction(RequestType.READ_CARD);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServices(i, 0);
        if (resolveInfos == null || resolveInfos.size() == 0) {
            i.setComponent(new ComponentName(USB_PAY_DISPLAY_PACKAGE, "com.clover.remote.protocol.usb.pos.UsbRemoteDeviceStateService"));
        }

        resolveInfos = context.getPackageManager().queryIntentServices(i, 0);
        if (resolveInfos == null || resolveInfos.size() == 0) {
            //Neither USB or LPD are installed
            return null;
        }

        return i;
    }
    
    /**
     * Card options that allow the Integrator to control the use of cards.
     */
    public static class CardOptions {
        private final Set<CardEntryMethod> cardEntryMethods;
        private CardOptions(Set<CardEntryMethod> cardEntryMethods) {
            this.cardEntryMethods = cardEntryMethods;
        }

        /**
         * CardOptions to control card options for a single transaction
         * @param cardEntryMethods - @see CardEntryMethod
         * @return
         */
        public static CardOptions Instance(Set<CardEntryMethod> cardEntryMethods) {
            return new CardOptions(cardEntryMethods);
        }
    }

    public static class Response {
        /**
         * The resulting card data, such as track data and card holder information.
         */
        public static final String CARD_DATA = Intents.EXTRA_CARD_DATA;
        /**
         * If the card read fails for any reason, there will be a failure message sent.
         */
        public static final String FAILURE_MESSAGE = Intents.EXTRA_FAILURE_MESSAGE;
    }
}
