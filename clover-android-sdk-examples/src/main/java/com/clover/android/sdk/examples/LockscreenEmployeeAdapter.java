package com.clover.android.sdk.examples;

import com.clover.sdk.v3.employees.Employee;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class LockscreenEmployeeAdapter extends RecyclerView.Adapter<LockscreenEmployeeAdapter.EmployeeViewHolder> {
  private List<Employee> employeeList;
  private Context context;
  private static ClickListener clickListener;
  private int selectedPosition = -1;
  private int preSelected = -1;

  LockscreenEmployeeAdapter(Context context, List<Employee> employeeList, ClickListener mClickListener) {
    this.context = context;
    this.employeeList = employeeList;
    LockscreenEmployeeAdapter.clickListener = mClickListener;
  }

  @NonNull
  @Override
  public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter_lockscreen, viewGroup, false);
    return new EmployeeViewHolder(v, clickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull EmployeeViewHolder employeeViewHolder, int i) {
    Employee employee = employeeList.get(i);
    employeeViewHolder.tvEmployee.setText(employee.getName());
    if (selectedPosition == i) {
      if (preSelected == selectedPosition) {
        employeeViewHolder.tvEmployee.setSelected(false);
        preSelected = -1;
      } else {
        employeeViewHolder.tvEmployee.setSelected(true);
        preSelected = selectedPosition;
      }
    } else {
      employeeViewHolder.tvEmployee.setSelected(false);
    }
  }

  @Override
  public int getItemCount() {
    return employeeList.size();
  }

  public class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView tvEmployee;

    public EmployeeViewHolder(@NonNull View itemView, ClickListener listener) {
      super(itemView);
      tvEmployee = (TextView) itemView.findViewById(R.id.tv_employee_name);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (getAdapterPosition() == RecyclerView.NO_POSITION) {
        return;
      }
      notifyItemChanged(selectedPosition);
      selectedPosition = getAdapterPosition();
      if (preSelected == selectedPosition) {
        LockscreenEmployeeAdapter.clickListener.onItemClick(-1, v);
      } else {
        LockscreenEmployeeAdapter.clickListener.onItemClick(selectedPosition, v);
      }
      notifyItemChanged(selectedPosition);
    }
  }

  public interface ClickListener {
    void onItemClick(int position, View v);
  }
}


