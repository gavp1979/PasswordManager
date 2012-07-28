package com.example.password.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	private static final String MASTER_PASSWORD	= "StupidFuckingPasswords";
	
	private boolean _bUnlocked	= false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
			final EditText edtPassword = new EditText(this);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Unlock");
			builder.setMessage("To unlock the application please enter the master password below");
			builder.setView(edtPassword);
			builder.setPositiveButton("Unlock", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					final String strPassword = edtPassword.getText().toString().trim();
					
					if (strPassword.equalsIgnoreCase(MASTER_PASSWORD))
						_bUnlocked = true;
				}
			});
			builder.show();
		}
	}
    
    
}
