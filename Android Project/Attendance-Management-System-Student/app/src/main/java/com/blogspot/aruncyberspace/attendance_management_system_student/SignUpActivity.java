package com.blogspot.aruncyberspace.attendance_management_system_student;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.io.Writer;


public class SignUpActivity extends ActionBarActivity {

    User user = new User();
    ;
    boolean validated = false;
    ProgressBar mProgressView;
    TextView mFirstNameView, mLastNameView, mEmailView, mPasswordView, mConfirmPasswordView,mAndroidDeviceView;
    Button btnSignUp;
    String TAG="AMS";
    String response;
    String address="0";
    //  private String URLFORMAT = "http://%s/DistributedMeetingSchedulerWebService/DMSWebService/Register/";
    private String URL = "http://dms.ngrok.io/AMSWebServices/AMSService/Register/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        try {
            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            address = info.getMacAddress();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();

        }

        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mFirstNameView = (TextView) findViewById(R.id.txtUserName);
        mLastNameView = (TextView) findViewById(R.id.txtLastName);
        mEmailView = (TextView) findViewById(R.id.txtMavEmail);
        mPasswordView = (TextView) findViewById(R.id.txtPassword);
        mConfirmPasswordView = (TextView) findViewById(R.id.txtConfirmPassword);
        mAndroidDeviceView = (TextView)findViewById(R.id.txtAndroidDeviceId);

        mAndroidDeviceView.setText(address);

        btnSignUp = (Button) findViewById(R.id.mav_sign_up_btn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncSignUpTask task = new AsyncSignUpTask();
                task.execute();

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public String getUserXml(User User) {
        String xml = "";
        Writer writer = new StringWriter();
        Serializer serializer = new Persister();
        try {
            serializer.write(User, writer);
            xml = writer.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return xml;
    }

    private String register(User user) {
        String result = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {

            //  String url = "http://192.168.0.6:8080/AMSWebServices/AMSService/Authenticate/"+userName+"/"+password;
            String url = URL;
            Log.w(TAG, "url=" + url);
            HttpPost postRequest = new HttpPost(url);

            String userXML = getUserXml(user);
            postRequest.addHeader("Content-Type", "application/xml");

            StringEntity postentity = new StringEntity(userXML, "UTF-8");
            postentity.setContentType("application/xml");

            postRequest.setEntity(postentity);


            //  postRequest.setEntity(new BasicHttpEntity(nameValuePair));
            //  HttpGet getRequest = new HttpGet(url);
            //Log.w("AMS-S",getRequest.toString());
            HttpResponse httpResponse = httpclient.execute(postRequest);

            HttpEntity entity = httpResponse.getEntity();
            Log.w("AMS-S", httpResponse.getStatusLine().toString());

            if (entity != null) {
                result = EntityUtils.toString(entity);

                //  userXML = result;
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

    private class AsyncSignUpTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if (validated) {
                String result =  register(user);
                response = result;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            mProgressView.setVisibility(View.GONE);
            if(response.equals("SUCCESS"))
            {
                //navigate back to login page
                Toast toast = Toast.makeText(getApplicationContext(),R.string.register_success,Toast.LENGTH_LONG);
                toast.show();
                mFirstNameView.setText("");
                mLastNameView.setText("");
                mEmailView.setText("");
                mPasswordView.setText("");
                mConfirmPasswordView.setText("");

                /*Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);*/

            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_occured, Toast.LENGTH_LONG);
                toast.show();

            }


           /* if(response.equals("SUCCESS"))
            {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                // startActivity(intent);
                //response = result;
                response = "";
            }
            else if(response.equals("FAILURE"))
            {

            }*/

        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            mProgressView.setVisibility(View.VISIBLE);
            if (mFirstNameView.getText().length() != 0 && mFirstNameView.getText().toString() != "") {

                user.setFirstName(mFirstNameView.getText().toString());
                if (mLastNameView.getText().length() != 0 && mLastNameView.getText().toString() != "") {

                    user.setLastName(mLastNameView.getText().toString());
                    if (mEmailView.getText().length() != 0 && mEmailView.getText().toString() != "") {
                        user.setMavEmail(mEmailView.getText().toString());
                        if (mPasswordView.getText().length() != 0 && mPasswordView.getText().toString() != "") {

                            if (mConfirmPasswordView.getText().length() != 0 && mConfirmPasswordView.getText().toString() != "") {

                                if (mPasswordView.getText().toString().equals(mConfirmPasswordView.getText().toString())) {
                                    user.setPassword(mPasswordView.getText().toString());

                                    user.setAndroidDeviceId(address);
                                    validated = true;
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.error_enter_password_not_match, Toast.LENGTH_LONG);
                                    toast.show();
                                    validated = false;

                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_enter_confirm_password, Toast.LENGTH_LONG);
                                toast.show();
                                validated = false;

                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.error_enter_password, Toast.LENGTH_LONG);
                            toast.show();
                            validated = false;

                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.error_enter_email, Toast.LENGTH_LONG);
                        toast.show();
                        validated = false;

                    }


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.error_enter_last_name, Toast.LENGTH_LONG);
                    toast.show();
                    validated = false;
                }
            } else

            {
                // mErrorMessageView.setText("Please enter Mav Email ID");
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_enter_first_name, Toast.LENGTH_LONG);
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
