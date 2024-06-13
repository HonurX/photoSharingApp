package com.photosharingapp

import android.app.DirectAction
import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.photosharingapp.databinding.FragmentLogInBinding


class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

    }

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

        val currentUser = auth.currentUser
        if (currentUser != null ) {
            val action = LogInFragmentDirections.actionLogInFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }



    }



    fun singup (view: View) {



        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if  (email.isNotEmpty() && password.isNotEmpty()) {

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
                if (task.isSuccessful) {
                    val action = LogInFragmentDirections.actionLogInFragmentToFeedFragment()
                    Navigation.findNavController(view).navigate(action)

                }

            }.addOnFailureListener {exception->

                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }




    }

    fun login(view: View) {

     val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener{

                val action = LogInFragmentDirections.actionLogInFragmentToFeedFragment()
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}