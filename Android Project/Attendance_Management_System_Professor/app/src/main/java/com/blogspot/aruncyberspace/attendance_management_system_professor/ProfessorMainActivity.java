package com.blogspot.aruncyberspace.attendance_management_system_professor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

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
import java.util.ArrayList;
import java.util.Collection;


public class ProfessorMainActivity extends ActionBarActivity {
    Button btnGetAttendance, btnManualAttendance, btnReport;
    String URL3 = "http://dms.ngrok.io/AMSWebServices/AMSService/AddManualAttendance/";
    TextView welcometxt;
    String qrcodeContent = "";
    ArrayList<String> results;
    String userXML;
    User user;
    ProgressBar mprogressView;
    TextView progressText;
    String saveManualAttendanceResponse;
AddManualAttendanceRequest addManualAttendanceRequest;

    String TAG="DMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_main);
        results = new ArrayList<String>();

        btnGetAttendance = (Button) findViewById(R.id.getAttendanceBtn);
        btnManualAttendance = (Button) findViewById(R.id.manualAttendanceBtn);
        btnReport = (Button) findViewById(R.id.getReportsBtn);
        welcometxt = (TextView) findViewById(R.id.welcomeText);
        mprogressView = (ProgressBar) findViewById(R.id.prograss_bar);
        progressText = (TextView) findViewById(R.id.progress);


        userXML = getIntent().getStringExtra("USER_XML");
        Serializer serializer = new Persister();
        try {

            user = serializer.read(User.class, userXML);
            String Name = user.getLastName() + "," + user.getFirstName();
            welcometxt.setText(Name);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final String result = "";
        //when get attendance button is clicked
        btnGetAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    IntentIntegrator intent = new IntentIntegrator(ProfessorMainActivity.this);
                    intent.initiateScan();
                    String all = "";


                } catch (Exception ex) {
                    Log.w("AMSP", "Activity not found Exception");
                }

            }
        });

        btnManualAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //manual attendance page
                Intent intent = new Intent(getApplicationContext(), ManualAttendanceActivity.class);
                startActivity(intent);
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ManualAttendanceActivity.class);
                intent.putExtra("USER_XML",userXML);
                startActivity(intent);

            }
        });


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_professor_main, menu);
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

    public class AsyncTaskSaveAttendance extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String response = saveManualAttendance();
            saveManualAttendanceResponse = response;
            return null;

        }

        @Override
        protected void onPreExecute() {
            mprogressView.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);

            addManualAttendanceRequest = new AddManualAttendanceRequest();
            Collection<ManualStudent> students = new ArrayList<ManualStudent>();
            try {
                for (String s : results) {
                    String[] values = s.split(";");
                    String studentEmail = values[0];
                    String courseId = values[1];
                    String address = values[2];
                    String timeStamp = values[3];

                    ManualStudent student = new ManualStudent();
                    student.setStudentId(studentEmail);
                    student.setCourseNumber(courseId);
                    student.setTimeStamp(timeStamp);

                    students.add(student);
                }

                addManualAttendanceRequest.setStudents(students);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mprogressView.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);

            if(saveManualAttendanceResponse.equals("SUCCESS"))
            {
                Toast toast= Toast.makeText(getApplicationContext(),"Saved Successfully!",Toast.LENGTH_LONG);
                toast.show();
            }
            else
            {
                Toast toast= Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_LONG);
                toast.show();

            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
                mprogressView.animate();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // welcometxt.setText(Integer.toString(requestCode));
        /*if (requestCode == 0)*/
        {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //
                qrcodeContent = contents;
                results.add(contents);

                Intent nextintent = new Intent("com.google.zxing.client.android.SCAN");
                nextintent.putExtra("SCAN_FORMATS", "QR_CODE");

                startActivityForResult(nextintent, 0); // start the next scan/* Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                //     String all = "empty";
                //    for (String s : results)
                //         all += " " + s;
                // welcometxt.setText(all);

                AsyncTaskSaveAttendance task = new AsyncTaskSaveAttendance();
                task.execute();
            }
        }
    }
}
