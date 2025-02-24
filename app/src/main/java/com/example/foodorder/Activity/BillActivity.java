package com.example.foodorder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.BillAdapter;
import com.example.foodorder.Domain.Bill;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityBillBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BillActivity extends AppCompatActivity {

    private ActivityBillBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the back button
        ImageView backBtn = findViewById(R.id.backBtn);

        // Set an OnClickListener for the back button
        backBtn.setOnClickListener(v -> {
            // Call finish() to close the current activity and return to the previous one
            finish();
        });

        // Initialize the Done button
        Button doneBtn = findViewById(R.id.DoneBtn);

        // Set an OnClickListener for the Done button
        doneBtn.setOnClickListener(v -> {
            // Get the current bill information from the UI
            double totalFee = Double.parseDouble(binding.totalFeeTxt.getText().toString().replace("$", ""));
            double tax = Double.parseDouble(binding.taxTxt.getText().toString().replace("$", ""));
            double delivery = Double.parseDouble(binding.deliveryTxt.getText().toString().replace("$", ""));
            double total = Double.parseDouble(binding.totalTxt.getText().toString().replace("$", ""));

            // Create a new Bill object with the current bill information
            Bill bill = new Bill(totalFee, tax, delivery, total);

            // Save the bill to Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Bills");
            myRef.push().setValue(bill);

            // Call startActivity() with an Intent to launch MainActivity
            Intent intent = new Intent(BillActivity.this, MainActivity.class);
            startActivity(intent);
        });

        initdata();
    }

    private void initdata() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double totalFee = 0.0;
                    double tax = 0.0;
                    double delivery = 0.0;
                    double total = 0.0;

                    ArrayList<Foods> listFoods = new ArrayList<>();

                    // Lấy danh sách sản phẩm từ Firebase
                    if (dataSnapshot.child("productList").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("productList").getChildren()) {
                            Foods food = snapshot.getValue(Foods.class); // Chuyển đổi dữ liệu Firebase thành đối tượng Foods
                            if (food != null) {
                                listFoods.add(food);
                            }
                        }
                    }

                    // Lấy giá trị tổng tiền, thuế, phí giao hàng
                    if (dataSnapshot.child("totalFee").exists()) {
                        totalFee = dataSnapshot.child("totalFee").getValue(Double.class);
                    }

                    if (dataSnapshot.child("tax").exists()) {
                        tax = dataSnapshot.child("tax").getValue(Double.class);
                    }

                    if (dataSnapshot.child("delivery").exists()) {
                        delivery = dataSnapshot.child("delivery").getValue(Double.class);
                    }

                    if (dataSnapshot.child("total").exists()) {
                        total = dataSnapshot.child("total").getValue(Double.class);
                    }

                    // Khởi tạo RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(BillActivity.this));

                    // Khởi tạo adapter và set vào RecyclerView
                    BillAdapter adapter = new BillAdapter(listFoods, BillActivity.this);
                    recyclerView.setAdapter(adapter);

                    // Hiển thị tổng tiền, thuế, phí giao hàng
                    binding.totalFeeTxt.setText("$" + totalFee);
                    binding.taxTxt.setText("$" + tax);
                    binding.deliveryTxt.setText("$" + delivery);
                    binding.totalTxt.setText("$" + total);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors
            }
        });
    }


}