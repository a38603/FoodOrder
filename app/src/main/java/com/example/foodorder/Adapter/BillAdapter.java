package com.example.foodorder.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Domain.Foods;
import com.example.foodorder.R;

import java.util.ArrayList;
import java.util.Objects;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private final ArrayList<Foods> listFoods;

    public BillAdapter(ArrayList<Foods> listFoods, Context ignoredContext) {
        this.listFoods = listFoods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);

        // Return a new ViewHolder instance
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = listFoods.get(position);

        if (food != null) {
            Objects.requireNonNull(holder).nameTextView.setText(food.getName());

            double price = 0.0;
            Object priceObj = food.getPrice();
            if (priceObj instanceof Long) {
                price = ((Long) priceObj).doubleValue();
            } else if (priceObj instanceof Double) {
                price = (Double) priceObj;
            }

            holder.priceTextView.setText("$" + String.format("%.2f", price));

            // Kiểm tra giá trị của numberInCart để tránh lỗi
            int quantity = food.getNumberInCart(); // Không thể là null nếu kiểu dữ liệu là int
            holder.quantityTextView.setText("x" + quantity);
        }
    }



    @Override
    public int getItemCount() {
        return listFoods.size();
    }

    // ViewHolder class to hold references to the TextViews
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView quantityTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
        }
    }
}
