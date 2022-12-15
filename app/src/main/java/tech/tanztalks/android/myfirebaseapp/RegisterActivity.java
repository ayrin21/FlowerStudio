package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import org.intellij.lang.annotations.Pattern;

import java.util.Calendar;
import java.util.regex.Matcher;


public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFulName, editTextRegisterEmail, editTextRegisterDOB, editTextRegisterMobile, editTextRegisterPwd, editTextConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private static final String TAG="RegisterActivity";
    private DatePickerDialog picker;

    private String selectedDistrict, selectedState;                 //vars to hold the values of selected State and District
    private TextView tvStateSpinner, tvDistrictSpinner;             //declaring TextView to show the errors
    private Spinner stateSpinner, districtSpinner;                  //Spinners
    private ArrayAdapter<CharSequence> stateAdapter, districtAdapter;   //declare adapters for the spinners

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        Toast.makeText(RegisterActivity.this, "You can register now", Toast.LENGTH_LONG).show();
        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFulName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDOB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextConfirmPwd = findViewById(R.id.editText_confirm_password);

        //RadioButton for Gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck(); // for clearing all checked radiobutton when activity is started or resumed.
        Button buttonRegister = findViewById(R.id.button_register);
        editTextRegisterDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day= calendar.get(Calendar.DAY_OF_MONTH);
                int month= calendar.get(Calendar.MONTH);
                int year= calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker= new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     editTextRegisterDOB.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
// spinner
        stateSpinner = findViewById(R.id.spinner_bangladesh_division);    //Finds a view that was identified by the android:id attribute in xml

        //Populate ArrayAdapter using string array and a spinner layout that we will define
        stateAdapter = ArrayAdapter.createFromResource(  getApplicationContext(),
                R.array.array_bangladesh_division, R.layout.spinner);

        // Specify the layout to use when the list of choices appear
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(stateAdapter);
        //When any item of the stateSpinner is selected
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Define City Spinner but we will populate the options through the selected state
                districtSpinner = findViewById(R.id.spinner_bangladesh_districts);

                selectedState = stateSpinner.getSelectedItem().toString();      //Obtain the selected State

                int parentID = parent.getId();
                if (parentID == R.id.spinner_bangladesh_division){
                    switch (selectedState){
                        case "Select Your State": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_default_districts, R.layout.spinner);
                            break;
                        case "Dhaka": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_dhaka_districts, R.layout.spinner);
                            break;
                        case "Chattogram": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_chattogram_districts, R.layout.spinner);
                            break;
                        case "Barishal": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_barishal_districts, R.layout.spinner);
                            break;
                        case "Mymensingh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_mymensingh_districts, R.layout.spinner);
                            break;
                        case "Rajshahi": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_rajshahi_districts, R.layout.spinner);
                            break;
                        case "Rangpur": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_rangpur_districts, R.layout.spinner);
                            break;
                        case "Sylhet": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_sylhet_districts, R.layout.spinner);
                            break;

                        default:  break;
                    }
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    districtSpinner.setAdapter(districtAdapter);        //Populate the list of Districts in respect of the State selected

                    //To obtain the selected District from the spinner
                    districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedDistrict = districtSpinner.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvStateSpinner = findViewById(R.id.textView_bangladesh_division);
        tvDistrictSpinner = findViewById(R.id.textView_bangladesh_districts);

        buttonRegister.setOnClickListener(v -> {
            if (selectedState.equals("Select Your State")) {
                Toast.makeText(RegisterActivity.this, "Please select your state from the list", Toast.LENGTH_LONG).show();
                tvStateSpinner.setError("State is required!");      //To set error on TextView
                tvStateSpinner.requestFocus();
            } else if (selectedDistrict.equals("Select Your District")) {
                Toast.makeText(RegisterActivity.this, "Please select your district from the list", Toast.LENGTH_LONG).show();
                tvDistrictSpinner.setError("District is required!");
                tvDistrictSpinner.requestFocus();
                tvStateSpinner.setError(null);                      //To reove error from stateSpinner
            } else {
                tvStateSpinner.setError(null);
                tvDistrictSpinner.setError(null);
                Toast.makeText(RegisterActivity.this, "Selected State: "+selectedState+"\nSelected District: "+selectedDistrict, Toast.LENGTH_LONG).show();
            }
        });

        //spinner
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectGenderId = radioGroupRegisterGender.getCheckedRadioButtonId(); // this methos return the identifier of the selected radio button in this group. Upon empty selection, the returned value is -1
                radioButtonRegisterGenderSelected = findViewById(selectGenderId);

                // Obtain the entered data
                String textFullName = editTextRegisterFulName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDOB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextConfirmPwd.getText().toString();
                String textGender;

//                String mobileRegex= "/^(?:\\|88)?(01[3-9]\\d{11})$/";
//                Matcher mobileMatcher;
//                java.util.regex.Pattern mobilePattern = java.util.regex.Pattern.compile(mobileRegex);
//                mobileMatcher = mobilePattern.matcher(textMobile);
// // TextUtils will always return a boolean value. In code, the former simply calls the equivalent of the other, plus a null check. checks is string length is zero only. isEmpty() return true if, and only if length() is 0
                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    editTextRegisterFulName.setError("Full name is required");
                    editTextRegisterFulName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    editTextRegisterDOB.setError("Date of Birth is required");
                    editTextRegisterDOB.requestFocus();
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your mobile no", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile no is required");
                    editTextRegisterMobile.requestFocus();
                } else if (textMobile.length() != 11) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile no", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile no should be 11 digits.");
                    editTextRegisterMobile.requestFocus();}
//                 else if(!mobileMatcher.find()){
//                    Toast.makeText(RegisterActivity.this,"Please re-enter your mobile no.",Toast.LENGTH_LONG).show();
//                    editTextRegisterMobile.setError("Mobile no is not valid");
//                    editTextRegisterMobile.requestFocus();
//                }
                    //spinner code starts from here





                   //spinner code ends here
               else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is required");
                    editTextRegisterPwd.requestFocus();
                } else if (textPwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits.", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is too weak");
                    editTextRegisterPwd.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Confirm your password.", Toast.LENGTH_LONG).show();
                    editTextConfirmPwd.setError("Password confirmation is required");
                    editTextConfirmPwd.requestFocus();
                } else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please enter same password.", Toast.LENGTH_LONG).show();
                    editTextConfirmPwd.setError("Same password is required");
                    editTextConfirmPwd.requestFocus();

                    // clear the entered password
                    editTextRegisterPwd.clearComposingText();
                    editTextConfirmPwd.clearComposingText();
                } else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(view.VISIBLE);
                    registerUser(textFullName, textEmail, textDoB, textGender, textMobile, textPwd);
                }
            }

        });
    }

    // Register user using the credentials given
    private void registerUser(String textFullName, String textEmail, String textDoB, String textGender, String textMobile, String textPwd) {
        //FirenaseAuth()- the entry point the Firebase authentication SDK. First, obtain an instance of this class by calling getInstance(). Then,sign up or sign in or register a user with one of the methods.
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User registered Successfully", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    //Send verification email
                    firebaseUser.sendEmailVerification();

               /*  // open user profile after successful registration
                  Intent intent= new Intent(RegisterActivity.this, UserProfileActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); // for the user not to return back the register activity after successful registration
                  startActivity(intent);
                  finish();*/


                } else{
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthUserCollisionException e){
                       editTextRegisterFulName.setError("User is already registered with this name");
                       editTextRegisterFulName.requestFocus();
                    } catch(Exception e){

                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
    //

}