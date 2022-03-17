package com.codegraphy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.net.NetworkInfo;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

//import com.codegraphy.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.Tasks;


public class MainActivity extends AppCompatActivity {

    @VisibleForTesting
    final StrokeHandler strokeHandler = new StrokeHandler();
    private final String TAG = "parameters";
    private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d(TAG, "------------------started MainActivity . . .-------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WritingView writingView = findViewById(R.id.writing_view);
        EditTextView editTextView = findViewById(R.id.edit_text_view);
        writingView.setStrokeHandler(strokeHandler);
        strokeHandler.setEditTextView(editTextView);
        //editTextView.setStrokeHandler(strokeHandler);

        //set model parameters and recognizer
        strokeHandler.modelManager.setModelParametersAndRecognizer();


        //download model
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTimeRunFlag", false)) {

            //download the model , once, if has internet
            if (!isNetwork(MainActivity.this)) {
                alert("Warning!", "An Internet connection is needed for a one time setup, please check you connection and restart the application");
            }
            strokeHandler.modelManager.downloadModel();
            Log.d(TAG, "------------------ ran one time-------------------");
            SharedPreferences.Editor editor = prefs.edit();
            if (isNetwork(MainActivity.this)) {
                editor.putBoolean("firstTimeRunFlag", true);
                Log.d(TAG, "------------------ has internet , flag set-------------------");
            }
            editor.commit();
        }

        //check if downloaded
        strokeHandler.modelManager.getRemoteModelManager()
                .isModelDownloaded(strokeHandler.modelManager.getModel())
                .onSuccessTask(result -> {
                    if (!result) {
                        Log.i(TAG, " en-us Model not downloaded yet");
                        return Tasks.forResult(null);
                    }
                    Log.i(TAG, "en-us Model downloaded successfully!!");
                    return Tasks.forResult(null);
                });


    }

    //check for network
    public static boolean isNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //alert box function - MainActivity
    private void alert(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();

    }

    public void recognizeInk(View view) {
        strokeHandler.recognize();
    }

    public void clearCurrentInkClick(View view) {
        strokeHandler.reset();
        WritingView writingView = findViewById(R.id.writing_view);
        writingView.clear();
    }
}