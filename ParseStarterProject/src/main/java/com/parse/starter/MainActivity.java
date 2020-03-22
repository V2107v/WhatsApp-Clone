/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

  boolean loginActive = false;

  public void redirect() {
    if(ParseUser.getCurrentUser() != null) {
      Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
      startActivity(intent);
    }
  }

  public void loginMode (View view) {

    Button loginSignupButton = findViewById(R.id.signUpButton);
    TextView loginText = findViewById(R.id.loginTextView);

    if(loginActive) {
      loginActive = false;
      loginSignupButton.setText("Sign up");
      loginText.setText("or, LogIn");

    } else {
      loginActive = true;
      loginSignupButton.setText("Log In");
      loginText.setText("or, SignUp");
    }

  }

  public void signUpLogin (View view) {

    EditText usernameText = findViewById(R.id.usernameEditText);
    EditText passwordText = findViewById(R.id.passwordEditText);

    if(loginActive) {

      ParseUser.logInInBackground(usernameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if (e == null) {
            redirect();
            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
          } else {
            String message = e.getMessage();
            if(message.toLowerCase().contains("java")) {
              Toast.makeText(MainActivity.this, message.substring(message.indexOf(" ")), Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

          }
        }
      });

    } else {

      ParseUser user = new ParseUser();
      user.setUsername(usernameText.getText().toString());
      user.setPassword(passwordText.getText().toString());

      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if(e == null) {
            redirect();
            Toast.makeText(MainActivity.this, "SignUp Successful!", Toast.LENGTH_SHORT).show();
          } else {
            String message = e.getMessage();
            if(message.toLowerCase().contains("java")) {
              Toast.makeText(MainActivity.this, message.substring(message.indexOf(" ")), Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
          }
        }
      });
    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("WhatsApp LogIn/SignUp");
    redirect();

    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}