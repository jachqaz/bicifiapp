package com.bicifiapp.ui.fragments.statistics

import android.os.Build
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseFragment
import com.bicifiapp.R
import com.bicifiapp.databinding.FragmentStatisticsBinding
import com.bicifiapp.extensions.empty
import com.bicifiapp.extensions.getSharedPreferences
import com.bicifiapp.extensions.safeString
import com.bicifiapp.statistics.TestStatistic
import com.bicifiapp.ui.dialogs.DialogLoading
import com.bicifiapp.ui.dialogs.showAnimLoading
import com.bicifiapp.ui.viewmodels.statistics.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.time.LocalDate
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

    override fun initView() {
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid.safeString()
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
                loadChart(state.responseTo())
                hideProgress()
            }
            null -> hideProgress()
        }

    private fun loadChart(testStatistics: List<TestStatistic>) {
        setLastLevel()

        val dataFilter: MutableList<BarEntry> = mutableListOf()

        val dateFormatted = testStatistics.map {
            getDateTimeFormat(it.date) ?: it.date
        }

        testStatistics.reversed().forEachIndexed { index, item ->
            dataFilter.add(BarEntry(item.level.toFloat(), index))
        }

        val barDataSet = BarDataSet(dataFilter, String.empty()).apply {
            valueTextSize = BAR_CHART_TEXT_SIZE
            color = getColor(R.color.colorAccent)
        }

        configBarChart(dateFormatted.reversed(), barDataSet)
        binding.barChart.invalidate()
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

    private fun getDateTimeFormat(date: String): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateSplit = date.split(SPLIT_DELIMITERS)
            val localDate = LocalDate.of(
                dateSplit[DATE_INDEX_TWO].toInt(),
                dateSplit[DATE_INDEX_ONE].toInt(),
                dateSplit[DATE_INDEX_ZERO].toInt()
            )
            return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(localDate)
        }
        return null
    }

    companion object {

        private const val BAR_CHART_TEXT_SIZE = 8f
        private const val SPLIT_DELIMITERS = "/"
        private const val DATE_INDEX_ZERO = 0
        private const val DATE_INDEX_ONE = 1
        private const val DATE_INDEX_TWO = 2
        private const val BAR_CHART_GRANULARITY = 1f
        private const val BAR_CHART_DRAW_GRID_LINES = false
        private const val HIDE_LEGEND = false
        private const val TEXT_LAST_LEVEL = "text_last_level"

        @JvmStatic
        fun newInstance() =
            StatisticsFragment()

    }
}
