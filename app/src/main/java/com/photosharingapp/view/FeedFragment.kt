package com.photosharingapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.photosharingapp.R
import com.photosharingapp.adapter.PostAdapter
import com.photosharingapp.databinding.FragmentFeedBinding
import com.photosharingapp.model.Post


class feedFragment : Fragment(),PopupMenu.OnMenuItemClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var  popupMenu: PopupMenu
    private var _binding: FragmentFeedBinding? = null
    val PostListim : ArrayList<Post> = ArrayList()
     var  adapter : PostAdapter? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        db = Firebase.firestore




        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {popupMenu(it)
            println("popup menu is clicked")
        }
        binding.recyclerViewFeed.layoutManager =LinearLayoutManager(requireContext())

        fireStoreDateGet()

        adapter= PostAdapter(PostListim)
        binding.recyclerViewFeed.adapter = adapter



    }


    fun popupMenu (view: View) {
        val popUp = PopupMenu(requireContext(),binding.floatingActionButton)
        val inflater = popUp.menuInflater
        inflater.inflate(R.menu.pop_up_menu,popUp.menu)
        popUp.setOnMenuItemClickListener(this)
        popUp.show()
    }

    private fun fireStoreDateGet () {
        db.collection("Posts").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
            if (error != null ) {
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
            } else {
                if (value != null ) {
                    if (!value.isEmpty) {
                        PostListim.clear()
                        val documents = value.documents
                        for (document in documents) {
                            val comment = document.get("comment") as String
                            val email = document.get("email") as String
                            val dowlandUrl = document.get("dowlandUrl") as String
                            val post = Post(comment, email, dowlandUrl)
                            PostListim.add(post)
                        }
                        adapter?.notifyDataSetChanged()

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.yuklemeItem) {
            val action = feedFragmentDirections.actionFeedFragmentToUploadingFragment()
            Navigation.findNavController(requireView()).navigate(action)
            println("upload is clicked")


        } else if (item?.itemId == R.id.cikisItem) {
            auth.signOut()

            val action = feedFragmentDirections.actionFeedFragmentToLogInFragment()
            Navigation.findNavController(requireView()).navigate(action)
            println("exit is clicked")
        }


        return true
    }



}