package com.example.yelphelp.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelphelp.Chat.ChatsActivity;
import com.example.yelphelp.R;

import org.jetbrains.annotations.NotNull;

public class OfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mOfferId, mOfferTitle, mChatId;
    public Button mMessage;
    public ImageView mOfferImage;
    public String test;

    public OfferViewHolder(@NonNull @NotNull View itemView, String test) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.test = test;

        mOfferId = (TextView) itemView.findViewById(R.id.offerId);
        mOfferTitle = (TextView) itemView.findViewById(R.id.title);
        mOfferImage = (ImageView) itemView.findViewById(R.id.offerImage);
        mChatId = (TextView) itemView.findViewById(R.id.chatId);
        mMessage = (Button) itemView.findViewById(R.id.message);

        if(this.test.equals("edit")){
            mMessage.setVisibility(View.GONE);
        }

        mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatsActivity.class);
                Bundle b = new Bundle();
                b.putString("chatId", mChatId.getText().toString());
                b.putString("offerId", mOfferId.getText().toString());
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Bundle b = new Bundle();
        b.putString("chatId", mChatId.getText().toString());
        b.putString("offerId", mOfferId.getText().toString());

        if (test.equals("view")) {
            Intent intent = new Intent(view.getContext(), ShowOfferActivity.class);
            intent.putExtras(b);
            view.getContext().startActivity(intent);
        }else if(test.equals("edit") || test.equals("belonging")){
            Intent intent = new Intent(view.getContext(), EditOfferActivity.class);
            intent.putExtras(b);
            view.getContext().startActivity(intent);
        }

    }
}
