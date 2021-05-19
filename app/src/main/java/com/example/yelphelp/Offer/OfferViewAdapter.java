package com.example.yelphelp.Offer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yelphelp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OfferViewAdapter extends RecyclerView.Adapter<OfferViewHolder>  {

    private List<Offer> offersList;
    private Context context;
    private String onClick;

    public OfferViewAdapter(List<Offer> offersList, Context context, String onClick){
        this.offersList = offersList;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer_list, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        OfferViewHolder rcv = new OfferViewHolder(layoutView, onClick);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OfferViewHolder holder, int position) {
        holder.mOfferId.setText(offersList.get(position).getOfferId());
        holder.mOfferTitle.setText(offersList.get(position).getTitle());
        holder.mChatId.setText(offersList.get(position).getChatId());
        if(!offersList.get(position).getOfferImageUrl().equals("default")){
            Glide.with(context).load(offersList.get(position).getOfferImageUrl()).into(holder.mOfferImage);
        }

    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }
}
