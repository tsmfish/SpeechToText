package com.example.speechtotext;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Switch;
import android.widget.TextView;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d(LOG_TAG, "onReadyForSpeech() called with: bundle = [" + bundle + "]");

            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(LOG_TAG, "onBeginningOfSpeech() called");
                ((TextView) findViewById(R.id.tv_state)).setText("recording started");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.d(LOG_TAG, "onBufferReceived() called with: bytes = [" + bytes + "]");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(LOG_TAG, "onEndOfSpeech() called");
                ((TextView) findViewById(R.id.tv_state)).setText("recording finished");
            }

            @Override
            public void onError(int i) {
                Log.d(LOG_TAG, "onError() called with: i = [" + i + "]");
                switch (i) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_AUDIO");
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_CLIENT");
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_INSUFFICIENT_PERMISSIONS");
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_NETWORK");
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_NETWORK_TIMEOUT");
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_NO_MATCH");
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_RECOGNIZER_BUSY");
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_SERVER");
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        ((TextView) findViewById(R.id.tv_state)).setText("Error while recognition: ERROR_SPEECH_TIMEOUT");
                        break;
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d(LOG_TAG, "onResults() called with: bundle = [" + bundle + "]");
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null)
                    ((TextView) findViewById(R.id.tv_result)).setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.d(LOG_TAG, "onEvent() called with: i = [" + i + "], bundle = [" + bundle + "]");
            }
        });

        findViewById(R.id.bt_recognise).setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    mSpeechRecognizer.stopListening();
                    break;

                case MotionEvent.ACTION_DOWN:
                    final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                            ((Switch) findViewById(R.id.sw_language)).isChecked() ?
                                    Locale.getDefault() :
                                    Locale.CHINA
                    );

                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Permissions.check(this, Manifest.permission.RECORD_AUDIO, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                Log.d(LOG_TAG, "onGranted() called");
            }
        });
    }
}
