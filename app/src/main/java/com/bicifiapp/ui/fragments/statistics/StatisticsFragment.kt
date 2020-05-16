package com.bicifiapp.ui.fragments.statistics

import android.os.Build
import android.view.View
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentStatisticsBinding
import com.bicifiapp.extensions.LAST_EMOTIONAL_STATE
import com.bicifiapp.extensions.empty
import com.bicifiapp.extensions.getSharedPreferences
import com.bicifiapp.extensions.userId
import com.bicifiapp.statistics.TestStatistic
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.statistics.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class StatisticsFragment : BaseFragment(R.layout.fragment_statistics) {

    private val statisticsViewModel by inject<StatisticsViewModel>()

    private var userId: String? = null
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
                loadChart(state.responseTo())
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun loadChart(testStatistics: List<TestStatistic>) {
        setLastLevel()

        val dataFilter: MutableList<BarEntry> = mutableListOf()

        val dateFormatted = testStatistics.map {
            getDateTimeFormat(it.date) ?: it.date.toString()
        }

        testStatistics.forEachIndexed { index, item ->
            dataFilter.add(BarEntry(item.level.toFloat(), index))
        }

        val barDataSet = BarDataSet(dataFilter, String.empty()).apply {
            valueTextSize = BAR_CHART_TEXT_SIZE
            color = getColor(R.color.colorPrimaryDark)
        }

        configBarChart(dateFormatted, barDataSet)
        binding.barChart.invalidate()
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

    private fun configBarChart(dateFormatted: List<String>, barDataSet: BarDataSet) {
        binding.barChart.apply {
            data = BarData(dateFormatted, barDataSet)
            xAxis.textSize = BAR_CHART_TEXT_SIZE
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(BAR_CHART_DRAW_GRID_LINES)
            axisLeft.granularity = BAR_CHART_GRANULARITY
            axisRight.granularity = BAR_CHART_GRANULARITY
            legend.isEnabled = HIDE_LEGEND
            setDescription(String.empty())
            data.setValueFormatter(ChartDecimalFormatter())
        }
    }

    private fun getDateTimeFormat(date: LocalDateTime?): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(date)
        }
        return null
    }

    companion object {

        private const val BAR_CHART_TEXT_SIZE = 8f
        private const val BAR_CHART_GRANULARITY = 1f
        private const val BAR_CHART_DRAW_GRID_LINES = false
        private const val HIDE_LEGEND = false
        private const val TEXT_LAST_LEVEL = "text_last_level"

        @JvmStatic
        fun newInstance() =
            StatisticsFragment()

    }
}
