package com.bicifiapp.ui.fragments.statistics

import android.view.View
import androidx.core.content.ContextCompat
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentStatisticsBinding
import com.bicifiapp.extensions.LAST_EMOTIONAL_STATE
import com.bicifiapp.extensions.empty
import com.bicifiapp.extensions.getSharedPreferences
import com.bicifiapp.extensions.userId
import com.bicifiapp.statistics.Statistics
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.statistics.StatisticsViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import org.koin.android.ext.android.inject

class StatisticsFragment : BaseFragment(R.layout.fragment_statistics) {
    private val statisticsViewModel by inject<StatisticsViewModel>()
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialogLoading: DialogLoading

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    override fun initView(view: View) {
        _binding = FragmentStatisticsBinding.bind(view)
        initLiveData()
        loadDataUI()
    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    private fun initLiveData() {
        liveDataObserve(
            statisticsViewModel.resultStatisticTestLiveData,
            ::onGetCurrentUserLevelStateChange
        )
    }

    private fun loadDataUI() {
        val userId = userId()
        statisticsViewModel.getStatisticsTestByUser(userId)
    }

    private fun onGetCurrentUserLevelStateChange(state: State?) =
        when (state) {
            is State.Failed -> {
                hideProgress()
                //handleFailure(state.failure)
            }
            State.Loading -> showProgress()
            State.Empty -> hideProgress()
            is State.Success -> {
                loadEmotionalState()
                loadCharts(state.responseTo())
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun loadCharts(statistics: Statistics) {
        setLastLevel()

        val dataFilter: MutableList<BarEntry> = mutableListOf()
        val dataFilterEmotionalQuestion: MutableList<BarEntry> = mutableListOf()

        val dateFormattedQuestions = statistics.statisticQuestions.map {
            getDateTimeFormat(it.date)
        }

        statistics.statisticQuestions.forEachIndexed { index, item ->
            val level = item.level?.toFloat() ?: 0F
            dataFilter.add(BarEntry(level, index))
        }

        statistics.statisticQuestions.forEachIndexed { index, item ->
            val level = item.levelEmotional.toFloat()
            dataFilterEmotionalQuestion.add(BarEntry(level, index))
        }

        val barDataSetQuestion = BarDataSet(
            dataFilter,
            String.empty()
        ).apply {
            valueTextSize = BAR_CHART_TEXT_SIZE
            color = ContextCompat.getColor(requireActivity(), R.color.colorAccent)
        }

        val barDataSetQuestionEmotional = BarDataSet(
            dataFilterEmotionalQuestion,
            String.empty()
        ).apply {
            valueTextSize = BAR_CHART_TEXT_SIZE
            color = ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        }

        if (dateFormattedQuestions.isNotEmpty()) {
            configBarChartQuestions(
                dateFormattedQuestions,
                barDataSetQuestion,
                barDataSetQuestionEmotional
            )
            binding.barChart.invalidate()
        }

        val dateFormattedEmotional = statistics.statisticEmotional.map {
            getDateTimeFormat(it.date)
        }

        val dataFilterEmotional: MutableList<BarEntry> = mutableListOf()

        statistics.statisticEmotional.forEachIndexed { index, item ->
            val level = item.levelEmotional.toFloat()
            dataFilterEmotional.add(BarEntry(level, index))
        }

        val barDataSetEmotional = BarDataSet(
            dataFilterEmotional,
            String.empty()
        ).apply {
            valueTextSize = BAR_CHART_TEXT_SIZE
            color = ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        }

        if (dateFormattedEmotional.isNotEmpty()) {
            configBarChartEmotional(dateFormattedEmotional, barDataSetEmotional)
            binding.barChartEmotion.invalidate()
        }
    }

    private fun loadEmotionalState() {
        getSharedPreferences()?.also {
            when (it.getString(LAST_EMOTIONAL_STATE, String.empty())) {
                getString(R.string.i_am_defeated) ->
                    binding.imgEmotional.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_color)

                getString(R.string.i_am_soso) ->
                    binding.imgEmotional.setImageResource(R.drawable.ic_sentiment_dissatisfied_color)

                getString(R.string.i_am_almost_fine) ->
                    binding.imgEmotional.setImageResource(R.drawable.ic_sentiment_satisfied_color)

                getString(R.string.i_am_fine) ->
                    binding.imgEmotional.setImageResource(R.drawable.ic_sentiment_very_satisfied_color)
            }
        }
    }

    private fun setLastLevel() {
        getSharedPreferences()?.also {
            binding.txtLastLevel.text = it.getString(TEXT_LAST_LEVEL, String.empty())
        }
    }

    private fun configBarChartQuestions(
        dateFormatted: List<String>,
        barDataSet: BarDataSet,
        barDataSet2: BarDataSet
    ) {
        binding.barChart.apply {
            data = BarData(dateFormatted, listOf(barDataSet, barDataSet2))
            xAxis.textSize = BAR_CHART_TEXT_SIZE
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(BAR_CHART_DRAW_GRID_LINES)
            axisLeft.granularity = BAR_CHART_GRANULARITY
            axisRight.granularity = BAR_CHART_GRANULARITY
            legend.setCustom(
                intArrayOf(
                    ContextCompat.getColor(requireActivity(), R.color.colorAccent),
                    ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
                ),
                arrayOf(LEGEND_LEVEL, LEGEND_LEVEL_EMOTION)
            )
            legend.textSize = TEXT_SIZE_LEGEND
            legend.position = Legend.LegendPosition.BELOW_CHART_CENTER
            legend.formToTextSpace = FORM_TO_TEXT_SPACE
            legend.xEntrySpace = X_ENTRY_SPACE
            legend.form = Legend.LegendForm.CIRCLE
            legend.formSize = TEXT_SIZE_LEGEND
            setDescription(String.empty())
            data.setValueFormatter(ChartDecimalFormatter())
            animateY(DURATION_ANIMATE_MILLISECONDS)
        }
    }

    private fun configBarChartEmotional(dateFormatted: List<String>, barDataSet: BarDataSet) {
        binding.barChartEmotion.apply {
            data = BarData(dateFormatted, barDataSet)
            xAxis.textSize = BAR_CHART_TEXT_SIZE
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(BAR_CHART_DRAW_GRID_LINES)
            axisLeft.granularity = BAR_CHART_GRANULARITY
            axisRight.granularity = BAR_CHART_GRANULARITY
            setDescription(String.empty())
            legend.setCustom(
                intArrayOf(ContextCompat.getColor(requireActivity(), R.color.colorPrimary)),
                arrayOf(LEGEND_LEVEL_EMOTION)
            )
            legend.textSize = TEXT_SIZE_LEGEND
            legend.formToTextSpace = FORM_TO_TEXT_SPACE
            legend.position = Legend.LegendPosition.BELOW_CHART_CENTER
            legend.yEntrySpace = X_ENTRY_SPACE
            legend.form = Legend.LegendForm.CIRCLE
            legend.formSize = TEXT_SIZE_LEGEND
            data.setValueFormatter(ChartDecimalFormatter())
            animateY(DURATION_ANIMATE_MILLISECONDS)
        }
    }

    private fun getDateTimeFormat(date: String): String =
        date.split(DELIMITER_SPACE)[0].split(DELIMITER_SLASH).let {
            "${it[0]}-${it[1]}"
        }

    companion object {

        private const val BAR_CHART_TEXT_SIZE = 8f
        private const val BAR_CHART_GRANULARITY = 1f
        private const val BAR_CHART_DRAW_GRID_LINES = false
        private const val TEXT_LAST_LEVEL = "text_last_level"
        private const val DURATION_ANIMATE_MILLISECONDS = 1500
        private const val DELIMITER_SPACE = " "
        private const val DELIMITER_SLASH = "/"
        private const val LEGEND_LEVEL = "nivel"
        private const val LEGEND_LEVEL_EMOTION = "emoción"
        private const val TEXT_SIZE_LEGEND = 12F
        private const val FORM_TO_TEXT_SPACE = 5F
        private const val X_ENTRY_SPACE = 35F

        @JvmStatic
        fun newInstance() =
            StatisticsFragment()
    }
}
