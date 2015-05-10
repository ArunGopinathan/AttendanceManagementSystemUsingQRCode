package com.blogspot.aruncyberspace.attendance_management_system_professor;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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


public class ReportActivity extends ActionBarActivity {

    String userXML;
    User user;
    Spinner spinner;
    TextView mTotalStudents, mTotalAttendanceToday, mPercentage;
    ProgressBar mProgressView;
    String reportResponse;
    String courseId;
    String professorXML;
    Professor professor;
    String URL = "http://dms.ngrok.io/AMSWebServices/AMSService/GetProfessorTeaches/";
    String URL2 = "http://dms.ngrok.io/AMSWebServices/AMSService/GetDailyReport/";
    String TAG = "DMS";
    ArrayList<String> values = new ArrayList<String>();
    ;
    String CourseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        userXML = getIntent().getStringExtra("USER_XML");
        Serializer serializer = new Persister();
        try {
            user = serializer.read(User.class, userXML);
        } catch (Exception ex) {

        }

        mTotalStudents = (TextView) findViewById(R.id.totalStudents);
        mTotalAttendanceToday = (TextView) findViewById(R.id.totalAttendanceToday);
        mPercentage = (TextView) findViewById(R.id.percentage);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        spinner = (Spinner) findViewById(R.id.courseSelector);

        AsyncGetCourses asyncGetCourses = new AsyncGetCourses();
        asyncGetCourses.execute();


        values.add("Select Course");

      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, values);
        spinner.setAdapter(adapter);*/

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CourseId = spinner.getSelectedItem().toString();
                //   if (CourseId != null)
                Log.w(TAG, CourseId);

                if (!CourseId.equals("Select Course")) {
                    AsyncGetReport task = new AsyncGetReport();
                    task.execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
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

    public String getDailyReport() {
        String response = "";
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(URL2 + CourseId + "/" + user.getUserId());
        try {
            HttpResponse httpResponse = client.execute(getRequest);

            HttpEntity entity = httpResponse.getEntity();
            Log.w("AMS-S", httpResponse.getStatusLine().toString());

            if (entity != null) {
                response = EntityUtils.toString(entity);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return response;
    }

    public class AsyncGetReport extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String response = getDailyReport();
            reportResponse = response;
            Log.w(TAG, response);

            return null;
        }

        @Override
        protected void onPreExecute() {
            //   super.onPreExecute();
            mProgressView.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            mProgressView.setVisibility(View.GONE);
            Serializer serializer = new Persister();
            try {
               ReportResponse response = serializer.read(ReportResponse.class,reportResponse);
                if(response.getDailyReport()!=null)
                {
                    DailyReport report = response.getDailyReport();
                    mTotalStudents.setText( report.getTotalNumberOfStudents());
                    mTotalAttendanceToday.setText(report.getTotalAttendanceToday());
                    mPercentage.setText(report.getAttendancePercentage());

                }

            } catch (Exception ex) {

            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //super.onProgressUpdate(values);
            mProgressView.animate();
        }
    }

    public class AsyncGetCourses extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            professorXML = getProfessorTeaches(user);

            Serializer serializer = new Persister();
            try {

                professor = serializer.read(Professor.class, professorXML);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute() {

            mProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            mProgressView.animate();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressView.setVisibility(View.GONE);

            if (professor != null) {
                for (Course c : professor.getCourses()) {
                    values.add(c.getCourseId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportActivity.this, android.R.layout.simple_list_item_1, values);
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }
    }
}
