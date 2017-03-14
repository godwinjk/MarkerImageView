package com.godwin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by WiSilica on 20-02-2017 12:04.
 *
 * @Author : Godwin Joseph Kurinjikattu
 */

public final class MarkerImageView extends android.support.v7.widget.AppCompatImageView {
    private ArrayList<MarkerCoordinate> coordinates = new ArrayList<>();
    private Bitmap markerBitmap;
    private MarkerListener markerListener;

    public MarkerImageView(Context context) {
        super(context);
    }

    public MarkerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MarkerImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    public void load(Uri imageUri) {
        Picasso.with(getContext()).load(imageUri).into(this);
    }

    public void addMarker(float x, float y) {
        if (x < 0 || y < 0)
            throw new InvalidArgumentException("Invalid arguments");
        this.coordinates.add(new MarkerCoordinate(x, y));
    }

    public void setMarkerBitmap(Bitmap bitmap) {
        if (bitmap == null)
            throw new NullPointerException("bitmap should not be null");
        markerBitmap = bitmap;
    }

    public void setMarkerResource(@DrawableRes int resId) {
        markerBitmap = BitmapFactory.decodeResource(getResources(), resId);
        if (markerBitmap == null)
            throw new ResourceNotFoundException("Resource Id should referred to a valid ");
    }

    public void clearCoordinates() {
        coordinates.clear();
        invalidate();
    }

    public void removeCoordinate(MarkerCoordinate coordinate) {
        removeCoordinate(coordinate.x, coordinate.y);
    }

    public void removeCoordinate(float x, float y) {
        ListIterator<MarkerCoordinate> iterator = coordinates.listIterator();
        for (; iterator.hasNext(); ) {
            MarkerCoordinate coordinate = iterator.next();
            if (coordinate.x == x && coordinate.y == y) {
                iterator.remove();
            }
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            getTouchedBitmapPixelPosition(event);
//            coordinates.add(new MarkerCoordinate(event.getX(), event.getY()));
            invalidate();
        }
        return false;
    }

    private void getTouchedBitmapPixelPosition(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        float[] eventXY = new float[]{eventX, eventY};

        Matrix invertMatrix = new Matrix();
        getImageMatrix().invert(invertMatrix);

        invertMatrix.mapPoints(eventXY);
        float x = eventXY[0];
        float y = eventXY[1];

        Bitmap bitmap = getBitmap();

        //Limit x, y range within bitmap
        if (x < 0) {
            x = 0;
        } else if (x > bitmap.getWidth() - 1) {
            x = bitmap.getWidth() - 1;
        }

        if (y < 0) {
            y = 0;
        } else if (y > bitmap.getHeight() - 1) {
            y = bitmap.getHeight() - 1;
        }
        MarkerCoordinate coordinate = new MarkerCoordinate(x, y);
        coordinates.add(coordinate);
        if (markerListener != null) {
            markerListener.onMarkerCreated(coordinate);
        }
    }

    private Bitmap getBitmap() {
        Drawable imgDrawable = getDrawable();
        return ((BitmapDrawable) imgDrawable).getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = getBitmap();
        Bitmap marker = markerBitmap == null ? BitmapFactory.decodeResource(getResources(), R.drawable.marker) : markerBitmap;
        for (MarkerCoordinate coordinate : coordinates) {
            float[] eventXY = new float[]{coordinate.x, coordinate.y};

            Matrix invertMatrix = getImageMatrix();
//            getImageMatrix().invert(invertMatrix);

            invertMatrix.mapPoints(eventXY);

            canvas.drawBitmap(marker, eventXY[0], eventXY[1], null);
        }
    }

    public void setMarkerListener(MarkerListener markerListener) {
        this.markerListener = markerListener;
    }

    public ArrayList<MarkerCoordinate> getMarkerCoordinates() {
        return coordinates;
    }

    public void setMarkerCoordinates(ArrayList<MarkerCoordinate> coordinates) {
        this.coordinates = coordinates;
        invalidate();
    }
}
