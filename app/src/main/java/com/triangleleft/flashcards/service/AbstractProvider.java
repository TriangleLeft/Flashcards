package com.triangleleft.flashcards.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProvider<Request extends IProviderRequest, Result extends IProviderResult<?>>
        implements IProvider<Request, Result> {

    private static final Logger log = LoggerFactory.getLogger(AbstractProvider.class);

    private final Map<String, RequestInfo> requestMap = new ConcurrentHashMap<>();

    @Override
    public void registerListener(@NonNull Request request, @NonNull IListener<Result> listener) {
        log.debug("registerListener() called with: request = [{}], listener = [{}]", request, listener);

        // Check that we re-attaching listener to valid request
        if (!requestMap.containsKey(request.getTag())) {
            throw new UnknownRequestException(request.getTag());
        } else {
            RequestInfo info = requestMap.get(request.getTag());
            // If request was already completed, notify listener immediately
            if (info.isCompleted()) {
                notifyListener(listener, info);
            } else {
                // Else, store it in request info, do be called later
                info.setListener(listener);
            }
        }
    }

    protected void notifyListener(@NonNull IListener<Result> listener, @NonNull RequestInfo info) {
        listener.onResult(info.getResult());
        // Clean up
        requestMap.remove(info.getRequest().getTag());
    }

    @Override
    public void unregisterListener(@NonNull Request request, @NonNull IListener<Result> listener) {
        log.debug("unregisterListener() called with: request = [{}], listener = [{}]", request, listener);

        String tag = request.getTag();
        if (!requestMap.containsKey(tag)) {
            throw new UnknownRequestException(tag);
        } else {
            requestMap.remove(tag);
        }
    }

    @Override
    public void doRequest(@NonNull Request request, @NonNull IListener<Result> listener) {
        log.debug("doRequest() called with: request = [{}], listener = [{}]", request, listener);

        // Dissalow duplicate requests
        String tag = request.getTag();
        if (requestMap.containsKey(tag)) {
            throw new DuplicateRequestException();
        }

        requestMap.put(tag, new RequestInfo(request, listener));

        processRequest(request);
    }

    protected abstract void processRequest(@NonNull Request request);

    protected void notifyResult(@NonNull Request request,@NonNull Result result) {
        log.debug("notifyResult() called with: request = [{}], result = [{}]", request, result);

        String tag = request.getTag();
        if (!requestMap.containsKey(tag)) {
            throw new UnknownRequestException(tag);
        }
        RequestInfo requestInfo = requestMap.get(tag);
        requestInfo.setResult(result);
        IListener<Result> listener = requestInfo.getListener();
        // If we have listener attached, notify imediately
        // Otherwise, set the result, we would notify user when he would re-attach listener
        if (listener != null) {
            notifyListener(listener, requestInfo);
        } else {
            requestInfo.setResult(result);
        }

    }

    protected class RequestInfo {
        private final Request request;
        private IListener<Result>  listener;
        private Result result;

        public RequestInfo(@NonNull Request request, @NonNull IListener<Result> listener) {
            this.setListener(listener);
            this.request = request;
        }

        public boolean isCompleted() {
            return result != null;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public IListener<Result>  getListener() {
            return listener;
        }

        public void setListener(IListener<Result>  listener) {
            this.listener = listener;
        }

        public Request getRequest() {
            return request;
        }
    }

}
