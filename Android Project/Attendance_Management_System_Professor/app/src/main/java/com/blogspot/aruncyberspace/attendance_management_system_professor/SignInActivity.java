package com.blogspot.aruncyberspace.attendance_management_system_professor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


public class SignInActivity extends ActionBarActivity {
    Button btnSignin;
    ProgressBar mProgressView;
    String TAG = "AMS";
    String userName, password;
    String userXML;
    boolean validated;
    EditText mPasswordView;
    AutoCompleteTextView mEmailView;
    String URL="http://dms.ngrok.io/AMSWebServices/AMSService/Authenticate/";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignin = (Button) findViewById(R.id.email_sign_in_button);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start the professor intent
                //  Intent intent = new Intent(getApplicationContext(),ProfessorMainActivity.class);
                // startActivity(intent);
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        });
    }

    public void onSignUpClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(intent);

    }
    public String getAuthenticatedUser(String userName, String password) {
        String result = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {

            //  String url = "http://192.168.0.6:8080/AMSWebServices/AMSService/Authenticate/"+userName+"/"+password;
            String url = URL + userName + "/" + password;
            Log.w(TAG, "url=" + url);
            HttpGet getRequest = new HttpGet(url);
            //Log.w("AMS-S",getRequest.toString());
            HttpResponse httpResponse = httpclient.execute(getRequest);

            HttpEntity entity = httpResponse.getEntity();
            Log.w("AMS-S", httpResponse.getStatusLine().toString());

            if (entity != null) {
                result = EntityUtils.toString(entity);
                userXML = result;
                Log.w("AMS-S", "Entity : " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }

        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if (validated) {
                String response = getAuthenticatedUser(userName, password);
                userXML = response;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            mProgressView.setVisibility(View.GONE);
            Serializer serializer = new Persister();
            try {
                user = new User();
                user = serializer.read(User.class, userXML);
                String name = user.getFirstName();

            } catch (Exception ex) {
                String exceptionstring = ex.toString();
                Log.i(TAG, "Async task error: " + exceptionstring);
                exceptionstring.toString();
            }

            if (user != null && user.getFirstName() != null) {

                Intent intent = new Intent(getApplicationContext(), ProfessorMainActivity.class);
                intent.putExtra("USER_XML", userXML);
                startActivity(intent);
            } else {

                // mErrorMessageView.setText("Invalid User Name or Password!");

                Context context = getApplicationContext();
                CharSequence text = "Invalid User Name or Password!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);

                toast.show();

            }
        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            mProgressView.setVisibility(View.VISIBLE);
            if (mEmailView.getText().length() != 0 && mEmailView.getText().toString() != "") {
                userName = mEmailView.getText().toString();
                if (mPasswordView.getText().length() != 0 && mPasswordView.getText().toString() != "") {

                    password = mPasswordView.getText().toString();
                    validated = true;
                } else {
                    //   mProgressView.setVisibility(View.GONE);
                    //  mErrorMessageView.setText("Please enter Password");
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_LONG);
                    toast.show();
                    validated = false;
                }
            }//If text control is empty

            else

            {
                // mErrorMessageView.setText("Please enter Mav Email ID");
                Toast toast = Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_LONG);
                toast.show();
                validated = false;
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
            mProgressView.animate();

        }
    }
}



