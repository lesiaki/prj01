package com.example.vehicle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Home extends Activity {
	User user;
	
	TextView txtHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// Create user from the passed user-id/email
		long id = getIntent().getExtras().getLong("user_id", -1);
		
		DatabaseControl dbControl = new DatabaseControl(this);
		dbControl.open();
		if (id >= 0) {	// user-id was passed
			if (dbControl != null) {
				user = dbControl.getUserById(id);
			}
		} else {	// email was passed
			String email = getIntent().getStringExtra("user_email");

			if (dbControl != null) {
				user = dbControl.getUserByEmail(email);
			}
		}
		dbControl.close();
		
		// Display user details
		txtHeader = (TextView) findViewById(R.id.home_header);
		txtHeader.setText(user.toString());
	}
}
