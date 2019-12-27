package me.khukhuna.premote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RelativeLayout layout;

    private EditText emailField;
    private EditText passwordField;
    private ProgressBar progressBar;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        layout = findViewById(R.id.login_layout);

        emailField = findViewById(R.id.login_email);
        passwordField = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress_bar);
        loginBtn = findViewById(R.id.login);

        loginBtn.setOnClickListener(view -> {
            boolean error = false;

            if (isEmpty(emailField)) {
                emailField.setError("E-mail can't be empty");
                error = true;
            }

            if (isEmpty(passwordField)) {
                passwordField.setError("Password can't be empty");
                error = true;
            }

            if (error) {
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setClickable(false);
            login(emailField.getText().toString(), passwordField.getText().toString());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        if (task.getException() == null || task.getException().getMessage() == null) {
                            return;
                        }
                        String error = task.getException().getMessage();
                        Snackbar.make(layout, error, Snackbar.LENGTH_SHORT).show();
                    }
                    loginBtn.setClickable(true);
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().equals("");
    }
}
