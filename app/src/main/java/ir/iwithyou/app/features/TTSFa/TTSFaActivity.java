package ir.iwithyou.app.features.TTSFa;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ir.iwithyou.app.R;
import okio.Utf8;

public class TTSFaActivity extends AppCompatActivity {


    Button btn_TTSFa;
    EditText et_TTSFa;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttsfa);

        btn_TTSFa = findViewById(R.id.btn_TTSFa);
        et_TTSFa = findViewById(R.id.et_TTSFa);


        btn_TTSFa.setOnClickListener(v -> speakPersian());


    }

    private void speakPersian() {

        String persianText = et_TTSFa.getText().toString();
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
        if (netInfo != null && netInfo.isConnected()){
            isAvailable = true;
        }
        return  isAvailable;

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
