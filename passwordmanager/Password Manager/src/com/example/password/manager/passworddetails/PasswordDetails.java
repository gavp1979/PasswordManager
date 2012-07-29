package com.example.password.manager.passworddetails;

import com.example.password.manager.R;
import com.example.password.manager.R.layout;
import com.example.password.manager.database.DBOpenHelper;
import com.example.password.manager.database.providers.PasswordsContentProvider;
import com.mentorbs.encryption.MBSEncryption;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;

public class PasswordDetails extends Activity 
{
	private EditText _edtName, _edtUserNanme, _edtPassword, _edtNotes;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_detail);
		
		_edtName = (EditText)findViewById(R.id.edtName);
		_edtUserNanme = (EditText)findViewById(R.id.edtUserName);
		_edtPassword = (EditText)findViewById(R.id.edtPassword);
		_edtNotes = (EditText)findViewById(R.id.edtNotes);
	}

	@Override
	protected void onPause()
	{
		ContentValues vals = new ContentValues();
		vals.put(PasswordsContentProvider.NAME, getNameEncrypted());
		vals.put(PasswordsContentProvider.USERNAME, getUserNameEncrypted());
		vals.put(PasswordsContentProvider.PASSWORD, getPasswordEncrypted());
		vals.put(PasswordsContentProvider.NOTES, getNotesEncrypted());
		
		getContentResolver().insert(PasswordsContentProvider.CONTENT_URI, vals);
		
		super.onPause();
	}

	private String getNotes()
	{
		return _edtNotes.getText().toString().trim();
	}

	private String getPassword()
	{
		return _edtPassword.getText().toString().trim();
	}

	private String getUserName()
	{
		return _edtUserNanme.getText().toString().trim();
	}

	private String getName()
	{
		return _edtName.getText().toString().trim();
	}
	
	private String getNotesEncrypted()
	{
		return MBSEncryption.EncryptStringToHexString(getNotes());
	}

	private String getPasswordEncrypted()
	{
		return MBSEncryption.EncryptStringToHexString(getPassword());
	}

	private String getUserNameEncrypted()
	{
		return MBSEncryption.EncryptStringToHexString(getUserName());
	}

	private String getNameEncrypted()
	{
		return MBSEncryption.EncryptStringToHexString(getName());
	}
}
