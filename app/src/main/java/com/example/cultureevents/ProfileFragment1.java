package com.example.cultureevents;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ProfileFragment1 extends Fragment {
    View view;
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile1, container, false);
        // Inflate the layout for this fragment
        button=view.findViewById(R.id.openProfile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFragment nextFrag= new UserFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.profile, nextFrag, "findThisFragment")
                        .commit();
                button.setVisibility(View.INVISIBLE);
//                Intent intent=new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);
            }
        });
        return view;
    }
}