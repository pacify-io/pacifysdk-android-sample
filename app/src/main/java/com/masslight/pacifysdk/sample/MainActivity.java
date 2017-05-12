package com.masslight.pacifysdk.sample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.masslight.lib.pacifysdk.core.model.Color;
import com.masslight.lib.pacifysdk.core.model.Token;
import com.masslight.lib.pacifysdk.sdk.PacifySdk;
import com.masslight.lib.pacifysdk.sdk.model.PacifyEnvironment;
import com.masslight.lib.pacifysdk.sdk.model.entity.Coupon;
import com.masslight.lib.pacifysdk.sdk.model.entity.PacifyAppearance;
import com.masslight.lib.pacifysdk.sdk.model.entity.PacifySettings;
import com.masslight.lib.pacifysdk.sdk.model.entity.PacifySupportInfo;
import com.masslight.lib.pacifysdk.sdk.model.entity.PacifyUserData;
import com.masslight.lib.pacifysdk.sdk.model.entity.TokensInfo;

import sample.pacifysdk.masslight.com.pacifysdkandroidsample.R;

public final class MainActivity extends AppCompatActivity implements PacifySdk.PacifyDelegate {

    private static final String TAG = MainActivity.class.getCanonicalName();

    //    private static final String SAMPLE_API_KEY = "test-3-org-api-key";
    private static final String SAMPLE_API_KEY = "8271740bb0ded5e1ff42258c794920e7"; //medela staging
    private static final String SAMPLE_USER_SESSION_TOKEN = "valid_1234";
    private static final String SAMPLE_COUPON = "6UA4CSIV";

    private EditText apiKeyField;
    private EditText tokenField;
    private EditText couponField;
    private Button callPacifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setListeners();
        enableCallToPacifyButtonIfNeeded();
        preFillFieldsWithSampleData();
    }

    private void bindViews() {
        apiKeyField = (EditText) findViewById(R.id.api_key_field);
        tokenField = (EditText) findViewById(R.id.token_field);
        couponField = (EditText) findViewById(R.id.coupon_field);
        callPacifyButton = (Button) findViewById(R.id.start_av_conference);
    }

    private void setListeners() {
        final TextChangesListener disableButtonOnEmptyTextFieldListener = new TextChangesListener() {
            @Override
            protected void onTextChanged(String text) {
                enableCallToPacifyButtonIfNeeded();
            }
        };

        apiKeyField.addTextChangedListener(disableButtonOnEmptyTextFieldListener);
        tokenField.addTextChangedListener(disableButtonOnEmptyTextFieldListener);
        callPacifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVideoConference();
            }
        });
    }

    private void preFillFieldsWithSampleData() {
        apiKeyField.setText(SAMPLE_API_KEY);
        tokenField.setText(SAMPLE_USER_SESSION_TOKEN);
        couponField.setText(SAMPLE_COUPON);
    }

    private void enableCallToPacifyButtonIfNeeded() {
        boolean isEnabled = !editTextContent(apiKeyField).isEmpty() && !editTextContent(tokenField).isEmpty();
        callPacifyButton.setEnabled(isEnabled);
    }

    private String editTextContent(EditText source) {
        return source.getText().toString().trim();
    }

    private void launchVideoConference() {
        final PacifyUserData pacifyUserData = new PacifyUserData(
                "Mister Twister",
                "Bos", // optional, used for payment only
                "mistertwister@matrix.com"
        );

        final TokensInfo tokensInfo = new TokensInfo(
                new Token(apiKeyField.getText().toString()),
//                new Token("valid_1001")
                new Token(tokenField.getText().toString())
        );

        final PacifySupportInfo pacifySupportInfo = new PacifySupportInfo("support@company.com", "1234567890");
        final PacifyAppearance pacifyAppearance = new PacifyAppearance(
                Color.ofRgb(249.0 / 255, 249.0 / 255, 249.0 / 255),
                Color.ofRgb(253.0 / 255, 217.0 / 255, 123.0 / 255),
                Color.BLACK.withAlphaComponent(0.8),
                Color.ofRgb(143.0 / 255, 98.0 / 255, 146.0 / 255),
                Color.WHITE,
                Color.ofRgb(76.0 / 255, 0, 82.0 / 255),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.medela_logo)
        );
        final PacifySettings pacifySettings = new PacifySettings(
                pacifyAppearance,
                PacifyEnvironment.Testing,
                pacifySupportInfo,
                "SampleApp"
        );

        PacifySdk.call(
                MainActivity.this,
                tokensInfo,
                Coupon.create(SAMPLE_COUPON),
                pacifyUserData,
                pacifySettings,
                this
        );
    }

    @Override
    public void onComplete() {
        Log.i(TAG, "Library call execution successfully completed.");
    }

    @Override
    public void onError(Throwable exception) {
        Log.e(TAG, "Library call execution failed with error: ", exception);
    }
}
