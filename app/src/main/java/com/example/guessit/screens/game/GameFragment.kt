package com.example.guessit.screens.game

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.guessit.R
import com.example.guessit.databinding.FragmentGameBinding

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    private lateinit var viewmodel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_game,container,false)

        // changed syntax as ViewModelProviders is not supported anymore..
        viewmodel = ViewModelProvider(this).get(GameViewModel::class.java)

        // adding data binding..
        binding.gameViewModel = viewmodel
        binding.lifecycleOwner = this

        // declaring observers for live data..
//        viewmodel.score.observe(this, Observer {newscore ->
//            binding.scoreText.text = newscore.toString()
//        })
//
//        viewmodel.word.observe(this, Observer { newword ->
//            binding.wordText.text = newword
//        })

        viewmodel.eventgameover.observe(this, Observer { event ->
            if(event) {
                gameFinished()
                viewmodel.onGameFinishEvent()
            }
        })

        viewmodel.eventBuzz.observe(this, Observer { event ->
            if(event != GameViewModel.BuzzType.NO_BUZZ ){
                buzz(event.pattern)
                viewmodel.onEventBuzz()
            }
        })

//        viewmodel.currentTime.observe(this, Observer { time ->
//            binding.timerText.text = DateUtils.formatElapsedTime(time)
//        })

//        binding.correctButton.setOnClickListener {
//            viewmodel.onCorrect()
//        }
//        binding.skipButton.setOnClickListener {
//            viewmodel.onSkip()
//        }
        return binding.root
    }


    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val currentscore = viewmodel.score.value ?:0
        val action = GameFragmentDirections.actionGameToScore(currentscore)
        findNavController().navigate(action)
    }

    // adding buzz to the game..
    private fun buzz(pattern: LongArray) {
        val buzzer: Vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}
