package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG= "login";

    private CheckBox cbRememberMe;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        editTextLoginEmail=findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar= findViewById(R.id.progressBar_login_user);
        cbRememberMe = (CheckBox) findViewById(R.id.cbRememberMe);



        authProfile= FirebaseAuth.getInstance();

//        //Show Hide password using Eye Icon
//        ImageView imageViewShowHidePwd=findViewById(R.id.imageView_show_hide_pwd);
//        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
//        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
//                    // if password is visible then hide it
//                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    //Change Icon
//                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
//                } else {
//                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
//                }
//            }
//        });

        //Reset Password
        Button buttonForgetPwd= findViewById(R.id.button_forget_password);
        buttonForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(login.this, "You can reset your password now",Toast.LENGTH_LONG).show();
                startActivity( new Intent(login.this, ForgotPasswordActivity.class));

            }
        });


        // login user
        Button buttonLogin = findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();

                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(login.this, "Please enter your email",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(login.this, "Please re-enter your email",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Valid Email is required");
                    editTextLoginEmail.requestFocus();
                } else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(login.this, "Please enter your password",Toast.LENGTH_LONG).show();
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();
                } else{
                    progressBar.setVisibility(View.VISIBLE);








                    loginUser(textEmail, textPwd);
                }
            }
        });





        SessionManager sessionManager = new SessionManager(login.this, SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkRememberMe()){
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            editTextLoginEmail.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONEMAIL));
            editTextLoginPwd.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));

        }

    }

    private void loginUser(String email, String Pwd) {







        if(cbRememberMe.isChecked()){
            SessionManager sessionManager = new SessionManager(login.this, SessionManager.SESSION_REMEMBERME);
            sessionManager.createRememberMeSession(email, Pwd);
        }

        authProfile.signInWithEmailAndPassword(email, Pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Get instance of the user
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    // check email is verified before user can access their profile


                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(login.this, "You are logged in now", Toast.LENGTH_LONG).show();
                        // open user profile
                        // start the UserActivity
                        startActivity(new Intent(login.this, UserAcitivity.class));
                        finish();
                    } else  {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();

                    }


                } else{
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        editTextLoginEmail.setError("User is not exist or is no longer valid. Please register again");
                        editTextLoginEmail.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e){
                        editTextLoginEmail.setError("Invalid credentials. Kindly, check and re-renter.");
                        editTextLoginEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                       
                    Toast.makeText(login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        //Set up the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification");

        // open email app if user clicks/taps Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the alertDialog
        alertDialog.show();
    }

 //   @Override
//    protected void onStart(){
//        super.onStart();
//        if(authProfile.getCurrentUser()!=null){
//           Toast.makeText(login.this,"Already logged in",Toast.LENGTH_LONG).show();
//           // Start the userActivity
//            startActivity(new Intent(login.this,UserAcitivity.class));
//            finish();
//        }
//        else {
//            Toast.makeText(login.this,"You can log in now", Toast.LENGTH_LONG).show();
//        }
//    }

}