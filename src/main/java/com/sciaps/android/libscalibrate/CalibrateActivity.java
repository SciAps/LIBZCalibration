package com.sciaps.android.libscalibrate;

import android.app.ActionBar;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.reflect.TypeToken;
import com.sciaps.common.serialize.JsonSerializerFactory;
import com.sciaps.common.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CalibrateActivity extends ActionBarActivity  implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callibrate);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);


        Class type = new TypeToken<ArrayList<CalibrationAlloy>>(){}.getClass();


        Serializer<CalibrationAlloy[]> serializer = JsonSerializerFactory.getSerializer(CalibrationAlloy[].class);



//        DialogFragment d_Fragment = new CustomeDialogFragment("Calibrate", "Message");
//        d_Fragment.setCancelable(false);
//        d_Fragment.show(getFragmentManager(), "Processing");
        CalibrationAlloy[] calibrations = null;

        AssetManager assetManager = getAssets();
        try {

            InputStream in = assetManager.open("calibrationJson.json");

            //FileInputStream fin = new FileInputStream(regiontablefile);
            calibrations = serializer.deserialize(in);
            in.close();


            Log.w("ActionBarActivity", calibrations.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.w("ActionBarActivity",e);
        }

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),calibrations);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
   //     Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_LONG).show();
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Title");
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                //mTitle = getString(R.string.title_section1);
                break;
            case 2:
               // mTitle = getString(R.string.title_section2);
                break;
            case 3:
                //mTitle = getString(R.string.title_section3);
                break;
        }
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
