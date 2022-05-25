package com.codegraphy;

import static com.codegraphy.MainActivity.mCodeView;

import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.VisibleForTesting;

import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.Ink;

import java.util.ArrayList;
import java.util.List;
public class StrokeHandler {


    //refference to the text view
    private EditTextView editTextView;

    /** Interface to register to be notified of changes in the recognized content. */
    public interface ContentChangedListener {

        /** This method is called when the recognized content changes. */
        void onContentChanged();
    }

    /** Interface to register to be notified of changes in the status. */
    public interface StatusChangedListener {

        /** This method is called when the recognized content changes. */
        void onStatusChanged();
    }
    @VisibleForTesting static final long CONVERSION_TIMEOUT_MS = 1000;
    private static final String TAG = "parameters";
    // This is a constant that is used as a message identifier to trigger the timeout.
    private static final int TIMEOUT_TRIGGER = 1;

    // For handling recognition and model downloading.
    private RecognizerTask recognitionTask = null;

    // Managing the recognition queue.
    private final List<RecognizerTask.RecognizedInk> content = new ArrayList<>();

    // Managing ink currently drawn.
    private Ink.Stroke.Builder strokeBuilder = Ink.Stroke.builder();
    private Ink.Builder inkBuilder = Ink.builder();

    @VisibleForTesting
    ModelManager modelManager = new ModelManager();

    private boolean stateChangedSinceLastRequest = false;
    private ContentChangedListener contentChangedListener = null;
    private StatusChangedListener statusChangedListener = null;

    private boolean triggerRecognitionAfterInput = true;
    private boolean clearCurrentInkAfterRecognition = true;
    private String status = "";

    public void setTriggerRecognitionAfterInput(boolean shouldTrigger) {
        triggerRecognitionAfterInput = shouldTrigger;
    }

    public void setClearCurrentInkAfterRecognition(boolean shouldClear) {
        clearCurrentInkAfterRecognition = shouldClear;
    }

    public void setEditTextView(EditTextView editTextView) {
        this.editTextView = editTextView;
    }

    public boolean addNewTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();
        long t = System.currentTimeMillis();

        // A new event happened -> clear all pending timeout messages.
       //uiHandler.removeMessages(TIMEOUT_TRIGGER);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                strokeBuilder = Ink.Stroke.builder();
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_MOVE:
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_UP:
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                inkBuilder.addStroke(strokeBuilder.build());
                strokeBuilder = null;
                break;
            default:
                // Indicate touch event wasn't handled.
                return false;
        }
        return true;
    }
    public Ink getInkObject(){
        return  inkBuilder.build();
    }

    public void recognize() {
        //get recognizer
        DigitalInkRecognizer recognizer =  modelManager.getRecognizer();
        //get ink object
        Ink ink = getInkObject();

        //Log.i(TAG, ink.getStrokes().t)
        //process the Ink object
        recognizer.recognize(ink)
                .addOnSuccessListener(
                        // `result` contains the recognizer's answers as a RecognitionResult.
                        // Logs the text from the top candidate.
                        result -> {
                            String recognitionResult = result.getCandidates().get(0).getText();
                            Log.i(TAG, recognitionResult);
                            editTextView.append(recognitionResult + "\n");

                        })
                .addOnFailureListener(
                        e -> Log.e(TAG, "Error during recognition: " + e));
    }


    public void reset() {
        inkBuilder =  Ink.builder();
        strokeBuilder = Ink.Stroke.builder();
    }

}
