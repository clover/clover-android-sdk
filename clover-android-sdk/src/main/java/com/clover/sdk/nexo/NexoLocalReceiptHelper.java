package com.clover.sdk.nexo;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clover.sdk.v3.payments.CardTransaction;
import com.clover.sdk.v3.payments.CardTransactionConstants;
import com.clover.sdk.v3.payments.DisplayAndPrintMessage;
import com.clover.sdk.v3.payments.MessageDestination;
import com.clover.sdk.v3.payments.SepaElvTransactionInfo;
import com.clover.sdk.v3.payments.TransactionInfo;
import com.clover.sdk.v3.payments.TransactionResult;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class is used to parse and prepare Nexo receipt data, to show them directly on the PrintReceiptActivity screen under "More Options"
 * or in the NexoTestApp.
 * <p>
 *                                                LOCAL RECEIPT DATA
 * ------------------------------------------------------------------------------------------------------------------------ *
 * |           Receipt Data           |    Additional text    |                  Source                |      M|O|C       | *
 * ------------------------------------------------------------------------------------------------------------------------ *
 *  Merchant name                               -                           TransactionInfo                     M           *
 *  Merchant ID                                MID:                         TransactionInfo                     M           *
 *  Terminal ID                                TID:                         TransactionInfo                     M           *
 *  External Terminal ID                   Device name                      TransactionInfo                     C           *
 *  AID                                         -                           CardTransaction                     C           *
 *  Application Label                           -                           TransactionInfo                     M           *
 *  ELV extended App Label                      -                       SepaElvTransactionInfo                  C           *
 *  PAN + PAN sequence number                   -                           CardTransaction                     M           *
 *  Date/Time                                   -                           TransactionInfo                     M           *
 *  Trace number                            Trace-Nbr:                      TransactionInfo                     M           *
 *  ELV fixed text CDGM                         -                       SepaElvTransactionInfo                  C           *
 *  ELV Short BIC                           Short-BC:                       TransactionInfo                     C           *
 *  ELV Bank account                        Bank-Acc.:                      TransactionInfo                     C           *
 *  ELV IBAN                                  IBAN:                     SepaElvTransactionInfo                  C           *
 *  ELV Creator ID                            G-ID:                     SepaElvTransactionInfo                  C           *
 *  ELV Mandat-ID                             M-ID:                     SepaElvTransactionInfo                  C           *
 *  Reference TSC                          Reference ID:                    CardTransaction              M or Auth Code     *
 *  AID Parameter                               -                           PrintMessages                       C           *
 *  Auth Code                               Auth ID:                        CardTransaction              M or Reference ID  *
 *  Reconciliation ID                       RECON-ID:                       TransactionInfo                     M           *
 *  Online result print messages                -                           TransactionInfo                     O           *
 *  CVM result                                  -                           CardTransaction                     M           *
 *  Card method                              Method:                        TransactionInfo                     M           *
 *  Balance                                  BALANCE                        CardTransaction                     C           *
 *  Receipt extra data                                                      TransactionInfo                     O           *
 *  Amount                                    AMOUNT                Payment/Credit/Refund/CreditRefund          M           *
 *  Cashback amount                          CASHBACK                        Payment/Refund                     C           *
 *  TIP amount                                 TIP                              Payment                         O           *
 *  NEXO Service                                -                           TransactionInfo                     M           *
 *  NEXO TX result                              -                           TransactionInfo                     M           *
 * ------------------------------------------------------------------------------------------------------------------------ *
 * </p>
 */
@SuppressWarnings("unused")
public class NexoLocalReceiptHelper {
    private static final String ELV_CDGM = "CDGM";
    private static final String LABEL_MID = "MID: ";
    private static final String LABEL_TID = "TID: ";
    private static final String LABEL_DEVICE_NAME = "Device Name: ";
    private static final String LABEL_TRACE_NBR = "Trace-Nbr: ";
    private static final String LABEL_SHORT_BC = "Short-BC: ";
    private static final String LABEL_BANK_ACC = "Bank-Acc.: ";
    private static final String LABEL_IBAN = "IBAN: ";
    private static final String LABEL_G_ID = "G-ID: ";
    private static final String LABEL_M_ID = "M-ID: ";
    private static final String LABEL_REFERENCE_ID = "Reference ID: ";
    private static final String LABEL_AUTH_ID = "Auth ID: ";
    private static final String LABEL_RECON_ID = "RECON-ID: ";
    private static final String LABEL_METHOD = "Method: ";
    private static final String LABEL_BALANCE = "BALANCE ";
    @SuppressWarnings("unused")
    public static final String LABEL_AMOUNT = "AMOUNT ";
    @SuppressWarnings("unused")
    public static final String LABEL_CASHBACK = "CASHBACK ";
    @SuppressWarnings("unused")
    public static final String LABEL_TIP = "TIP ";

