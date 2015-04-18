package com.blogspot.aruncyberspace.attendance_management_system_professor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;


public class ProfessorMainActivity extends ActionBarActivity {
    Button btnGetAttendance, btnManualAttendance, btnReport;
    TextView welcometxt;
    String qrcodeContent="";
    ArrayList<String> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_main);
        results = new ArrayList<String>();

        btnGetAttendance = (Button) findViewById(R.id.getAttendanceBtn);
        btnManualAttendance = (Button) findViewById(R.id.manualAttendanceBtn);
        btnReport = (Button) findViewById(R.id.getReportsBtn);
        welcometxt=(TextView) findViewById(R.id.welcomeText);
        final String result="";
        //when get attendance button is clicked
        btnGetAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    IntentIntegrator intent = new IntentIntegrator(ProfessorMainActivity.this);
                    intent.initiateScan();
                    String all  = "";


                }
                catch(Exception ex)
                {
                    Log.w("AMSP","Activity not found Exception");
                }

            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
       // welcometxt.setText(Integer.toString(requestCode));
        /*if (requestCode == 0)*/ {
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
                String all="empty";
                for(String s: results)
                    all+=" "+s;
                welcometxt.setText(all);



            }
        }
    }
}
