package com.example.yelphelp.Offer;

import androidx.annotation.NonNull;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yelphelp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EditOfferActivity extends CreateOfferActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offerId = this.getIntent().getExtras().getString("offerId");
        getOfferInfo();

        mBack.setText("Delete");
        mBack.setBackgroundColor(Color.parseColor("#FF0000"));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
                finish();
                return;
            }
        });


    }

    protected void getOfferInfo() {
        mOfferDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.child(offerId).getValue();
                    if (map.get("title") != null) {
                        title = map.get("title").toString();
                        mTitleEdit.setText(map.get("title").toString());
                        mTitleView.setText(map.get("title").toString());
                    }

                    if (map.get("description") != null) {
                        description= map.get("description").toString();
                        mDescriptionEdit.setText(map.get("description").toString());
                        mDescriptionView.setText(map.get("description").toString());
                    }

                    if(map.get("lat") != null){
                        lat = Double.parseDouble(map.get("lat").toString());
                    }

                    if(map.get("lang") != null){
                        lang = Double.parseDouble(map.get("lang").toString());
                    }

                    if (map.get("offerImage") != null) {
                        offerImageUrl= map.get("offerImage").toString();
                        switch (offerImageUrl) {
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mOfferImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(offerImageUrl).into(mOfferImage);
                                break;

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void delete(){
        DatabaseReference chatDb = FirebaseDatabase.getInstance().getReference().child("Chat");
        mOfferDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot user : snapshot.child(offerId).child("connections").child("accepted").getChildren()){
                        if(user.child("chatId").exists()){
                            chatDb.child(user.child("chatId").getValue().toString()).removeValue();
                        }
                    }
                    mOfferDb.child(offerId).removeValue();

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}