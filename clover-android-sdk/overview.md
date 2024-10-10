# Module clover-android-sdk
The Clover SDK for Android facilitates development of applications running on Clover devices, see
below for more information about the functionality provided by this SDK.

## Service Interfaces
Interfaces to use with bound Clover services that allow management of a merchant's data: orders,
  inventory, customers, employees, printers, properties, etc. Service classes starting with 'I' are
  the underlying AIDL interfaces. Clover provides corresponding ServiceConnector classes for simpler
  usage.
 * [com.clover.sdk.v3.apps.IAppsService]
 * [com.clover.sdk.v1.customer.ICustomerService]
 * [com.clover.sdk.v3.employees.IEmployeeService]
 * [com.clover.sdk.v3.inventory.IInventoryService]
 * [com.clover.sdk.v1.merchant.IMerchantService]
 * [com.clover.sdk.v3.order.IOrderServiceV3_1]
 * [com.clover.sdk.v1.printer.IPrinterService]
 * [com.clover.sdk.v1.printer.IReceiptRegistrationService]
 * [com.clover.sdk.v1.tender.ITenderService]

For more information about binding to services, refer to the Android documentation:
<a href="http://developer.android.com/guide/components/bound-services.html#Binding" target="_blank">
    Bound Services</a>.

## Intents
Intents are used to send and receive broadcast messages in Android as well as start activities and
  services. The [com.clover.sdk.v1.Intents] class has definitions for most commonly used
  intents for broadcasts sent by Clover and starting Clover activities.

## Clover Device Interfaces
Clover devices offer additional functionality beyond standard Android, such as:
 * [com.clover.sdk.v3.scanner.BarcodeResult]
 * [com.clover.sdk.util.CustomerMode]
 * [com.clover.sdk.cashdrawer.CashDrawers]
 * [com.clover.sdk.v1.printer.job.PrintJob]

## Provider Contracts
Searching for and displaying large amounts of data is best done through a content provider.
  Inserting, updating and deleting Clover data objects via content provider is generally not
  supported.
 * [com.clover.sdk.v1.customer.CustomerContract]
 * [com.clover.sdk.v1.printer.job.PrintJobsContract]
 * [com.clover.sdk.v1.printer.PrinterContract]
 * [com.clover.sdk.v1.printer.ReceiptContract]
 * [com.clover.sdk.v1.printer.ReceiptFileContract]
 * [com.clover.sdk.v1.printer.ReceiptRegistrationContract]
 * [com.clover.sdk.v3.cash.CashContract]
 * [com.clover.sdk.v3.inventory.InventoryContract]
 * [com.clover.sdk.v3.order.OrderContract]

For more information about content providers, refer to the Android documentation:
  <a href="https://developer.android.com/guide/topics/providers/content-provider-basics.html" target="_blank">
    Content Provider Basics</a>.

## Clover Data Objects
The v1 data objects and associated interfaces tend to conform to the deprecated Clover v1 and v2
  web REST API, the v3 objects and associated interfaces tend to conform to the Clover v3 web REST
  API. Changes to Clover data objects must be sent to the appropriate service interface to take
  effect.

Clover data objects offer interfaces to convert to and from JSON as well as being Parcelable.

For complete examples and more information about Clover development please visit the
  <a href="https://www.clover.com/developers" target="_blank">Clover development website</a>.