    private static final int PAN_MASK_APPLY = 6;
    private static final int APPLY_MERCHANT_PAN_MASK = 6;

    public static final String RECEIPT_EXTRA_DATA_ON_MERCHANT_RECEIPT = "isReceiptExtraDataOnMerchantReceipt";
    public static final String RECEIPT_EXTRA_DATA_ON_CUSTOMER_RECEIPT = "isReceiptExtraDataOnCustomerReceipt";

    @SuppressWarnings("unused")
    public String getLocalReceiptBody(TransactionInfo transactionInfo, @Nullable CardTransaction cardTransaction, String appLabel, String date, String time, String cvmResult, String balance, Map<String, Boolean> receiptExtraDataMap, boolean useReferenceId, boolean isCardholderReceipt) {
        StringBuilder stringBuilder = new StringBuilder();

        if (transactionInfo != null) {
            String mid = transactionInfo.getMerchantIdentifier();
            stringBuilder.append(LABEL_MID).append(transactionInfo.getMerchantIdentifier()).append("\n");
            String tid = transactionInfo.getTerminalIdentification() != null ? transactionInfo.getTerminalIdentification() : transactionInfo.getExternalTerminalId();
            stringBuilder.append(LABEL_TID).append(tid).append("\n");
            String deviceName = transactionInfo.getExternalTerminalId();
            if (deviceName != null) {
              stringBuilder.append(LABEL_DEVICE_NAME).append(deviceName).append("\n\n");
            }

            if (cardTransaction != null) {
                String aid = cardTransaction.getExtra().get(CardTransactionConstants.APPLICATION_IDENTIFIER);
                if (aid != null) {
                  stringBuilder.append(aid).append("\n");
                }
                if (appLabel == null) {
                    appLabel = cardTransaction.getExtra().get(CardTransactionConstants.APPLICATION_LABEL);
                }
                stringBuilder.append(appLabel).append("\n");
            }

            //ELV
            if (transactionInfo.getIsSepaElv() != null && transactionInfo.getIsSepaElv()) {
                stringBuilder.append(transactionInfo.getSepaElvTransactionInfo().getExtAppLabel()).append("\n");
            }

            if (cardTransaction != null) {
                String track2Data = transactionInfo.getMaskedTrack2();
                String panMask = transactionInfo.getPanMask();
                String pan = getNexoCardPan(track2Data, panMask, isCardholderReceipt);
                stringBuilder.append(pan);
            }

            String panSequenceNumber = transactionInfo.getApplicationPanSequenceNumber();
            if (panSequenceNumber != null) {
              stringBuilder.append(" ").append(panSequenceNumber).append("\n\n");
            }
            stringBuilder.append(date).append(" ").append(time).append("\n");
            String tsc = transactionInfo.getTransactionSequenceCounter();
            stringBuilder.append(LABEL_TRACE_NBR).append(tsc).append("\n");

            //ELV
            if (transactionInfo.getIsSepaElv() != null && transactionInfo.getIsSepaElv()) {
                stringBuilder.append(ELV_CDGM).append("\n");
                String maskedTrack2 = transactionInfo.getMaskedTrack2();
                stringBuilder.append(LABEL_SHORT_BC).append(maskedTrack2.substring(3, 8)).append("\n");
                stringBuilder.append(LABEL_BANK_ACC).append(maskedTrack2.substring(8, 18)).append("\n");
                SepaElvTransactionInfo sepaElvTransactionInfo = transactionInfo.getSepaElvTransactionInfo();
                String iban = sepaElvTransactionInfo.getIban();
                if (iban != null) {
                  String panMask = transactionInfo.getPanMask();
                  String maskedIban = getNexoIban(iban, panMask, isCardholderReceipt);
                  stringBuilder.append(LABEL_IBAN).append(maskedIban).append("\n");
                }
                stringBuilder.append(LABEL_G_ID).append(sepaElvTransactionInfo.getCreditorId()).append("\n");
                stringBuilder.append(LABEL_M_ID).append(sepaElvTransactionInfo.getMandateId()).append("\n");
            }

            if (cardTransaction != null && useReferenceId) {
                stringBuilder.append(LABEL_REFERENCE_ID).append(cardTransaction.getReferenceId()).append("\n");
            }

            String aidParam = getPrintMessage(transactionInfo, true, isCardholderReceipt);
            if (aidParam != null) {
              stringBuilder.append(aidParam).append("\n");
            }

            if (cardTransaction != null && !useReferenceId) {
                String authCode = cardTransaction.getAuthCode();
                if (authCode != null) {
                  stringBuilder.append(LABEL_AUTH_ID).append(cardTransaction.getAuthCode()).append("\n");
                }
            }

            String reconciliationId = transactionInfo.getBatchNumber();
            stringBuilder.append(LABEL_RECON_ID).append(reconciliationId).append("\n\n");
            String printMessage = getPrintMessage(transactionInfo, false, isCardholderReceipt);
            if (printMessage != null) {
              stringBuilder.append(printMessage).append("\n\n");
            }
            if (TransactionResult.APPROVED.equals(transactionInfo.getTransactionResult())) {
              stringBuilder.append(cvmResult).append("\n");
            }

            // Entry method
            String method;
            if (transactionInfo.getCardEntryType() != null) {
                method = transactionInfo.getCardEntryType().name();
            } else {
                if (cardTransaction != null && cardTransaction.getEntryType() != null) {
                    method = cardTransaction.getEntryType().name();
                } else {
                    method = "";
                }
            }
            if (TransactionResult.APPROVED.equals(transactionInfo.getTransactionResult())) {
              stringBuilder.append(LABEL_METHOD).append(method).append("\n\n");
            }

            if (isCardholderReceipt && balance != null) {
                stringBuilder.append(LABEL_BALANCE).append(balance).append("\n");
            }

            boolean showReceiptExtraData;
            if (isCardholderReceipt) {
              showReceiptExtraData = Boolean.TRUE.equals(receiptExtraDataMap.get(RECEIPT_EXTRA_DATA_ON_CUSTOMER_RECEIPT));
            } else {
              showReceiptExtraData = Boolean.TRUE.equals(receiptExtraDataMap.get(RECEIPT_EXTRA_DATA_ON_MERCHANT_RECEIPT));
            }
            String receiptExtraData = transactionInfo.getReceiptExtraData();
            if (receiptExtraData != null && showReceiptExtraData) {
                stringBuilder.append("\n").append(receiptExtraData).append("\n\n");
            }
        }

        return stringBuilder.toString();
    }

