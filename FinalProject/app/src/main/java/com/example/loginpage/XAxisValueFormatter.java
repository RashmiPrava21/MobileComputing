package com.example.loginpage;

import com.github.mikephil.charting.utils.ViewPortHandler;

public interface XAxisValueFormatter {

    /**
     * Returns the customized label that is drawn on the x-axis.
     * For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param original        the original x-axis label to be drawn
     * @param index           the x-index that is currently being drawn
     * @param viewPortHandler provides information about the current chart state (scale, translation, ...)
     * @return
     */
    String getXValue(String original, int index, ViewPortHandler viewPortHandler);
}