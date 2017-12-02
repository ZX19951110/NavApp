package com.serviceslab.unipv.librarynavapp.classes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.serviceslab.unipv.librarynavapp.R;

/**
 * Created by mikim on 11/01/2017.
 */
/**
 * Extends great ImageView library by Dave Morrissey. See more:
 * https://github.com/davemorrissey/subsampling-scale-image-view.
 */
public class BlueDotView extends SubsamplingScaleImageView {
    private float radius = 1.0f;
    private PointF dotCenter = null;
    private Context context;
    private static String TAG = "BlueDotView";
    private PointF printerPoint = null;

    //This point is the one closest to the initial user position.
    private PointF initialPoint = null;

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setDotCenter(PointF dotCenter) {
        this.dotCenter = dotCenter;
    }

    public void setPrinterPoint(PointF printerPoint) {
        this.printerPoint = printerPoint;
    }

    public BlueDotView(Context context) {
        this(context, null);
    }

    public BlueDotView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        initialise();
    }

    private void initialise() {
        setWillNotDraw(false);
        setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_CENTER);
        Log.i(TAG, "initialized");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady()) {
            return;
        }

        /** original
        if (dotCenter != null) {
            PointF vPoint = sourceToViewCoord(dotCenter);
            float scaledRadius = getScale() * radius;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            //paint.setColor(getResources().getColor(R.color.ia_blue));
            paint.setColor(ContextCompat.getColor(context, R.color.ia_blue));
            canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius, paint);
            Log.i(TAG, "drawn");
        } **/

        //Miki's
        if (dotCenter != null && printerPoint != null) {
            PointF vPoint = sourceToViewCoord(dotCenter);
            PointF printer = sourceToViewCoord(printerPoint);

            float scaledRadius = getScale() * radius;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            //paint.setColor(getResources().getColor(R.color.ia_blue));
            paint.setColor(ContextCompat.getColor(context, R.color.ia_blue));
            canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius, paint);
            paint.setColor(ContextCompat.getColor(context, R.color.miki_printer));
            //canvas.drawCircle(printer.x, printer.y, scaledRadius, paint);
            canvas.drawRect(printer.x - scaledRadius, printer.y - scaledRadius, printer.x + scaledRadius, printer.y + scaledRadius, paint);
            //canvas.drawLine(printer.x, printer.y, vPoint.x, vPoint.y, paint);
            //Log.i(TAG, "drawn");
        }


    }

}
