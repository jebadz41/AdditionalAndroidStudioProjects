package com.example.stripe;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.jetbrains.annotations.Nullable;
import com.stripe.okhttp3.MediaType;
import com.stripe.okhttp3.OkHttpClient;
import com.stripe.okhttp3.Request;
import com.stripe.okhttp3.RequestBody;
import com.stripe.okhttp3.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity
{

    private static final String BACKEND_URL = "http://192.168.0.151:4242/";
    EditText etAmount;
    CardInputWidget cardInputWidget;
    Button payButton;
    private Stripe stripe;
    Double amountDouble=null;
    private OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etAmount = findViewById(R.id.etAmount);
        cardInputWidget = findViewById(R.id.cardInputWidget);
        payButton = findViewById(R.id.btnPay);
        httpClient = new OkHttpClient();

        stripe = new Stripe(
                getApplicationContext(),
                "pk_test_TYooMQauvdEDq54NiTphI7jx"
        );


        payButton.setOnClickListener(v -> {
            if(etAmount.getText().toString().isEmpty() || (Integer.parseInt(etAmount.getText().toString()) < 50 ))
                Toast.makeText(this, "Enter all fields/Amount is not minimum", Toast.LENGTH_SHORT).show();
            else if (cardInputWidget.getPaymentMethodCreateParams() == null )
                Toast.makeText(this, "Card Details Invalid", Toast.LENGTH_SHORT).show();
            else
            {
                amountDouble = Double.valueOf(etAmount.getText().toString());
                Toast.makeText(this, "Transaction in progress", Toast.LENGTH_LONG).show();
                startCheckout();
            }
        });
    }

    private void startCheckout()
    {
            MediaType mediaType = MediaType.get("application/json; charset=utf-8");
            double amount=amountDouble*100;

            Map<String,Object> payMap=new HashMap<>();
            Map<String,Object> itemMap=new HashMap<>();
            List<Map<String,Object>> itemList =new ArrayList<>();

            payMap.put("currency","INR");
            itemMap.put("id","photo_subscription");
            itemMap.put("amount",amount);
            itemList.add(itemMap);
            payMap.put("items",itemList);
            String json = new Gson().toJson(payMap);
            RequestBody body = RequestBody.create(json, mediaType);
            Request request = new Request.Builder()
                    .url(BACKEND_URL + "create-payment-intent")
                    .post(body)
                    .build();
            httpClient.newCall(request)
                    .enqueue(new PayCallback(this));
    }

    public void onPaymentSuccess(@NonNull final Response response) throws IOException
    {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        String paymentIntentClientSecret = responseMap.get("clientSecret");

        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null) {
            assert paymentIntentClientSecret != null;
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
            stripe.confirmPayment(this, confirmParams);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    public void displayAlert(@NonNull String title, @Nullable String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }
}