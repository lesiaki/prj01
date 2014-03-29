package com.example.vehicle;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {

	TextView loginScreen;
	Button btnRegister;
	EditText txtFullName;
	EditText txtEmail;
	EditText txtPassoword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set View to register.xml
		setContentView(R.layout.register);

		// Link to Login
		loginScreen = (TextView) findViewById(R.id.link_to_login);
		loginScreen.setOnClickListener(this);

		// Button
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);

		// User inputs
		txtFullName = (EditText) findViewById(R.id.reg_fullname);
		txtEmail = (EditText) findViewById(R.id.reg_email);
		txtPassoword = (EditText) findViewById(R.id.reg_password);
	}

	@Override
	public void onClick(View v) {
		if (v == loginScreen) {
			finish();
		} else if (v == btnRegister) {
			// Grab user inputs
			String fullName = txtFullName.getText().toString();
			String email = txtEmail.getText().toString();
			String password = txtPassoword.getText().toString();

			// Validate user inputs
			if (!isValidInputValues(fullName, email, password))
				return;

			// Save user details
			try {
				DatabaseControl dbControl = new DatabaseControl(this);
				dbControl.open();

				long userId = -1;

				if (dbControl != null) {
					userId = dbControl.addUser(fullName, email, password);
				}
				android.util.Log
						.i("Register", "Added user with id = " + userId);

				// Go to Home screen
				if (userId > 0) {
					Intent i = new Intent(getApplicationContext(),
							Home.class);
					i.putExtra("user_id", userId);
					finish();
					startActivity(i);
				}

				dbControl.close();
			} catch (SQLException e) {
				android.util.Log.e("Register", e.toString());
			} catch (Exception e) {
				android.util.Log.e("Register", e.toString());
			}
		}

	}

	private boolean isValidInputValues(String fullName, String email,
			String password) {
		// TODO Finish registration values validation
		// Validate username
		if (fullName != null && fullName.length() < 3) {
			popMessage("Username too short!");
			return false;
		}

		// Validate email
		if (email != null && email.length() < 3) {
			popMessage("Username too short!");
			return false;
		}

		// Validate password
		if (password != null && password.length() < 3) {
			popMessage("Username too short!");
			return false;
		}

		return true;
	}

	// displays the passed msg as a pop up message to the user
	public void popMessage(String msg) {
		Toast message = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		message.show();
	}
}
