package br.com.luan2.pdfgenerator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import br.com.luan2.pdfgenerator.pdfwriter.PDFWriter;
import br.com.luan2.pdfgenerator.pdfwriter.PaperSize;
import br.com.luan2.pdfgenerator.pdfwriter.StandardFonts;


public class MainActivity extends AppCompatActivity {

    Bitmap barcode = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        setContentView(R.layout.activity_main);


        // barcode data
        String barcode_data = "12345678901234567890";

        // barcode image
        ImageView iv = new ImageView(this);

        try {

            barcode = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 310, 55);

        } catch (WriterException e) {
            e.printStackTrace();
        }


        try {

            PDFWriter writer = new PDFWriter(PaperSize.FOLIO_WIDTH, PaperSize.FOLIO_HEIGHT);
            writer.setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_BOLD, StandardFonts.WIN_ANSI_ENCODING);
            writer.addText(85, 75, 18, "Code Research Laboratories");


            Bitmap mLogo = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.ic_launcher);

            Bitmap other = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.ic_launcher);


// center images ...
            int left  = (PaperSize.FOLIO_WIDTH - barcode.getWidth()) / 2;

            writer.addText(left, PaperSize.FOLIO_HEIGHT - 100, 25, "Code Research Laboratories1");
            writer.addText(left, PaperSize.FOLIO_HEIGHT - 70, 22, "Code Research Laboratorie2");
            writer.addText(left, PaperSize.FOLIO_HEIGHT - 180, 18, "Code Research Laboratories3");
            writer.addImage(left, PaperSize.FOLIO_HEIGHT - 160, barcode);


            outputToFile("MyFirstReport.pdf", writer.asString(), "ISO-8859-1");

        }catch (Exception e){
            Log.e("error", e.getMessage());

        }



    }

    /**************************************************************
     * getting from com.google.zxing.client.android.encode.QRCodeEncoder
     *
     * See the sites below
     * http://code.google.com/p/zxing/
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/EncodeActivity.java
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java
     */

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public void outputToFile(String fileName, String pdfContent, String encoding) {
        File newFile = new File(Environment.getExternalStorageDirectory() + "/" + fileName);

        try {
            newFile.createNewFile();

            try {
                FileOutputStream pdfFile = new FileOutputStream(newFile);
                pdfFile.write(pdfContent.getBytes(encoding));
                pdfFile.close();
            } catch(FileNotFoundException e) {
                Log.e("error", e.getMessage());
            }

        } catch(IOException e) {
            Log.e("error", e.getMessage());
            // ...
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
