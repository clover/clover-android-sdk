package com.clover.sdk.v3.mifare.service;

import com.clover.sdk.v3.mifare.model.MifareCardDataRequest;
import com.clover.sdk.v3.mifare.model.MifareCardUuid;
import com.clover.sdk.v3.mifare.model.MifareCard;
import com.clover.sdk.v3.mifare.model.MifareCardWriteRequest;
import com.clover.sdk.v3.mifare.model.MifareMobileDriverLicense;
import com.clover.sdk.v3.mifare.model.MifareCardLightDataRequest;
import com.clover.sdk.v3.mifare.model.MifareMobileDriverLicenseRequest;

interface IMifareCardReaderService {

 void openSession();

 MifareCardUuid cardUuid();

 MifareCard cardUltralightRead(in MifareCardLightDataRequest mifareCardDataReqeust);

 MifareCard cardUltralightEv1Read(in MifareCardLightDataRequest mifareCardDataReqeust);

 MifareCard cardClassicRead(in MifareCardDataRequest mifareCardDataReqeust);

 boolean writeCard(in MifareCardWriteRequest mifareCardWriteRequest);

 MifareMobileDriverLicense mobileDriverLicenseRead(in MifareMobileDriverLicenseRequest  mifareMobileDriverLicenseRequest);

 void cancel();

 void closeSession();
}