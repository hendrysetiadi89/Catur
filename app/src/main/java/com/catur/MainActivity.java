package com.catur;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    ImageView imageViews[] = new ImageView[10];

    Handler mHandler;
    Runnable mRunnable;

    class GetCaturTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection conn= null;
            try {
                URL url = new URL("http://mobile.suitmedia.com/bl/chess.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream is = conn.getInputStream();
                StringBuilder hasilStringBuilder = new StringBuilder();

                BufferedReader bReader =
                        new BufferedReader(
                                new InputStreamReader(is));

                String strLine;
                /** Reading the contents of the file , line by line */
                while ((strLine = bReader.readLine()) != null) {
                    hasilStringBuilder.append(strLine);
                }
                return hasilStringBuilder.toString();
            }
            catch (Exception e) {
                Log.i("Test", e.getMessage());
                return null;
            }
            finally {
                if (null!= conn){
                    conn.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] line = s.split("<br/>");
            for (int i= 0; i<line.length; i++) {
                String [] itemInLine = line[i].split(",");
                String code = itemInLine[0];
                String column = itemInLine[1];
                String row = itemInLine[2];
                ImageView iv = imageViews[i];
                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) iv.getLayoutParams();
                // TODO using char ASCII
                int marginLeft = 0;
                switch (column) {
                    case "a":
                        marginLeft = 0;
                        break;
                    case "b":
                        marginLeft = widthCol;
                        break;
                    case "c":
                        marginLeft = 2*widthCol;
                        break;
                    case "d":
                        marginLeft = 3*widthCol;
                        break;
                    case "e":
                        marginLeft = 4*widthCol;
                        break;
                    case "f":
                        marginLeft = 5*widthCol;
                        break;
                    case "g":
                        marginLeft = 6*widthCol;
                        break;
                    case "h":
                        marginLeft = 7*widthCol;
                        break;
                }
                int marginTop = (8 - Integer.parseInt(row)) * widthCol;
                params.setMargins(marginLeft, marginTop,0,0);
                iv.setLayoutParams(params);

                int resource = 0;
                switch (code) {
                    case "K":
                        resource = R.drawable.wk;
                        break;
                    case "k":
                        resource = R.drawable.bk;
                        break;
                    case "Q":
                        resource = R.drawable.wq;
                        break;
                    case "q":
                        resource = R.drawable.bq;
                        break;
                    case "B":
                        resource = R.drawable.wb;
                        break;
                    case "b":
                        resource = R.drawable.bb;
                        break;
                    case "N":
                        resource = R.drawable.wn;
                        break;
                    case "n":
                        resource = R.drawable.bn;
                        break;
                    case "R":
                        resource = R.drawable.wr;
                        break;
                    case "r":
                        resource = R.drawable.br;
                        break;
                }
                iv.setImageResource(resource);

            }
            Log.i("Test", s);
        }
    }

    Socket mSocket;

    class GetCatur2Task extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                mSocket = new Socket("xinuc.org", 7387);
                InputStream is = mSocket.getInputStream();

                byte[] buffer = new byte[40];
                int read = is.read(buffer);

//                StringBuilder sb = new StringBuilder();
//                int r;
//                while ((r = is.read()) != -1) {
//                    char c = (char) r;
//                    if (c == '\n') {
//                        String sbString = sb.toString();
//                        publishProgress(sbString );
//                        sb = new StringBuilder();
//                        continue;
//                    }
//                    sb.append(c);
//                }

                while(read != -1){
                    if (isCancelled()) {
                        break;
                    }
                    publishProgress(new String(buffer, "UTF-8") );
                    read = is.read(buffer);
                }

//                int bytesRead;
//                StringBuilder hasilStringBuilder = new StringBuilder();
//                byte[] buffer = new byte[1024];
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
//                        1024);
////
//                while ((bytesRead = is.read(buffer)) != -1) {
//                    byteArrayOutputStream.write(buffer, 0, bytesRead);
//                    hasilStringBuilder.append(byteArrayOutputStream.toString("UTF-8") );
//                }
//                return hasilStringBuilder.toString();

