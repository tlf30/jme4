/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme.scene.plugins.blender.textures;

import com.jme.math.ColorRGBA;
import com.jme.math.FastMath;
import com.jme.scene.plugins.blender.BlenderContext;
import com.jme.scene.plugins.blender.file.BlenderFileException;
import com.jme.scene.plugins.blender.file.DynamicArray;
import com.jme.scene.plugins.blender.file.Pointer;
import com.jme.scene.plugins.blender.file.Structure;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class constaining the colorband data.
 * 
 * @author Marcin Roguski (Kaelthas)
 */
public class ColorBand {
    private static final Logger LOGGER       = Logger.getLogger(ColorBand.class.getName());

    // interpolation types
    public static final int     IPO_LINEAR   = 0;
    public static final int     IPO_EASE     = 1;
    public static final int     IPO_BSPLINE  = 2;
    public static final int     IPO_CARDINAL = 3;
    public static final int     IPO_CONSTANT = 4;

    private int                 cursorsAmount, ipoType;
    /** The default amount of possible cursor positions. */
    private int                 resultSize   = 1001;
    private ColorBandData[]     data;

    /**
     * A constructor used to instantiate color band by hand instead of reading it from the blend file.
     * @param ipoType
     *            the interpolation type
     * @param colors
     *            the colorband colors
     * @param positions
     *            the positions for colors' cursors
     * @param resultSize
     *            the size of the result table
     */
    public ColorBand(int ipoType, List<ColorRGBA> colors, List<Integer> positions, int resultSize) {
        if (colors == null || colors.size() < 1) {
            throw new IllegalArgumentException("The amount of colorband's colors must be at least 1.");
        }
        if (ipoType < IPO_LINEAR || ipoType > IPO_CONSTANT) {
            throw new IllegalArgumentException("Unknown colorband interpolation type: " + ipoType);
        }
        if (positions == null || positions.size() != colors.size()) {
            throw new IllegalArgumentException("The size of positions and colors list should be equal!");
        }
        for (Integer position : positions) {
            if (position.intValue() < 0 || position.intValue() >= resultSize) {
                throw new IllegalArgumentException("Invalid position value: " + position + "! Should be from range: [0, " + resultSize + "]!");
            }
        }

        cursorsAmount = colors.size();
        this.ipoType = ipoType;
        this.resultSize = resultSize;
        data = new ColorBandData[cursorsAmount];
        for (int i = 0; i < cursorsAmount; ++i) {
            data[i] = new ColorBandData(colors.get(i), positions.get(i));
        }
    }

    /**
     * Constructor. Loads the data from the given structure.
     * @param tex
     * @param blenderContext
     */
    @SuppressWarnings("unchecked")
    public ColorBand(Structure tex, BlenderContext blenderContext) {
        int flag = ((Number) tex.getFieldValue("flag")).intValue();
        if ((flag & GeneratedTexture.TEX_COLORBAND) != 0) {
            Pointer pColorband = (Pointer) tex.getFieldValue("coba");
            try {
                Structure colorbandStructure = pColorband.fetchData().get(0);
                cursorsAmount = ((Number) colorbandStructure.getFieldValue("tot")).intValue();
                ipoType = ((Number) colorbandStructure.getFieldValue("ipotype")).intValue();
                data = new ColorBandData[cursorsAmount];
                DynamicArray<Structure> data = (DynamicArray<Structure>) colorbandStructure.getFieldValue("data");
                for (int i = 0; i < cursorsAmount; ++i) {
                    this.data[i] = new ColorBandData(data.get(i));
                }
            } catch (BlenderFileException e) {
                LOGGER.log(Level.WARNING, "Cannot fetch the colorband structure. The reason: {0}", e.getLocalizedMessage());
            }
        }
    }

