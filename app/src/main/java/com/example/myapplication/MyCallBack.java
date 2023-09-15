package com.example.myapplication;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;

public class MyCallBack extends UrlRequest.Callback{
    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
        request.read(ByteBuffer.allocateDirect(102400));
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
        byteBuffer.clear();
        request.read(byteBuffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {

    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {

    }
}
