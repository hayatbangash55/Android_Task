package com.example.android_task.CartList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_task.Helper.GlobalVariables;
import com.example.android_task.R;
import com.example.android_task.Rooms.RoomsListActivity;
import com.example.android_task.databinding.CartListItemBinding;

import java.util.ArrayList;

public class CarListRecyclerAdapter extends RecyclerView.Adapter<CarListRecyclerAdapter.ViewHolder> {

    ArrayList<CartModel> roomsList = new ArrayList<>();
    Context mContext;

    public CarListRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        roomsList.addAll(GlobalVariables.cartList);
        calculateTotalAmount();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        CartModel model = roomsList.get(position);
        if (model.images.size() > 0) {
            Glide.with(mContext).load(model.images.get(0)).into(holder.binding.image);
        }

        holder.binding.roomName.setText(model.roomName);
        holder.binding.price.setText(String.format("Price : %s USD", model.price));
        holder.binding.facility.setText(model.roomFacilityName);
        holder.binding.capacity.setText(String.format("Capacity : %s Person", model.roomMaxPerson));

        holder.binding.tvCount.setText(model.quantity + "");

        int index = position;
        holder.binding.btnIncrementCount.setOnClickListener(v -> {
            try {
                model.quantity++;
                holder.binding.tvCount.setText(model.quantity + "");
                roomsList.remove(index);
                roomsList.add(index, model);
                GlobalVariables.cartCount++;
                calculateTotalAmount();

                GlobalVariables.cartList = new ArrayList<>();
                GlobalVariables.cartList.addAll(roomsList);
            } catch (Exception e) {
                Log.d("TAG", "onClick: " + e.getMessage());
            }
        });

        holder.binding.btnDecrementCount.setOnClickListener(v -> {
            try {
                if (model.quantity == 1) {
                    roomsList.remove(index);
                    notifyItemRemoved(index);
                    notifyDataSetChanged();
                    if (roomsList.size() == 0) {
                        ((CartActivity) mContext).showEmptyCart();
                    }
                } else {
                    model.quantity--;
                    holder.binding.tvCount.setText(model.quantity + "");
                    roomsList.remove(index);
                    roomsList.add(index, model);
                }
                GlobalVariables.cartCount--;
                calculateTotalAmount();
                GlobalVariables.cartList = new ArrayList<>();
                GlobalVariables.cartList.addAll(roomsList);
            } catch (Exception e) {
                Log.d("TAG", "onClick: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CartListItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CartListItemBinding.bind(itemView);
        }
    }

    void calculateTotalAmount() {
        double totalAmount = 0;
        for (int i = 0; i < roomsList.size(); i++) {
            totalAmount += (roomsList.get(i).quantity * Double.parseDouble(roomsList.get(i).price));
        }

        ((CartActivity) mContext).setTotalAmount(totalAmount);
    }

    ArrayList<CartModel> getRoomList() {
        Log.d("TAG", "getRoomList: " + this.roomsList.size());
        return roomsList;
    }
}

