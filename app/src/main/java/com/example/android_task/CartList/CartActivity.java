package com.example.android_task.CartList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.android_task.Helper.GlobalVariables;
import com.example.android_task.R;
import com.example.android_task.Rooms.RoomsListActivity;
import com.example.android_task.Rooms.RoomsListRecyclerAdapter;
import com.example.android_task.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity implements CartInterface {

    ActivityCartBinding binding;
    CarListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.continueShoppingBtn.setOnClickListener(v -> {
            onCloseActivity();
        });

        setUpCartList();
    }

    void setUpCartList() {
        if (GlobalVariables.cartList.size() > 0) {
            binding.emptyCartLayout.setVisibility(View.GONE);
            binding.cartLayout.setVisibility(View.VISIBLE);
        }

        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        adapter = new CarListRecyclerAdapter(CartActivity.this);
        binding.cartRecyclerView.setAdapter(adapter);

    }

    @Override
    public void showEmptyCart() {
        binding.emptyCartLayout.setVisibility(View.VISIBLE);
        binding.cartLayout.setVisibility(View.GONE);
    }

    @Override
    public void setTotalAmount(double totalAmount) {
        binding.totalAmount.setText(totalAmount + "");
    }

    @Override
    public void onBackPressed() {
        GlobalVariables.cartList.clear();
        GlobalVariables.cartList.addAll(adapter.getRoomList());
        super.onBackPressed();
    }

    void onCloseActivity() {
        GlobalVariables.cartList.clear();
        GlobalVariables.cartList.addAll(adapter.getRoomList());
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            onCloseActivity();
        }
        return true;
    }
}