package com.example.qrcode.eanddonzxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;


public class MainActivity extends ActionBarActivity {
    private Button btn;
    private TextView txt;
    private ImageView img;
    private Button.OnClickListener but = new Button.OnClickListener() {
        public void onClick(View v) {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        txt = (TextView) findViewById(R.id.textView);
        img = (ImageView) findViewById(R.id.imageView);
        btn.setOnClickListener(but);
        //testGit
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        int width = 400, height = 400;
        String contents = null;
        int[] pixels = new int[width * height];

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                //編碼
                try {
                    BitMatrix bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, width, height);

                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            if(bitMatrix.get(x, y)){
                                pixels[y * width + x] = 0xff000000;
                            }else{
                                pixels[y * width + x] = 0xffffffff;
                            }

                        }
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                //編碼結束

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                img.setImageBitmap(bitmap);

                //解碼
                RGBLuminanceSource source = new RGBLuminanceSource(width,height, pixels);

                BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
                QRCodeReader reader2= new QRCodeReader();
                Result result = null;

                try {
                    result = reader2.decode(bitmap1); //載入影像解碼

                    txt.setText(result.toString());

                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (ChecksumException e) {
                    e.printStackTrace();
                } catch (com.google.zxing.FormatException e) {
                    e.printStackTrace();
                }
                //解碼結束

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }

        }

    }
}
