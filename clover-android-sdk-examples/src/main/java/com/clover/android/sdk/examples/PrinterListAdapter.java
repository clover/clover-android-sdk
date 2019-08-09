package com.clover.android.sdk.examples;

import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
  List<Printer> printerList;
  Context context;
  private static ClickListener mClickListener;
  private int selectedPosition = -1;

  public PrinterListAdapter(Context context, List<Printer> result, ClickListener mClickListener) {
    this.printerList = result;
    this.context = context;
    this.mClickListener = mClickListener;
  }

  @NonNull
  @Override
  public PrinterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v = LayoutInflater.from(context)
        .inflate(R.layout.printer_adapter_layout, viewGroup, false);
    PrinterViewHolder holder = new PrinterViewHolder(v, mClickListener);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull PrinterViewHolder printerViewHolder, int i) {
    Printer item = printerList.get(i);
    printerViewHolder.tvId.setText(item.getUuid());
    printerViewHolder.tvName.setText(item.getName());
    int categoryIndex = item.getCategory().ordinal();
    printerViewHolder.tvType.setText(Category.values()[categoryIndex].toString());
    if (selectedPosition == i) {
      printerViewHolder.cbSelectedPrinter.setChecked(true);
    } else {
      printerViewHolder.cbSelectedPrinter.setChecked(false);
    }
  }

  @Override
  public int getItemCount() {
    return printerList.size();
  }

  public class PrinterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvId;
    ClickListener mClickListener;
    TextView tvName;
    TextView tvType;
    CheckBox cbSelectedPrinter;

    public PrinterViewHolder(@NonNull View itemView, ClickListener clickListener) {
      super(itemView);
      tvId = (TextView) itemView.findViewById(R.id.tvId);
      tvName = (TextView) itemView.findViewById(R.id.tvName);
      tvType = (TextView) itemView.findViewById(R.id.tvType);
      cbSelectedPrinter = (CheckBox) itemView.findViewById(R.id.cbPrinterSelect);
      this.mClickListener = clickListener;
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (getAdapterPosition() == RecyclerView.NO_POSITION) {
        return;
      }
      notifyItemChanged(selectedPosition);
      PrinterListAdapter.mClickListener.onItemClick(getAdapterPosition(), v);
      selectedPosition = getAdapterPosition();
      notifyItemChanged(selectedPosition);
    }
  }

  public interface ClickListener {
    void onItemClick(int position, View v);
  }
}
