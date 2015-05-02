package com.blogspot.aruncyberspace.attendance_management_system_student;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.sql.Timestamp;
import java.util.Date;
import java.util.EnumMap;


public class QRCodeGenerateActivity extends ActionBarActivity {

    String LOGTAG = "AMS";
    private ProgressBar mProgressView;
    private ImageView mQRCodeImageView;
    private TextView mHelpTextView;
    Bitmap generatedImage;
    String userXML;
    String courseId;
    String timeStamp;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generate);
        //to prevent from taking screenshots
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Date date = new Date();
        userXML = getIntent().getStringExtra("USER_XML");
        courseId = getIntent().getStringExtra("course_id");
        user = deserializeUserXML(userXML);
        timeStamp = (new Timestamp(date.getTime())).toString();

        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mQRCodeImageView = (ImageView) findViewById(R.id.qrCodeImage);
        //setting empty transparent background
        mQRCodeImageView.setImageResource(android.R.color.transparent);
        //mHelpTextView = (TextView) findViewById(R.id.helpText);
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }
    protected User deserializeUserXML(String userXML) {
        User user = new User();
        Serializer serializer = new Persister();
        try {

            user = serializer.read(User.class, userXML);


        } catch (Exception ex) {
            String exceptionstring = ex.toString();
            exceptionstring.toString();
        }
        return user;
    }

   private Bitmap generateQRCode(String content)
   {
       QRCodeWriter writer = new QRCodeWriter();
       try {
           //Find screen size


          EnumMap<EncodeHintType, Object> hint = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
           hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
           BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 800  , 800,hint);
           int width = bitMatrix.getWidth();
           int height = bitMatrix.getHeight();
           int[] pixels = new int[width * height];
           for (int y = 0; y < height; y++) {
               int offset = y * width;
               for (int x = 0; x < width; x++) {
                   // pixels[offset + x] = bitMatrix.get(x, y) ? 0xFF000000
                   // : 0xFFFFFFFF;
                   pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
               }
           }

           Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
           bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
           return bitmap;
       }
       catch (Exception e)
       {
           Log.w(LOGTAG, "QR Code Generator Error:"+e.toString());

       }
       return null;
   }


    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
           // Log.i(LOGTAG, "doInBackground");
            String address = "0";
            try {
                WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                 address = info.getMacAddress();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();

            }
           // String address = "00:0a:95:9d:68:16";

           // String content = "1001160219;CSE5324;"+address;
            String content = user.getUserId()+";"+courseId+";"+address+";"+timeStamp;
            Log.w("AMS",content);
            generatedImage = generateQRCode(content);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressView.setVisibility(View.GONE);
            if(generatedImage!=null) {


                mQRCodeImageView.setImageBitmap(generatedImage);

            }
            else
                Log.w(LOGTAG,"QR Code was null");


        }


        @Override
        protected void onPreExecute() {
            Log.i(LOGTAG, "onPreExecute");
            mProgressView.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(LOGTAG, "onProgressUpdate");
            mProgressView.animate();

        }

    }

}
