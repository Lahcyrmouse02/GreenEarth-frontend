package com.example.greenearth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BreakdownAdapter extends RecyclerView.Adapter<BreakdownAdapter.BreakdownViewHolder> {

    private List<BreakdownItem> breakdownList;

    public BreakdownAdapter(List<BreakdownItem> breakdownList) {
        this.breakdownList = breakdownList;
    }

    @NonNull
    @Override
    public BreakdownViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_breakdown, parent, false);
        return new BreakdownViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreakdownViewHolder holder, int position) {
        BreakdownItem item = breakdownList.get(position);

        // Amankan dari nilai null
        String type = item.getType() != null ? item.getType() : "Unknown";
        String desc = item.getDescription() != null ? item.getDescription() : "";

        holder.txtTitle.setText(type);
        holder.txtDesc.setText(desc);
        holder.txtCarbon.setText(String.format("%.1f kg", item.getCarbonValue()));

        // Logika Pengaturan Ikon yang Tahan Kras
        if ("Transit".equalsIgnoreCase(type)) {
            holder.imgIcon.setImageResource(android.R.drawable.ic_menu_directions);
        } else if ("Energy".equalsIgnoreCase(type)) {
            holder.imgIcon.setImageResource(android.R.drawable.ic_menu_info_details);
        } else if ("Food".equalsIgnoreCase(type)) {
            holder.imgIcon.setImageResource(android.R.drawable.ic_menu_sort_by_size);
        } else {
            holder.imgIcon.setImageResource(android.R.drawable.ic_menu_help); // Ikon default
        }
    }

    @Override
    public int getItemCount() {
        return breakdownList.size();
    }

    public static class BreakdownViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTitle, txtDesc, txtCarbon;

        public BreakdownViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgBreakdownIcon);
            txtTitle = itemView.findViewById(R.id.txtBreakdownTitle);
            txtDesc = itemView.findViewById(R.id.txtBreakdownDesc);
            txtCarbon = itemView.findViewById(R.id.txtBreakdownCarbon);
        }
    }
}