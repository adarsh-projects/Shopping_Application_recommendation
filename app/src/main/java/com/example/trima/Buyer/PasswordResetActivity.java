package com.example.trima.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trima.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.trima.prevalent.*;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PasswordResetActivity extends AppCompatActivity {
    private String check ;
    private TextView headername, titlequestion;
    private EditText phonenumber, question1, question2;
    private Button  verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        check = getIntent().getStringExtra("check");

        headername = (TextView)findViewById(R.id.reset_password);
        titlequestion = (TextView)findViewById(R.id.reset_page_header);

        phonenumber = (EditText)findViewById(R.id.reset_page_phone_number);
        question1 = (EditText)findViewById(R.id.reset_page_question1);
        question2 = (EditText)findViewById(R.id.reset_page_question2);

        verify = (Button)findViewById(R.id.reset_page_confirmation);

    }

    @Override
    protected void onStart() {
        super.onStart();
        phonenumber.setVisibility(View.GONE);
        if(check.equals("setting")){
            headername.setText("Set Question");
            titlequestion.setText("Please write a answer for following security question");
            verify.setText("Set");
            displayAnswer();
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAnswer();
                    Intent i = new Intent(PasswordResetActivity.this, SettingActivity.class);
                    startActivity(i);
                }
            });

        }else if(check.equals("login")){
            phonenumber.setVisibility(View.VISIBLE);
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });
        }
    }

    private void verifyUser() {
        final String phone = phonenumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child(phone);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            ref.child("Security Question").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){

                                        String ans1 = snapshot.child("answer1").getValue().toString();
                                        String ans2 = snapshot.child("answer2").getValue().toString();

                                        if(!ans1.equals(answer1)){
                                            Toast.makeText(PasswordResetActivity.this, "First answer is wrong", Toast.LENGTH_SHORT).show();
                                        }else if(!ans2.equals(answer2)){
                                            Toast.makeText(PasswordResetActivity.this, "Second answer is wrong", Toast.LENGTH_SHORT).show();
                                        }else{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordResetActivity.this);
                                            builder.setTitle("New Password");
                                            final EditText newPassword = new EditText(PasswordResetActivity.this);
                                            newPassword.setHint("change password");
                                            builder.setView(newPassword);
                                            builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String np = newPassword.getText().toString();
                                                    if(np.equals("")){
                                                        Toast.makeText(PasswordResetActivity.this, "Please write a password", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        ref.child("Password").setValue(np)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(PasswordResetActivity.this, "Password changed successful", Toast.LENGTH_SHORT).show();
                                                                    Intent i = new Intent(PasswordResetActivity.this, LoginPage.class);
                                                                    startActivity(i);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                            builder.show();
                                        }

                                    }else{
                                        Toast.makeText(PasswordResetActivity.this, "You have not provided security question.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }else{
                            Toast.makeText(PasswordResetActivity.this, "This Number not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }

    private void setAnswer(){
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if(question1.equals("") || question2.equals("")){
            Toast.makeText(PasswordResetActivity.this, "Please write a answer", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(prevalent.CurrentOnlineUser.getPhone());
            ref.child("Security Question");
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("answer1", answer1);
            hm.put("answer2", answer2);
            ref.child("Security Question").updateChildren(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(PasswordResetActivity.this, "Answer updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void displayAnswer(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(prevalent.CurrentOnlineUser.getPhone()).child("Security Question");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}