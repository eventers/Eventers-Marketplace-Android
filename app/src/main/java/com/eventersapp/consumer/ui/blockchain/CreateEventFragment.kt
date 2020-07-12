package com.eventersapp.consumer.ui.blockchain

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.consumer.R
import com.eventersapp.consumer.databinding.FragmentCreateEventBinding
import com.eventersapp.consumer.ui.viewmodel.CreateEventViewModel
import com.eventersapp.consumer.ui.viewmodelfactory.CreateEventViewModelFactory
import com.eventersapp.consumer.util.*
import com.github.dhaval2404.imagepicker.ImagePicker
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File


class CreateEventFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private lateinit var dataBind: FragmentCreateEventBinding
    private val factory: CreateEventViewModelFactory by instance()
    private val viewModel: CreateEventViewModel by lazy {
        ViewModelProvider(this, factory).get(CreateEventViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_event,
            container,
            false
        )
        dataBind.viewmodel = viewModel
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initializeObserver()
        setupAPICall()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                //dataBind.inputTicketImage.setImageURI(fileUri)

                //You can get File object from intent
                val file: File = ImagePicker.getFile(data)!!

                //You can also get File Path from intent
                val filePath: String = ImagePicker.getFilePath(data)!!

                dataBind.inputTicketImage.setText(filePath)
                blobImage(filePath)

            }
            ImagePicker.RESULT_ERROR -> {
                requireActivity().showToast(ImagePicker.getError(data))
            }
            else -> {
                requireActivity().showToast("Task Cancelled")
            }
        }
    }

    private fun setupUI() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        dataBind.inputTicketImage.setOnClickListener {
            imagePicker()
        }
    }

    private fun initializeObserver() {
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it.isNotBlank()) {
                dataBind.rootLayout.snackbar(it)
            }
        })
    }

    private fun setupAPICall() {
        viewModel.createEventLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(requireContext())
                }
                is State.Success -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast("Event created successfully")
                    findNavController().navigate(R.id.action_createEventFragment_to_dashboardFragment)
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
    }


    private fun getUserInfo() {
        val userData = AppUtils.getUserPreference(requireContext())
        viewModel.getUserInfo(userData, requireContext().deviceId())
    }

    private fun imagePicker() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    //encode image to base64 string
    private fun blobImage(file: String) {
        val bitmap = BitmapFactory.decodeFile(file)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val byteArr: ByteArray = stream.toByteArray()
        val imageStr: String = Base64.encodeToString(byteArr, Base64.DEFAULT)
        Log.i("Info", imageStr)
        viewModel.eventImage(imageStr)
    }
}
