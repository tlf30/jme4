/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 */
package com.jme.opencl.lwjgl.info;

import com.jme.lwjgl3.utils.APIBuffer;
import static com.jme.lwjgl3.utils.APIUtil.apiBuffer;
import static com.jme.opencl.lwjgl.info.CLUtil.checkCLError;
import org.lwjgl.PointerBuffer;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.Pointer.*;

/**
 * Base class for OpenCL object information queries.
 * <p/>
 * All methods require the object being queried (a pointer value), a second
 * integer argument and the integer parameter name.
 *
 * @see Info
 */
abstract class InfoQueryInt {

    protected abstract int get(long pointer, int arg, int param_name, long param_value_size, long param_value, long param_value_size_ret);

    InfoQueryInt() {
    }

    /**
     * Returns the integer value for the specified {@code param_name}, converted
     * to a boolean.
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     *
     * @return the parameter's boolean value
     */
    boolean getBoolean(long object, int arg, int param_name) {
        return getInt(object, arg, param_name) != 0;
    }

    /**
     * Returns the integer value for the specified {@code param_name}.
     * <p/>
     * For integer parameters that may be 32 or 64 bits (e.g. {@code size_t}),
     * {@link #getPointer} should be used instead.
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     *
     * @return the parameter's int value
     */
    int getInt(long object, int arg, int param_name) {
        APIBuffer __buffer = apiBuffer();
        int errcode = get(object, arg, param_name, 4L, __buffer.address(), NULL);
        if (DEBUG) {
            checkCLError(errcode);
        }
        return __buffer.intValue(0);
    }

    /**
     * Returns the long value for the specified {@code param_name}.
     * <p/>
     * For integer parameters that may be 32 or 64 bits (e.g. {@code size_t}),
     * {@link #getPointer} should be used instead.
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     *
     * @return the parameter's long value
     */
    long getLong(long object, int arg, int param_name) {
        APIBuffer __buffer = apiBuffer();
        int errcode = get(object, arg, param_name, 8L, __buffer.address(), NULL);
        if (DEBUG) {
            checkCLError(errcode);
        }
        return __buffer.longValue(0);
    }

    /**
     * Returns the pointer value for the specified {@code param_name}.
     * <p/>
     * This method should also be used for integer parameters that may be 32 or
     * 64 bits (e.g. {@code size_t}).
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     *
     * @return the parameter's pointer value
     */
    long getPointer(long object, int arg, int param_name) {
        APIBuffer __buffer = apiBuffer();
        int errcode = get(object, arg, param_name, POINTER_SIZE, __buffer.address(), NULL);
        if (DEBUG) {
            checkCLError(errcode);
        }
        return __buffer.pointerValue(0);
    }

    /**
     * Writes the pointer list for the specified {@code param_name} into
     * {@code target}.
     * <p/>
     * This method should also be used for integer parameters that may be 32 or
     * 64 bits (e.g. {@code size_t}).
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     * @param target the buffer in which to put the returned pointer list
     *
     * @return how many pointers were actually returned
     */
    int getPointers(long object, int arg, int param_name, PointerBuffer target) {
        APIBuffer __buffer = apiBuffer();
        int errcode = get(object, arg, param_name, target.remaining() * POINTER_SIZE, memAddress(target), __buffer.address());
        if (DEBUG) {
            checkCLError(errcode);
        }
        return (int) (__buffer.pointerValue(0) >> POINTER_SHIFT);
    }

    /**
     * Returns the string value for the specified {@code param_name}. The raw
     * bytes returned are assumed to be ASCII encoded.
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     *
     * @return the parameter's string value
     */
    String getStringASCII(long object, int arg, int param_name) {
        APIBuffer __buffer = apiBuffer();
        int bytes = getString(object, arg, param_name, __buffer);
        return __buffer.stringValueASCII(0, bytes);
    }

    /**
     * Returns the string value for the specified {@code param_name}. The raw
     * bytes returned are assumed to be ASCII encoded and have length equal to {@code
     * param_value_size}.
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     * @param param_value_size the explicit string length
     *
     * @return the parameter's string value
     */
    String getStringASCII(long object, int arg, int param_name, int param_value_size) {
        APIBuffer __buffer = apiBuffer();
        int errcode = get(object, arg, param_name, param_value_size, __buffer.address(), NULL);
        if (DEBUG) {
            checkCLError(errcode);
        }
        return __buffer.stringValueASCII(0, param_value_size);
    }

    /**
     * Returns the string value for the specified {@code param_name}. The raw
     * bytes returned are assumed to be UTF-8 encoded.
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     *
     * @return the parameter's string value
     */
    String getStringUTF8(long object, int arg, int param_name) {
        APIBuffer __buffer = apiBuffer();
        int bytes = getString(object, arg, param_name, __buffer);
        return __buffer.stringValueUTF8(0, bytes);
    }

    /**
     * Returns the string value for the specified {@code param_name}. The raw
     * bytes returned are assumed to be UTF-8 encoded and have length equal to {@code
     * param_value_size}.
     *
     * @param object the object to query
     * @param arg an integer argument
     * @param param_name the parameter to query
     * @param param_value_size the explicit string length
     *
     * @return the parameter's string value
     */
    String getStringUTF8(long object, int arg, int param_name, int param_value_size) {
        APIBuffer __buffer = apiBuffer();
        int errcode = get(object, arg, param_name, param_value_size, __buffer.address(), NULL);
        if (DEBUG) {
            checkCLError(errcode);
        }
        return __buffer.stringValueUTF8(0, param_value_size);
    }

    private int getString(long object, int arg, int param_name, APIBuffer __buffer) {
        // Get string length
        int errcode = get(object, arg, param_name, 0, NULL, __buffer.address());
        if (DEBUG) {
            checkCLError(errcode);
        }

        int bytes = (int) __buffer.pointerValue(0);
        __buffer.bufferParam(bytes + POINTER_SIZE);

        // Get string
        errcode = get(object, arg, param_name, bytes, __buffer.address(), NULL);
        if (DEBUG) {
            checkCLError(errcode);
        }

        return bytes - 1; // all OpenCL char[] parameters are null-terminated
    }

}
