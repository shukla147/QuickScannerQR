package price.bit.com.quickscanner_qr;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GeneratorActivity extends Activity implements View.OnClickListener {

    private String LOG_TAG = "GenerateQRCode";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        Button button1 =  findViewById(R.id.button1);
        button1.setOnClickListener(GeneratorActivity.this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button1:
                EditText qrInput = findViewById(R.id.qrInput);
                String qInput = qrInput.getText().toString();
                if (qInput.matches("")) {
                    Toast.makeText(this, "You did not enter anything", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    String qrInputText = qrInput.getText().toString();
                    Log.v(LOG_TAG, qrInputText);

                    //Find screen size
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    //Encode with a QR Code image
                    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                            null,
                            Contents.Type.TEXT,
                            BarcodeFormat.QR_CODE.toString(),
                            smallerDimension);

                    try {
                        final Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                        ImageView myImage = (ImageView) findViewById(R.id.imageView1);
                        myImage.setImageBitmap(bitmap);
                        //
                        final int[] i = {0};
                        Button download = findViewById(R.id.download);
                       // Button share = findViewById(R.id.share);
                       // share.setVisibility(View.VISIBLE);
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "img"+ i[0]++ +".jpg");
                                try {
                                    f.createNewFile();
                                    FileOutputStream fo = new FileOutputStream(f);
                                    fo.write(bytes.toByteArray());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast toast = Toast.makeText(GeneratorActivity.this,"QR code saved in DCIM folder",Toast.LENGTH_LONG);
                                toast.show();
                              //  share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                               // startActivity(Intent.createChooser(share, "Share Image"));
                            }
                        });


                       /* download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "img"+ i[0]++ +".jpg");
                                try {
                                    f.createNewFile();
                                    FileOutputStream fo = new FileOutputStream(f);
                                    fo.write(bytes.toByteArray());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast toast = Toast.makeText(GeneratorActivity.this,"QR code saved",Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });*/


                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    break;
                }



            // More buttons go here (if any) ...

        }
    }

}
