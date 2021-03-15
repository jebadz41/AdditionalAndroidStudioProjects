package com.example.stripe;

import android.widget.Toast;
import androidx.annotation.NonNull;
import com.stripe.okhttp3.Call;
import com.stripe.okhttp3.Callback;
import com.stripe.okhttp3.Response;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class PayCallback implements Callback {

    @NonNull
    private final WeakReference<MainActivity> activityRef;
    PayCallback(@NonNull MainActivity activity) {
        activityRef = new WeakReference<>(activity);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        final MainActivity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(() ->
                Toast.makeText(
                        activity, "Error: " + e.getMessage(), Toast.LENGTH_LONG
                ).show()
        );
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull final Response response)
            throws IOException {
        final MainActivity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        if (!response.isSuccessful()) {
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        } else {
            activity.onPaymentSuccess(response);
        }
    }
}