package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel(){
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

    init {
        _eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0
    }

    init {
        Log.i("GameViewModel!","GameViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel!","GameViewModel destroyed!")
    }
    /**
     * Resets the list of words and randomizes the order
     */
    fun resetList():MutableList<String> {
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
            _eventGameFinish.value = true
        } else {
            _word.value = wordList.removeAt(0)
        }
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