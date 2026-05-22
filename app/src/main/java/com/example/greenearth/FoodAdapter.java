package com.example.greenearth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;

    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = foodList.get(position);
        holder.txtFoodName.setText(item.getName());
        holder.txtImpactLabel.setText(item.getImpactLabel());
        holder.imgFoodIcon.setImageResource(item.getImageResId());

        // Opsional: Ubah warna label berdasarkan teksnya agar sesuai desain
        if (item.getImpactLabel().equals("High Impact")) {
            holder.txtImpactLabel.setTextColor(0xFFD84315); // Merah/Orange
            holder.txtImpactLabel.setBackgroundColor(0xFFFBE9E7);
        } else if (item.getImpactLabel().equals("Medium")) {
            holder.txtImpactLabel.setTextColor(0xFFEF6C00); // Orange
            holder.txtImpactLabel.setBackgroundColor(0xFFFFF3E0);
        } else {
            holder.txtImpactLabel.setTextColor(0xFF2E7D32); // Hijau
            holder.txtImpactLabel.setBackgroundColor(0xFFE8F5E9);
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoodIcon;
        TextView txtFoodName, txtImpactLabel;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoodIcon = itemView.findViewById(R.id.imgFoodIcon);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtImpactLabel = itemView.findViewById(R.id.txtImpactLabel);
        }
    }
}