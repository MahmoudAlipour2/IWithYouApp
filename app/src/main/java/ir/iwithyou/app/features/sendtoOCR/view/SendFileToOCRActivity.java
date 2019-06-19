package ir.iwithyou.app.features.sendtoOCR.view;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ir.iwithyou.app.R;
import ir.iwithyou.app.features.sendtoOCR.model.OCRResponse;
import ir.iwithyou.app.features.sendtoOCR.model.Response;
import ir.iwithyou.app.features.sendtoOCR.webService.APIClient;
import ir.iwithyou.app.features.sendtoOCR.webService.APIInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class SendFileToOCRActivity extends AppCompatActivity {
    String firstToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6ImVlOTI3MzhhLWZiZWUtNDA2MS1iOTI3LTczODJiOGM1MDhlNiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWUiOiJtYWhtb3VkYWxpcG91cjExMUBnbWFpbC5jb20iLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJNb2JpbGVVc2VyIiwiZXhwIjoxNTYxMDI4MTI5LCJpc3MiOiJodHRwOi8vZXlld2l0aHlvdS5pciIsImF1ZCI6Imh0dHA6Ly9leWV3aXRoeW91LmlyIn0.TV9WR2TzLBMX2YnZt-R5Jtdfum4TFPhjc2rsSXRL2H4";
    String bearer = "Bearer ";
    String token = bearer + firstToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file_to_ocr);


        //Get image Uri
        Intent intent = getIntent();
        Uri imageUri = intent.getData();
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            String type = getFileExtension(imageUri);
            sendUploadRequest(getBytes(inputStream), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendUploadRequest(byte[] bytes, String type) {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), bytes);

        MultipartBody.Part file = MultipartBody.Part.createFormData("file", "myImage." + type, requestFile);

        Call<Response> call = apiInterface.upload(file, token);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {

                    String s = response.body().getTransformedText();
                    if (s == null) {
                        Toast.makeText(SendFileToOCRActivity.this, "متنی درون تصویر وجود ندارد!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SendFileToOCRActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SendFileToOCRActivity.this, "login not correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(SendFileToOCRActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Change uri to real address.
    private String getFileExtension(Uri uri) {
        ContentResolver cr = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //Image size reduction.
    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }


}
