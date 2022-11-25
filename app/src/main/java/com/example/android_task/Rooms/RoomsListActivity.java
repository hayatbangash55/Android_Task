package com.example.android_task.Rooms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android_task.CartList.CartActivity;
import com.example.android_task.CartList.CartModel;
import com.example.android_task.Helper.GlobalVariables;
import com.example.android_task.Helper.LoadingDialog;
import com.example.android_task.R;
import com.example.android_task.databinding.ActivityRoomsBinding;
import com.example.android_task.databinding.BadgeLayoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomsListActivity extends AppCompatActivity implements RoomInterface {

    ActivityRoomsBinding binding;
    BadgeLayoutBinding badgeLayoutBinding;
    TextView textCartItemCount;
    LoadingDialog loadingDialog;
    ArrayList<RoomModel> roomsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        badgeLayoutBinding = BadgeLayoutBinding.inflate(getLayoutInflater());
        loadingDialog = new LoadingDialog(RoomsListActivity.this);
        getData();
    }


    void getData() {
        String url = "https://securereservation.org/api/getavailability.php?accommodationid=15&checkin=13-1-2023&checkout=14-1-2023&promo=&profileid=&multilanguageid=1&currency=USD&device=mobile";
        RequestQueue requestQueue = Volley.newRequestQueue(RoomsListActivity.this);
        loadingDialog.startLoadingDialog();
        StringRequest request = new StringRequest(
                Request.Method.GET, url, response -> {
            try {
                roomsList = new ArrayList<>();
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    RoomModel model = new RoomModel();
                    model.roomId = obj.getString("roomId");
                    model.roomName = obj.getString("roomName");
                    model.roomDescription = obj.getString("roomDescription");
                    model.price = obj.getString("price");
                    model.roomMaxPerson = obj.getString("roomMaxPerson");
                    model.roomFacilityName = obj.getString("roomFacilityName");
                    JSONArray images = obj.getJSONArray("roomImages");
                    model.images = new ArrayList<>();
                    for (int j = 0; j < images.length(); j++) {
                        String imageUrl = images.getString(j);
                        model.images.add(imageUrl);
                        Log.d("TAG", "getData: " + imageUrl);
                    }
                    roomsList.add(model);
                }

                binding.roomsList.setLayoutManager(new LinearLayoutManager(RoomsListActivity.this));
                RoomsListRecyclerAdapter adapter = new RoomsListRecyclerAdapter(RoomsListActivity.this, roomsList);
                binding.roomsList.setAdapter(adapter);
                loadingDialog.dismissDialog();
            } catch (JSONException e) {
                e.printStackTrace();
                loadingDialog.dismissDialog();
            }
        }, error -> {
            System.out.println(error.getMessage());
            loadingDialog.dismissDialog();
        }
        );
        requestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBadge();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cart_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);

        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        setupBadge();

        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.cart) {
            startActivity(new Intent(RoomsListActivity.this, CartActivity.class));
        }
        return true;
    }

    private void setupBadge() {
        Log.d("TAG", "setupBadge: " + GlobalVariables.cartCount);
        if (textCartItemCount != null) {
            if (GlobalVariables.cartCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(GlobalVariables.cartCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void addToCart(int index) {

        RoomModel roomModel = roomsList.get(index);
        //check item already exist
        boolean isExist = false;
        for (int i = 0; i < GlobalVariables.cartList.size(); i++) {
            CartModel model;
            if (GlobalVariables.cartList.get(i).roomId.equals(roomModel.roomId)) {
                model = GlobalVariables.cartList.get(i);
                model.quantity++;
                GlobalVariables.cartList.remove(i);
                GlobalVariables.cartList.add(i, model);
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            CartModel model = new CartModel();
            model.roomId = roomModel.roomId;
            model.roomName = roomModel.roomName;
            model.price = roomModel.price;
            model.roomMaxPerson = roomModel.roomMaxPerson;
            model.roomDescription = roomModel.roomDescription;
            model.roomFacilityName = roomModel.roomFacilityName;
            model.images = roomModel.images;
            model.quantity = 1;

            GlobalVariables.cartList.add(model);
        }

        GlobalVariables.cartCount++;
        setupBadge();

        Log.d("TAG", "addToCart: " + GlobalVariables.cartList.size());

    }
}