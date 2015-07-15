package com.myschool.tp4.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.myschool.tp4.R;
import com.myschool.tp4.VolleyApp;
import com.myschool.tp4.volley.CustomJsonObjectRequest;

public class MainActivity extends ActionBarActivity {

	TextView mWelcomeTextView = null;
	ProgressBar mProgressBar = null;

	String mUserName = null;
	String mUserEmail = null;
	String mUserToken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWelcomeTextView = (TextView) findViewById(R.id.welcome);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	protected void onStart() {
		startGetUserInfos();
		super.onStart();
	}

	@Override
	protected void onResume() {
		// updateDisplay();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_logout) {
			AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
			alertDialog.setTitle(getResources().getString(R.string.confirmation));
			alertDialog.setMessage(getResources().getString(R.string.confirmation_msg));
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							leaveTheApp();
						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog.show();

		}
		return super.onOptionsItemSelected(item);
	}

	private void leaveTheApp() {
		getSharedPreferences(VolleyApp.PREF_ACTIVE_USER, 0).edit().clear().commit();
		finish();
	}

	private void startGetUserInfos() {

		CustomJsonObjectRequest req = new CustomJsonObjectRequest(this, Method.GET, VolleyApp.URL_ME, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.i("VOLLEY", "Receive response " + response.toString());
						try {

							mUserName = response.getString("name");
							mUserEmail = response.getString("email");

							updateDisplay();
						} catch (JSONException e) {

						}

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("VOLLEY", "Error getting user infos");
					}

				});

		VolleyApp.getInstance().addToRequestQueue(req);

	}

	private void updateDisplay() {
		String welcomeMsg = getResources().getString(R.string.welcome).replace("%s", mUserName);
		mWelcomeTextView.setText(welcomeMsg);
		mProgressBar.setVisibility(View.GONE);
		mWelcomeTextView.setVisibility(View.VISIBLE);
	}

}
