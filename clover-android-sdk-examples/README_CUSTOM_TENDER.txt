This is a quick introduction on how to create a custom tender that is available in the Clover register application. We will cleanup the code and expose an easy to use service in the near future.

Basic Flow
----------

1. Create a System Tender. 

	This tender has a key that is unique across the whole system. 
	
	Important : If you are planning on having an android app implement this tender (i.e respond to the clover.intent.action.PAY) then this key MUST equal your android package name.

	Because this is system wide request we require the api secret for your app. This can be found on your developer account. You can use the api explorer to make this call if you choose https://www.clover.com/api_explorer.

	post /v2/tenders 
	{
  		"tender": {
    		"opensCashDrawer": false,
    		"labelKey": "your.package.name",
    		"enabled": true,
    		"label": "My Tender"
  		}
	}

	The response will be the new tender (you can also call get /v2/tenders to check).

2. When a merchant first installs your app you need to register your tender type against this merchant. The 'label' field is the string shown to the user, you can localize this on a merchant by merchant basis.

	On first run:

	post /v2/merchant/{mId}/tenders 
	{
  		"tender": {
    		"opensCashDrawer": false,
    		"labelKey": "your.package.name",
    		"enabled": true,
    		"label": "My Tender"
  		}
	}

3. Create an activity that will respond to the clover.intent.action.PAY intent. Now start register again, create a new order, add an item and then click pay. You will now see your tender as an option.




Example App
-----------

We have already done step 1 for our example app. You can check that by calling get /v2/tenders and seeing a tender with labelKey = "com.clover.android.sdk.examples"


In the clover sdk example app we have two activities.

The first one is called 'CreateCustomTenderTestActivity'. This will do step 2 in the above process. 

The second one is 'CustomTenderHandlerTestActivity'. This is shown when the user clicks on the custom tender in the register application. It shows you how to authorize and decline a payment.
