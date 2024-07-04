package com.photosharingapp.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.photosharingapp.databinding.FragmentUploadingBinding
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.IOException
import java.util.UUID

class uploadingFragment : Fragment() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var secilenGorsel: Uri? = null
    private var secilenBitmap: Bitmap? = null
    private var _binding: FragmentUploadingBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var db : FirebaseFirestore



    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

           auth = Firebase.auth
        storage = Firebase.storage
        db = Firebase.firestore
        registerLauncher()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUploadingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        binding.imageView.setOnClickListener { gorselSec(it) }
        binding.button.setOnClickListener { kaydetButton(it)  }
    }

 fun kaydetButton (view: View) {
     val uuid  = UUID.randomUUID()
     val imageName = "${uuid}.jpg"
     val storage = Firebase.storage
     val references = storage.reference
    val imageReference =  references.child("images").child(imageName)
     if (secilenGorsel != null) {
         imageReference.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot->

            val uploadPictureReference = storage.reference.child("images").child(imageName)
                imageReference.downloadUrl.addOnSuccessListener { uri ->

                 if (auth.currentUser != null ) {
                     val dowlandUrl = uri.toString()
                     //println(dowlandUri)
                     val postMap = hashMapOf <String, Any>()
                     postMap.put("dowlandUrl" , dowlandUrl)
                     postMap.put("email", auth.currentUser!!.email.toString())
                     postMap.put("comment" , binding.editTextComment.text.toString())
                     postMap.put("date", Timestamp.now())

                     db.collection("Posts").add(postMap).addOnCompleteListener {task ->
                         if (task.isSuccessful && task.isComplete){
                             val action = uploadingFragmentDirections.actionUploadingFragmentToFeedFragment()
                             Navigation.findNavController(view).navigate(action)
                         }

                     }.addOnFailureListener {exception ->

                         Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()


                     }

                 }



             }



         }.addOnFailureListener {exception ->
             Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()


         }
     }


 }
    fun gorselSec(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    Snackbar.make(
                        view,
                        "please give permission to access image galery",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Permission Needed", View.OnClickListener {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()
                } else {

                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            } else {
                val galeryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(galeryIntent)


            }

        } else {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(
                        view,
                        "please give permission to access image galery",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Permission Needed", View.OnClickListener {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
                } else {

                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            } else {
                val galeryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(galeryIntent)


            }
        }


    }


    private fun registerLauncher() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val resultFromGallery = result.data
                    if (resultFromGallery != null) {
                        secilenGorsel = resultFromGallery.data

                        try {


                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    secilenGorsel!!
                                )

                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(secilenBitmap)

                            } else {

                                secilenBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    secilenGorsel!!
                                )
                                binding.imageView.setImageBitmap(secilenBitmap)


                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }


                    }

                }
            }

                permissionLauncher =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                        if (result) {
                            val galeryIntent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            activityResultLauncher.launch(galeryIntent)
                        } else {
                            Toast.makeText(requireContext(), "Permission Needed", Toast.LENGTH_LONG)
                                .show()

                        }

                    }





    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }



}