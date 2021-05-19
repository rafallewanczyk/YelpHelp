package com.example.yelphelp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yelphelp.Offer.EditOfferActivity;
import com.example.yelphelp.Offer.Offer;
import com.example.yelphelp.Offer.OfferArrayAdapter;
import com.example.yelphelp.Offer.ShowOfferActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BrowseOffersActivity extends AppCompatActivity {

    private Offer offers[];
    private OfferArrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    private String currentUid;
    private DatabaseReference usersDb, offersDb;

    ListView listView;
    List<Offer> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_offers);
        mAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        offersDb = FirebaseDatabase.getInstance().getReference().child("Offers");
        currentUid = mAuth.getUid();

        getAvailableOffers();


        rowItems = new ArrayList<Offer>();
        arrayAdapter = new OfferArrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Offer offer = (Offer) dataObject;
                offersDb.child(offer.getOfferId()).child("connections").child("declined").child(currentUid).setValue("true");

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Offer offer = (Offer) dataObject;
                String chatKey = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                offersDb.child(offer.getOfferId()).child("connections").child("accepted").child(currentUid).setValue("true");
                offersDb.child(offer.getOfferId()).child("connections").child("accepted").child(currentUid).child("chatId").setValue(chatKey);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
//                View view = flingContainer.getSelectedView();
//                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
//                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Offer offer = (Offer)dataObject;
                Intent intent = new Intent(BrowseOffersActivity.this, ShowOfferActivity.class);
                Bundle b = new Bundle();
                b.putString("chatId", offer.getChatId());
                b.putString("offerId", offer.getOfferId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }


    public void getAvailableOffers() {
        offersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists() && !snapshot.child("connections").child("accepted").hasChild(currentUid) && !snapshot.child("connections").child("declined").hasChild(currentUid) && !snapshot.child("owner").getValue().toString().equals(currentUid)) {
                    String offerImageUrl = "defult";
                    if (!snapshot.child("offerImage").getValue().equals("default")) {
                        offerImageUrl = snapshot.child("offerImage").getValue().toString();
                    }
                    Offer offer = new Offer(snapshot.getKey(), snapshot.child("description").getValue().toString(), snapshot.child("title").getValue().toString(), offerImageUrl, "");
                    rowItems.add(offer);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}