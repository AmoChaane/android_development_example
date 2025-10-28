package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout contactNameLayout;
    private TextInputLayout phoneNumberLayout;
    private TextInputLayout emailLayout;
    private TextInputEditText contactNameEditText;
    private TextInputEditText phoneNumberEditText;
    private TextInputEditText emailEditText;
    private ToggleButton toggleButton;
    private Button saveButton;

    // Validation patterns
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,15}$");
    private static final Pattern EMAIL_PATTERN = Patterns.EMAIL_ADDRESS;

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

        initializeViews();
        setupTextWatchers();
        loadSavedData();
    }

    private void initializeViews() {
        contactNameLayout = findViewById(R.id.contact_name_layout);
        phoneNumberLayout = findViewById(R.id.phone_number_layout);
        emailLayout = findViewById(R.id.email_layout);
        contactNameEditText = findViewById(R.id.contact_name);
        phoneNumberEditText = findViewById(R.id.phone_number);
        emailEditText = findViewById(R.id.email);
        toggleButton = findViewById(R.id.toggleButton);
        saveButton = findViewById(R.id.button);
    }

    private void setupTextWatchers() {
        // Real-time validation for contact name
        contactNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateContactName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Real-time validation for phone number
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhoneNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Real-time validation for email
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean validateContactName(String name) {
        if (name.trim().isEmpty()) {
            contactNameLayout.setError("Contact name is required");
            return false;
        } else if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            contactNameLayout.setError("Contact name should contain only alphabetic characters");
            return false;
        } else {
            contactNameLayout.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNumber(String phone) {
        if (phone.trim().isEmpty()) {
            phoneNumberLayout.setError("Phone number is required");
            return false;
        } else if (!PHONE_PATTERN.matcher(phone.trim()).matches()) {
            phoneNumberLayout.setError("Enter a valid phone number (10-15 digits)");
            return false;
        } else {
            phoneNumberLayout.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String email) {
        if (email.trim().isEmpty()) {
            emailLayout.setError("Email is required");
            return false;
        } else if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            emailLayout.setError("Enter a valid email address");
            return false;
        } else {
            emailLayout.setError(null);
            return true;
        }
    }

    private boolean validateAllFields() {
        String name = contactNameEditText.getText().toString();
        String phone = phoneNumberEditText.getText().toString();
        String email = emailEditText.getText().toString();

        boolean isNameValid = validateContactName(name);
        boolean isPhoneValid = validatePhoneNumber(phone);
        boolean isEmailValid = validateEmail(email);

        return isNameValid && isPhoneValid && isEmailValid;
    }

    private void loadSavedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("StoredData", MODE_PRIVATE);
        
        String savedName = sharedPreferences.getString("contact_name", "");
        String savedPhone = sharedPreferences.getString("phone_number", "");
        String savedEmail = sharedPreferences.getString("email", "");

        contactNameEditText.setText(savedName);
        phoneNumberEditText.setText(savedPhone);
        emailEditText.setText(savedEmail);

        Log.d("LoadedData", "contact_name: " + savedName);
        Log.d("LoadedData", "phone_number: " + savedPhone);
        Log.d("LoadedData", "email: " + savedEmail);
    }

    public void onToggle(View v) {
        ToggleButton toggleB = (ToggleButton)v;

        if(toggleB.isChecked()) { 
            // View mode - disable editing
            contactNameLayout.setEnabled(false);
            phoneNumberLayout.setEnabled(false);
            emailLayout.setEnabled(false);
            saveButton.setEnabled(false);
            
            // Clear any existing errors in view mode
            contactNameLayout.setError(null);
            phoneNumberLayout.setError(null);
            emailLayout.setError(null);
            
            Toast.makeText(this, "Switched to View Mode", Toast.LENGTH_SHORT).show();
        } else {
            // Edit mode - enable editing
            contactNameLayout.setEnabled(true);
            phoneNumberLayout.setEnabled(true);
            emailLayout.setEnabled(true);
            saveButton.setEnabled(true);
            
            Toast.makeText(this, "Switched to Edit Mode", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveData(View v) {
        if (!validateAllFields()) {
            Toast.makeText(this, "Please correct the errors before saving", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("StoredData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String name = contactNameEditText.getText().toString().trim();
        String phone = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        editor.putString("contact_name", name);
        editor.putString("phone_number", phone);
        editor.putString("email", email);

        boolean success = editor.commit();

        if (success) {
            Toast.makeText(this, "Contact saved successfully!", Toast.LENGTH_SHORT).show();
            Log.d("SavedData", "contact_name: " + name);
            Log.d("SavedData", "phone_number: " + phone);
            Log.d("SavedData", "email: " + email);
        } else {
            Toast.makeText(this, "Failed to save contact. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}


