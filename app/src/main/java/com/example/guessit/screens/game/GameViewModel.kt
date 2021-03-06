package com.example.guessit.screens.game

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

class GameViewModel: ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    private val panicTime:Int = 5

    private var _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType> = _eventBuzz

    private val timer: CountDownTimer
    private var _currentTime = MutableLiveData<Long>()
    private val currentTime: LiveData<Long> = _currentTime

    // The current word
    private var _word = MutableLiveData<String>() // internal
    val word: LiveData<String> = _word    // external

    val currentTimeString: LiveData<String> = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The current score
    private var _score = MutableLiveData<Int>() // internal
    val score: LiveData<Int> = _score   // external

    // event game over
    var eventgameover = MutableLiveData<Boolean>()

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 20000L
    }

    init{
//        Log.i("GameViewModel","View Model Created!!")
        _score.value=0
        resetList()
        nextWord()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
//                Log.i("GameViewModel","$millisUntilFinished")
                val time = millisUntilFinished/ ONE_SECOND
                _currentTime.value = time
                if( time <= panicTime){
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventBuzz.value= BuzzType.GAME_OVER
                eventgameover.value = true
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
//        Log.i("GameViewModel","View Model Destroyed!!")
        timer.cancel()
    }

    fun onGameFinishEvent(){
        eventgameover.value = false
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
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

    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = (_score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        _eventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onEventBuzz(){
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

}