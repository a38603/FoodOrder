package com.example.foodorder.Activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodorder.Adapter.CartAdapter;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.Helper.ManagmentCart;
import com.example.foodorder.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter<CartAdapter.viewholder> adapter;
    private ManagmentCart managmentCart;
    private double tax;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        setVariable();
        calculateCart();
        initList();
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.05; //percent 5% tax
        double delivery = 10; // 10 Dollar

        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);

        uploadDataToFirebase();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        // ...
        binding.buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, BillActivity.class);
                startActivity(intent);
            }
        });
    }

// ...



    private void uploadDataToFirebase() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ArrayList<Foods> productList = managmentCart.getListCart();

        for (Foods food : productList) {
            String key = databaseReference.child("products").push().getKey();
            databaseReference.child("products").child(key).setValue(food);
        }

        double totalFee = Math.round(managmentCart.getTotalFee() * 100) / 100;
        double tax = Math.round(managmentCart.getTotalFee() * 0.05 * 100) / 100;
        double delivery = 10;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;

        Map<String, Object> data = new HashMap<>();
        // Thêm dữ liệu từ initList()
        data.put("productList", managmentCart.getListCart());


        // Thêm dữ liệu từ calculateCart()
        data.put("totalFee", managmentCart.getTotalFee());
        data.put("tax", tax);
        data.put("delivery", delivery);
        data.put("total", total);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");
        myRef.setValue(data);
    }


}