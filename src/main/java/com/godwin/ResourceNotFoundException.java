package com.godwin;

import android.content.res.Resources;

/**
 * Created by WiSilica on 20-02-2017 12:28.
 *
 * @Author : Godwin Joseph Kurinjikattu
 */

final class ResourceNotFoundException extends Resources.NotFoundException {
    public ResourceNotFoundException(String message) {
        super("MARKER IMAGE VIEW " + message);
    }
}
