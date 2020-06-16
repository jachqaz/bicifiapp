package com.bicifiapp.ui.viewmodels.statistics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.bicifiapp.statistics.Statistics
import com.bicifiapp.statistics.StatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    private lateinit var resultStatisticTest: Either<Failure, Statistics>

    val resultStatisticTestLiveData by lazy {
        MutableLiveData<State>()
    }

    fun getStatisticsTestByUser(userId: String) {
        resultStatisticTestLiveData.value = State.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                resultStatisticTest = statisticsRepository.getStatisticsTestByUser(userId)
            }
            resultStatisticTest.either(
                ::handlerFailureStatisticsTestByUser,
                ::handlerStatisticsTestByUser
            )
        }
    }

    private fun handlerFailureStatisticsTestByUser(failure: Failure) {
        resultStatisticTestLiveData.value = State.Failed(failure)
    }

    private fun handlerStatisticsTestByUser(statistics: Statistics) {
        resultStatisticTestLiveData.value = State.Success(statistics)
    }
}
