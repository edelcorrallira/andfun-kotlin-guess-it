package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel(){

    private var _finalScore = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _finalScore

    val scoreString = Transformations.map(score,{
        score.value.toString()
    })

    private val _playAgainEvent = MutableLiveData<Boolean>()

    val playAgainEvent: LiveData<Boolean>
        get() = _playAgainEvent

    init{
        Log.i("ScoreViewModel","Final score is: $finalScore")
        _finalScore.value = finalScore
        _playAgainEvent.value = false
    }

    fun playAgain(){
        _playAgainEvent.value = true
    }

}

