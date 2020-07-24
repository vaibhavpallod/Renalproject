package com.vsp.renalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.tasks.Task;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executor;

public class Googlepaybtn extends AppCompatActivity {
//    Button googlePayButton;
RelativeLayout googlePayButton = null;

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private static final long SHIPPING_COST_CENTS = 90 * PaymentsUtil.CENTS_IN_A_UNIT.longValue();

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

//    private ActivityCheckoutBinding layoutBinding;
//    private View googlePayButton;

    private JSONArray garmentList;
    private JSONObject selectedGarment;
//    private ActivityCheckoutBinding layoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlepaybtn_activity);
//        googlePayButton = layoutBinding.googlePayButton.getRoot()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Drawable background = getResources().getDrawable(R.drawable.gradient_actionbar);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().setBackgroundDrawable(background);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_actionbar));
        getSupportActionBar().setTitle("Payment Button");
        googlePayButton=findViewById(R.id.includeid);
        paymentsClient = PaymentsUtil.createPaymentsClient(this);
        possiblyShowGooglePayButton();
        googlePayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPayment(view);
                    }
                });




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);

        task.addOnCompleteListener(this,
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setGooglePayAvailable(task.getResult());
                        } else {
                            Log.w("isReadyToPay failed", task.getException());
                        }
                    }
                });
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.setClickable(true);
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String tokenizationType = tokenizationData.getString("type");
            final String token = tokenizationData.getString("token");

            if ("PAYMENT_GATEWAY".equals(tokenizationType) && "examplePaymentMethodToken".equals(token)) {
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage(getString(R.string.gateway_replace_name_example))
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }

            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, getString(R.string.payments_show_name, billingName),
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

/*
    private void initializeUi() {

        // Use view binding to access the UI elements
        layoutBinding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        // Dismiss the notification UI if the activity was opened from a notification
        if (Notifications.ACTION_PAY_GOOGLE_PAY.equals(getIntent().getAction())) {
            sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }

        // The Google Pay button is a layout file – take the root view
        googlePayButton = layoutBinding.googlePayButton.getRoot();
        googlePayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPayment(view);
                    }
                });
    }
*/

 /*   private void displayGarment(JSONObject garment) throws JSONException {
        layoutBinding.detailTitle.setText(garment.getString("title"));
        layoutBinding.detailPrice.setText(
                String.format(Locale.getDefault(), "$%.2f", garment.getDouble("price")));

        final String escapedHtmlText = Html.fromHtml(
                garment.getString("description"), Html.FROM_HTML_MODE_COMPACT).toString();
        layoutBinding.detailDescription.setText(Html.fromHtml(
                escapedHtmlText, Html.FROM_HTML_MODE_COMPACT));

        final String imageUri = String.format("@drawable/%s", garment.getString("image"));
        final int imageResource = getResources().getIdentifier(imageUri, null, getPackageName());
        layoutBinding.detailImage.setImageResource(imageResource);
    }*/



    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "GooglePayAvailable not available", Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(int statusCode) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }
    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        /*    double garmentPrice = selectedGarment.getDouble("price");
            long garmentPriceCents = Math.round(garmentPrice * PaymentsUtil.CENTS_IN_A_UNIT.longValue());
            long priceCents = garmentPriceCents + SHIPPING_COST_CENTS;*/

        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(Long.valueOf(1));
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }

        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }

    }


  /*  private JSONObject fetchRandomGarment() {

        // Only load the list of items if it has not been loaded before
        if (garmentList == null) {
            garmentList = Json.readFromResources(this, R.raw.tshirts);
        }

        // Take a random element from the list
        int randomIndex = Math.toIntExact(Math.round(Math.random() * (garmentList.length() - 1)));
        try {
            return garmentList.getJSONObject(randomIndex);
        } catch (JSONException e) {
            throw new RuntimeException("The index specified is out of bounds.");
        }
    }*/


}