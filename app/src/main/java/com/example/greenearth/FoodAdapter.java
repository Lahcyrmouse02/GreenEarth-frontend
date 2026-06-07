package com.example.greenearth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;

    // 1. TAMBAHKAN VARIABEL LISTENER INI
    private OnLogMealClickListener listener;

    // 2. BUAT INTERFACE SEBAGAI JEMBATAN
    public interface OnLogMealClickListener {
        void onMealClicked(float carbonValue);
    }

    // 3. UBAH CONSTRUCTOR UNTUK MENERIMA LISTENER
    public FoodAdapter(List<FoodItem> foodList, OnLogMealClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;
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
        holder.txtCarbonValue.setText(String.format("%.1f kg CO2", item.getCarbonValue()));

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imgFoodIcon);

        // Logika warna label (tetap sama)
        if (item.getImpactLabel().equals("High Impact")) {
            holder.txtImpactLabel.setTextColor(0xFFD84315);
            holder.txtImpactLabel.setBackgroundColor(0xFFFBE9E7);
        } else if (item.getImpactLabel().equals("Medium")) {
            holder.txtImpactLabel.setTextColor(0xFFEF6C00);
            holder.txtImpactLabel.setBackgroundColor(0xFFFFF3E0);
        } else {
            holder.txtImpactLabel.setTextColor(0xFF2E7D32);
            holder.txtImpactLabel.setBackgroundColor(0xFFE8F5E9);
        }

        // 4. UBAH LOGIKA TOMBOL KLIK DI SINI
        holder.btnLogMeal.setOnClickListener(v -> {
            if (listener != null) {
                // Kirim nilai karbon ke Fragment saat tombol ditekan
                listener.onMealClicked(item.getCarbonValue());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoodIcon;
        TextView txtFoodName, txtImpactLabel, txtCarbonValue;
        Button btnLogMeal;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoodIcon = itemView.findViewById(R.id.imgFoodIcon);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtImpactLabel = itemView.findViewById(R.id.txtImpactLabel);
            txtCarbonValue = itemView.findViewById(R.id.txtCarbonValue);
            btnLogMeal = itemView.findViewById(R.id.btnLogMeal);
        }
    }
}