    /**
     * This method determines if the colorband has any transparencies or is not
     * transparent at all.
     * 
     * @return <b>true</b> if the colorband has transparencies and <b>false</b>
     *         otherwise
     */
    public boolean hasTransparencies() {
        if (data != null) {
            for (ColorBandData colorBandData : data) {
                if (colorBandData.a < 1.0f) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method computes the values of the colorband.
     * 
     * @return an array of 1001 elements and each element is float[4] object
     *         containing rgba values
     */
    public float[][] computeValues() {
        float[][] result = null;
        if (data != null) {
            result = new float[resultSize][4];// resultSize - amount of possible cursor positions; 4 = [r, g, b, a]
            if (data.length == 1) {// special case; use only one color for all types of colorband interpolation
                for (int i = 0; i < result.length; ++i) {
                    result[i][0] = data[0].r;
                    result[i][1] = data[0].g;
                    result[i][2] = data[0].b;
                    result[i][3] = data[0].a;
                }
            } else {
                int currentCursor = 0;
                ColorBandData currentData = data[0];
                ColorBandData nextData = data[0];
                switch (ipoType) {
                    case ColorBand.IPO_LINEAR:
                        float rDiff = 0,
                        gDiff = 0,
                        bDiff = 0,
                        aDiff = 0,
                        posDiff;
                        for (int i = 0; i < result.length; ++i) {
                            posDiff = i - currentData.pos;
                            result[i][0] = currentData.r + rDiff * posDiff;
                            result[i][1] = currentData.g + gDiff * posDiff;
                            result[i][2] = currentData.b + bDiff * posDiff;
                            result[i][3] = currentData.a + aDiff * posDiff;
                            if (nextData.pos == i) {
                                currentData = data[currentCursor++];
                                if (currentCursor < data.length) {
                                    nextData = data[currentCursor];
                                    // calculate differences
                                    int d = nextData.pos - currentData.pos;
                                    rDiff = (nextData.r - currentData.r) / d;
                                    gDiff = (nextData.g - currentData.g) / d;
                                    bDiff = (nextData.b - currentData.b) / d;
                                    aDiff = (nextData.a - currentData.a) / d;
                                } else {
                                    rDiff = gDiff = bDiff = aDiff = 0;
                                }
                            }
                        }
                        break;
                    case ColorBand.IPO_BSPLINE:
                    case ColorBand.IPO_CARDINAL:
                        Map<Integer, ColorBandData> cbDataMap = new TreeMap<Integer, ColorBandData>();
                        for (int i = 0; i < data.length; ++i) {
                            cbDataMap.put(Integer.valueOf(i), data[i]);
                        }

                        if (data[0].pos == 0) {
                            cbDataMap.put(Integer.valueOf(-1), data[0]);
                        } else {
                            ColorBandData cbData = new ColorBandData(data[0]);
                            cbData.pos = 0;
                            cbDataMap.put(Integer.valueOf(-1), cbData);
                            cbDataMap.put(Integer.valueOf(-2), cbData);
                        }

                        if (data[data.length - 1].pos == 1000) {
                            cbDataMap.put(Integer.valueOf(data.length), data[data.length - 1]);
                        } else {
                            ColorBandData cbData = new ColorBandData(data[data.length - 1]);
                            cbData.pos = 1000;
                            cbDataMap.put(Integer.valueOf(data.length), cbData);
                            cbDataMap.put(Integer.valueOf(data.length + 1), cbData);
                        }

                        float[] ipoFactors = new float[4];
                        float f;

                        ColorBandData data0 = this.getColorbandData(currentCursor - 2, cbDataMap);
                        ColorBandData data1 = this.getColorbandData(currentCursor - 1, cbDataMap);
                        ColorBandData data2 = this.getColorbandData(currentCursor, cbDataMap);
                        ColorBandData data3 = this.getColorbandData(currentCursor + 1, cbDataMap);

                        for (int i = 0; i < result.length; ++i) {
                            if (data2.pos != data1.pos) {
                                f = (i - data2.pos) / (float) (data1.pos - data2.pos);
                                f = FastMath.clamp(f, 0.0f, 1.0f);
                            } else {
                                f = 0.0f;
                            }
                            this.getIpoData(f, ipoFactors);
                            result[i][0] = ipoFactors[3] * data0.r + ipoFactors[2] * data1.r + ipoFactors[1] * data2.r + ipoFactors[0] * data3.r;
                            result[i][1] = ipoFactors[3] * data0.g + ipoFactors[2] * data1.g + ipoFactors[1] * data2.g + ipoFactors[0] * data3.g;
                            result[i][2] = ipoFactors[3] * data0.b + ipoFactors[2] * data1.b + ipoFactors[1] * data2.b + ipoFactors[0] * data3.b;
                            result[i][3] = ipoFactors[3] * data0.a + ipoFactors[2] * data1.a + ipoFactors[1] * data2.a + ipoFactors[0] * data3.a;
                            result[i][0] = FastMath.clamp(result[i][0], 0.0f, 1.0f);
                            result[i][1] = FastMath.clamp(result[i][1], 0.0f, 1.0f);
                            result[i][2] = FastMath.clamp(result[i][2], 0.0f, 1.0f);
                            result[i][3] = FastMath.clamp(result[i][3], 0.0f, 1.0f);

                            if (nextData.pos == i) {
                                ++currentCursor;
                                data0 = cbDataMap.get(currentCursor - 2);
                                data1 = cbDataMap.get(currentCursor - 1);
                                data2 = cbDataMap.get(currentCursor);
                                data3 = cbDataMap.get(currentCursor + 1);
                            }
                        }
                        break;
                    case ColorBand.IPO_EASE:
                        float d,
                        a,
                        b,
                        d2;
                        for (int i = 0; i < result.length; ++i) {
                            if (nextData.pos != currentData.pos) {
                                d = (i - currentData.pos) / (float) (nextData.pos - currentData.pos);
                                d2 = d * d;
                                a = 3.0f * d2 - 2.0f * d * d2;
                                b = 1.0f - a;
                            } else {
                                d = a = 0.0f;
                                b = 1.0f;
                            }

                            result[i][0] = b * currentData.r + a * nextData.r;
                            result[i][1] = b * currentData.g + a * nextData.g;
                            result[i][2] = b * currentData.b + a * nextData.b;
                            result[i][3] = b * currentData.a + a * nextData.a;
                            if (nextData.pos == i) {
                                currentData = data[currentCursor++];
                                if (currentCursor < data.length) {
                                    nextData = data[currentCursor];
                                }
                            }
                        }
                        break;
                    case ColorBand.IPO_CONSTANT:
                        for (int i = 0; i < result.length; ++i) {
                            result[i][0] = currentData.r;
                            result[i][1] = currentData.g;
                            result[i][2] = currentData.b;
                            result[i][3] = currentData.a;
                            if (nextData.pos == i) {
                                currentData = data[currentCursor++];
                                if (currentCursor < data.length) {
                                    nextData = data[currentCursor];
                                }
                            }
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unknown interpolation type: " + ipoType);
                }
            }
        }
        return result;
    }

    private ColorBandData getColorbandData(int index, Map<Integer, ColorBandData> cbDataMap) {
        ColorBandData result = cbDataMap.get(index);
        if (result == null) {
            result = new ColorBandData();
        }
        return result;
    }

    /**
     * This method returns the data for either B-spline of Cardinal
     * interpolation.
     * 
     * @param d
     *            distance factor for the current intensity
     * @param ipoFactors
     *            table to store the results (size of the table must be at least
     *            4)
     */
    private void getIpoData(float d, float[] ipoFactors) {
        float d2 = d * d;
        float d3 = d2 * d;
        if (ipoType == ColorBand.IPO_BSPLINE) {
            ipoFactors[0] = -0.71f * d3 + 1.42f * d2 - 0.71f * d;
            ipoFactors[1] = 1.29f * d3 - 2.29f * d2 + 1.0f;
            ipoFactors[2] = -1.29f * d3 + 1.58f * d2 + 0.71f * d;
            ipoFactors[3] = 0.71f * d3 - 0.71f * d2;
        } else if (ipoType == ColorBand.IPO_CARDINAL) {
            ipoFactors[0] = -0.16666666f * d3 + 0.5f * d2 - 0.5f * d + 0.16666666f;
            ipoFactors[1] = 0.5f * d3 - d2 + 0.6666666f;
            ipoFactors[2] = -0.5f * d3 + 0.5f * d2 + 0.5f * d + 0.16666666f;
            ipoFactors[3] = 0.16666666f * d3;
        } else {
            throw new IllegalStateException("Cannot get interpolation data for other colorband types than B-spline and Cardinal!");
        }
    }

    /**
     * Class to store the single colorband cursor data.
     * 
     * @author Marcin Roguski (Kaelthas)
     */
    private static class ColorBandData {
        public final float r, g, b, a;
        public int         pos;

        public ColorBandData() {
            r = g = b = 0;
            a = 1;
        }

        /**
         * Constructor that stores the color and position of the cursor.
         * @param color
         *            the cursor's color
         * @param pos
         *            the cursor's position
         */
        public ColorBandData(ColorRGBA color, int pos) {
            r = color.r;
            g = color.g;
            b = color.b;
            a = color.a;
            this.pos = pos;
        }

        /**
         * Copy constructor.
         */
        private ColorBandData(ColorBandData data) {
            r = data.r;
            g = data.g;
            b = data.b;
            a = data.a;
            pos = data.pos;
        }

        /**
         * Constructor. Loads the data from the given structure.
         * 
         * @param cbdataStructure
         *            the structure containing the CBData object
         */
        public ColorBandData(Structure cbdataStructure) {
            r = ((Number) cbdataStructure.getFieldValue("r")).floatValue();
            g = ((Number) cbdataStructure.getFieldValue("g")).floatValue();
            b = ((Number) cbdataStructure.getFieldValue("b")).floatValue();
            a = ((Number) cbdataStructure.getFieldValue("a")).floatValue();
            pos = (int) (((Number) cbdataStructure.getFieldValue("pos")).floatValue() * 1000.0f);
        }

        @Override
        public String toString() {
            return "P: " + pos + " [" + r + ", " + g + ", " + b + ", " + a + "]";
        }
    }
}
