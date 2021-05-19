package com.example.yelphelp.Offer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yelphelp.Cards.Card;
import com.example.yelphelp.R;

import java.util.List;

public class OfferArrayAdapter extends ArrayAdapter {
    Context context;

    public OfferArrayAdapter(Context context, int resourceId, List<Offer> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Offer offer = (Offer) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(offer.getTitle());
        switch (offer.getOfferImageUrl()){
            case "default":
                Glide.with(getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.with(getContext()).load(offer.getOfferImageUrl()).into(image);
                break;

        }

        return convertView;
    }
}
