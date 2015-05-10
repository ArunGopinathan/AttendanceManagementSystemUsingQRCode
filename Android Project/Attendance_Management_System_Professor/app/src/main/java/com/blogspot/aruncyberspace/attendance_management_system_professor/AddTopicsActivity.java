package com.blogspot.aruncyberspace.attendance_management_system_professor;

import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddTopicsActivity extends ActionBarActivity {

    User user;
    String userXML;
    Button btnSaveButton;
    EditText topicsText;
    TextView dateTxt;
    ProgressBar mProgressView;
    Spinner spinner;
    String URL2 = "http://dms.ngrok.io/AMSWebServices/AMSService/AddTopics/";
    String URL = "http://dms.ngrok.io/AMSWebServices/AMSService/GetProfessorTeaches/";
    String addTopicsRequestXML;
    AddTopicsRequest addTopicsRequest;
    String TAG = "DMS";
    ArrayList<String> list = new ArrayList<String>();
    Professor professor = new Professor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topics);
        dateTxt = (TextView) findViewById(R.id.date);
        topicsText = (EditText) findViewById(R.id.topicstxt);
        btnSaveButton = (Button) findViewById(R.id.btnAddTopics);

        btnSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTopicsAsyncTask task = new UpdateTopicsAsyncTask();
                task.execute("SAVE");

            }
        });

        spinner = (Spinner) findViewById(R.id.courseSelector);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateTxt.setText(sdf.format(date));
        userXML = getIntent().getStringExtra("USER_XML");
        Serializer serializer = new Persister();
        try {
            user = serializer.read(User.class, userXML);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        UpdateTopicsAsyncTask task = new UpdateTopicsAsyncTask();
        task.execute("GET_COURSE");



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_topics, menu);
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

    public String getProfessorTeaches(User user) {

        //   String response = "";
        String result = "";
        String request = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {

            //  String url = "http://192.168.0.6:8080/AMSWebServices/AMSService/Authenticate/"+userName+"/"+password;
            String url = URL;
            Log.w(TAG, "url=" + url);
            HttpPost postRequest = new HttpPost(url);

            //requestXML =	Writer writer = new StringWriter();
            Writer writer = new StringWriter();
            Serializer serializer = new Persister();
            try {
                serializer.write(user, writer);
                request = writer.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            postRequest.addHeader("Content-Type", "application/xml");

            StringEntity postentity = new StringEntity(request, "UTF-8");
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


    public void addTopics(String addTopicsRequestXML) {
        String result = "";


        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(URL2);
        try {
            postRequest.addHeader("Content-Type", "application/xml");

            StringEntity postentity = new StringEntity(addTopicsRequestXML, "UTF-8");
            postentity.setContentType("application/xml");

            postRequest.setEntity(postentity);
            //Log.w("AMS-S",getRequest.toString());
            HttpResponse httpResponse = httpClient.execute(postRequest);

            HttpEntity entity = httpResponse.getEntity();
            Log.w("AMS-S", httpResponse.getStatusLine().toString());

            if (entity != null) {
                result = EntityUtils.toString(entity);

                //  userXML = result;
                Log.w("AMS-S", "Entity : " + result);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            httpClient.getConnectionManager().shutdown();
        }

    }

    public class UpdateTopicsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            if (params[0].equals("GET_COURSE")) {
                String response = getProfessorTeaches(user);

                Serializer serializer = new Persister();
                try {
                    professor = new Professor();
                    professor = serializer.read(Professor.class, response);

                    return "GET_COURSE";

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else if(params[0].equals("SAVE")){
                try {
                    AddTopicsRequest request = new AddTopicsRequest();
                    request.setDate(dateTxt.getText().toString());
                    request.setCourseId(spinner.getSelectedItem().toString());
                    request.setProfessorId(user.getUserId());
                    request.setTopics(topicsText.getText().toString());
                    Serializer serializer = new Persister();
                    Writer writer = new StringWriter();
                    serializer.write(request, writer);
                    addTopicsRequestXML = writer.toString();
                    addTopics(addTopicsRequestXML);
                    return "SAVE_SUCCESS";
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                    return "SAVE_FAILURE";
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mProgressView.setVisibility(View.VISIBLE);
          //  addTopicsRequest.setDate(dateTxt.getText().toString());
        }

        @Override
        protected void onPostExecute(String aVoid) {
            mProgressView.setVisibility(View.GONE);


            if (aVoid.equals("GET_COURSE")) {
                list.clear();
                list.add("Select Course");
                for (Course c : professor.getCourses()) {
                    list.add(c.getCourseId());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddTopicsActivity.this, android.R.layout.simple_list_item_1, list);
                spinner.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            } else if(aVoid.equals("SAVE_SUCCESS")){
            Toast toast = Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_LONG);
                toast.show();
            }
            else if(aVoid.equals("SAVE_FAILURE"))
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Failed Successfully",Toast.LENGTH_LONG);
                toast.show();

            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mProgressView.animate();
        }
    }


}
