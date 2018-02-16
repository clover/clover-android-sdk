package com.clover.sdk.v3.employees;

import com.clover.sdk.v3.employees.Employee;
import com.clover.sdk.v3.employees.EmployeeCard;

import com.clover.sdk.v3.employees.IEmployeeListener;
import com.clover.sdk.v1.ResultStatus;

/**
 * An interface for interacting with the Clover employee service. The employee
 * service is a bound AIDL service. Bind to this service as follows,
 * <pre>
 * <code>
 * Intent serviceIntent = new Intent(EmployeeIntent.ACTION_EMPLOYEE_SERVICE);
 * serviceIntent.putExtra(Intents.EXTRA_ACCOUNT, CloverAccount.getAccount(context));
 * serviceIntent.putExtra(Intents.EXTRA_VERSION, 1);
 * context.bindService(serviceIntent);
 * </code>
 * </pre>
 * For more information about bound services, refer to
 * the Android documentation:
 * <a href="http://developer.android.com/guide/components/bound-services.html#Binding">
 * Bound Services
 * </a>.
 * <br/><br/>
 * You may also interact with the employee service through the
 * {@link com.clover.sdk.v3.employee.EmployeeConnector} class, which handles binding and
 * asynchronous invocation of service methods.
 *
 * @see com.clover.sdk.v3.employee.EmployeeIntent
 * @see com.clover.sdk.util.CloverAccount
 * @see com.clover.sdk.v3.employee.EmployeeConnector
 */
interface IEmployeeService {

    Employee getActiveEmployee(out ResultStatus resultStatus);

    Employee getEmployee(String id, out ResultStatus resultStatus);

    List<Employee> getEmployees(out ResultStatus resultStatus);

    Employee createEmployee(in Employee employee, out ResultStatus resultStatus);

    Employee updateEmployee(in Employee employee, out ResultStatus resultStatus);

    Employee setEmployeePin(String id, String pin, out ResultStatus resultStatus);

    void deleteEmployee(String id, out ResultStatus resultStatus);

    void logout(out ResultStatus resultStatus);

    void login(out ResultStatus resultStatus);

    void addListener(IEmployeeListener listener, out ResultStatus resultStatus);

    void removeListener(IEmployeeListener listener, out ResultStatus resultStatus);

    EmployeeCard addEmployeeCard(in EmployeeCard employeeCard, out ResultStatus resultStatus);

    void deleteEmployeeCard(in EmployeeCard employeeCard, out ResultStatus resultStatus);

    List<EmployeeCard> getEmployeeCards(String employeeId, out ResultStatus resultStatus);

    Employee getEmployeeForCard(in String cardNumber, out ResultStatus resultStatus);

    /**
       * Not available to non-Clover apps.
       *
       * Set a fingerprint for an employee.
       * @param employeeId, the employee's ID for whom this fingerprint is set.
       * @param fingerprintId, the fingerprint ID.
       * @param resultStatus, Possible status results from calling Clover services. Most Clover service calls accept an instance of the ResultStatus class as an "out" parameter
       * @y.exclude
       *
       * The method sets the fiungerprintId and assosciates it to the EmployeeId. It throws a client exception for incorrect employeeId.
       * In this case resutStatus is between 400 and 499 or is equal to 999
       */

    void setFingerprintId(in String employeeId, in String fingerprintId, out ResultStatus resultStatus);

    /**
       * Not available to non-Clover apps.
       *
       * Delete the fingerprint of an employee.
       * @param employeeId, the employee's ID for whom this fingerprint is set.
       * @param resultStatus, Possible status results from calling Clover services. Most Clover service calls accept an instance of the ResultStatus class as an "out" parameter
       * @y.exclude
       *
       * The method deletes the fiungerprintId assosciated to the EmployeeId (input parameter). It throws a client exception for incorrect employeeId.
       * In this case resutStatus is between 400 and 499 or is equal to 999
       */

    void deleteFingerprintId(in String employeeId, out ResultStatus resultStatus);

    /**
       * Not available to non-Clover apps.
       *
       * Get the fingerprint of an employee.
       * @param employeeId, the employee's ID for whom this fingerprint is set.
       * @param resultStatus, Possible status results from calling Clover services. Most Clover service calls accept an instance of the ResultStatus class as an "out" parameter
       * @y.exclude
       *
       * The method gets the fiungerprintId assosciated to the EmployeeId (input parameter). It throws a client exception for incorrect employeeId.
       * In this case resutStatus is between 400 and 499 or is equal to 999
       */

    int getFingerprintId(in String employeeId, out ResultStatus resultStatus);

    /**
           * Not available to non-Clover apps.
           *
           * Get all the enrolled fingerprints in merchant db.
           * @param resultStatus, Possible status results from calling Clover services. Most Clover service calls accept an instance of the ResultStatus class as an "out" parameter
           * @y.exclude
           */
    int[] getEnrolledFingerprints(out ResultStatus resultStatus);
    /**
       * Not available to non-Clover apps.
       *
       * Get employeeId using the fingerprintId.
       * @param fingerprintId, the fingerprint ID of the employee.
       * @param resultStatus, Possible status results from calling Clover services. Most Clover service calls accept an instance of the ResultStatus class as an "out" parameter
       * @y.exclude
       *
       * The method gets the employeeId, given the fingerprintId. It throws a client exception if fingerprintId is incorrect (does not exist).
       * In this case resutStatus is between 400 and 499 or is equal to 999
       */

    String getEmployeeId(in int fingerprintId, out ResultStatus resultStatus);

}
