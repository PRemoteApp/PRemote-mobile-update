package me.khukhuna.premote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RelativeLayout layout;

    private EditText email;
    private EditText password;

    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        layout = findViewById(R.id.login_layout);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(view -> {
            boolean error = false;

            if(isEmpty(email)){
                email.setError("E-mail can't be empty");
                error = true;
            }

            if(isEmpty(password)){
                password.setError("Password can't be empty");
                error = true;
            }

            if(error){
                return;
            }
            login(email.getText().toString(), password.getText().toString());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        if(task.getException() == null)return;
                        String error = task.getException().getMessage();
                        Snackbar.make(layout, error, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().equals("");
    }
}
