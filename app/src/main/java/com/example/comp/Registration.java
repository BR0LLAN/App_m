package com.example.comp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    //объявление и определение компонентов
    ProgressBar progressBar;
    private EditText email_address;
    private EditText first_lastName;
    private EditText phone_user;
    private EditText password_user;
    private CheckBox check;
    private Button back_btn;
    private Button nxt_btn;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_register);

        // Инициализация компонентов экрана
        first_lastName = findViewById(R.id.editTextName);
        email_address = findViewById(R.id.editTextEmailAddress);
        phone_user = findViewById(R.id.editTextTextPhone);
        password_user = findViewById(R.id.editTextPassword);
        check = findViewById(R.id.checkBox);
        back_btn = findViewById(R.id.back_btn);
        nxt_btn = findViewById(R.id.next_btn);
        check = findViewById(R.id.checkBox);
        progressBar = findViewById(R.id.progress_load);

        final FirebaseFirestore dbStore = FirebaseFirestore.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        sessionManager = new SessionManager(getApplicationContext());
        auth = FirebaseAuth.getInstance();


        // Поведения класса
        //Функция кнопки "Назад"
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = Registration.this;
                Class nextActivity = MainActivity.class;

                Intent nextSignAct = new Intent(context, nextActivity);
                startActivity(nextSignAct);
                finish();
            }
        });

        //Функция регистрации
        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txt_email = email_address.getText().toString();
                final String txt_phone = phone_user.getText().toString();
                final String txt_pass = password_user.getText().toString();
                final String txt_name = first_lastName.getText().toString();


                if (validateEmailAddress(email_address) && TextUtils.isEmpty(txt_email)
                        || TextUtils.isEmpty(txt_phone) || TextUtils.isEmpty(txt_name)) {
                    Toast.makeText(Registration.this, "Заполните поля!", Toast.LENGTH_SHORT).show();
                } else if (txt_pass.length() < 6) {
                    Toast.makeText(Registration.this, "Пароль должен состоять не менее, чем из 6 символов!", Toast.LENGTH_SHORT).show();
                } else if (!check.isChecked()) {
                    Toast.makeText(Registration.this, "Вы не приняли соглашение!", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(txt_email, txt_pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressBar.setVisibility(View.GONE);

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    String uid = user.getUid();
                                    String emailU = user.getEmail();

                                    Map<String, Object> dataUser = new HashMap<>();
                                    String[] order = {" "};
                                    String[] favorite = {" "};

                                    dataUser.put("uid", uid);
                                    dataUser.put("email", emailU);
                                    dataUser.put("name", txt_name);
                                    dataUser.put("phone", txt_phone);
                                    dataUser.put("password", txt_pass);
                                    dataUser.put("orders", Arrays.asList(order));
                                    dataUser.put("favorites", Arrays.asList(favorite));
                                    dataUser.put("card", "");

                                    dbStore.collection("Users").document(uid).set(dataUser);
                                    Toast.makeText(Registration.this, "Успешно", Toast.LENGTH_SHORT).show();
                                    sessionManager.setLogin(true);

                                    startActivity(new Intent(getApplicationContext(), MainFragment.class));
                                    finish();
                                        }
                                    });
                                }
            }
        });
        if (sessionManager.getLogin()) {
            startActivity(new Intent(getApplicationContext(), MainFragment.class));
        }
    }

    private boolean validateEmailAddress(EditText email_address) {
        String emailInput = email_address.getText().toString();
        if (!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            return true;
        } else {
            Toast.makeText(this, "invalid email!", Toast.LENGTH_SHORT).show(); return false;
        }
    }
}