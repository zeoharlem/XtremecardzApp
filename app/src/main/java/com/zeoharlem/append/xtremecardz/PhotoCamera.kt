package com.zeoharlem.append.xtremecardz


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.media.MediaActionSound
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil.clear
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wajahatkarim3.easyvalidation.core.collection_ktx.startWithNonNumberList
import com.zeoharlem.append.xtremecardz.adapters.CapturedImageAdapters
import com.zeoharlem.append.xtremecardz.bottomsheets.CameraBottomSheetDialog
import com.zeoharlem.append.xtremecardz.databinding.ActivityPhotoCameraBinding
import com.zeoharlem.append.xtremecardz.models.CapturedImage
import com.zeoharlem.append.xtremecardz.models.HotDeals
import com.zeoharlem.append.xtremecardz.models.Profile
import com.zeoharlem.append.xtremecardz.ui.activities.CropUploadFormActivity
import java.io.*
import java.io.File.separator
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("LogNotTimber")
class PhotoCamera : AppCompatActivity(), CapturedImageAdapters.OnItemClickListener {

    private var hotDeals: HotDeals? = null
    private lateinit var cameraProviderState: Camera
    private var flashLightState: Boolean?   = false
    private var imageCapture: ImageCapture? = null
    private lateinit var capturedImages: ArrayList<CapturedImage>
    private lateinit var photoCameraBinding: ActivityPhotoCameraBinding
    private lateinit var cameraExecutors: ExecutorService
    private lateinit var capturedImageData: CapturedImage
    private lateinit var capturedImageAdapter: CapturedImageAdapters
    private var cameraSelector  = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var capturedUriImages: ArrayList<String>
    private var profileForm: Profile? = null


