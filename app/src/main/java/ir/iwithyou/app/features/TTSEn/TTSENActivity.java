package ir.iwithyou.app.features.TTSEn;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;

import ir.iwithyou.app.R;

public class TTSENActivity extends AppCompatActivity {

    private static final String GOOGLE_TTS_PACKAGE = "com.google.android.tts";
    Button btn_TTSEn;
    SeekBar sb_SpeedTTSEn;
    SeekBar sb_PitchTTSEn;
    EditText et_TTSEn;
    TextToSpeech mTTS;

    private static final String TTS_TAG = "TTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttsen);

        btn_TTSEn = findViewById(R.id.btn_TTSEn);
        sb_PitchTTSEn = findViewById(R.id.sb_PitchTTSEn);
        sb_SpeedTTSEn = findViewById(R.id.sb_SpeedTTSEn);
        et_TTSEn = findViewById(R.id.et_TTSEn);


        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {
                    Log.e(TTS_TAG, "Initialize  failed!");
                    downloadGoogleTTS();
                } else {
                    if (isPackageInstalled(GOOGLE_TTS_PACKAGE)) {
                        mTTS.setEngineByPackageName(GOOGLE_TTS_PACKAGE);
                    }

                    int result = mTTS.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e(TTS_TAG, "Language not supported");
                    }
                }
            }
        });

        btn_TTSEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

    }

    private boolean isPackageInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private void downloadGoogleTTS() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + GOOGLE_TTS_PACKAGE)));
        } catch (ActivityNotFoundException ante) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + GOOGLE_TTS_PACKAGE)));
        }
    }

    private void speak() {
        String text = et_TTSEn.getText().toString();
        if (text.length() == 0) {
            Toast.makeText(this, "Text is empty", Toast.LENGTH_SHORT).show();
        } else {
            //pitch
            float pitch = sb_PitchTTSEn.getProgress() / 50;
            if (pitch <= 0.1) pitch = 0.1f;
            mTTS.setPitch(pitch);

            //speed
            float speed = sb_SpeedTTSEn.getProgress() / 50;
            if (speed <= 0.1) speed = 0.1f;
            mTTS.setSpeechRate(speed);
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
    }
}
