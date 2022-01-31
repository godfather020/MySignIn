package com.example.mysignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;

    GoogleSignInClient mGoogleSignInClient;
    public static int RC_SIGN_IN = 100;
    public static String user1,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        TextView user = (TextView) findViewById(R.id.user);
        TextView email = (TextView) findViewById(R.id.email);
        EditText pass = (EditText) findViewById(R.id.pass);
        EditText compass = (EditText) findViewById(R.id.compass);
        TextView btn = findViewById(R.id.login1);
        TextView cc = (TextView) findViewById(R.id.CC);
        TextView phonenum = (TextView) findViewById(R.id.phone);


        MaterialButton signup = (MaterialButton) findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.getText().toString().contains("*") || user.getText().toString().contains("%") || user.getText().toString().contains("$")
                        || user.getText().toString().contains("#") || user.getText().toString().contains("!") || user.getText().toString().contains("^") || user.getText().toString().contains("&")
                        || user.getText().toString().contains("(") || user.getText().toString().contains(")") || user.getText().toString().contains("-")
                        || user.getText().toString().contains("+") || user.getText().toString().contains("=")){
                        Toast.makeText(Register.this, "Only @ Special Character is allowed", Toast.LENGTH_SHORT).show();
                }

                else {
                    if (!email.getText().toString().contains("@")){
                        Toast.makeText(Register.this, "Please Enter a Valid Email", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (pass.getText().toString().equals(compass.getText().toString())){
                            if (pass.getText().toString().length() < 8){
                                Toast.makeText(Register.this, "At Least 8 Characters Should be in Password", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Register.this, "SignUp SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                user1 = user.getText().toString();
                                password = compass.getText().toString();
                                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), compass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Register.this, "User Account is Created", Toast.LENGTH_SHORT).show();
                                        //send User to verify Phone number
                                        Intent phone = new Intent(Register.this, VerifyOTP.class);
                                        phone.putExtra("phone", "+"+cc.getText().toString()+phonenum.getText().toString());
                                        startActivity(phone);
                                        Log.d(TAG,"onSuccess"+cc.getText().toString()+phonenum.getText().toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Register.this, "Error!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        else {
                            Toast.makeText(Register.this, "Check Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Toast.makeText(this , "User Email" + personEmail, Toast.LENGTH_SHORT).show();
            }

            // Signed in successfully, show authenticated UI.

            startActivity(new Intent(Register.this,AfterLogin.class));

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Message" , e.toString());

        }
    }
}