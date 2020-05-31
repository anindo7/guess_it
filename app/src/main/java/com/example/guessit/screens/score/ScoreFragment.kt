package com.example.guessit.screens.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.guessit.R
import com.example.guessit.databinding.FragmentScoreBinding

/**
 * A simple [Fragment] subclass.
 */
class ScoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentScoreBinding>(inflater, R.layout.fragment_score, container, false)
        // Get args using by navArgs property delegate
        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()
        val viewModelFactory = ScoreViewModelFactory(scoreFragmentArgs.score)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(ScoreViewModel::class.java)

//        viewModel.score.observe(this, Observer { newScore ->
//            binding.scoreText.text = newScore.toString()
//        })

        viewModel.eventPlayAgain.observe(this, Observer { event ->
            if(event){
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgain()
            }
        })

        // adding data binding..
        binding.scoreViewModel = viewModel
        binding.lifecycleOwner = this

//        binding.playAgainButton.setOnClickListener { viewModel.playAgain() }
        return binding.root
    }

}
