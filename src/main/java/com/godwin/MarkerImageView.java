package com.godwin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by WiSilica on 20-02-2017 12:04.
 *
 * @Author : Godwin Joseph Kurinjikattu
 */

public final class MarkerImageView extends ImageView {
    private ArrayList<MarkerCoordinate> coordinates = new ArrayList<>();
    private Bitmap markerBitmap;

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
        super(context, attrs, defStyleAttr, defStyleRes);
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
        markerBitmap = BitmapFactory.decodeResource(getResources(),resId);
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
            coordinates.add(new MarkerCoordinate(event.getX(), event.getY()));
            invalidate();
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap marker = markerBitmap == null ? BitmapFactory.decodeResource(getResources(), R.drawable.marker) : markerBitmap;
        for (MarkerCoordinate coordinate : coordinates) {
            canvas.drawBitmap(marker, coordinate.x, coordinate.y, null);
        }
    }
}
