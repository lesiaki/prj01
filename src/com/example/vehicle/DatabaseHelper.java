package com.example.vehicle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	// Constants for the database details
	private static final int 	DB_VERSION = 2;
	private static final String DB_NAME = "app.db";
	private static final String TABLE_USERS = "users";

	// Queries for creating tables
	// USERS Table
	private static final String SQL_USERS_CREATE = "CREATE TABLE "
			+ TABLE_USERS 
			+ "(" 
			+ "user_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ "email	VARCHAR(45) NOT NULL ," 
			+ "username VARCHAR(45) NOT NULL ," 
			+ "password VARCHAR(45) NOT NULL"
			+ ")";

	// A general SQL command for deleting a table
	private static final String SQL_DROP = "DROP TABLE IF EXISTS ";

	// Constructor
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create the tables if they do not exist
		db.execSQL(SQL_USERS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Simply replaces the old version of the database tables with the new one
		db.execSQL(SQL_DROP + TABLE_USERS);
		onCreate(db);
	}
}
