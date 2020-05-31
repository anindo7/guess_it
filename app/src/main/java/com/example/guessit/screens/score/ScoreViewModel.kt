package com.example.guessit.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel() {

    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private var _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean> = _eventPlayAgain

    init{
        _score.value = finalScore
    }

    fun onPlayAgain(){
        _eventPlayAgain.value = false
    }

    fun playAgain(){
        _eventPlayAgain.value = true
    }

}