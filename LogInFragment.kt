package com.photosharingapp

import android.app.DirectAction
import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.photosharingapp.databinding.FragmentLogInBinding


class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpbutton.setOnClickListener { singup(it)
            println("singup is clicked")
        }

        binding.logInbutton.setOnClickListener { login(it)
        println("login is clicked")
        }



    }



    fun singup (view: View) {
  val action = LogInFragmentDirections.actionLogInFragmentToFeedFragment()
        Navigation.findNavController(view).navigate(action)


    }

    fun login(view: View) {


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}