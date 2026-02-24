package com.clover.sdk.v3.nfc.service;

import com.clover.sdk.v3.nfc.model.FelicaCardUuid;
import com.clover.sdk.v3.nfc.model.FelicaCardCommand;
import com.clover.sdk.v3.nfc.model.FelicaCardResponse;

interface INfcReaderService {

 void openSession();

 void cancel();

 void closeSession();

 FelicaCardUuid felicaUuid();

 FelicaCardResponse felicaCommand(in FelicaCardCommand felicaCardCmd);

 void felicaRfOn();

 FelicaCardResponse felicaCommandRaw(in FelicaCardCommand felicaCardCmd);

 void felicaRfOff();
}