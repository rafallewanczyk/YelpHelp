package com.example.yelphelp.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yelphelp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatsViewAdapter extends RecyclerView.Adapter<ChatsViewHolders>  {

    private List<Chat> chatsList;
    private Context context;

    public ChatsViewAdapter(List<Chat> matchesList, Context context){
        this.chatsList= matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatsViewHolders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatsViewHolders rcv = new ChatsViewHolders((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatsViewHolders holder, int position) {
        holder.mMessage.setText(chatsList.get(position).getMessage());
        if(chatsList.get(position).getCurrentUser() == true){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }

        if(chatsList.get(position).getCurrentUser() == true){
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }

    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }
}
