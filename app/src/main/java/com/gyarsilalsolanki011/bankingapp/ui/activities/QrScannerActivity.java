package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityQrScannerBinding;

import java.util.Objects;
import java.util.concurrent.Executors;

public class QrScannerActivity extends AppCompatActivity {

    private ActivityQrScannerBinding binding;
    private BarcodeScanner scanner;
    private boolean isFlashOn = false;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityQrScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scanner = BarcodeScanning.getClient();

        startCamera();

        binding.btnFlashlight.setOnClickListener(v -> {
            if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
                isFlashOn = !isFlashOn;
                camera.getCameraControl().enableTorch(isFlashOn);
                binding.btnFlashlight.setImageResource(
                        isFlashOn ? R.drawable.ic_flash_on : R.drawable.ic_flash_off
                );
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

                ImageAnalysis analysis = new ImageAnalysis.Builder().build();
                analysis.setAnalyzer(Executors.newSingleThreadExecutor(), imageProxy -> {
                    @SuppressLint("UnsafeOptInUsageError")
                    InputImage image = InputImage.fromMediaImage(
                            Objects.requireNonNull(imageProxy.getImage()), imageProxy.getImageInfo().getRotationDegrees());

                    scanner.process(image)
                            .addOnSuccessListener(barcodes -> {
                                for (Barcode barcode : barcodes) {
                                    String accountNumber = barcode.getRawValue();
                                    if (accountNumber != null) {
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("accountNumber", accountNumber);
                                        setResult(RESULT_OK, resultIntent);
                                        finish();
                                    }
                                }
                            })
                            .addOnFailureListener(Throwable::printStackTrace)
                            .addOnCompleteListener(task -> imageProxy.close());
                });

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, analysis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }
}