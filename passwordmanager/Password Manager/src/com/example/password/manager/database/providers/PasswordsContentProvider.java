/*
 ******************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0   *
 * Copyright (c) 2009, Android Open Handset Alliance. All rights reserved.    *
 *																			  *																			*
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2010, Motorola Mobility, Inc. All rights reserved.           *
 * 																			  *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf        * 
 * in your installation folder.                                               *
 ******************************************************************************
 */
package com.example.password.manager.database.providers;

import java.util.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;

import com.example.password.manager.database.*;

public class PasswordsContentProvider extends ContentProvider {

	private DBOpenHelper dbHelper;
	private static HashMap<String, String> PASSWORDS_PROJECTION_MAP;
	private static final String TABLE_NAME = "passwords";
	private static final String AUTHORITY = "com.example.password.manager.database.providers.passwordscontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri NAME_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/name");
	public static final Uri USERNAME_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/username");
	public static final Uri PASSWORD_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/password");
	public static final Uri NOTES_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/notes");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int PASSWORDS = 1;
	private static final int PASSWORDS__ID = 2;
	private static final int PASSWORDS_NAME = 3;
	private static final int PASSWORDS_USERNAME = 4;
	private static final int PASSWORDS_PASSWORD = 5;
	private static final int PASSWORDS_NOTES = 6;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String NAME = "Name";
	public static final String USERNAME = "UserName";
	public static final String PASSWORD = "Password";
	public static final String NOTES = "Notes";

	public boolean onCreate() {
		dbHelper = new DBOpenHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case PASSWORDS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(PASSWORDS_PROJECTION_MAP);
			break;
		case PASSWORDS__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case PASSWORDS_NAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("name='" + url.getPathSegments().get(2) + "'");
			break;
		case PASSWORDS_USERNAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("username='" + url.getPathSegments().get(2) + "'");
			break;
		case PASSWORDS_PASSWORD:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("password='" + url.getPathSegments().get(2) + "'");
			break;
		case PASSWORDS_NOTES:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("notes='" + url.getPathSegments().get(2) + "'");
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		String orderBy = "";
		if (TextUtils.isEmpty(sort)) {
			orderBy = DEFAULT_SORT_ORDER;
		} else {
			orderBy = sort;
		}
		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	public String getType(Uri url) {
		switch (URL_MATCHER.match(url)) {
		case PASSWORDS:
			return "vnd.android.cursor.dir/vnd.com.example.password.manager.database.providers.passwords";
		case PASSWORDS__ID:
			return "vnd.android.cursor.item/vnd.com.example.password.manager.database.providers.passwords";
		case PASSWORDS_NAME:
			return "vnd.android.cursor.item/vnd.com.example.password.manager.database.providers.passwords";
		case PASSWORDS_USERNAME:
			return "vnd.android.cursor.item/vnd.com.example.password.manager.database.providers.passwords";
		case PASSWORDS_PASSWORD:
			return "vnd.android.cursor.item/vnd.com.example.password.manager.database.providers.passwords";
		case PASSWORDS_NOTES:
			return "vnd.android.cursor.item/vnd.com.example.password.manager.database.providers.passwords";

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
	}

	public Uri insert(Uri url, ContentValues initialValues) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		if (URL_MATCHER.match(url) != PASSWORDS) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("passwords", "passwords", values);
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + url);
	}

	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case PASSWORDS:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case PASSWORDS__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PASSWORDS_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PASSWORDS_USERNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"username="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PASSWORDS_PASSWORD:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"password="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PASSWORDS_NOTES:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"notes="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case PASSWORDS:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case PASSWORDS__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PASSWORDS_NAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"name="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PASSWORDS_USERNAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"username="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PASSWORDS_PASSWORD:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"password="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PASSWORDS_NOTES:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"notes="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	static {
		URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), PASSWORDS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#",
				PASSWORDS__ID);
		URL_MATCHER.addURI(AUTHORITY,
				TABLE_NAME.toLowerCase() + "/name" + "/*", PASSWORDS_NAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/username"
				+ "/*", PASSWORDS_USERNAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/password"
				+ "/*", PASSWORDS_PASSWORD);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/notes"
				+ "/*", PASSWORDS_NOTES);

		PASSWORDS_PROJECTION_MAP = new HashMap<String, String>();
		PASSWORDS_PROJECTION_MAP.put(_ID, "_id");
		PASSWORDS_PROJECTION_MAP.put(NAME, "name");
		PASSWORDS_PROJECTION_MAP.put(USERNAME, "username");
		PASSWORDS_PROJECTION_MAP.put(PASSWORD, "password");
		PASSWORDS_PROJECTION_MAP.put(NOTES, "notes");

	}
}