    private static String getPrintMessage(TransactionInfo transactionInfo, boolean isAidParam, boolean isCardholderReceipt) {
        MessageDestination messageDestinationMerchant = isAidParam ? MessageDestination.MERCHANT_RECEIPT_AID_PARAM : MessageDestination.MERCHANT_RECEIPT;
        MessageDestination messageDestinationCustomer = isAidParam ? MessageDestination.CUSTOMER_RECEIPT_AID_PARAM : MessageDestination.CUSTOMER_RECEIPT;
        List<DisplayAndPrintMessage> displayAndPrintMessageList = transactionInfo.getPrintMessages();

        if (displayAndPrintMessageList != null) {
            for (DisplayAndPrintMessage displayAndPrintMessage : displayAndPrintMessageList) {
                if (displayAndPrintMessage != null && displayAndPrintMessage.getDestination() != null &&
                        ((displayAndPrintMessage.getDestination() == messageDestinationMerchant && !isCardholderReceipt) ||
                                (displayAndPrintMessage.getDestination() == messageDestinationCustomer && isCardholderReceipt))) {
                    return displayAndPrintMessage.getContent();
                }
            }
        }
        return null;
    }

  public String getNexoCardPan(String track2, String panMask, boolean isCustomerCopy) {
    if (track2 == null) {
      return null;
    }
    int index = 0;
    try {
      index = track2.indexOf('=');
      if (index == -1) {
        index = track2.indexOf('D');
      }
      if (index == -1) {
        index = track2.indexOf('d');
      }
    } catch (Exception e) {
      //ALog.e(NexoReceiptData.class, e, "BCD ERROR");
    }
    if (index <= 0) {
      return null;
    }

    String pan = track2.substring(0, index);
    if (TextUtils.isEmpty(pan)) {
      return null;
    }
    pan = pan.replace("f", "x");
    return getMaskValue(pan, panMask, isCustomerCopy);
  }

