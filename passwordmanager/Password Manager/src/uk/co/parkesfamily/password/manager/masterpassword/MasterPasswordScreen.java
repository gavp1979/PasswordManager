package uk.co.parkesfamily.password.manager.masterpassword;
import uk.co.parkesfamily.password.manager.R;
import uk.co.parkesfamily.password.manager.helperclasses.DBHelper;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class MasterPasswordScreen extends FragmentActivity
{
	EditText _edtOldPassword, _edtNewPassword, _edtConfirmPassword;
	
	public static Intent createIntent(Context context)
	{
		Intent intResult = new Intent(context, MasterPasswordScreen.class);
		
		return intResult;
	}

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.master_password_screen);
		
		_edtNewPassword = (EditText) findViewById(R.id.edtExistingPassword);
		_edtOldPassword = (EditText) findViewById(R.id.edtNewPassword);
		_edtConfirmPassword = (EditText) findViewById(R.id.edtRetypePassword);
	}

	public void btnOk_Click(View view)
	{
		Toast.makeText(this, "OK button clicked", Toast.LENGTH_LONG).show();
		
		final String strOldPassword = DBHelper.getMasterPassword(getContentResolver());
		final String strNewPassword = 
				
		if 
	}
	
	public void btnCancel_Click(View view)
	{
		//Toast.makeText(this, "Cancel button clicked", Toast.LENGTH_LONG).show();
		AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setTitle("Confirmation");
		dlg.setMessage("Are you sure you want to leave without saving?");
		dlg.setPositiveButton("Yes", new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		});
		dlg.setNegativeButton("No", null);
		dlg.show();
	}
}
