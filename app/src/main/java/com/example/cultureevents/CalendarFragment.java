package com.example.cultureevents;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cultureevents.models.Event;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;


public class CalendarFragment extends Fragment {

  //  CalendarView calendar;
    CalendarView calendar;
    View view;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference ref;
    Button myAgenda;
    Button agenda;
    Dialog login_dialog;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    List<String> names=new ArrayList<>();
    List<String> dates=new ArrayList<>();
    List<String> times=new ArrayList<>();
    List<String> description=new ArrayList<>();

    private DocumentReference documentReference;
    Event event;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_calendar1, container, false);
        CollapsingToolbarLayout collapsingToolbarLayout=view.findViewById(R.id.collapsingLayout);
        //collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
       // collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        login_dialog=new Dialog(getContext());
        recyclerView=view.findViewById(R.id.recyclerView);
        agenda=view.findViewById(R.id.agendaBtn);
        myAgenda=view.findViewById(R.id.myAgendaBtn);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Events");
        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        calendar=view.findViewById(R.id.calendar);

        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
       // ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                final String datePicked= i2 +"-"+(i1+1)+"-"+i;
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      names.clear();
                      times.clear();
                      description.clear();
                      dates.clear();

                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            String date=snapshot.child("date").getValue(String.class);
                            if (date.equals(datePicked)){
                               // eventList.add((Event) snapshot.getValue());
                                String n=snapshot.child("name").getValue(String.class);
                                String t=snapshot.child("time").getValue(String.class);
                                String d=snapshot.child("date").getValue(String.class).substring(0,2);
                                String ds=snapshot.child("description").getValue(String.class);
                                names.add(n);
                                times.add(t);
                                dates.add(d);
                                description.add(ds);

                            }

                        }
                        EventsAdapter adapter=new EventsAdapter(getActivity(), names, dates, times, description);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names.clear();
                times.clear();
                description.clear();
                dates.clear();

                names.add("Струшки вечери на поезијата");
                names.add("Маратон Тоше Проески");
                times.add("18:0");
                times.add("12:30");
                description.add("Струшки вечери на поезијата е меѓународен поетски фестивал што се одржуваво Струга.");
                description.add("Пречекување на маратонците во Крушево");
                dates.add("24");
                dates.add("24");
                EventsAdapter adapter=new EventsAdapter(getActivity(), names, dates, times, description);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });


        myAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrRemoveToFavourite(view);
            }
        });

        return view;
    }

    public void showPopUp(View v){
        TextView txtClose;
        SignInButton google_signIn;
        login_dialog.setContentView(R.layout.login_popup);
        txtClose=login_dialog.findViewById(R.id.txtclose);
        google_signIn=login_dialog.findViewById(R.id.sign_in_button);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_dialog.dismiss();

            }
        });

        google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });

        login_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        login_dialog.show();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Error occur while login",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void addOrRemoveToFavourite(View view) {

     //   documentReference = FirebaseFirestore.getInstance().document("users/" + title);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            showPopUp(view);
        }
    //    if(currentUser!=null) {
            names.clear();
            times.clear();
            description.clear();
            dates.clear();
            names.add("Струшки вечери на поезијата");
            times.add("18:0");
            description.add("Струшки вечери на поезијата е меѓународен поетски фестивал што се одржуваво Струга.");
            dates.add("24");
            EventsAdapter adapter=new EventsAdapter(getActivity(), names, dates, times, description);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      //  }
    }


    private void updateUI(FirebaseUser user){
//        Intent intent = new Intent(this, OpenRecipe.class);
//        startActivity(intent);
        login_dialog.dismiss();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}