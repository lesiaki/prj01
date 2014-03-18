package com.example.vehicle;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseControl {

	// Table names for the database
	private static final String TABLE_USERS = "users";

	// Column names for the USERS table
	private static final String KEY_UID = "user_id";
	private static final String KEY_UUSER_NAME = "username";
	private static final String KEY_UPASSWORD = "password";

	// Database handles
	private Context context; // Application's handle
	private SQLiteDatabase database; // Database connection handle
	private DatabaseHelper dbHelper; // Database creation/upgrading helper

	/*------------------General Database Control Functions-------------------*/

	// Constructor
	public DatabaseControl(Context context) {
		// Initialize global variables
		this.context = context;
		database = null;
		dbHelper = null;
	}

	// Obtain database handle
	public DatabaseControl open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	// Close the database connection
	public void close() {
		dbHelper.close();
	}
}
