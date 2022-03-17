package com.codegraphy;

import android.util.Log;

import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;

public class ModelManager {

    private final String TAG = "parameters";
    private DigitalInkRecognitionModel model;
    private DigitalInkRecognizer recognizer;
    final RemoteModelManager remoteModelManager = RemoteModelManager.getInstance();

    public void setModelParametersAndRecognizer(){
        //set model identifier from language tag
        String languageTag = "en-US";
        DigitalInkRecognitionModelIdentifier modelIdentifier = null;

        try {
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag(languageTag);
            Log.e(TAG, "Successfully created modelIdentifier for language tag '" + languageTag + "'");
        } catch (MlKitException e) {
            Log.e(TAG, "Failed to parse language '" + languageTag + "'");
            return;
        }
        if (modelIdentifier == null) {
            Log.e(TAG, "No model for language:" + languageTag + "'");

        }

        //create model object
        model = DigitalInkRecognitionModel.builder(modelIdentifier).build();

        //set recognizer
        recognizer =  DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model).build());

    }

    public void downloadModel(){

        if(model == null){
            Log.e(TAG, "Model object not set correctly");
            return;
        }
        //download model
        remoteModelManager
                .download(model, new DownloadConditions.Builder().build())
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Model downloaded"))
                .addOnFailureListener(
                        e -> Log.e(TAG, "Error while downloading a model: " + e));

    }

    public DigitalInkRecognizer getRecognizer(){
        return this.recognizer;
    }

    public DigitalInkRecognitionModel getModel() {
        return model;
    }

    public RemoteModelManager getRemoteModelManager() {
        return remoteModelManager;
    }
}
