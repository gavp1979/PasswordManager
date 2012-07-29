package com.example.password.manager;

import com.example.password.manager.passworddetails.PasswordDetails;
import com.example.password.manager.unlock.UnlockDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity
{

	private static final String	MASTER_PASSWORD	= "StupidFuckingPasswords";

	private boolean				_bUnlocked		= false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//TODO Create a cursor loader to populate the list.
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
				Intent intDetails = new Intent(this, PasswordDetails.class);
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

}
