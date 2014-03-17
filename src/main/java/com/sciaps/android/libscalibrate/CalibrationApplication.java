package com.sciaps.android.libscalibrate;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by MonkeyFish on 3/17/14.
 */
public class CalibrationApplication extends Application {

    private Injector mInjector;

    public Injector getInjector() {
        if (mInjector == null) {
            mInjector = Guice.createInjector(
                    new ApplicationModule());
        }
        return mInjector;
    }

    public static CalibrationApplication getInstance(Activity activity) {
        return (CalibrationApplication) activity.getApplication();
    }


    @Override
    public void onCreate() {
        super.onCreate();




    }


    private void copyJsonFromAssets(InputStream in, FileOutputStream out) throws IOException {


        try {


            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: ", e);
        }

    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public class ApplicationModule extends AbstractModule {

        @Override
        protected void configure() {

        }


        @Provides
        @Named("analysisroot")
        File provideAnalysisRoot() {
            File analysisRoot = new File(Environment.getExternalStorageDirectory(), "LIBZAnalysis");
            if (!analysisRoot.exists()) {
                analysisRoot.mkdirs();
            }
            return analysisRoot;
        }

        @Provides
        @Named("alloy_calibration")
        File provideCalibrationFile(@Named("analysisroot") File analysisRoot) {

            File json = new File(analysisRoot, "calibrationJson.json");

            if (!json.exists()) {
                json = new File(analysisRoot, "/calibrationJson.json");
                try {
                    json.createNewFile();
                } catch (IOException e) {
                    Log.e("IOException", "exception in createNewFile() method", e);
                }
                //we have to bind the new file with a FileOutputStream
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(json);
                } catch (FileNotFoundException e) {
                    Log.e("FileNotFoundException", "can't create FileOutputStream", e);
                }


                try {
                    copyJsonFromAssets(getAssets().open("calibrationJson.json"), fileos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return json;
        }

    }

}
