package com.clover.android.sdk.examples;

import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter class to populate data in the RecyclerView used in PrintTaskTestActivity
 * @see PrintTaskTestActivity
 */
public class PrinterListAdapter extends RecyclerView.Adapter<PrinterListAdapter.PrinterViewHolder> {

  private final List<Printer> printerList;
  private final Context context;
  private final CheckListener mCheckListener;
  private int selectedPosition = -1;

  public PrinterListAdapter(Context context, List<Printer> result, CheckListener mCheckListener) {
    this.printerList = result;
    this.context = context;
    this.mCheckListener = mCheckListener;
  }

  @NonNull
  @Override
  public PrinterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v = LayoutInflater.from(context)
        .inflate(R.layout.printer_adapter_layout, viewGroup, false);
    PrinterViewHolder holder = new PrinterViewHolder(v, mCheckListener);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull PrinterViewHolder printerViewHolder, int position) {
    Printer item = printerList.get(position);
    printerViewHolder.tvId.setText(item.getUuid());
    printerViewHolder.tvName.setText(item.getName());
    int categoryIndex = item.getCategory().ordinal();
    printerViewHolder.tvType.setText(Category.values()[categoryIndex].toString());
    if (selectedPosition == position) {
      if (!printerViewHolder.cbSelectedPrinter.isChecked()) {
        printerViewHolder.cbSelectedPrinter.performClick();
      }
    } else {
      if (printerViewHolder.cbSelectedPrinter.isChecked()) {
        printerViewHolder.cbSelectedPrinter.performClick();
      }
    }
  }

  @Override
  public int getItemCount() {
    return printerList.size();
  }

  public class PrinterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvId;
    CheckListener mCheckListener;
    TextView tvName;
    TextView tvType;
    CheckBox cbSelectedPrinter;

    public PrinterViewHolder(@NonNull View itemView, CheckListener checkListener) {
      super(itemView);
      tvId = (TextView) itemView.findViewById(R.id.tvId);
      tvName = (TextView) itemView.findViewById(R.id.tvName);
      tvType = (TextView) itemView.findViewById(R.id.tvType);
      cbSelectedPrinter = (CheckBox) itemView.findViewById(R.id.cbPrinterSelect);
      this.mCheckListener = checkListener;
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (getAdapterPosition() == RecyclerView.NO_POSITION) {
        return;
      }

      if (cbSelectedPrinter.isChecked()) {
        selectedPosition = -1;
        mCheckListener.onItemUnchecked();
      } else {
        selectedPosition = getAdapterPosition();
        mCheckListener.onItemChecked(getAdapterPosition(), v);
      }

      PrinterListAdapter.this.notifyDataSetChanged();
    }
  }

  public interface CheckListener {
    void onItemChecked(int position, View v);
    void onItemUnchecked();
  }
}
