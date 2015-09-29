package com.clover.sdk.v3.employees;

import com.clover.sdk.v3.employees.Employee;

/**
 * An interface for receiving events pertaining to an employee. Add a listener as follows,
 * <pre>
 * <code>
 * iEmployeeService.addListener(new IEmployeeListener.Stub() {
 *     {@literal @}Override
 *     public void onActiveEmployeeChanged(Merchant merchant) {
 *       // active employee has changed, use it here
 *     }
 * };
 * </code>
 * </pre>
 * If using {@link com.clover.sdk.v1.employee.EmployeeConnector} to interact with the employee
 * serivce, you may add listeners there.
 *
 * @see com.clover.sdk.v1.employee.IEmployeeService
 * @see com.clover.sdk.v1.employee.IEmployeeService#addListener(IEmployeeListener,ResultStatus)
 * @see com.clover.sdk.v1.employee.IEmployeeService#removeListener(IEmployeeListener,ResultStatus)
 * @see com.clover.sdk.v1.employee.EmployeeConnector
 */
oneway interface IEmployeeListener {
    void onActiveEmployeeChanged(in Employee employee);
}
