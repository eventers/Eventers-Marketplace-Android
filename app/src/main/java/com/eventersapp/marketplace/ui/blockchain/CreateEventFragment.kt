package com.eventersapp.marketplace.ui.blockchain

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.FragmentCreateEventBinding
import com.eventersapp.marketplace.ui.viewmodel.CreateEventViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.CreateEventViewModelFactory
import com.eventersapp.marketplace.util.*
import com.github.dhaval2404.imagepicker.ImagePicker
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
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
        setUserInfo()
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
                dataBind.imageTicket.show()
                dataBind.imageAddTicket.invisible()
                //You can also get File Path from intent
                val imgFile: File? = ImagePicker.getFile(data)
                val filePath: String? = ImagePicker.getFilePath(data)
                val fileName: String = imgFile?.name ?: ""
                viewModel.setFilePath(filePath ?: "")
                viewModel.setMediaName(fileName)
                val bitmap = BitmapFactory.decodeFile(imgFile?.absolutePath)
                dataBind.imageTicket.setImageBitmap(bitmap)

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
        (activity as AppCompatActivity?)?.setSupportActionBar(dataBind.toolbar)
        dataBind.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        dataBind.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        dataBind.imageAddTicket.setOnClickListener {
            imagePicker()
        }
        dataBind.imageTicket.setOnClickListener {
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
                    uploadImage()
                    findNavController().navigate(R.id.action_createEventFragment_to_dashboardFragment)
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    requireActivity().showToast(state.message)
                }
            }
        })
    }


    private fun setUserInfo() {
        val userData = AppUtils.getUserPreference(requireContext())
        viewModel.setUserInfo(userData, requireContext().deviceId())
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

    private fun uploadImage() {
        // This is One Time Work Request.
        val data = Data.Builder()
        data.putString(AppConstants.ARG_FILE_PATH, viewModel.getFilePath())
        data.putString(AppConstants.ARG_MEDIA_PATH, viewModel.getMediaPath())
        data.putString(AppConstants.ARG_MEDIA_NAME, viewModel.getMediaName())
        val oneTimeWorkRequest =
            OneTimeWorkRequest.Builder(UploadImage::class.java)
                .setInputData(data.build())
                .build()
        WorkManager.getInstance(requireContext()).enqueue(oneTimeWorkRequest)

    }
}
