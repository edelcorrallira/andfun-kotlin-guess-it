package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel(){

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
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

    private val _eventGameBuzz = MutableLiveData<BuzzType>()
    val eventGameBuzz: LiveData<BuzzType>
        get() = _eventGameBuzz

    private var _countDownTimer = MutableLiveData<CountDownTimer>()

    private var _countDown = MutableLiveData<Long>()

    val countDown: LiveData<Long>
        get() = _countDown

    val countDownString = Transformations.map(countDown,{ time ->
        DateUtils.formatElapsedTime(time)
    })
    init {
        Log.i("GameViewModel!","GameViewModel created!")
        _eventGameFinish.value = false
        _eventGameBuzz.value = BuzzType.NO_BUZZ
        resetList()
        nextWord()
        _score.value = 0
        _countDownTimer.value = object: CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _countDown.value = millisUntilFinished/1000
                if(millisUntilFinished/1000 == 10L){
                    _eventGameBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _eventGameFinish.value=true
                _eventGameBuzz.value = BuzzType.GAME_OVER
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
        _eventGameBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishCOmplete(){
        _eventGameFinish.value = false
    }
}