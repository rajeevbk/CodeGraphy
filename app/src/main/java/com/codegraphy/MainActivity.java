package com.codegraphy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

//import com.codegraphy.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.Tasks;

import java.io.File;
import java.io.FileWriter;

import br.tiagohm.codeview.CodeView;
import br.tiagohm.codeview.Language;
import br.tiagohm.codeview.Theme;

public class MainActivity extends AppCompatActivity implements CodeView.OnHighlightListener {

    @VisibleForTesting
    final StrokeHandler strokeHandler = new StrokeHandler();
    private final String TAG = "parameters";
    private ProgressDialog mProgressDialog;
    private int themePos = 0;
    public static CodeView mCodeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WritingView writingView = findViewById(R.id.writing_view);
        EditTextView editTextView = findViewById(R.id.edit_text_view);
        mCodeView =  findViewById(R.id.codeView);
        writingView.setStrokeHandler(strokeHandler);
        strokeHandler.setEditTextView(editTextView);


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
        strokeHandler.getEditTextView().getText().clear();
    }

    @Override
    public void onStartCodeHighlight() {
        mProgressDialog = ProgressDialog.show(this, null, "Carregando...", true);

    }

    @Override
    public void onFinishCodeHighlight() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        Toast.makeText(this, "line count: " + mCodeView.getLineCount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLanguageDetected(Language language, int relevance) {
        Toast.makeText(this, "language: " + language + " relevance: " + relevance, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFontSizeChanged(int sizeInPx) {
        Log.d("TAG", "font-size: " + sizeInPx + "px");
    }

    @Override
    public void onLineClicked(int lineNumber, String content) {
        Toast.makeText(this, "line: " + lineNumber + " python " + content, Toast.LENGTH_SHORT).show();
    }

    public void onClickSpace_1(View view) {
        String code = strokeHandler.getEditTextView().getText().toString();
        String prev = mCodeView.getCode();

        String PYTHON_CODE = prev + code;

        mCodeView.setOnHighlightListener(this)
                .setTheme(Theme.ARDUINO_LIGHT)
                .setCode(PYTHON_CODE)
                .setLanguage(Language.PYTHON)
                .setWrapLine(true)
                .setFontSize(14)
                .setZoomEnabled(true)
                .setShowLineNumber(true)
                .setStartLineNumber(1)
                .apply();
    }
    public void onClickSpace_2(View view) {
        String code = " " + strokeHandler.getEditTextView().getText().toString();
        String prev = mCodeView.getCode();
        String PYTHON_CODE = prev +  code ;
        mCodeView.setOnHighlightListener(this)
                .setTheme(Theme.ARDUINO_LIGHT)
                .setCode(PYTHON_CODE)
                .setLanguage(Language.PYTHON)
                .setWrapLine(true)
                .setFontSize(14)
                .setZoomEnabled(true)
                .setShowLineNumber(true)
                .setStartLineNumber(1)
                .apply();

    }
    public void onClickSpace_3(View view) {
        String code = "  " + strokeHandler.getEditTextView().getText().toString();
        String prev = mCodeView.getCode();
        String PYTHON_CODE = prev +  code ;
        mCodeView.setOnHighlightListener(this)
                .setTheme(Theme.ARDUINO_LIGHT)
                .setCode(PYTHON_CODE)
                .setLanguage(Language.PYTHON)
                .setWrapLine(true)
                .setFontSize(14)
                .setZoomEnabled(true)
                .setShowLineNumber(true)
                .setStartLineNumber(1)
                .apply();
    }
}