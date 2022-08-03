package com.zeoharlem.append.xtremecardz

import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.zeoharlem.append.xtremecardz.databinding.FragmentUploadBottomSheetBinding
import com.zeoharlem.append.xtremecardz.models.CapturedImage
import com.zeoharlem.append.xtremecardz.models.SelectedImages
import com.zeoharlem.append.xtremecardz.models.UploadRequestBody
import com.zeoharlem.append.xtremecardz.notifications.MyAppsNotificationManager
import com.zeoharlem.append.xtremecardz.sealed.TimerEvent
import com.zeoharlem.append.xtremecardz.sealed.ZipDirectoryState
import com.zeoharlem.append.xtremecardz.services.MyUploadService
import com.zeoharlem.append.xtremecardz.utils.Constants
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils.getFileName
import com.zeoharlem.append.xtremecardz.viewmodels.ZipDirectoryViewModel
import com.zeoharlem.append.xtremecardz.workers.CallbackWorker
import com.zeoharlem.append.xtremecardz.workers.MyZipUploadWorker
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap


@AndroidEntryPoint
class UploadBottomSheetFragment : BottomSheetDialogFragment(), UploadRequestBody.UploadFileCallbackListener {
    private var isTimerRunning = false
    private var selectedZipFile: Uri? = null
    private lateinit var prepareImagesArrayList: ArrayList<String>
    private lateinit var capturedImages: ArrayList<SelectedImages>
    private lateinit var capturedImageData: SelectedImages
    private lateinit var fileCollection: ArrayList<CapturedImage>
    private lateinit var selectedItems: CapturedImage
    private var _binding: FragmentUploadBottomSheetBinding? = null
    private lateinit var zipDirectoryViewModel: ZipDirectoryViewModel
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var myAppNotificationManager: MyAppsNotificationManager
    private var typeSelectedAction = ""

