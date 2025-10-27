package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        var sharedPreferences = getSharedPreferences("StoredData", MODE_PRIVATE);
        Log.d("contact_name", sharedPreferences.getString("contact_name", ""));
        Log.d("phone_number", sharedPreferences.getString("phone_number", ""));
        Log.d("email", sharedPreferences.getString("email", ""));
    }

    public void onToggle(View v) {
        ToggleButton toggleB = (ToggleButton)v;

        View v1 = findViewById(R.id.contact_name);
        View v2 = findViewById(R.id.phone_number);
        View v3 = findViewById(R.id.email);
        if(toggleB.isChecked()) { // when toggled, it should go into View mode
            v1.setEnabled(false);
            v2.setEnabled(false);
            v3.setEnabled(false);
        } else {
            v1.setEnabled(true);
            v2.setEnabled(true);
            v3.setEnabled(true);
        }
    }

    public void saveData(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("StoredData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        TextInputEditText v1 = findViewById(R.id.contact_name);
        TextInputEditText v2 = findViewById(R.id.phone_number);
        TextInputEditText v3 = findViewById(R.id.email);

        editor.putString("contact_name", v1.getText().toString());
        editor.putString("phone_number", v2.getText().toString());
        editor.putString("email", v3.getText().toString());

        editor.apply();

        Log.d("contact_name", sharedPreferences.getString("contact_name", ""));
        Log.d("phone_number", sharedPreferences.getString("phone_number", ""));
        Log.d("email", sharedPreferences.getString("email", ""));
    }
}


