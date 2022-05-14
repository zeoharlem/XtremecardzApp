package com.zeoharlem.append.xtremecardz.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.canhub.cropper.databinding.CropImageActivityBinding
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.models.CapturedImage

class CropUploadFormActivity : AppCompatActivity() {
    private var cropUploadFormBinding: CropImageActivityBinding? = null
    private var capturedImageData: CapturedImage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        cropUploadFormBinding   = CropImageActivityBinding.inflate(layoutInflater)
        setContentView(cropUploadFormBinding!!.root)
        if(intent.hasExtra("capturedImageData")){
            capturedImageData   = intent.getParcelableExtra("capturedImageData")
        }
    }

    private fun startCroppingImageAction(){
        capturedImageData?.let {
            cropUploadFormBinding!!.cropImageView.setImageUriAsync(it.capturedItem)
        }
    }
}