    @WorkerThread
    private fun convertUriToBitmap(imageUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try{
            bitmap  = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver,imageUri))
            }
            else{
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            }
        }
        catch (e: Exception){
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
        return bitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor   = ContextCompat.getColor(this, R.color.black)
        photoCameraBinding  = ActivityPhotoCameraBinding.inflate(layoutInflater)
        setContentView(photoCameraBinding.root)
        setSupportActionBar(photoCameraBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        cameraExecutors     = Executors.newSingleThreadExecutor()
        capturedImages      = ArrayList<CapturedImage>()
        capturedUriImages   = ArrayList()
        
        //check permission and preview with surfaceview holder
        checkPermission()

        //Clcikc to take photo
        setUpPhotoAction()

        if(intent.hasExtra("profileForm")){
            photoCameraBinding.nextPageAction.isEnabled = true
            profileForm = intent.getParcelableExtra("profileForm")
            photoCameraBinding.nextPageAction.setOnClickListener {
                val bundle: Bundle = Bundle()
                val bottomSheetDialogFragment = CameraBottomSheetDialog()
                bundle.putParcelable("profileForm", profileForm)
                bundle.putParcelable("capturedImageData", capturedImageData)
                bottomSheetDialogFragment.arguments = bundle
                bottomSheetDialogFragment.show(supportFragmentManager, "cameraBottomDialogFrag")
                cameraExecutors.shutdown()
            }
        }

    }

    //Play Beep or Shutter Sound
    private fun playBeepSound() {
        val sound   = MediaActionSound()
        sound.play(MediaActionSound.SHUTTER_CLICK)
    }

    private fun setUpPhotoAction(){
        photoCameraBinding.floatingActionButton.setOnClickListener {
            var mediaPlayer = MediaPlayer.create(this, R.raw.shutter)
            if(capturedImages.size <= 1) {
                mediaPlayer.start()
                Toast.makeText(this, "Image Captured", Toast.LENGTH_SHORT).show()
                takePhoto()
            }
            else{
                Toast.makeText(this, "Images can't be more than 3", Toast.LENGTH_SHORT).show()
            }
            mediaPlayer.setOnCompletionListener { mediaPlayerIn ->
                mediaPlayerIn.reset()
                mediaPlayerIn.release()
                mediaPlayer.release()
                mediaPlayer = null
            }
        }

        //Switch Preview CameraSelector Lens Facing
        photoCameraBinding.imageButton.setOnClickListener {
            switchCameraLensFacingSelector()
            Toast.makeText(this, "Flipped Camera", Toast.LENGTH_SHORT).show()
        }

        //Pick From Phone Gallery
        pickPhotoFromGallery()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.flash_nav, menu)
        val flashMenuItem   = menu.findItem(R.id.off_flash)
        flashMenuItem?.actionView?.setOnClickListener {
            onOptionsItemSelected(flashMenuItem)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun pickPhotoFromGallery(){
        //Set the Category Launcher
        photoCameraBinding.imageButton2.setOnClickListener {
            imagePickerResultActivityLauncher.launch(
                options {
                    setGuidelines(CropImageView.Guidelines.ON).setFixAspectRatio(true)
                    setImageSource(includeGallery = true, includeCamera = false)
                }
            )
        }
    }

    private var imagePickerResultActivityLauncher  = registerForActivityResult(
        CropImageContract()){ result ->
        if(result!!.isSuccessful){
            capturedUriImages.clear()
            try{
                capturedImageData   = CapturedImage(
                    result.uriContent,
                    result.getUriFilePath(this, false)!!
                )
                capturedUriImages.add(result.getUriFilePath(this)!!)
                photoCameraBinding.imageCaptured.setImageURI(result.uriContent)
            }
            catch (e: Exception){
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(applicationContext, "You didn't pick any image", Toast.LENGTH_LONG).show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.off_flash -> {
                flashLightToogle(item.itemId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun checkPermission(){
        Dexter.withContext(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object: MultiplePermissionsListener{
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if(report.areAllPermissionsGranted()){
                        Toast.makeText(this@PhotoCamera, "Permission Granted", Toast.LENGTH_SHORT).show()
                        //setUpSurfaceHolder()
                        startPhoneCamera()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalForDialogPermissions()
            }
        }).onSameThread().check()
    }

    private fun showRationalForDialogPermissions(){
        AlertDialog.Builder(this).setMessage("Seems the permissions needed for " +
                "this feature has been turned off").setPositiveButton("Go to Settings"){
                    _, _ ->
            try{
                val intent  = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri     = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            catch (exception: ActivityNotFoundException){
                Log.e(
                    "PhotoCameraEx",
                    "showRationalForDialogPermissions: ${exception.localizedMessage}"
                )
            }
        }
            .setNegativeButton("Cancel"){dialog, _->
                dialog.dismiss()
            }.show()
    }

    private fun startPhoneCamera(){
        val cameraProviderFuture    = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider   = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also { mPreview ->
                mPreview.setSurfaceProvider(photoCameraBinding.surfaceView.surfaceProvider)
            }
            imageCapture        = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProviderState = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            }
            catch (exception: Exception){
                Log.e("PhotoCameraEx", "startPhoneCamera: Failed", exception)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun switchCameraLensFacingSelector(){
        cameraSelector  = if(CameraSelector.DEFAULT_BACK_CAMERA == cameraSelector){
            CameraSelector.DEFAULT_FRONT_CAMERA
        }
        else{
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startPhoneCamera()
    }

    private fun flashLightToogle(itemId: Int) {
        if(cameraProviderState.cameraInfo.hasFlashUnit() && flashLightState == false){
            flashLightState = true
            cameraProviderState.cameraControl.enableTorch(true)
        }
        else if(cameraProviderState.cameraInfo.hasFlashUnit() && flashLightState == true){
            flashLightState = false
            cameraProviderState.cameraControl.enableTorch(false)
        }
    }

    private fun getBitmapResource(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream   = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun takePhoto(){
        takeBitmapPhoto()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun takeBitmapPhoto(){
        val imageCapture    = imageCapture ?: return
        imageCapture.takePicture(ContextCompat.getMainExecutor(this), object: ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                capturedUriImages.clear()
                val bitmap  = imageProxyToBitmap(image)
                val uri     = saveImage(bitmap, this@PhotoCamera, "xc")
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
                capturedImageData   = CapturedImage(uri, uri!!.path!!)
                photoCameraBinding.imageCaptured.setImageBitmap(bitmap)
                capturedUriImages.add(uri!!.path!!)
                //Close Image Object Before Acquiring a new onw
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }
        })
    }

    private fun setUpRecyclerViewWithAdapter(){
        capturedImageAdapter    = CapturedImageAdapters(capturedImages, this)
        //photoCameraBinding.imagesCapturedRow.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //photoCameraBinding.imagesCapturedRow.setHasFixedSize(true)
        //photoCameraBinding.imagesCapturedRow.adapter        = capturedImageAdapter
    }

    /// @param folderName can be your app's name
    @WorkerThread
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String): Uri? {
        if (Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
            return uri
        }
        else {
            val directory = File(getExternalFilesDir(null), folderName)
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName    = System.currentTimeMillis().toString() + ".jpeg"
            val file        = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            val values      = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            return file.toUri()
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getImageUri(bitmap: Bitmap) : Uri{
        val bytes: ByteArrayOutputStream   = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path    = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "", null)
        return Uri.parse(path)
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy          = image.planes[0]
        val buffer: ByteBuffer  = planeProxy.buffer
        val bytes               = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun saveImageInQ(bitmap: Bitmap):Uri {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = application.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        contentResolver.update(imageUri!!, contentValues, null, null)

        return imageUri as Uri
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutors.shutdown()
        capturedImages.clear()
        capturedUriImages.clear()
        //capturedImageAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        cameraExecutors.shutdown()
//        capturedImages.clear()
        capturedUriImages.clear()
    }

    override fun onItemClicked(capturedImage: CapturedImage, position: Int) {
        //Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show()
        //deleteImageAlertConfirmBox(capturedImage.capturedItem!!, position)
        //capturedImages.remove(capturedImage)
        //capturedUriImages.removeAt(position)
        //capturedImageAdapter.removeItem(position)
        //capturedImageAdapter.notifyItemRemoved(position)
    }

    private fun deleteImageAlertConfirmBox(bitmap: Bitmap, position: Int){
        val dialog  = Dialog(this)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_delete_alert_dialog)
        val closeButton         = dialog.findViewById<ImageView>(R.id.close_window)
        val displayImageOffer   = dialog.findViewById<ImageView>(R.id.display_image_offer)
        val errorMessage        = dialog.findViewById<TextView>(R.id.errorMessage)
        val deleteButton        = dialog.findViewById<Button>(R.id.deleteBtn)
        errorMessage.text       = "You are about deleting the image. Press delete to continue"
        displayImageOffer.setImageBitmap(bitmap)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        deleteButton.setOnClickListener {
            capturedImageAdapter.removeItem(position)
            capturedImageAdapter.notifyItemRemoved(position)
            displayImageOffer.clear()
            displayImageOffer.invalidate()
            dialog.dismiss()
        }
        dialog.show()
    }

}