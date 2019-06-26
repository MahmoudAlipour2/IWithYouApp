package ir.iwithyou.app.features.sendtoOCR.view;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    String bearer = "Bearer ";
    String token;
    String firstToken;
    ImageView img_SendtoOCR;
    TextView txt_SendtoOCR;
    public Uri imageUri;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file_to_ocr);
        Hawk.init(getApplicationContext()).build();
        // firstToken = Hawk.get(token);
        firstToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6ImVlOTI3MzhhLWZiZWUtNDA2MS1iOTI3LTczODJiOGM1MDhlNiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWUiOiJtYWhtb3VkYWxpcG91cjExMUBnbWFpbC5jb20iLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJNb2JpbGVVc2VyIiwiZXhwIjoxNTYxNjQ2ODcyLCJpc3MiOiJodHRwOi8vZXlld2l0aHlvdS5pciIsImF1ZCI6Imh0dHA6Ly9leWV3aXRoeW91LmlyIn0.TnBWdcA5wWTmhoR8qvHvjb9sPhzu_iBltk6V5t099ig";
        token = bearer + firstToken;
        img_SendtoOCR = findViewById(R.id.img_SendtoOCR);
        txt_SendtoOCR = findViewById(R.id.txt_SendtoOCR);

        //Get image Uri
        Intent intent = getIntent();
        imageUri = intent.getData();
        // img_SendtoOCR.setImageURI(imageUri);
        // txt_SendtoOCR.setText("hello");
        //Toast.makeText(this, "hamrah", Toast.LENGTH_SHORT).show();
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
                        img_SendtoOCR.setImageURI(imageUri);
                        txt_SendtoOCR.setText(s);
                        speakPersian(s);
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

    //speak persian.
    private void speakPersian(String s) {

        String persianText = s;
        if (persianText.length() == 0) {
            Toast.makeText(this, "text is empty", Toast.LENGTH_SHORT).show();
        } else {
            if (!internetIsConnected()) {
                Toast.makeText(this, "You must be online for persian speech!", Toast.LENGTH_SHORT).show();
            } else {
                String apiKey = "R95OC8TFXXAJ5G6";
                String encodedText = new String();
                try {
                    encodedText = URLEncoder.encode(persianText, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = "http://api.farsireader.com/ArianaCloudService/ReadTextGET?APIKey=" + apiKey + "&Text=" + encodedText + "&Speaker=Female1&Format=mp3%2F32%2Fm&GainLevel=0&PitchLevel=0&PunctuationLevel=0&SpeechSpeedLevel=0&ToneLevel=0";
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        }

    }

    private boolean internetIsConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        boolean isAvailable = false;
        if (netInfo != null && netInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();

    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


}
