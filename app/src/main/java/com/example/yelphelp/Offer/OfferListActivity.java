package com.example.yelphelp.Offer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.yelphelp.Matches.Match;
import com.example.yelphelp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OfferListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mOfferAdapter;
    private RecyclerView.LayoutManager mOfferLayoutManager;
    private TextView mTitle;
    private String offersType;

    private String currentUserId;
    private String onElementClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);


        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        offersType = getIntent().getExtras().getString("offersType");

        if(offersType.equals("editOffers")){
            onElementClick = "edit";
        }else if (offersType.equals("belongingOffers")){
            onElementClick = "belonging";
        }else {
            onElementClick = "view";
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mOfferLayoutManager = new LinearLayoutManager(OfferListActivity.this);
        mRecyclerView.setLayoutManager(mOfferLayoutManager);
        mOfferAdapter = new OfferViewAdapter(getDatasetOffers(),OfferListActivity.this, onElementClick);
        mRecyclerView.setAdapter(mOfferAdapter);

        if(offersType.equals("acceptedOffers")){
            getAcceptedOffersIds();
        }

        if(offersType.equals("belongingOffers")){
            getBelongingOffersIds();
        }

        if(offersType.equals("editOffers")){
            getBelongingOffersIds();
        }

    }

    @Override
    public void onRestart() {

        super.onRestart();
        if(offersType.equals("acceptedOffers")){
            getAcceptedOffersIds();
        }

        if(offersType.equals("belongingOffers")){
            getBelongingOffersIds();
        }

        if(offersType.equals("editOffers")){
            getBelongingOffersIds();
        }
    }

    private void getBelongingOffersIds() {
        resultsOffers.clear();
        DatabaseReference offerDb = FirebaseDatabase.getInstance().getReference().child("Offers");
        offerDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot offer: snapshot.getChildren()){
                        if(offer.child("owner").getValue().toString().equals(currentUserId)){
                            FetchBelongingOfferInformation(offer.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        mOfferAdapter.notifyDataSetChanged();
    }

    private void FetchBelongingOfferInformation(String key) {
        DatabaseReference offerDb= FirebaseDatabase.getInstance().getReference().child("Offers").child(key);
        offerDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String offerId = snapshot.getKey();
                    String title= "";
                    String description= "";
                    String offerImageUrl= "";
                    String chatId = "";

                    if(snapshot.child("title").getValue() != null){
                        title= snapshot.child("title").getValue().toString();
                    }

                    if(snapshot.child("description").getValue() != null){
                        description= snapshot.child("description").getValue().toString();
                    }
                    if(snapshot.child("offerImage").getValue() != null){
                        offerImageUrl= snapshot.child("offerImage").getValue().toString();
                    }
                    if(offersType.equals("belongingOffers")){
                        for(DataSnapshot acceptedBy : snapshot.child("connections").child("accepted").getChildren()){
                            chatId = acceptedBy.child("chatId").getValue().toString();
                            Offer offer = new Offer(offerId, description, title, offerImageUrl, chatId);
                            resultsOffers.add(offer);
                            mOfferAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Offer offer = new Offer(offerId, description, title, offerImageUrl, chatId);
                        resultsOffers.add(offer);
                        mOfferAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getAcceptedOffersIds() {
        resultsOffers.clear();
        DatabaseReference offerDb = FirebaseDatabase.getInstance().getReference().child("Offers");
        offerDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot offer: snapshot.getChildren()){
                        if(offer.child("connections").child("accepted").hasChild(currentUserId)){
                            FetchOfferInformation(offer.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void FetchOfferInformation(String key) {
        DatabaseReference offerDb= FirebaseDatabase.getInstance().getReference().child("Offers").child(key);
        offerDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String offerId = snapshot.getKey();
                    String title= "";
                    String description= "";
                    String offerImageUrl= "";
                    String chatId = "";

                    if(snapshot.child("title").getValue() != null){
                        title= snapshot.child("title").getValue().toString();
                    }

                    if(snapshot.child("description").getValue() != null){
                        description= snapshot.child("description").getValue().toString();
                    }
                    if(snapshot.child("offerImage").getValue() != null){
                        offerImageUrl= snapshot.child("offerImage").getValue().toString();
                    }
                    if(snapshot.child("connections").child("accepted").child(currentUserId).child("chatId").getValue() != null){
                        chatId = snapshot.child("connections").child("accepted").child(currentUserId).child("chatId").getValue().toString();
                    }
                    Offer offer = new Offer(offerId, description, title, offerImageUrl, chatId);
                    resultsOffers.add(offer);
                    mOfferAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private ArrayList<Offer> resultsOffers = new ArrayList<Offer>();
    private List<Offer> getDatasetOffers() {
        return resultsOffers;
    }
}