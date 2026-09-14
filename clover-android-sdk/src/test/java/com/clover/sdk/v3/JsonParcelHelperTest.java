/**
 * This test class checks JSON parcel conversion.
 * <p>
 * Main objective: To test how different data types are handled by genericClient.setArrayOther().
 * This includes Lists, Enums, Strings, and other common types.
 * <p>
 * It makes sure data is correctly converted to and from parcel
 * without losing or changing values.
 */

package com.clover.sdk.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import android.os.Parcel;

import com.clover.sdk.v3.apps.App;
import com.clover.sdk.v3.employees.EmployeePermission;
import com.clover.sdk.v3.employees.Permission;
import com.clover.sdk.v3.employees.PermissionSet;
import com.clover.sdk.v3.employees.Permissions;
import com.clover.sdk.v3.payment.raw.model.EmvTrackIds;
import com.clover.sdk.v3.payment.raw.model.EncryptMode;
import com.clover.sdk.v3.payment.raw.model.GetCardDataDetailsRequest;
import com.clover.sdk.v3.payment.raw.model.KeyStorageKeyType;
import com.clover.sdk.v3.payment.raw.service.IRawExtTransactionService;
import com.clover.sdk.v3.payments.BatchRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class JsonParcelHelperTest {

    @Mock
    private IRawExtTransactionService rawTransactionService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCardDataDetails_Tracks() {

        String tracks = "1111";

        List<EmvTrackIds> requestedTracks = new ArrayList<>(4);

        if (tracks.charAt(0) == '1') requestedTracks.add(EmvTrackIds.PAN);
        if (tracks.charAt(1) == '1') requestedTracks.add(EmvTrackIds.TRACK1);
        if (tracks.charAt(2) == '1') requestedTracks.add(EmvTrackIds.TRACK2);
        if (tracks.charAt(3) == '1') requestedTracks.add(EmvTrackIds.TRACK3);

        GetCardDataDetailsRequest request = new GetCardDataDetailsRequest();
        request.setEncryptMode(EncryptMode.CBC);
        request.setKeyType(KeyStorageKeyType.DUKPT_3DES);
        request.setInitVector("0000000000000000");
        request.setNumClearDigits(2);
        request.setKeySlot("08");
        request.setRequestedTracks(requestedTracks);

        Parcel parcel = Parcel.obtain();
        request.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        GetCardDataDetailsRequest recreatedRequest =
                GetCardDataDetailsRequest.CREATOR.createFromParcel(parcel);

        assertNotNull(recreatedRequest.getRequestedTracks());
        assertEquals(4, recreatedRequest.getRequestedTracks().size());
        assertTrue(recreatedRequest.getRequestedTracks().contains(EmvTrackIds.PAN));
        assertTrue(recreatedRequest.getRequestedTracks().contains(EmvTrackIds.TRACK1));
        assertTrue(recreatedRequest.getRequestedTracks().contains(EmvTrackIds.TRACK2));
        assertTrue(recreatedRequest.getRequestedTracks().contains(EmvTrackIds.TRACK3));

        parcel.recycle();
    }

    @Test
    public void testEnumParcelable() throws Exception {
        List<EmployeePermission> employeePermissions = new ArrayList<>();
        employeePermissions.add(EmployeePermission.MERCHANT_R);

        PermissionSet originalPermissionSet = new PermissionSet();
        originalPermissionSet.setEmployeePermissions(employeePermissions);

        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(Permission.MERCHANT_R);

        Permissions originalPermissions = new Permissions();
        originalPermissions.setReadablePermissions(permissionList);

        Parcel parcel = Parcel.obtain();
        originalPermissionSet.writeToParcel(parcel, 0);
        originalPermissions.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        PermissionSet recreatedPermissionSet = PermissionSet.CREATOR.createFromParcel(parcel);
        Permissions recreatedPermissions = Permissions.CREATOR.createFromParcel(parcel);

        assertEquals(originalPermissionSet.getEmployeePermissions(),
                recreatedPermissionSet.getEmployeePermissions());

        assertEquals(originalPermissions.getReadablePermissions(),
                recreatedPermissions.getReadablePermissions());

        parcel.recycle();
    }

    @Test
    public void testEmvTrackIdsEnumListParcelable() {
        List<EmvTrackIds> requestedTracks = new ArrayList<>();
        requestedTracks.add(EmvTrackIds.PAN);
        requestedTracks.add(EmvTrackIds.TRACK1);
        requestedTracks.add(EmvTrackIds.TRACK2);

        GetCardDataDetailsRequest request = new GetCardDataDetailsRequest();
        request.setRequestedTracks(requestedTracks);

        Parcel parcel = Parcel.obtain();
        request.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        GetCardDataDetailsRequest recreated =
                GetCardDataDetailsRequest.CREATOR.createFromParcel(parcel);

        assertEquals(requestedTracks, recreated.getRequestedTracks());
        parcel.recycle();
    }

    @Test
    public void testReadablePermissionsEnumListParcelable() {
        List<Permission> readablePermissions = new ArrayList<>();
        readablePermissions.add(Permission.ORDERS_R);
        readablePermissions.add(Permission.ORDERS_W);
        readablePermissions.add(Permission.INVENTORY_R);
        readablePermissions.add(Permission.INVENTORY_W);

        Permissions request = new Permissions();
        request.setReadablePermissions(readablePermissions);

        Parcel parcel = Parcel.obtain();
        request.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Permissions createdFromParcel = Permissions.CREATOR.createFromParcel(parcel);

        assertNotNull(createdFromParcel);
        assertEquals(readablePermissions, createdFromParcel.getReadablePermissions());

        parcel.recycle();
    }

    @Test
    public void testMerchantsPlanIdsExclusionListParcelable() {
        List<Long> merchantsPlanIds = new ArrayList<>();
        merchantsPlanIds.add(101L);
        merchantsPlanIds.add(102L);
        merchantsPlanIds.add(103L);

        App request = new App();
        request.setMerchantsPlanIdsExclusion(merchantsPlanIds);

        Parcel parcel = Parcel.obtain();
        request.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        App createdFromParcel = App.CREATOR.createFromParcel(parcel);

        assertNotNull(createdFromParcel);
        assertEquals(merchantsPlanIds, createdFromParcel.getMerchantsPlanIdsExclusion());

        parcel.recycle();
    }

    @Test
    public void testDevicesListParcelable() {
        List<String> devices = new ArrayList<>();
        devices.add("Station Duo");
        devices.add("Clover Mini");
        devices.add("Clover Compact");
        devices.add("Flex3");
        devices.add("Flex4");

        BatchRequest request = new BatchRequest();
        request.setDevices(devices);

        Parcel parcel = Parcel.obtain();
        request.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        BatchRequest createdFromParcel = BatchRequest.CREATOR.createFromParcel(parcel);

        assertNotNull(createdFromParcel);
        assertEquals(devices, createdFromParcel.getDevices());

        parcel.recycle();
    }

    @Test
    public void testParcelUnparcel_ListOfString() {
        List<String> original = Arrays.asList("one", "two", "three");

        Parcel parcel = Parcel.obtain();
        parcel.writeStringList(original);

        parcel.setDataPosition(0);

        List<String> result = new ArrayList<>();
        parcel.readStringList(result);

        assertEquals(original, result);

        parcel.recycle();
    }

    @Test
    public void testParcelUnparcel_EmptyStringList() {
        List<String> original = new ArrayList<>();

        Parcel parcel = Parcel.obtain();
        parcel.writeStringList(original);

        parcel.setDataPosition(0);

        List<String> result = new ArrayList<>();
        parcel.readStringList(result);

        assertTrue(result.isEmpty());

        parcel.recycle();
    }

    @Test
    public void testParcelUnparcel_NullStringList() {
        List<String> original = null;

        Parcel parcel = Parcel.obtain();
        parcel.writeStringList(original);

        parcel.setDataPosition(0);

        List<String> result = parcel.createStringArrayList();

        assertNull(result);

        parcel.recycle();
    }

    @Test
    public void testParcelUnparcel_LargeStringList() {
        List<String> original = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            original.add("item_" + i);
        }

        Parcel parcel = Parcel.obtain();
        parcel.writeStringList(original);
        parcel.setDataPosition(0);

        List<String> result = new ArrayList<>();
        parcel.readStringList(result);

        assertEquals(original.size(), result.size());
        assertEquals(original.get(500), result.get(500));

        parcel.recycle();
    }
}
