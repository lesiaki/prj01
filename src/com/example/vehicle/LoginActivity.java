package com.example.vehicle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	TextView registerScreen;

	EditText txtEmail;
	EditText txtPassoword;
	Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Register new account link
		registerScreen = (TextView) findViewById(R.id.link_to_register);
		registerScreen.setOnClickListener(this);

		// Button
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		// User inputs
		txtEmail = (EditText) findViewById(R.id.login_email);
		txtPassoword = (EditText) findViewById(R.id.login_password);
	}

	@Override
	public void onClick(View v) {
		if (v == registerScreen) {
			// Switching to Register screen
			Intent i = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(i);
		} else if (v == btnLogin) {
			// Grab user inputs
			String email = txtEmail.getText().toString();
			String password = txtPassoword.getText().toString();

			// Validate user inputs
			if (!isValidLoginValues(email, password))
				return;

			// Save user details
			try {
				DatabaseControl dbControl = new DatabaseControl(this);
				dbControl.open();

				if (dbControl != null) {
					if(dbControl.isValidEmailPassword(email, password) == true){
						// Valid user credentials, Go to Home Screen
						Intent i = new Intent(getApplicationContext(), Home.class);
						i.putExtra("user_email", email);
						finish();
						startActivity(i);
					}else{
						// Invalid user credentials
						txtPassoword.setText("");
						popMessage("Invalid Email or Password!");
					}
				}

				dbControl.close();
			} catch (SQLException e) {
				android.util.Log.e("Register", e.toString());
			} catch (Exception e) {
				android.util.Log.e("Register", e.toString());
			}
		}
	}

	private boolean isValidLoginValues(String email, String password) {
		// TODO Validate input values
		return true;
	}

	// displays the passed msg as a pop up message to the user
	public void popMessage(String msg) {
		Toast message = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		message.show();
	}
}
