package com.example.vehicle;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseControl {

	// Table names for the database
	private static final String TABLE_USERS = "users";

	// Column names for the USERS table
	private static final String KEY_UID = "user_id";
	private static final String KEY_UUSER_NAME = "username";
	private static final String KEY_UEMAIL = "email";
	private static final String KEY_UPASSWORD = "password";

	// Database handles
	private Context context; // Application's handle
	private SQLiteDatabase database; // Database connection handle
	private DatabaseHelper dbHelper; // Database creation/upgrading helper
	private long temp_id; // Temporary storage of user id

	/*------------------General Database Control Functions-------------------*/

	/**
	 * Constructor
	 * 
	 * @param context
	 *            -> Application's context
	 */
	public DatabaseControl(Context context) {
		// Initialize global variables
		this.context = context;
		database = null;
		dbHelper = null;
	}

	/**
	 * Obtain database handle
	 * 
	 * @return this class's handle
	 * @throws SQLException
	 */
	public DatabaseControl open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	// Close the database connection
	public void close() {
		dbHelper.close();
	}

	/*-------------------------USERS table Functions-------------------------*/

	/**
	 * Select the user from the database with the given id
	 * 
	 * @param user_id
	 *            -> user's id
	 * @return found user
	 */
	public User getUserById(long user_id) {
		User user = null;

		try {
			String[] columns = new String[] { KEY_UID, KEY_UUSER_NAME,
					KEY_UEMAIL, KEY_UPASSWORD };

			Cursor cursor = database.query(true, TABLE_USERS, columns, KEY_UID
					+ "=" + user_id, null, null, null, null, null);

			if (cursor != null) {
				cursor.moveToFirst();
				String username = cursor.getString(cursor
						.getColumnIndex(KEY_UUSER_NAME));
				String email = cursor.getString(cursor
						.getColumnIndex(KEY_UEMAIL));
				String password = cursor.getString(cursor
						.getColumnIndex(KEY_UPASSWORD));

				user = new User(user_id, username, email, password);
			}
		} catch (SQLiteException e) {
			user = null;
		} catch (CursorIndexOutOfBoundsException e) {
			user = null;
		}
		return user;
	}

	/**
	 * Select the user from the database with the given username
	 * 
	 * @param username
	 *            -> user's name
	 * @return found user
	 */
	public User getUserByUsername(String username) {
		User user = null;

		try {
			String[] columns = new String[] { KEY_UID, KEY_UUSER_NAME,
					KEY_UEMAIL, KEY_UPASSWORD };

			// TODO Avoid sql-injection...use prepared statements
			Cursor cursor = database.query(true, TABLE_USERS, columns,
					KEY_UUSER_NAME + "='" + username + "'", null, null, null,
					null, null);

			if (cursor != null) {
				cursor.moveToFirst();
				long id = cursor.getLong(cursor.getColumnIndex(KEY_UID));
				String email = cursor.getString(cursor
						.getColumnIndex(KEY_UEMAIL));
				String password = cursor.getString(cursor
						.getColumnIndex(KEY_UPASSWORD));

				user = new User(id, username, email, password);
			}
		} catch (SQLiteException e) {
			user = null;
		} catch (CursorIndexOutOfBoundsException e) {
			user = null;
		}
		return user;
	}

	/**
	 * Select the user from the database with the given email
	 * 
	 * @param email
	 *            -> user's email
	 * @return found user
	 */
	public User getUserByEmail(String email) {
		User user = null;

		try {
			String[] columns = new String[] { KEY_UID, KEY_UUSER_NAME,
					KEY_UEMAIL, KEY_UPASSWORD };

			// TODO Avoid sql-injection...use prepared statements
			Cursor cursor = database.query(true, TABLE_USERS, columns,
					KEY_UEMAIL + "='" + email + "'", null, null, null, null,
					null);

			if (cursor != null) {
				cursor.moveToFirst();
				long id = cursor.getLong(cursor.getColumnIndex(KEY_UID));
				String username = cursor.getString(cursor
						.getColumnIndex(KEY_UUSER_NAME));
				String password = cursor.getString(cursor
						.getColumnIndex(KEY_UPASSWORD));

				user = new User(id, username, email, password);
			}
		} catch (SQLiteException e) {
			user = null;
		} catch (CursorIndexOutOfBoundsException e) {
			user = null;
		}
		return user;
	}

	/**
	 * Select all users from the database
	 * 
	 * @return list of all users in the database
	 */
	public ArrayList<User> getAllUsers() {
		ArrayList<User> userList = new ArrayList<User>();

		try {
			Cursor cursor;
			String[] columns = new String[] { KEY_UID, KEY_UUSER_NAME,
					KEY_UEMAIL, KEY_UPASSWORD };

			cursor = database.query(true, TABLE_USERS, columns, null, null,
					null, null, KEY_UID + " ASC", null);

			if (cursor != null) {
				long id;
				String username;
				String email;
				String password;

				// Appending all the users into the userList
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex(KEY_UID));
					username = cursor.getString(cursor
							.getColumnIndex(KEY_UUSER_NAME));
					email = cursor.getString(cursor.getColumnIndex(KEY_UEMAIL));
					password = cursor.getString(cursor
							.getColumnIndex(KEY_UPASSWORD));

					userList.add(new User(id, username, email, password));
				}
			}
		} catch (SQLiteException e) {
			userList = null;
		} catch (CursorIndexOutOfBoundsException e) {
			userList = null;
		}
		return userList;
	}

	/**
	 * Add a new user into the table
	 * 
	 * @param username
	 *            -> User's name
	 * @param email
	 *            -> User's email
	 * @param password
	 *            -> User's password
	 * @return user's id
	 */
	public long addUser(String username, String email, String password) {
		return addUser(username, email, password, false);
	}

	public long addUser(String username, String email, String password,
			boolean updateIfExisting) {
		ContentValues values = new ContentValues();

		values.put(KEY_UUSER_NAME, username);
		values.put(KEY_UEMAIL, email);
		values.put(KEY_UPASSWORD, password);

		if (isUserInUsersTable(email)) {
			// Update the password
			if (updateIfExisting) {
				database.update(TABLE_USERS, values, KEY_UID + "=" + temp_id,
						null);
			}
			return temp_id;
		} else {
			// Inserting a new user into the USERS table
			return database.insert(TABLE_USERS, null, values);
		}
	}

	/**
	 * Checks if a user with the given email exists in the USERS table
	 * 
	 * @param email
	 *            -> user's email
	 * @return true if the user found and false otherwise
	 */
	public boolean isUserInUsersTable(String email) {
		boolean status = false;
		try {
			Cursor cursor = database.query(true, TABLE_USERS,
					new String[] { KEY_UID }, KEY_UEMAIL + "='" + email + "'",
					null, null, null, null, null);

			if (cursor != null) {
				cursor.moveToFirst();
				temp_id = cursor.getInt(cursor.getColumnIndex(KEY_UID));
				status = true;
			}
		} catch (SQLiteException e) {
			status = false;
			temp_id = -1;
		} catch (CursorIndexOutOfBoundsException e) {
			status = false;
			temp_id = -1;
		}
		return status;
	}

	/**
	 * Checks if a user with the given email and password is valid
	 * 
	 * @param email
	 *            -> user's email
	 * @param password
	 *            -> sser's password
	 * @return true if the user found and false otherwise
	 */
	public boolean isValidEmailPassword(String email, String password) {
		boolean status = false;
		try {
			Cursor cursor = database.query(true, TABLE_USERS,
					new String[] { KEY_UID }, KEY_UEMAIL + "='" + email
							+ "' AND " + KEY_UPASSWORD + "='" + password + "'",
					null, null, null, null, null);

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				temp_id = cursor.getInt(cursor.getColumnIndex(KEY_UID));
				status = true;
			}
		} catch (SQLiteException e) {
			status = false;
			temp_id = -1;
		} catch (CursorIndexOutOfBoundsException e) {
			status = false;
			temp_id = -1;
		}
		return status;
	}
}
