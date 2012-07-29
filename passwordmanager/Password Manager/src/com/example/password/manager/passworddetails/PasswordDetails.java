package com.example.password.manager.passworddetails;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.EditText;

import com.example.password.manager.R;
import com.example.password.manager.database.providers.PasswordsContentProvider;
import com.example.password.manager.helperclasses.GPCursorHelper;
import com.mentorbs.encryption.MBSEncryption;

public class PasswordDetails extends FragmentActivity 
	implements LoaderCallbacks<Cursor>
{
	public static class ExtraFields
	{
		public static final String RECNO	= "RecordNumber";
	}
	
	public static Intent createIntent(final Context context, final long lRecNo)
	{
		Intent intResult = new Intent(context, PasswordDetails.class);
		intResult.putExtra(ExtraFields.RECNO, lRecNo);
		
		return intResult;
	}
	
	public static long	NEW_RECORD	= -100;
	public static int	LOADER_DETAILS	= 0;
	
	private EditText _edtName, _edtUserNanme, _edtPassword, _edtNotes;
	private long _lRowID = NEW_RECORD;
	
	private final String[] _lstCols;

	
	
	public PasswordDetails()
	{
		super();

		_lstCols = new String[] {
				PasswordsContentProvider._ID,
				PasswordsContentProvider.NAME,
				PasswordsContentProvider.USERNAME,
				PasswordsContentProvider.PASSWORD,
				PasswordsContentProvider.NOTES
				};
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_detail);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			_lRowID = extras.getLong(ExtraFields.RECNO);
		}
		
		_edtName = (EditText)findViewById(R.id.edtName);
		_edtUserNanme = (EditText)findViewById(R.id.edtUserName);
		_edtPassword = (EditText)findViewById(R.id.edtPassword);
		_edtNotes = (EditText)findViewById(R.id.edtNotes);
		
		if (_lRowID != NEW_RECORD)
		{
			getSupportLoaderManager().initLoader(LOADER_DETAILS, null, this);
		}
	}

	@Override
	protected void onPause()
	{
		// TODO Create a GPContentValues which will auto encrypt values.
		// TODO Check if any values have changed before saving.
		ContentValues vals = new ContentValues();
		vals.put(PasswordsContentProvider.NAME, getNameEncrypted());
		vals.put(PasswordsContentProvider.USERNAME, getUserNameEncrypted());
		vals.put(PasswordsContentProvider.PASSWORD, getPasswordEncrypted());
		vals.put(PasswordsContentProvider.NOTES, getNotesEncrypted());
		
		//getContentResolver().insert(PasswordsContentProvider.CONTENT_URI, vals);
		
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

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
	{
		Uri.Builder builder = PasswordsContentProvider._ID_FIELD_CONTENT_URI.buildUpon();
		builder.appendPath(Long.toString(_lRowID));
		Uri uri = builder.build();
		
		
		CursorLoader cursorLoader = new CursorLoader(this, uri, _lstCols, null, null, null);
	    return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		populateData(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		populateData(null);
	}
	
	public void populateData(Cursor cursor)
	{
		final String strName, strUserName, strPassword, strNotes;
		
		if (cursor != null)
		{
			cursor.moveToFirst();
			
			strName = GPCursorHelper.getStringDecrypted(cursor, PasswordsContentProvider.NAME);
			strUserName = GPCursorHelper.getStringDecrypted(cursor, PasswordsContentProvider.USERNAME);
			strPassword = GPCursorHelper.getStringDecrypted(cursor, PasswordsContentProvider.PASSWORD);
			strNotes = GPCursorHelper.getStringDecrypted(cursor, PasswordsContentProvider.NOTES);;
		}
		else
		{
			strName = strUserName = strPassword = strNotes = "";
		}
		
		_edtName.setText(strName);
		_edtUserNanme.setText(strUserName);
		_edtPassword.setText(strPassword);
		_edtNotes.setText(strNotes);
	}
}
