package com.example.myapplication;


import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.simple.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

public abstract class MyCallBack extends UrlRequest.Callback{
    private static final int MAX_SIZE = 512 * 1024;
    private final ByteArrayOutputStream bytesReceived = new ByteArrayOutputStream();
    private final WritableByteChannel receiveChannel = Channels.newChannel(bytesReceived);
    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info,
                                   String newLocationUrl) throws Exception {
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
        request.read(ByteBuffer.allocateDirect(MAX_SIZE));
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
        byteBuffer.flip();
        try {
            receiveChannel.write(byteBuffer);
        } catch (IOException e) {
            android.util.Log.i("the the tag", "IOException during ByteBuffer read. Details: ", e);
        }
        byteBuffer.clear();
        request.read(byteBuffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        info.getAllHeadersAsList();
        byte[] bodyBytes = bytesReceived.toByteArray();
        String result = new String(bodyBytes,StandardCharsets.UTF_8);
        onSucceeded(result);
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {

    }

    public abstract void onSucceeded(String meaning);
    public abstract void onFailed();
}
