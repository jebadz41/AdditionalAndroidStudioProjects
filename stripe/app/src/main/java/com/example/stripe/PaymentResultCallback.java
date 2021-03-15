package com.example.stripe;

import android.view.Gravity;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.model.PaymentIntent;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult>
{
    @NonNull
    private final WeakReference<MainActivity> activityRef;

    PaymentResultCallback(@NonNull MainActivity activity) {
        activityRef = new WeakReference<>(activity);
    }

    @Override
    public void onSuccess(@NonNull PaymentIntentResult result) {
        final MainActivity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        PaymentIntent paymentIntent = result.getIntent();
        PaymentIntent.Status status = paymentIntent.getStatus();
        if (status == PaymentIntent.Status.Succeeded) {
            Toast toast =Toast.makeText(activity, "Ordered Successful", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else  {
            // Payment failed – allow retrying using a different payment method
            activity.displayAlert(
                    "Payment failed",
                    Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
            );
        }
    }

    @Override
    public void onError(@NonNull Exception e) {
        final MainActivity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        activity.displayAlert("Error", e.getMessage());
    }
}