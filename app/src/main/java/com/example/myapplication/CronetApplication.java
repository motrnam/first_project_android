package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.net.CronetProviderInstaller;
import com.google.android.gms.tasks.Task;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetProvider;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CronetApplication {
    private CronetEngine cronetEngine;
    private ExecutorService cronetCallbackExecutorService;
    Context activity;
    public CronetApplication(Context mainActivity){
        this.activity = mainActivity;
    }
    public void onCreate() {
        cronetEngine = createDefaultCronetEngine(activity);
        cronetCallbackExecutorService = Executors.newFixedThreadPool(4);
    }

    public CronetEngine getCronetEngine() {
        return cronetEngine;
    }

    public ExecutorService getCronetCallbackExecutorService() {
        return cronetCallbackExecutorService;
    }

    private CronetEngine createDefaultCronetEngine(Context context) {
        return new CronetEngine.Builder(context).setStoragePath(context.getFilesDir().
                getAbsolutePath()).enableHttp2(true).enableQuic(true).enableBrotli(
                        true).setUserAgent("CronetSampleApp").build();
    }


    private static void createCustomCronetEngine(Context context) {
        Task<?> installTask = CronetProviderInstaller.installProvider(context);
        installTask.addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        // create a Cronet engine
                        return;
                    }
                    if (task.getException() != null) {
                        Exception cause = task.getException();
                        if (cause instanceof GooglePlayServicesNotAvailableException) {
                            Toast.makeText(context, "Google Play services not available.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (cause instanceof GooglePlayServicesRepairableException) {
                            Toast.makeText(context, "Google Play services update is required.",
                                    Toast.LENGTH_SHORT).show();
                            context.startActivity(((GooglePlayServicesRepairableException) cause)
                                    .getIntent());
                        } else {
                            Toast.makeText(context, "Unexpected error: " + cause,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Unable to load Google Play services.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        List<CronetProvider> enabledProviders =
                CronetProvider.getAllProviders(context)
                        .stream()
                        .filter(CronetProvider::isEnabled)
                        .collect(Collectors.toList());

        if (enabledProviders.isEmpty()) {
            Toast.makeText(context, "No enabled Cronet providers!", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(enabledProviders);
        CronetProvider winner = enabledProviders.get(0);
        Toast.makeText(context, "And the winning Cronet implementation is " + winner.getName() +
                        ", version " + winner.getVersion(),
                Toast.LENGTH_SHORT).show();
        winner.createBuilder().enableBrotli(true).build();
    }
}