    private val binding get() = _binding!!
    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zipDirectoryViewModel = ViewModelProvider(requireActivity())[ZipDirectoryViewModel::class.java]
        myAppNotificationManager = MyAppsNotificationManager(requireContext())
        prepareImagesArrayList = ArrayList<String>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fileCollection = ArrayList<CapturedImage>()
        capturedImages = ArrayList<SelectedImages>()
        _binding    = FragmentUploadBottomSheetBinding.inflate(layoutInflater)
        requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.black)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.uploadSingleMultiple.setOnClickListener {
            capturedImages.clear()
            imagePickerResultActivityLauncher.launch("image/*")
        }
        binding.createZipFile.setOnClickListener {
            fileCollection.clear()
            filePickerResultActivityLauncher.launch("*/*")
        }
        binding.selectionInfo.setOnClickListener {
            capturedImages.clear()
        }

        binding.uploadSingleZip.setOnClickListener {
            zipPickerResultActivityLauncher.launch("application/*")
        }

        binding.uploadSelections.setOnClickListener {
            val projectCode = "${mAuth.currentUser!!.uid}-${System.currentTimeMillis()}"
            //Trigger the Upload Event
            clickUploadSelection(projectCode)
        }

        //Start Observing the upload
        setUpObservers()

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.black)
        return super.onCreateDialog(savedInstanceState)
    }

    private var imagePickerResultActivityLauncher  = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()){ result ->
        if(result.isNotEmpty() || result.size > 1) {
            typeSelectedAction = "image"
            for (x in result) {
                val bitmap  = convertUriToBitmap(x)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
                capturedImageData = SelectedImages(bitmap, setFileInputOutStream(x).absolutePath)
                capturedImages.add(capturedImageData)
            }
            binding.selectionInfo.text = "You have selected ${capturedImages.size} files"
        }
        else{
            Toast.makeText(
                requireContext(),
                "Make sure your selection is not more 3 or empty",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private var filePickerResultActivityLauncher  = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()){ result ->
        if(result.isNotEmpty() || result.size > 1) {
            typeSelectedAction = "zipped"
            for (x in result) {
                selectedItems = CapturedImage(x, x.path.toString())
                fileCollection.add(selectedItems)
            }
            val createdFolderName = "xtremeZip${System.currentTimeMillis()}"
            zipDirectoryViewModel.createZipDirectory(createdFolderName, fileCollection)
            zipDirectoryViewModel.zipFileProgress.observe(viewLifecycleOwner){
                when(it){
                    is ZipDirectoryState.Loading -> {
                        binding.uploadSelections.isEnabled = false
                        binding.selectionInfo.text = "You are zipping a folder. kindly wait"
                        binding.uploadSelections.text = "Please Wait..."
                    }
                    is ZipDirectoryState.Success -> {
                        binding.uploadSelections.isEnabled = true
                        selectedZipFile = Uri.fromFile(File(it.dataSource!!))
                        val fileSize = XtremeCardzUtils.getFileSize(File(it.dataSource))
                        binding.selectionInfo.text = "Zipping folder completed - (${fileSize})"
                        binding.uploadSelections.text = "Upload Selection"
                    }
                    is ZipDirectoryState.Error -> {
                        binding.uploadSelections.isEnabled = true
                        binding.selectionInfo.text = "Error: ${it.message}"
                        binding.uploadSelections.text = "Upload Selection"
                    }
                }
            }
        }
        else{
            Toast.makeText(
                requireContext(),
                "Make sure your selection is not more 3 or empty",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private var zipPickerResultActivityLauncher  = registerForActivityResult(
        ActivityResultContracts.GetContent()){ result ->
        if(result != null){
            typeSelectedAction = "zip"
            val fileName = requireActivity().getFileName(result)
            selectedZipFile = result
        }
    }

    private fun clickUploadSelection(project: String){
        when (typeSelectedAction) {
            "zip" -> {
                Toast.makeText(requireContext(), "Single Zip Upload call", Toast.LENGTH_SHORT).show()
                selectedZipFile?.let { uploadSelectedZipFileAction(project) }
            }
            "zipped" -> {
                Toast.makeText(requireContext(), "Single Zipped File call", Toast.LENGTH_SHORT).show()
                selectedZipFile?.let { uploadZippedFolderAction(project) }
            }
            "image" -> {
                Toast.makeText(requireContext(), "Multiple Image File call", Toast.LENGTH_SHORT).show()
                uploadArrayImagesFileAction(project)
            }
            else -> Toast.makeText(requireContext(), "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadSelectedZipFileAction(project: String){
        binding.uploadSelections.setOnClickListener {
            //toggleUploadAction()
            binding.uploadSelections.text = "Please wait, upload in progress"
            sendCommandToService(Constants.ACTION_START_SERVICE)
            val file = setFileInputOutStream(selectedZipFile!!)
            val myZipWorkRequest = OneTimeWorkRequestBuilder<CallbackWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresStorageNotLow(true)
                        //.setRequiresBatteryNotLow(true)
                        .build())
                .setInputData(workDataOf(
                    "files" to file.absolutePath,
                    "uid" to mAuth.currentUser!!.uid,
                    "project" to project,
                    "type" to "single"
                )).build()

            val workManager: WorkManager = WorkManager.getInstance(requireActivity())
            workManager.beginUniqueWork("MyZipUpload", ExistingWorkPolicy.REPLACE,
                myZipWorkRequest).enqueue()

            workManager.getWorkInfoByIdLiveData(myZipWorkRequest.id)
                .observe(viewLifecycleOwner){ workInfo ->
                    if(workInfo != null){
                        val progress = workInfo.progress
                        if(workInfo.state == WorkInfo.State.RUNNING){
                            binding.uploadSelections.text = "Upload still in progress"
                            binding.uploadSelections.isEnabled = false
                        }
                        else if(workInfo.state == WorkInfo.State.SUCCEEDED) {
                            sendCommandToService(Constants.ACTION_STOP_SERVICE)
                            binding.uploadSelections.isEnabled = true
                            binding.uploadSelections.text = "Upload Completed"
                            val networkResult = workInfo.outputData.getString("networkResult")
                            Toast.makeText(requireActivity(), "File Upload Completed", Toast.LENGTH_LONG).show()
                            workManager.cancelAllWork()

                            val mapQuery: Map<String, Any> = HashMap()
                            val networkResultObject = gson.fromJson(networkResult, mapQuery.javaClass)

                            //Navigate to next screen
                            val action = UploadBottomSheetFragmentDirections
                                .actionUploadBottomSheetFragmentToProfileFormFragment(
                                    project, networkResultObject.get("_id").toString())
                            findNavController().navigate(action)
                        }
                    }
                }
        }
    }

    private fun uploadZippedFolderAction(project: String){
        binding.uploadSelections.setOnClickListener {
            //toggleUploadAction()
            if(typeSelectedAction.isEmpty() || typeSelectedAction == ""){
                Toast.makeText(requireContext(),
                    "You have selected a file yet", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            sendCommandToService(Constants.ACTION_START_SERVICE)
            val file = setFileInputOutStream(selectedZipFile!!)
            val myZipWorkRequest = OneTimeWorkRequestBuilder<CallbackWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresStorageNotLow(true)
                        //.setRequiresBatteryNotLow(true)
                        .build())
                .setInputData(workDataOf(
                    "files" to file.absolutePath,
                    "uid" to mAuth.currentUser!!.uid,
                    "project" to project,
                    "type" to "single"
                )).build()

            val workManager: WorkManager = WorkManager.getInstance(requireContext())
            workManager.beginUniqueWork("MyZipUpload", ExistingWorkPolicy.REPLACE,
                myZipWorkRequest).enqueue()

            workManager.getWorkInfoByIdLiveData(myZipWorkRequest.id)
                .observe(viewLifecycleOwner){ workInfo ->
                    if(workInfo != null){
                        val progress = workInfo.progress
                        if(workInfo.state == WorkInfo.State.SUCCEEDED) {
                            sendCommandToService(Constants.ACTION_STOP_SERVICE)
                            val networkResult = workInfo.outputData.getString("networkResult")
                            Toast.makeText(requireActivity(), "File Upload Completed", Toast.LENGTH_LONG).show()
                            workManager.cancelAllWork()

                            val mapQuery: Map<String, Any> = HashMap()
                            val networkResultObject = gson.fromJson(networkResult, mapQuery.javaClass)

                            //Navigate to next screen
                            val action = UploadBottomSheetFragmentDirections
                                .actionUploadBottomSheetFragmentToProfileFormFragment(
                                    project,
                                    networkResultObject.get("_id").toString()
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
        }
    }

    private fun uploadArrayImagesFileAction(project: String){
        capturedImages.forEach { selectedImage ->
            prepareImagesArrayList.add(selectedImage.filePath)
        }
        sendCommandToService(Constants.ACTION_START_SERVICE)
        //val file = setFileInputOutStream(selectedZipFile!!)
        val myZipWorkRequest = OneTimeWorkRequestBuilder<CallbackWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresStorageNotLow(true)
                    //.setRequiresBatteryNotLow(true)
                    .build())
            .setInputData(workDataOf(
                "files" to prepareImagesArrayList.joinToString(","),
                "uid" to mAuth.currentUser!!.uid,
                "project" to project,
                "type" to "multiple"
            )).build()

        val workManager: WorkManager = WorkManager.getInstance(requireActivity())
        workManager.beginUniqueWork("MyZipUpload", ExistingWorkPolicy.REPLACE,
            myZipWorkRequest).enqueue()

        workManager.getWorkInfoByIdLiveData(myZipWorkRequest.id)
            .observe(viewLifecycleOwner){ workInfo ->
                if(workInfo != null){
                    val progress = workInfo.progress
                    if(workInfo.state == WorkInfo.State.SUCCEEDED) {
                        sendCommandToService(Constants.ACTION_STOP_SERVICE)
                        val networkResult = workInfo.outputData.getString("networkResult")
                        val mapQuery: Map<String, Any> = HashMap()
                        val networkResultObject = gson.fromJson(networkResult, mapQuery.javaClass)
                        Toast.makeText(requireActivity(), "File Upload Completed", Toast.LENGTH_LONG).show()
                        //Navigate to next screen
                        val action = UploadBottomSheetFragmentDirections
                            .actionUploadBottomSheetFragmentToProfileFormFragment(
                                project,
                                networkResultObject.get("_id").toString()
                            )
                        findNavController().navigate(action)
                        workManager.cancelAllWork()
                    }
                }
            }
    }

    private fun getImageFilePath(uri: Uri): String? {
        var thePath = "no-path-found"
        val filePathColumn = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val cursor: Cursor = requireActivity().contentResolver.query(uri, null, null, null, null)!!
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            thePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return thePath
    }

    private fun getRealPathFromUri(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = requireActivity().contentResolver.query(
            contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        }
        else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startToSendCommandToService(){
        Log.e("MyUploadService", "startToSendCommandToService: called")
        sendCommandToService(Constants.ACTION_START_SERVICE)
    }

    private fun sendCommandToService(action: String){
        requireActivity().startService(Intent(requireContext(), MyUploadService::class.java).apply {
            this.action = action
        })
    }

    private fun setUpObservers(){
        MyUploadService.timerEvent.observe(viewLifecycleOwner) {
            updateFloatingActionBarUi(it)
        }
        MyUploadService.timerInMillis.observe(viewLifecycleOwner){
            //binding.timerView.text = TimerUtil.getFormattedTime(it, true)
        }
    }

    private fun updateFloatingActionBarUi(event: TimerEvent) {
        when (event) {
            is TimerEvent.START -> {
                isTimerRunning = true
                Log.e("MyUpload", "setUpObservers: I got start from service")
            }
            is TimerEvent.STOP -> {
                isTimerRunning = false
                Log.e("MyUpload", "setUpObservers: I got stop from service")
            }
        }

    }

    private fun toggleUploadAction(){
        if(isTimerRunning){
            sendCommandToService(Constants.ACTION_STOP_SERVICE)
        }
        else{
            sendCommandToService(Constants.ACTION_START_SERVICE)
        }
    }

    private fun sendProgressUpdateService(){
        sendCommandToService(Constants.ACTION_START_SERVICE)
    }

    @WorkerThread
    private fun convertUriToBitmap(imageUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try{
            bitmap  = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver,imageUri))
            }
            else{
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
            }
        }
        catch (e: Exception){
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
        return bitmap
    }

    override fun onStart() {
        super.onStart()

        // remove black outer overlay, or change opacity
        dialog?.window?.also { window ->
            window.attributes?.also { attributes ->
                attributes.dimAmount    = 0.02f
                window.attributes       = attributes
            }
            window.navigationBarColor = requireActivity().getColor(R.color.dark)
        }
    }

    private fun requestBodyQueries(): HashMap<String, RequestBody> {
        val partQueriesBody: HashMap<String, RequestBody>   = HashMap()
        partQueriesBody["apiKey"]       = XtremeCardzUtils.createPartFromString(Constants.API_KEY)
        partQueriesBody["reference"]    = XtremeCardzUtils.createPartFromString(XtremeCardzUtils.randomKeyString(15))
        partQueriesBody["uid"]          = XtremeCardzUtils.createPartFromString(mAuth.currentUser!!.uid)
        return partQueriesBody
    }

    private fun setMultipartBody(file: File): MultipartBody.Part {
        //val contentType = "application/octet-stream".toMediaTypeOrNull()
        //val requestBody: RequestBody    = file.asRequestBody(contentType)
        val requestBody    = UploadRequestBody(file, "application", this)
        return MultipartBody.Part.createFormData("xtreme_file", file.name, requestBody)
    }

    //Check Upload Status on active upload of file
    override fun uploadCallback(percentage: Int) {
        Log.e("UploadBottomSheet", "uploadCallback: $percentage")

    }

    private fun setFileInputOutStream(selectedImageUri: Uri): File {
        val parcelFileDescriptor = requireActivity().contentResolver.openFileDescriptor(
            selectedImageUri, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(requireActivity().cacheDir,
            requireActivity().getFileName(selectedImageUri)!!
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    private fun getMimeType(uri: Uri): String? {
        var mimeType: String? = null
        mimeType = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cr: ContentResolver = requireActivity().contentResolver
            cr.getType(uri)
        }
        else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                uri.toString()
            )
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.lowercase(Locale.getDefault())
            )
        }
        return mimeType
    }

}