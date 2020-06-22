package com.krystofrapp.limelightbeauty.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.krystofrapp.limelightbeauty.R;

public class Profile extends AppCompatActivity {

    private AppCompatImageButton nav_arrow;
    // private TextView busName, busPhone, busAddress, busNickname;
    private FirebaseFirestore cloudDb = FirebaseFirestore.getInstance();
    private CollectionReference profileDetailsRef = cloudDb.collection("Business Accounts");
    private ProfileDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nav_arrow = findViewById(R.id.toAccount);

        //busName = findViewById(R.id.profile_bsn);
        //busPhone = findViewById(R.id.profile_number);
        //busAddress = findViewById(R.id.profile_address);
        //busNickname = findViewById(R.id.profile_nickname);

        nav_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, AccountFragment.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = profileDetailsRef.orderBy("businessName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ProfileDetails> options = new FirestoreRecyclerOptions.Builder<ProfileDetails>()
                .setQuery(query, ProfileDetails.class)
                .build();

        adapter = new ProfileDetailsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
