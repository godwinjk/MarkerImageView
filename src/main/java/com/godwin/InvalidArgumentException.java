package com.godwin;

/**
 * Created by WiSilica on 20-02-2017 12:32.
 *
 * @Author : Godwin Joseph Kurinjikattu
 */

final class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super("MARKER IMAGE VIEW " + message);
    }
}
