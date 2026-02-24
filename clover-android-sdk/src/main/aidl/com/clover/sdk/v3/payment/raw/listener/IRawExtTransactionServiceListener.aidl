// IRawExtTransactionServiceListener.aidl
package com.clover.sdk.v3.payment.raw.listener;

import com.clover.sdk.v3.pay.PaymentRequest;
import com.clover.sdk.v3.payment.raw.model.EntryMode;
import com.clover.sdk.v3.payment.raw.model.CardReaderError;
import com.clover.sdk.v3.payment.raw.model.CardApplication;
import com.clover.sdk.v3.payment.raw.model.CardDataResponse;
import com.clover.sdk.v3.payment.raw.model.GetPinResponse;
import com.clover.sdk.v3.payment.raw.model.GoOnChipResponse;
import com.clover.sdk.v3.payment.raw.model.FinishChipResponse;
import com.clover.sdk.v3.payment.raw.model.SecureEntryResult;
import com.clover.sdk.v3.payment.raw.model.SecureEntryType;
import com.clover.sdk.v3.payment.raw.model.SecureEntryScreenParameters;
import com.clover.sdk.v3.payment.raw.handler.IGenericAlertHandler;
import com.clover.sdk.v3.payment.raw.handler.ISelectApplicationHandler;
import com.clover.sdk.v3.payment.raw.model.GetCardEmvDataResponse;
import com.clover.sdk.v3.payment.raw.model.EncryptBufferResponse;

interface IRawExtTransactionServiceListener {
  /**
   * Called on successful card detection (insert/swipe/tap) to allow client to display
   * required processing indication (e.g. contactless leds)
   * entryMode - the detected mode
   */
  void onCardDetected(in EntryMode entryMode);

  /**
   * Called to indicate that user has removed the card for contact entry mode
   * the client should dismiss the remove card message if present
   */
  void onCardRemoved();

  /**
   * Called on successful card data read (insert/swipe/tap) to allow client to use card data
   * cardDataResponse - the response of sucessful card read
   */
  void onCardReadCompleted(in CardDataResponse cardDataResponse);

  /**
   * Called on successful completion of PIN entry for swipe
   * getPinResponse - the response of sucessful pin entry containing pin block and ksn
   */
  void onGetPinCompleted(in GetPinResponse getPinResponse);

  /**
   * Called on successful processing of goOnChip (CVM, decision outcome, etc.)
   * paymentRequest - the response of sucessful goOnChip command
   */
  void onGoOnChipCompleted(in GoOnChipResponse goOnChipResponse);

  /**
   * Called on successful processing of finishChip (CVM, decision outcome, etc.)
   * emvData - the response of sucessful finishChip command
   */
  void onFinishChipCompleted(in FinishChipResponse finishChipResponse);

  /**
   * Called when the service and hardware is ready to accept card input
   */
  void onCardReadReady(in List<EntryMode> entryModes);

  /**
   * Called in the event of a reader error while waiting for or processing card detection
   * error - the error encounterd
   * extras - unused
   */
  void onError(in CardReaderError error);

  /**
   * Called in the event that the client needs to prompt for multiple application IDs
   */
  void onSelectApplication(in CardApplication[] cardApplication,in ISelectApplicationHandler iSelectApplicationHandler);

  void onPinRequired(in SecureEntryScreenParameters screenParams);

   /**
    * Called to indicate that service is restarting the transaction because on internal error such as contactless timeout
    * UI should show processing and wait for the onCardReadReady callback.
    */
   void onTransactionRestarted();

   /**
   * Called when card processing throgh gereric alert where user should be informed for error but the
   * transaction should be resumed from the current state. IGenericAlertHandler will be null when
   * the user input is not required to proceed with the transaction.
   */
   void onGenericAlert(in CardReaderError error, in IGenericAlertHandler iGenericAlertHandler);

   void onSecureEntryKeyPress(in String echoStr, in boolean dataValid);

   void onSecureEntryComplete(in SecureEntryType secEntryType,in SecureEntryResult result);

   void onCardDataExchangeCompleted();

   /**
   * Executed when read card EMV data (insert/tap) is available for processing
   * getCardEmvDataResponse - the response of sucessful get emv data command
   */
   void onGetCardEmvDataCompleted(in GetCardEmvDataResponse getCardEmvDataResponse);

   /**
    * Callback executed when encrypted data is received.
    * encryptBufferResponse - the response containing the encrypted data and KSN.
   */
   void onEncryptedDataReceived(in EncryptBufferResponse encryptedBufferResponse);
}