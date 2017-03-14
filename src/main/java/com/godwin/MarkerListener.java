package com.godwin;

/**
 * Created by WiSilica on 14-03-2017 13:52.
 *
 * @author : Godwin Joseph Kurinjikattu
 */

public interface MarkerListener {
    void onMarkerCreated(MarkerCoordinate coordinate);

    void onMarkerRemoved(MarkerCoordinate coordinate);
}
