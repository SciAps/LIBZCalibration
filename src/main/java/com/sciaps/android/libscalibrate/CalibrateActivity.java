package com.sciaps.android.libscalibrate;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.sciaps.android.libscalibrate.data.CalibrationAlloy;
import com.sciaps.android.libscalibrate.fragments.CustomeDialogFragment;
import com.sciaps.android.libscalibrate.fragments.NavigationDrawerFragment;
import com.sciaps.android.libscalibrate.views.Preview;
import com.sciaps.common.serialize.JsonSerializerFactory;
import com.sciaps.common.serialize.Serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalibrateActivity extends ActionBarActivity  implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String TAG = "CalibrateActivity";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CalibrationAlloy[] calibrations;
    private int currentAlloyPossition;
    private Preview preview;
    private DialogFragment testDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callibrate);

        preview = new Preview(getApplicationContext(), (SurfaceView)findViewById(R.id.surfaceView));
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        ((FrameLayout) findViewById(R.id.preview)).addView(preview);
        preview.setKeepScreenOn(true);



        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

                getCalibrationsArray();


        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),calibrations);

        Button btnSkip = (Button) findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener(skipButtonListner);

        Button btntest = (Button) findViewById(R.id.btn_test);
        btntest.setOnClickListener(testButtonListner);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
         currentAlloyPossition = position;
        if (calibrations==null){
            getCalibrationsArray();

        }
        CalibrationAlloy selectedItem = calibrations[position];

        TextView alloyName = (TextView) findViewById(R.id.alloyName);
        alloyName.setText(selectedItem.name);
        TextView alloyState = (TextView) findViewById(R.id.txt_status);
        TextView date = (TextView) findViewById(R.id.txt_date);

        if (selectedItem.wasTaken){
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

            alloyState.setText("Calibrated On:");
            Date taken = new Date(selectedItem.takenDateMS);
            date.setText(df.format(taken.getTime()));
        }else {
            alloyState.setText("Not Calibrated");
            date.setText("");
        }


    }

    private View.OnClickListener skipButtonListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //moove to the next untested Item
            goToNextItem();
        }
    };

    private View.OnClickListener testButtonListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //test the alloy
            CalibrationAlloy alloy = calibrations[currentAlloyPossition];
            //mark item as tested
            testDialog  = new CustomeDialogFragment("Calibrating",alloy.name);
            testDialog.setCancelable(false);
            testDialog.show(getFragmentManager(),"Loading");

            TestAsyncTask testTask = new TestAsyncTask();
            testTask.execute(alloy);
        }
    };

    class TestAsyncTask extends AsyncTask<CalibrationAlloy, Integer, String> {

        @Override
        protected String doInBackground(CalibrationAlloy... alloys) {


            try {
                Thread.sleep(2000);
                return "Success";

            } catch (InterruptedException e) {
                e.printStackTrace();
                return "Success";

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            CalibrationAlloy alloy = calibrations[currentAlloyPossition];

            alloy.wasTaken=true;
            //add date stamp
            long currentDateTime = System.currentTimeMillis();

            alloy.takenDateMS =currentDateTime;

            //moove to the next untested Item
            goToNextItem();

            saveCalibrations();

            testDialog.dismiss();
            testDialog = null;

        }
    }

    private void getCalibrationsArray(){
        
        Serializer<CalibrationAlloy[]> serializer = JsonSerializerFactory.getSerializer(CalibrationAlloy[].class);

        File jsonFile = CalibrationApplication.getInstance(this).getInjector().getInstance(Key.get(File.class, Names.named("alloy_calibration")));
            try {
                FileInputStream ins = new FileInputStream(jsonFile);
                calibrations = serializer.deserialize(ins);
                ins.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "json Error",e);

            } catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "json Error",e);

            }

    };

    private void saveCalibrations(){
        Serializer<CalibrationAlloy[]> serializer = JsonSerializerFactory.getSerializer(CalibrationAlloy[].class);
        try {

            File jsonFile = CalibrationApplication.getInstance(this).getInjector().getInstance(Key.get(File.class, Names.named("alloy_calibration")));

            try{
                jsonFile.createNewFile();
            }catch(IOException e){
                Log.e("IOException", "exception in createNewFile() method",e);
            }
            //we have to bind the new file with a FileOutputStream
            FileOutputStream fileos = null;
            try{
                fileos = new FileOutputStream(jsonFile);
            }catch(FileNotFoundException e){
                Log.e("FileNotFoundException", "can't create FileOutputStream",e);
            }
            //FileInputStream fin = new FileInputStream(regiontablefile);
            serializer.serialize(calibrations,fileos);
            fileos.close();

            Log.w("ActionBarActivity", calibrations.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.w("ActionBarActivity",e);
        }
    }

    private void goToNextItem(){
        CalibrationAlloy calibration;
        for(int i=0;i<calibrations.length;i++){
            int nextItem = i+1+currentAlloyPossition;


            if(nextItem>=calibrations.length){



                nextItem = nextItem-calibrations.length;
            }
            calibration  = calibrations[nextItem];


            if (calibration.wasTaken==false){
                mNavigationDrawerFragment.selectItem(nextItem);

                break;
            }
            if (i==calibrations.length-1){
                //  selectNextItem
                nextItem = currentAlloyPossition+1;
                if(currentAlloyPossition+1>=calibrations.length){
                    nextItem = nextItem-calibrations.length;

                }
                mNavigationDrawerFragment.selectItem(nextItem);

            }

        };

    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Title");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.callibrate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
