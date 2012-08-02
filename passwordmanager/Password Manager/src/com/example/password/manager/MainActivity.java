package com.example.password.manager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.password.manager.database.providers.PasswordsContentProvider;
import com.example.password.manager.passworddetails.PasswordDetails;
import com.example.password.manager.unlock.UnlockDialog;
import com.example.password.manager.unlock.UnlockDialog.IUnlockDialog;

public class MainActivity extends FragmentActivity 
	implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener, IUnlockDialog
{
	private static int 			LOADER_PASSWORDS	= 0;
	private boolean				_bUnlocked			= false;
	
	private ListView			_lstPasswords;
	private PasswordRowAdapter	_adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_adapter = new PasswordRowAdapter(this, null, false);
		
		_lstPasswords = (ListView) findViewById(R.id.lstPasswords);
		_lstPasswords.setAdapter(_adapter);
		_lstPasswords.setOnItemClickListener(this);
		
		// Create a cursor loader to populate the list.
		getSupportLoaderManager().initLoader(LOADER_PASSWORDS, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean bHandled = true;

		switch (item.getItemId())
		{
			case R.id.mniAdd:
				Intent intDetails = PasswordDetails.createIntent(this, PasswordDetails.NEW_RECORD);
				startActivity(intDetails);
				break;

			default:
				bHandled = super.onOptionsItemSelected(item);
				break;
		}

		return bHandled;
	}

	public void btnClick(View view)
	{
		if (view.getId() == R.id.btnUnlock)
		{
			new UnlockDialog(this).show();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle)
	{
		String[] projection = { PasswordsContentProvider._ID, PasswordsContentProvider.NAME };
		 
	    CursorLoader cursorLoader = new CursorLoader(this,
	    		PasswordsContentProvider.CONTENT_URI, projection, null, null, PasswordsContentProvider.NAME);
	    return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		_adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> l)
	{
		_adapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intDetails = PasswordDetails.createIntent(this, id);
		startActivity(intDetails);
	}

	@Override
	public void unlock(boolean bUnlocked)
	{
		_bUnlocked = bUnlocked;
		_adapter._bUnlocked = this._bUnlocked;
		getSupportLoaderManager().restartLoader(LOADER_PASSWORDS, null, this);
	}

}