  public String getNexoIban(@NonNull String iban, String panMask, boolean isCustomerCopy) {
    return getMaskValue(iban, panMask, isCustomerCopy);
  }

  private String getMaskValue(String value, String panMask, boolean isCardholder) {
    StringBuilder leadingPanMaskStringBuilder = new StringBuilder();
    StringBuilder trailingPanMaskStringBuilder = new StringBuilder();
    boolean applyPanMaskToMerchantReceipt = false;
    if (!TextUtils.isEmpty(panMask)) {
      byte[] panMaskArray = hexStringToByteArray(panMask);
      if (panMaskArray.length == 7) {
        applyPanMaskToMerchantReceipt = (panMaskArray[PAN_MASK_APPLY] & (byte) (0x01 << APPLY_MERCHANT_PAN_MASK)) != 0;
        for (int i = 0; i < panMaskArray.length; i++) {
          if (i < 3) {
            leadingPanMaskStringBuilder.append(String.format(Locale.US, "%8s", Integer.toBinaryString(panMaskArray[i] & 0xFF)).replace(' ', '0'));
          } else if (i < 6) {
            trailingPanMaskStringBuilder.append(String.format(Locale.US, "%8s", Integer.toBinaryString(panMaskArray[i] & 0xFF)).replace(' ', '0'));
          } else {
            break;
          }
        }
      }
    }
    if ((isCardholder || applyPanMaskToMerchantReceipt) && leadingPanMaskStringBuilder.length() > 0 && trailingPanMaskStringBuilder.length() > 0) {
      final StringBuilder leadingPanStringBuilder = new StringBuilder();
      final StringBuilder trailingPanStringBuilder = new StringBuilder();
      final String leadingPanMask = leadingPanMaskStringBuilder.toString();
      final String trailingPanMask = trailingPanMaskStringBuilder.toString();
      final char maskingChar = 0x30; // ascii "0"
      final char maskedChar = 0x78; // ascii "x"
      for (int i = 0; i < value.length(); i++) {
        if (i >= leadingPanMask.length()) {
          break; // should never be
        }
        if (maskingChar == leadingPanMask.charAt(i)) {
          leadingPanStringBuilder.append(maskedChar);
        } else {
          leadingPanStringBuilder.append(value.charAt(i));
        }
      }
      for (int i = 0; i < value.length(); i++) {
        if (i >= trailingPanMask.length()) {
          break; // should never be
        }
        if (maskingChar == (trailingPanMask.charAt(trailingPanMask.length() - 1 - i))) {
          trailingPanStringBuilder.append(maskedChar);
        } else {
          trailingPanStringBuilder.append(value.charAt(value.length() - 1 - i));
        }
      }
      final StringBuilder panMaskedStringBuilder = new StringBuilder();
      final String leadingPanString = leadingPanStringBuilder.toString();
      final String trailingPanString = trailingPanStringBuilder.reverse().toString();
      if (value.length() != leadingPanString.length() || value.length() != trailingPanString.length()) {
        return value;
      }
      for (int i = 0; i < value.length(); i++) {
        final char tmpChar = leadingPanString.charAt(i) != maskedChar ? leadingPanString.charAt(i) : trailingPanString.charAt(i) != maskedChar ? trailingPanString.charAt(i) : maskedChar;
        panMaskedStringBuilder.append(tmpChar);
      }
      return panMaskedStringBuilder.toString();
    } else {
      return value;
    }
  }

  public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];

    for(int i = 0; i < len; i += 2) {
      data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }

    return data;
  }
}
