package com.codegraphy;

import android.view.View;

import com.google.mlkit.vision.digitalink.Ink;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class WritingView extends View //implements
{
    private static final int STROKE_WIDTH_DP = 3;

    private final String TAG = "parameters";
    private final Paint recognizedStrokePaint;

    private final Paint currentStrokePaint;
    private final Paint canvasPaint;

    private final Path currentStroke;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private StrokeHandler strokeHandler;


    public WritingView(Context context) {
        this(context, null);
    }

    //create paint object, create canvas object,override onDraw with Canvas.

    public WritingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        currentStrokePaint = new Paint();
        currentStrokePaint.setColor(0xFF0000FF); // blue
        currentStrokePaint.setAntiAlias(true);

        // Set stroke width based on display density.
        currentStrokePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STROKE_WIDTH_DP, getResources().getDisplayMetrics()));
        currentStrokePaint.setStyle(Paint.Style.STROKE);
        currentStrokePaint.setStrokeJoin(Paint.Join.ROUND);
        currentStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        recognizedStrokePaint = new Paint(currentStrokePaint);
        recognizedStrokePaint.setColor(0xFFFFCCFF); // Green

        currentStroke = new Path();
        canvasPaint = new Paint(Paint.DITHER_FLAG); // create canvas using paint object

    }

    void setStrokeHandler(StrokeHandler strokeHandler) {
        this.strokeHandler = strokeHandler;
    }

    //Re-calculates the dimensions of the view
    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        Log.i(TAG, "onSizeChanged");
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        invalidate();
    }

    //handles drawing
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(currentStroke, currentStrokePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                currentStroke.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                currentStroke.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                currentStroke.lineTo(x, y);
                drawCanvas.drawPath(currentStroke, currentStrokePaint);
                currentStroke.reset();
                break;
            default:
                break;
        }
        strokeHandler.addNewTouchEvent(event);
        invalidate();
        return true;
    }

    public void clear() {
        currentStroke.reset();
        onSizeChanged(
                canvasBitmap.getWidth(),
                canvasBitmap.getHeight(),
                canvasBitmap.getWidth(),
                canvasBitmap.getHeight());
    }
}
