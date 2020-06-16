package com.bicifiapp.ui.fragments.statistics

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

class ChartDecimalFormatter : ValueFormatter {
    private val mFormat: DecimalFormat = DecimalFormat("#")

    override fun getFormattedValue(
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String {
        return mFormat.format(value)
    }
}
