package com.example.android_task.Rooms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.android_task.R;
import com.example.android_task.databinding.RoomListItemBinding;

import java.util.ArrayList;

public class RoomsListRecyclerAdapter extends RecyclerView.Adapter<RoomsListRecyclerAdapter.ViewHolder> {

    ArrayList<RoomModel> roomsList;
    Context mContext;

    public RoomsListRecyclerAdapter(Context mContext, ArrayList<RoomModel> roomsList) {
        this.roomsList = roomsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        RoomModel model = roomsList.get(position);
        int index = position;
        ArrayList imageList = new ArrayList();
        for (int i = 0; i < model.images.size(); i++) {
            imageList.add(new SlideModel(model.images.get(i), ScaleTypes.FIT));
        }
        holder.binding.imageSlider.setImageList(imageList);
        holder.binding.roomName.setText(model.roomName);
        holder.binding.price.setText("Price : " + model.price + " USD");
        holder.binding.facility.setText(model.roomFacilityName);
        holder.binding.capacity.setText("Capacity : " + model.roomMaxPerson + " Person");

        holder.binding.addToCartBtn.setOnClickListener(v -> ((RoomsListActivity) mContext).addToCart(index));
    }

    @Override
    public int getItemCount() {
        return roomsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoomListItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RoomListItemBinding.bind(itemView);
        }
    }


}

