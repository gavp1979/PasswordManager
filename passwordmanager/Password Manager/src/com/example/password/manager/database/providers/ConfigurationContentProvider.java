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

public class ConfigurationContentProvider extends ContentProvider {

	private DBOpenHelper dbHelper;
	private static HashMap<String, String> CONFIGURATION_PROJECTION_MAP;
	private static final String TABLE_NAME = "configuration";
	private static final String AUTHORITY = "com.example.password.manager.database.providers.configurationcontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri _ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase());
	public static final Uri PASSWORD_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/password");

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int CONFIGURATION = 1;
	private static final int CONFIGURATION__ID = 2;
	private static final int CONFIGURATION_PASSWORD = 3;

	// Content values keys (using column names)
	public static final String _ID = "_id";
	public static final String PASSWORD = "password";

	public boolean onCreate() {
		dbHelper = new DBOpenHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case CONFIGURATION:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(CONFIGURATION_PROJECTION_MAP);
			break;
		case CONFIGURATION__ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("_id=" + url.getPathSegments().get(1));
			break;
		case CONFIGURATION_PASSWORD:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("password='" + url.getPathSegments().get(2) + "'");
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
		case CONFIGURATION:
			return "vnd.android.cursor.dir/vnd.com.example.password.manager.database.providers.configuration";
		case CONFIGURATION__ID:
			return "vnd.android.cursor.item/vnd.com.example.password.manager.database.providers.configuration";
		case CONFIGURATION_PASSWORD:
			return "vnd.android.cursor.item/vnd.com.example.password.manager.database.providers.configuration";

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
		if (URL_MATCHER.match(url) != CONFIGURATION) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("configuration", "configuration", values);
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
		case CONFIGURATION:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case CONFIGURATION__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.delete(TABLE_NAME,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case CONFIGURATION_PASSWORD:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"password="
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
		case CONFIGURATION:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case CONFIGURATION__ID:
			segment = url.getPathSegments().get(1);
			count = mDB.update(TABLE_NAME, values,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case CONFIGURATION_PASSWORD:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"password="
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), CONFIGURATION);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#",
				CONFIGURATION__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/password"
				+ "/*", CONFIGURATION_PASSWORD);

		CONFIGURATION_PROJECTION_MAP = new HashMap<String, String>();
		CONFIGURATION_PROJECTION_MAP.put(_ID, "_id");
		CONFIGURATION_PROJECTION_MAP.put(PASSWORD, "password");

	}
}
