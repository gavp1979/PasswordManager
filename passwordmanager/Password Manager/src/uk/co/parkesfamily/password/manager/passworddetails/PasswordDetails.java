package uk.co.parkesfamily.password.manager.passworddetails;

import uk.co.parkesfamily.password.manager.database.providers.PasswordsContentProvider;
import uk.co.parkesfamily.password.manager.helperclasses.GPContentValues;
import uk.co.parkesfamily.password.manager.helperclasses.GPCursorHelper;
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

import uk.co.parkesfamily.password.manager.R;

public class PasswordDetails extends FragmentActivity implements LoaderCallbacks<Cursor>
{
	/**
	 * Collection of keys to use when populating or retrieving values from the
	 * launching {@link Intent}.
	 * 
	 * @author Administrator
	 */
	public static class ExtraFields
	{
		/**
		 * Key for _id value of record to display.
		 */
		public static final String	RECNO		= "RecordNumber";
		/**
		 * <code>true</code> if program is unlocked, <code>false</code> if
		 * program is locked.
		 */
		public static final String	Unlocked	= "Unlocked";
	}

	/**
	 * Static class to create an {@link Intent} capable of starting this
	 * activity.
	 * 
	 * @param context
	 *            The launching {@link Intent#}
	 * @param lRecNo
	 *            The _id value of the record to launch.
	 * @param bUnlocked
	 *            <code>true</code> if program is unlocked, <code>false</code>
	 *            if program is locked.
	 * @return The populated intent.
	 */
	public static Intent createIntent(final Context context, final long lRecNo,
			final boolean bUnlocked)
	{
		Intent intResult = new Intent(context, PasswordDetails.class);
		intResult.putExtra(ExtraFields.RECNO, lRecNo);
		intResult.putExtra(ExtraFields.Unlocked, bUnlocked);

		return intResult;
	}

	public static long		NEW_RECORD		= -100;
	public static int		LOADER_DETAILS	= 0;

	private EditText		_edtName, _edtUserNanme, _edtPassword, _edtNotes;
	private long			_lRowID			= NEW_RECORD;
	private boolean			_bUnlocked;

	private final String[]	_lstCols;
	private DataSnapshot	_initialVals;

	public PasswordDetails()
	{
		super();

		_lstCols = new String[] { PasswordsContentProvider._ID,
				PasswordsContentProvider.NAME, PasswordsContentProvider.USERNAME,
				PasswordsContentProvider.PASSWORD, PasswordsContentProvider.NOTES };
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
			_bUnlocked = extras.getBoolean(ExtraFields.Unlocked);
		}

		_edtName = (EditText) findViewById(R.id.edtName);
		_edtUserNanme = (EditText) findViewById(R.id.edtUserName);
		_edtPassword = (EditText) findViewById(R.id.edtPassword);
		_edtNotes = (EditText) findViewById(R.id.edtNotes);

		if (_lRowID != NEW_RECORD)
		{
			getSupportLoaderManager().initLoader(LOADER_DETAILS, null, this);
		}
		else
			_initialVals = new DataSnapshot();
	}

	@Override
	protected void onPause()
	{
		// if (!_initialVals.equals(new DataSnapshot()))
		if (_initialVals.hasChanged())
		{
			GPContentValues vals = new GPContentValues();
			vals.put(PasswordsContentProvider.NAME, getName());
			vals.put(PasswordsContentProvider.USERNAME, getUserName());
			vals.put(PasswordsContentProvider.PASSWORD, getPassword());
			vals.put(PasswordsContentProvider.NOTES, getNotes());

			getContentResolver().insert(PasswordsContentProvider.CONTENT_URI,
					vals.getContentValues());
		}

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

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
	{
		Uri.Builder builder = PasswordsContentProvider._ID_FIELD_CONTENT_URI.buildUpon();
		builder.appendPath(Long.toString(_lRowID));
		Uri uri = builder.build();

		CursorLoader cursorLoader = new CursorLoader(this, uri, _lstCols, null, null,
				null);
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

			strName = GPCursorHelper.getStringDecrypted(cursor,
					PasswordsContentProvider.NAME, _bUnlocked);
			strUserName = GPCursorHelper.getStringDecrypted(cursor,
					PasswordsContentProvider.USERNAME, _bUnlocked);
			strPassword = GPCursorHelper.getStringDecrypted(cursor,
					PasswordsContentProvider.PASSWORD, _bUnlocked);
			strNotes = GPCursorHelper.getStringDecrypted(cursor,
					PasswordsContentProvider.NOTES, _bUnlocked);
			;
		}
		else
		{
			strName = strUserName = strPassword = strNotes = "";
		}

		_edtName.setText(strName);
		_edtUserNanme.setText(strUserName);
		_edtPassword.setText(strPassword);
		_edtNotes.setText(strNotes);

		_initialVals = new DataSnapshot();
	}

	/**
	 * Takes a snapshot of the data at the current time.
	 * 
	 * @author Administrator
	 */
	private class DataSnapshot
	{
		private final String	strName, strUserName, strPassword, strNotes;

		/**
		 * Constructor, creates a new {@link DataSnapshot} object.
		 */
		public DataSnapshot()
		{
			super();

			strName = getName();
			strUserName = getUserName();
			strPassword = getPassword();
			strNotes = getNotes();
		}

		/**
		 * Checks if the data in this {@link DataSnapshot} is equal to the data
		 * in the passed {@link DataSnapshot}.
		 */
		@Override
		public boolean equals(Object o)
		{
			boolean bEquals = false;

			try
			{
				DataSnapshot snapShot = (DataSnapshot) o;

				bEquals = (this.strName.equals(snapShot.strName))
						&& (this.strUserName.equals(snapShot.strUserName))
						&& (this.strPassword.equals(snapShot.strPassword))
						&& (this.strNotes.equals(snapShot.strNotes));
			}
			catch (ClassCastException e)
			{
				throw new ClassCastException(o.toString() + " must be an instance of "
						+ DataSnapshot.class.toString());
			}

			return bEquals;
		}

		@Override
		public int hashCode()
		{
			throw new UnsupportedOperationException(
					"This method has not been implemented");
		}

		public boolean hasChanged()
		{
			return !equals(new DataSnapshot());
		}
	}
}