//                StringBuilder hasilStringBuilder = new StringBuilder();
//
//                BufferedReader bReader =
//                        new BufferedReader(
//                                new InputStreamReader(is));
//
//                String strLine;
//                /** Reading the contents of the file , line by line */
//                while ((strLine = bReader.readLine()) != null) {
//                    hasilStringBuilder.append(strLine);
//                }
//                return hasilStringBuilder.toString();
            } catch (Exception e) {
                Log.i("Test", "Test" );
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            String[] line = values[0].split(" ");
            for (int i= 0; i<line.length; i++) {
                // String [] itemInLine = line[i].split(",");
                char code = line[i].charAt(0);
                char column = line[i].charAt(1);
                String row = String.valueOf( line[i].charAt(2) );
                ImageView iv = imageViews[i];
                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) iv.getLayoutParams();
                // TODO using char ASCII
                int marginLeft = 0/* = (column - 95) * widthCol*/;
                switch (column) {
                    case 'a':
                        marginLeft = 0;
                        break;
                    case 'b':
                        marginLeft = widthCol;
                        break;
                    case 'c':
                        marginLeft = 2*widthCol;
                        break;
                    case 'd':
                        marginLeft = 3*widthCol;
                        break;
                    case 'e':
                        marginLeft = 4*widthCol;
                        break;
                    case 'f':
                        marginLeft = 5*widthCol;
                        break;
                    case 'g':
                        marginLeft = 6*widthCol;
                        break;
                    case 'h':
                        marginLeft = 7*widthCol;
                        break;
                }
                int rowInt;
                try {
                    rowInt = Integer.parseInt(row);
                }
                catch (Exception e) {
                    rowInt = 0;
                }
                int marginTop = (8 - rowInt) * widthCol;
                params.setMargins(marginLeft, marginTop,0,0);
                iv.setLayoutParams(params);

                int resource = 0;
                switch (code) {
                    case 'K':
                        resource = R.drawable.wk;
                        break;
                    case 'k':
                        resource = R.drawable.bk;
                        break;
                    case 'Q':
                        resource = R.drawable.wq;
                        break;
                    case 'q':
                        resource = R.drawable.bq;
                        break;
                    case 'B':
                        resource = R.drawable.wb;
                        break;
                    case 'b':
                        resource = R.drawable.bb;
                        break;
                    case 'N':
                        resource = R.drawable.wn;
                        break;
                    case 'n':
                        resource = R.drawable.bn;
                        break;
                    case 'R':
                        resource = R.drawable.wr;
                        break;
                    case 'r':
                        resource = R.drawable.br;
                        break;
                }
                iv.setImageResource(resource);
            }
        }

    }

    public int widthCol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        widthCol = (int) getResources().getDimension(R.dimen.widthCol);

        // TODO use loop
        imageViews[0] = (ImageView) findViewById(R.id.iv0);
        imageViews[1] = (ImageView) findViewById(R.id.iv1);
        imageViews[2] = (ImageView) findViewById(R.id.iv2);
        imageViews[3] = (ImageView) findViewById(R.id.iv3);
        imageViews[4] = (ImageView) findViewById(R.id.iv4);
        imageViews[5] = (ImageView) findViewById(R.id.iv5);
        imageViews[6] = (ImageView) findViewById(R.id.iv6);
        imageViews[7] = (ImageView) findViewById(R.id.iv7);
        imageViews[8] = (ImageView) findViewById(R.id.iv8);
        imageViews[9] = (ImageView) findViewById(R.id.iv9);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mHandler == null) {
//            mHandler = new Handler();
//        }
//        if (mRunnable == null) {
//            mRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    //run asysntask
//                    new GetCaturTask().execute();
//                    new GetCatur2Task().execute();
//                    //run each 1 seconds
//                    if (null!= mHandler) {
//                        mHandler.postDelayed(mRunnable, 1000);
//                    }
//                }
//            };
//        }
//        mHandler.post(mRunnable);

        if (null == mCaturTask) {
            mCaturTask = new GetCatur2Task();
        }
        mCaturTask.execute();
    }
    GetCatur2Task mCaturTask;

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler!=null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
        if (null!= mSocket) {
            mCaturTask.cancel(true);
            mCaturTask = null;
        }
    }
}
