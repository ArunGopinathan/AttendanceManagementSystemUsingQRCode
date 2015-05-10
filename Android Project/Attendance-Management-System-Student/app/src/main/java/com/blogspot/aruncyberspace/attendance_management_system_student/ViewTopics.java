package com.blogspot.aruncyberspace.attendance_management_system_student;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;


public class ViewTopics extends ActionBarActivity {

    String URL2 = "http://dms.ngrok.io/AMSWebServices/AMSService/GetTopics/";
    String URL = "http://dms.ngrok.io/AMSWebServices/AMSService/GetStudentEnrolled/";
    String TAG = "DMS";
    User user;
    String userXML;
    ProgressBar mProgressView;
    Spinner spinner;
    String topicsResponseXML;
    TableLayout tableLayout;
GetTopicsResponse getTopicsResponse;
    ArrayList<String> list = new ArrayList<String>();
    Professor professor = new Professor();
    String addTopicsRequestXML;

    String courseId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_topics);
        tableLayout = (TableLayout)findViewById(R.id.tableLayout1);
        spinner = (Spinner) findViewById(R.id.courseSelector);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courseId = spinner.getSelectedItem().toString();
                GetTopicsAsyncTask task = new GetTopicsAsyncTask();
                task.execute("GET");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        userXML = getIntent().getStringExtra("USER_XML");
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        Serializer serializer = new Persister();
        try {
            user = serializer.read(User.class, userXML);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GetTopicsAsyncTask task = new GetTopicsAsyncTask();
        task.execute("GET_COURSE");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_topics, menu);
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
    public String addTopics() {
        String result = "";


        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet postRequest = new HttpGet(URL2+courseId);
        try {
          //  postRequest.addHeader("Content-Type", "application/xml");

           // StringEntity postentity = new StringEntity(addTopicsRequestXML, "UTF-8");
        //    postentity.setContentType("application/xml");

          //  postRequest.setEntity(postentity);
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

        return result;

    }



    public class GetTopicsAsyncTask extends AsyncTask<String, Void, String> {
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

            } else if(params[0].equals("GET")){
                try {
                 /*   AddTopicsRequest request = new AddTopicsRequest();
                    request.setDate(dateTxt.getText().toString());
                    request.setCourseId(spinner.getSelectedItem().toString());
                    request.setProfessorId(user.getUserId());
                    request.setTopics(topicsText.getText().toString());
                    Serializer serializer = new Persister();
                    Writer writer = new StringWriter();
                    serializer.write(request, writer);
                    addTopicsRequestXML = writer.toString();
                    addTopics(addTopicsRequestXML);*/
                    topicsResponseXML = addTopics();
                    Log.w(TAG,topicsResponseXML);
                    return "GET_SUCCESS";
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
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewTopics.this, android.R.layout.simple_list_item_1, list);
                spinner.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            } else if(aVoid.equals("GET_SUCCESS")){
             //   Toast toast = Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_LONG);
              //  toast.show();
                getTopicsResponse  = new GetTopicsResponse();
                Serializer serializer = new Persister();
                try {
                    getTopicsResponse = serializer.read(GetTopicsResponse.class, topicsResponseXML);

                    for(Map.Entry<String,String> entry : getTopicsResponse.getTopics().entrySet())
                    {
                        TableRow row = new TableRow(ViewTopics.this);
                        row.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        TextView textViewdate = new TextView(ViewTopics.this);
                        textViewdate.setText(entry.getKey());
                        textViewdate.setTextAppearance(ViewTopics.this,android.R.style.TextAppearance_DeviceDefault_Medium);
                        textViewdate.setPadding(10,10,10,10);

                        TextView textViewTopics = new TextView(ViewTopics.this);
                        textViewTopics.setText(entry.getValue());
                        textViewTopics.setTextAppearance(ViewTopics.this,android.R.style.TextAppearance_DeviceDefault_Medium);
                        textViewTopics.setPadding(10,10,10,10);
                        row.addView(textViewdate);
                        row.addView(textViewTopics);
                        tableLayout.addView(row);

                    }



                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }



            }
            else if(aVoid.equals("GET_FAILURE"))
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
