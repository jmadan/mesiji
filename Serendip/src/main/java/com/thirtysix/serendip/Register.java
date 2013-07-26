package com.thirtysix.serendip;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.register, menu);
//		return true;
//	}
	
	public void signUp(View view){
		Context context = getApplicationContext();
		EditText userHandle = (EditText)findViewById(R.id.handle);
		EditText userEmail = (EditText)findViewById(R.id.email);
		EditText userName = (EditText)findViewById(R.id.name);
		RequestParams params = new RequestParams();
		params.put("user_name", userName.getText().toString());
		params.put("user_email", userEmail.getText().toString());
		params.put("user_handle", userHandle.getText().toString());
		
//		AsyncHttpClient client = new AsyncHttpClient();
		MesijiClient.post("/auth/register/", params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String response){
				System.out.println(response);
			}
		});
	}

}
