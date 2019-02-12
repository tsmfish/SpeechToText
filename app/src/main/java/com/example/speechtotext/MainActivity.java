package com.example.speechtotext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.tv_result)
    TextView tvResult;
    @ViewById(R.id.tv_state)
    TextView tvState;
    @ViewById(R.id.sw_language)
    Switch swLanguage;
    private SpeechRecognizer mSpeechRecognizer;

    @SuppressLint("ClickableViewAccessibility")
    @AfterViews
    void AfterView() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d(LOG_TAG, "onReadyForSpeech() called with: bundle = [" + bundle + "]");

            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(LOG_TAG, "onBeginningOfSpeech() called");
                tvState.setText("recording started");
                tvResult.setText("");
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
                tvState.setText("recording finished");
            }

            @Override
            public void onError(int i) {
                Log.d(LOG_TAG, "onError() called with: i = [" + i + "]");
                switch (i) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        tvState.setText("Error while recognition: ERROR_AUDIO");
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        tvState.setText("Error while recognition: ERROR_CLIENT");
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        tvState.setText("Error while recognition: ERROR_INSUFFICIENT_PERMISSIONS");
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        tvState.setText("Error while recognition: ERROR_NETWORK");
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        tvState.setText("Error while recognition: ERROR_NETWORK_TIMEOUT");
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        tvState.setText("Error while recognition: ERROR_NO_MATCH");
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        tvState.setText("Error while recognition: ERROR_RECOGNIZER_BUSY");
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        tvState.setText("Error while recognition: ERROR_SERVER");
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        tvState.setText("Error while recognition: ERROR_SPEECH_TIMEOUT");
                        break;
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d(LOG_TAG, "onResults() called with: bundle = [" + bundle + "]");
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null)
                    tvResult.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.d(LOG_TAG, "onEvent() called with: i = [" + i + "], bundle = [" + bundle + "]");
            }
        });
    }


    @Touch(R.id.bt_recognise)
    boolean onRecogniseTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mSpeechRecognizer.stopListening();
                break;

            case MotionEvent.ACTION_DOWN:
                Log.d(LOG_TAG, "OnTouchListener called with: MotionEvent.ACTION_DOWN");
                Log.d(LOG_TAG, String.format("onCreate: %b", ((Switch) findViewById(R.id.sw_language)).isChecked()));
                final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                final Locale language = swLanguage.isChecked() ?
                        Locale.SIMPLIFIED_CHINESE :
                        Locale.getDefault();
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        language
                );
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                        language
                );
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
                        language
                );

                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                break;
        }
        return false;
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
