package com.blogspot.aruncyberspace.attendance_management_system_professor;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Collection;
import java.util.jar.JarEntry;


public class ManualAttendanceActivity extends ActionBarActivity {

    Spinner spinner;
    ProgressBar mProgressView;
    String getcoursesResponse, getStudentsReponse, saveManualAttendanceResponse;
    GetManualStudentsInCourseResponse getManualStudentsInCourseResponse;
    AddManualAttendanceRequest addManualAttendanceRequest;
    User user;
    String URL = "http://dms.ngrok.io/AMSWebServices/AMSService/GetProfessorTeaches/";
    String URL2 = "http://dms.ngrok.io/AMSWebServices/AMSService/GetManualStudentsInCourse/";
    String URL3 = "http://dms.ngrok.io/AMSWebServices/AMSService/AddManualAttendance/";
    String TAG = "AMS";
    Professor professor;
    String selectedCourse = "";
    ArrayList<String> spinnerList;
    ArrayList<String> values = new ArrayList<String>();
    TableLayout tblLayout;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_attendance);

        spinner = (Spinner) findViewById(R.id.courseSelector);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        tblLayout = (TableLayout) findViewById(R.id.tableLayout);

        btnSave = (Button) findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addManualAttendanceRequest = new
                        AddManualAttendanceRequest();

                Collection<ManualStudent> students = new ArrayList<ManualStudent>();

                for (int i = 0; i < tblLayout.getChildCount(); i++) {
                    View view = tblLayout.getChildAt(i);
                    if (view instanceof TableRow) {
                        for (int j = 0; j < ((TableRow) view).getChildCount(); j++) {

                            View viewRow = ((TableRow) view).getChildAt(j);
                            if (viewRow instanceof CheckBox) {

                                if (((CheckBox) viewRow).isChecked()) {
                                    ManualStudent s = new ManualStudent();

                                    String tag = viewRow.getTag().toString();
                                    s.setStudentId(tag);
                                    String checked = Boolean.toString(((CheckBox) viewRow).isChecked());
                                    Log.w(TAG, tag + " = " + checked);
                                    s.setCourseNumber(selectedCourse);

                                    students.add(s);
                                }
                            }
                        }
                    }
                }

                addManualAttendanceRequest.setStudents(students);

                AsyncAddManualAttendance task = new AsyncAddManualAttendance();
                task.execute();

            }
        });


        values.add("Select Course");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String courseId = spinner.getSelectedItem().toString();
                selectedCourse = courseId;
                Log.w(TAG, "selected Item:" + courseId);

                if (!selectedCourse.equals("Select Course")) {
                    AsyncStudentsInCourse task = new AsyncStudentsInCourse();
                    task.execute();
                } else {
                    tblLayout.removeAllViews();
                    TableRow row = new TableRow(ManualAttendanceActivity.this);

                    final TextView text = new TextView(ManualAttendanceActivity.this);
                    text.setText("<Not Available>");
                    text.setGravity(Gravity.CENTER);
                    text.setTextAppearance(ManualAttendanceActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
                    text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                    row.addView(text);
                    tblLayout.addView(row);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        user = new User();

        user.setFirstName("Test");
        user.setLastName("Test");
        user.setMavEmail("d");

        AsyncTaskLoadCourse task = new AsyncTaskLoadCourse();
        task.execute();

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

    public String saveManualAttendance() {
        String result = "";
        String request = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {

            //  String url = "http://192.168.0.6:8080/AMSWebServices/AMSService/Authenticate/"+userName+"/"+password;
            String url = URL3;

            Log.w(TAG, "url=" + url);
            HttpPost postRequest = new HttpPost(url);

            //requestXML =	Writer writer = new StringWriter();
            Writer writer = new StringWriter();
            Serializer serializer = new Persister();
            try {
                serializer.write(addManualAttendanceRequest, writer);
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

    public String getStudentsInCourses(String courseId, String ProfessorEmail) {

        //   String response = "";
        String result = "";
        String request = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {

            //  String url = "http://192.168.0.6:8080/AMSWebServices/AMSService/Authenticate/"+userName+"/"+password;
            String url = URL2 + courseId + "/" + ProfessorEmail;
            Log.w(TAG, "url=" + url);
            HttpGet postRequest = new HttpGet(url);

            //requestXML =	Writer writer = new StringWriter();
        /*    Writer writer = new StringWriter();
            Serializer serializer = new Persister();
            try {
                serializer.write(user, writer);
                request = writer.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

       /*     postRequest.addHeader("Content-Type", "application/xml");

            StringEntity postentity = new StringEntity(request, "UTF-8");
            postentity.setContentType("application/xml");

            postRequest.setEntity(postentity);*/


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual_attendance, menu);
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


    public class AsyncAddManualAttendance extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String response = saveManualAttendance();
            saveManualAttendanceResponse = response;
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mProgressView.animate();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressView.setVisibility(View.GONE);
            if (saveManualAttendanceResponse.equals("SUCCESS")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Saved Successfully!", Toast.LENGTH_LONG);
                toast.show();
                //make it
                spinner.setSelection(0);

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressView.setVisibility(View.VISIBLE);
        }
    }

    public class AsyncStudentsInCourse extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            String response = getStudentsInCourses(selectedCourse, professor.getEmailId());
            getStudentsReponse = response;
            Serializer serializer =
                    new Persister();
            try {
                getManualStudentsInCourseResponse = serializer.read(GetManualStudentsInCourseResponse.class, response);
                Log.w(TAG, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressView.setVisibility(View.GONE);

            if (getManualStudentsInCourseResponse.getStudents().size() > 0) {
                tblLayout.removeAllViews();
            }
            for (ManualStudent s : getManualStudentsInCourseResponse.getStudents()) {
                final TableRow tableRow = new TableRow(ManualAttendanceActivity.this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                // Creation textView
                final TextView text = new TextView(ManualAttendanceActivity.this);
                text.setText(s.getFirstName() + "," + s.getLastName() + "                   ");
                text.setTextAppearance(ManualAttendanceActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


                final TextView text2 = new TextView(ManualAttendanceActivity.this);
                text2.setText(s.getStudentId() + "  ");
                text2.setTextAppearance(ManualAttendanceActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
                text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


                CheckBox chk = new CheckBox(ManualAttendanceActivity.this);
                //chk.setText("chk");
                chk.setTextAppearance(ManualAttendanceActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
                chk.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                // Creation  button
                chk.setTag(s.getStudentId());
                chk.setGravity(Gravity.RIGHT);

                // tableRow.addView(text2);
                tableRow.addView(text);
                tableRow.addView(chk);
                //   tableRow.addView(button);

                tblLayout.addView(tableRow);


            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mProgressView.animate();
        }
    }

    public class AsyncTaskLoadCourse extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onProgressUpdate(Void... values) {
            //  super.onProgressUpdate(values);
            mProgressView.animate();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressView.setVisibility(View.GONE);

            Serializer serializer = new Persister();
            try {
                professor = serializer.read(Professor.class, getcoursesResponse);
                // spinnerList = new ArrayList<String>();
                for (Course c : professor.getCourses()) {
                    values.add(c.getCourseId());
                }
                if (values.size() > 0) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ManualAttendanceActivity.this, android.R.layout.simple_list_item_1, values);
                    spinner.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            getcoursesResponse = getProfessorTeaches(user);
            Log.w(TAG, getcoursesResponse);
            return null;
        }
    }
}
