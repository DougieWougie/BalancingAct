package eu.lynxworks.balancingact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScanActivity extends AppCompatActivity {
    /*  This Activity accesses the devices installed camera application and uses it to take
        a photograph. This is used in the BarcodeActivity to scan for a barcode.
     */
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE = 1;

    private Bitmap barcodeBitmap;
    private String barcode = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findBarcode();
    }

    private void setBarcodeBitmap(Bitmap barcodeBitmap) {
        this.barcodeBitmap = barcodeBitmap;
    }

    private Bitmap getBarcodeBitmap() {
        return this.barcodeBitmap;
    }

    private void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    private String getBarcode() {
        return this.barcode;
    }

    private void findBarcode() {
        /*  There are a lot of issues around Android's permission model. The correct permission is
            already set in the application's manifest however for SDK 24 upwards (Android M)
            permission checking for the camera is dynamic - hence the SDK build version check, which
            obtains the permission dynamically if required.
         */
        try {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
            } else {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /*  This function is invoked when the Operating System completes the camera
            activity. It extracts an image from the returned data, creates a BarcodeScanner
            and scans the image.
         */
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                /*  Bundles allow transient data to be passed between activities.
                    in this case there is image data being returned from the
                    camera.
                 */
                Bundle extras = intent.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                setBarcodeBitmap(bitmap);
                BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(
                        getApplicationContext())
                        .setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E )
                        .build();
                // Check the detector is running.
                if (!barcodeDetector.isOperational()) {
                    setBarcode("Not found");
                } else {
                    // We have to consider what happens when more than one barcode is in the photo!
                    Frame frame = new Frame.Builder().setBitmap(getBarcodeBitmap()).build();
                    SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
                    // If there's no barcode detected, there's no point decoding it.
                    if (barcodes.size() > 0) {
                        Barcode barcode = barcodes.valueAt(0);
                        setBarcode(barcode.toString());
                    } else
                        setBarcode("No barcode detected");
                }
            } catch (Exception e) {
                setBarcode("No barcode detected");
            }
            /*  Data is passed back using an new bundle
             */
            Intent returnData = new Intent();
            returnData.putExtra("barcodeKey", getBarcode());
            setResult(RESULT_OK, returnData);
            finish();
        }
    }
}