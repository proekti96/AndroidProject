package com.example.cultureevents;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import static java.lang.Thread.sleep;


public class UserFragment extends Fragment {
    ProgressBar prg;
    Button btn;
    View view;
    EditText text;
    CircleProgressbar circleProgressbar;
    CheckBox checkBox;
    Dialog location_dialog;
    int j=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user, container, false);
        // Inflate the layout for this fragment
       // prg = view.findViewById(R.id.progressBar);
        btn = view.findViewById(R.id.button);
        circleProgressbar=view.findViewById(R.id.progress_circular);
        location_dialog=new Dialog(getContext());
        circleProgressbar.enabledTouch(false);
        text = view.findViewById(R.id.editTextTextPersonName);
        checkBox=view.findViewById(R.id.checkBox);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText("");

                while(true) {
                    j += 1;
                    break;
                }
               circleProgressbar.setProgress(5*j);

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showPopUp(view);
                circleProgressbar.setProgress(15);
            }
        });



        return view;
    }

    public void showPopUp(View v){
        location_dialog.setContentView(R.layout.location_check);
        TextView txtClose;
        ProgressBar progressBar=v.findViewById(R.id.pBar);
        txtClose=location_dialog.findViewById(R.id.txtclose);


        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                location_dialog.dismiss();
            }
        }.start();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  location_dialog.dismiss();
                try {
                    sleep(1000);
                    location_dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // close splash activity
                location_dialog.dismiss();
            }
        });


        location_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        location_dialog.show();
    }
}