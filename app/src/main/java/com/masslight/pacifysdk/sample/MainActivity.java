package com.masslight.pacifysdk.sample;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.masslight.lib.pacifysdk.sdk.PacifySdk;
import com.masslight.lib.pacifysdk.sdk.entity.PacifyEnvironment;
import com.masslight.lib.pacifysdk.sdk.entity.PacifySdkSettings;
import com.masslight.lib.pacifysdk.sdk.entity.PacifySupportInfo;
import com.masslight.lib.pacifysdk.sdk.entity.PacifyUserData;
import com.masslight.lib.pacifysdk.sdk.entity.TokensInfo;
import com.masslight.pacify.framework.core.manager.LanguageManager;
import com.masslight.pacify.framework.core.model.Color;
import com.masslight.pacify.framework.core.model.Coupon;
import com.masslight.pacify.framework.core.model.Currency;
import com.masslight.pacify.framework.core.model.PacifyAppearance;
import com.masslight.pacify.framework.core.model.Token;

import org.apache.commons.lang3.StringUtils;

import sample.pacifysdk.masslight.com.pacifysdkandroidsample.R;

public final class MainActivity extends AppCompatActivity implements PacifySdk.PacifyDelegate {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private static final String SAMPLE_API_KEY = "your_api_token";
    private static final String SAMPLE_USER_SESSION_TOKEN = "valid_1234";
    private static final String SAMPLE_COUPON = "valid_coupon";

    private EditText apiKeyField;
    private EditText tokenField;
    private EditText couponField;
    private Button callPacifyButton;

    /**
     * Just demonstrates use of {@link PacifySdk#isRunning()} method :-)
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (PacifySdk.isRunning()) {
            Toast.makeText(this, "PacifySdk is still running", Toast.LENGTH_LONG).show();
        }
    }

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
                "First Name",
                "Last Name", // optional, used for payment only
                "anemail@domain.com"
        );

        final TokensInfo tokensInfo = new TokensInfo(
                new Token(getText(apiKeyField)),
//                new Token("valid_1001")
                new Token(getText(tokenField))
        );

        final PacifySupportInfo pacifySupportInfo = new PacifySupportInfo("support@company.com", "1234567890");
        final PacifyAppearance pacifyAppearance = new PacifyAppearance(
                Color.ofResId(this, R.color.background_color),
                Color.ofResId(this, R.color.toolbar_tint_color),
                Color.ofResId(this, R.color.toolbar_title_color),
                Color.ofResId(this, R.color.button_background_color),
                Color.ofResId(this, R.color.button_title_color),
                Color.ofResId(this, R.color.text_color),
                ContextCompat.getDrawable(this, R.drawable.medela_logo)
        );
        final PacifySdkSettings pacifySettings = new PacifySdkSettings(
                pacifyAppearance,
                PacifyEnvironment.Testing,
                pacifySupportInfo,
                "SampleApp",
                LanguageManager.SDKSupportedLanguage.English,  // pass in appropriate language with every launch.
                Currency.USD // pass in appropriate currency with every launch.
        );

        String couponAsString = getText(couponField);
        Coupon coupon = couponAsString.isEmpty() ? Coupon.notExists() : Coupon.create(couponAsString);

        PacifySdk.call(
                MainActivity.this,
                tokensInfo,
                coupon,
                pacifyUserData,
                pacifySettings,
                this
        );
    }

    private static String getText(TextView fromView) {
        if (fromView == null) {
            return "";
        }
        if (fromView.getText() == null) {
            return "";
        }
        return StringUtils.defaultString(fromView.getText().toString());
    }

    @Override
    public void onComplete() {
        Log.i(TAG, "Library call execution successfully completed.");
    }

    @Override
    public void onError(Throwable exception) {
        Log.e(TAG, "Library call execution failed with error: ", exception);
        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
