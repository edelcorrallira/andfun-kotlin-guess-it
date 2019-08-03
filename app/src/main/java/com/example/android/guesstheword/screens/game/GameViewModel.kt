package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel(){

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 6000L
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score
    // The current word
    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    //Game Finished Event
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private var _countDownTimer = MutableLiveData<CountDownTimer>()

    private var _countDown = MutableLiveData<Long>()

    val countDown: LiveData<Long>
        get() = _countDown

    init {
        Log.i("GameViewModel!","GameViewModel created!")
        _eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0
        _countDownTimer.value = object: CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _countDown.value = millisUntilFinished/1000
            }

            override fun onFinish() {
                _eventGameFinish.value=true
            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        _countDownTimer.value?.cancel()
        Log.i("GameViewModel!","GameViewModel destroyed!")
    }
    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList():MutableList<String> {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "bass",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
        return wordList
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
//            _eventGameFinish.value = true
            resetList()
        }
        _word.value = wordList.removeAt(0)

    }

    fun onSkip() {
        if(score.value != null) {
            _score.value = score.value?.minus(1)
        }
        nextWord()
    }

    fun onCorrect() {
        if(score.value != null) {
            _score.value = score.value?.plus(1)
        }
        nextWord()
    }

    fun onGameFinishCOmplete(){
        _eventGameFinish.value = false
    }
}