package com.triangleleft.flashcards.service.provider;

import com.triangleleft.flashcards.service.error.CommonError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractProvider<Request extends IProviderRequest, Result extends IProviderResult<?>>
        implements IProvider<Request, Result> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractProvider.class);

    private final Map<String, RequestInfo> requestMap = new ConcurrentHashMap<>();

    @Override
    public void registerListener(@NonNull Request request, @NonNull IListener<Result> listener) {
        logger.debug("registerListener() called with: request = [{}], listener = [{}]", request, listener);

        // Check that we re-attaching listener to valid request
        RequestInfo info = requestMap.get(request.getTag());
        if (info == null) {
            throw new UnknownRequestException(request.getTag());
        } else {
            // If request was already completed, notify listener immediately
            if (info.isCompleted()) {
                notifyListener(listener, info);
            } else {
                // Else, store it in request info, do be called later
                info.setListener(listener);
            }
        }
    }

    @Override
    public void unregisterListener(@NonNull Request request, @NonNull IListener<Result> listener) {
        logger.debug("unregisterListener() called with: request = [{}], listener = [{}]", request, listener);

        String tag = request.getTag();
        RequestInfo requestInfo = requestMap.get(tag);
        if (requestInfo == null) {
            throw new UnknownRequestException(tag);
        } else {
            requestInfo.setListener(null);
        }
    }

    @Override
    public void doRequest(@NonNull Request request, @NonNull IListener<Result> listener) {
        logger.debug("doRequest() called with: request = [{}], listener = [{}]", request, listener);

        // Dissalow duplicate requests
        String tag = request.getTag();
        if (requestMap.containsKey(tag)) {
            throw new DuplicateRequestException();
        }

        requestMap.put(tag, new RequestInfo(request, listener));

        processRequest(request);
    }

    protected abstract void processRequest(@NonNull Request request);


    protected void notifyListener(@NonNull IListener<Result> listener, @NonNull RequestInfo info) {
        logger.debug("notifyListener() called with: listener = [{}], info = [{}]", listener, info);

        Result result = info.getResult();
        if (result != null) {
            listener.onResult(result);
        } else {
            CommonError error = info.getError();
            checkNotNull(error, "Got null error for unsuccessful result");
            listener.onFailure(error);
        }
        // Clean up
        requestMap.remove(info.getRequest().getTag());
    }


    protected void notifyResult(@NonNull Request request, @Nullable Result result, @Nullable CommonError error) {
        logger.debug("notifyResult() called with: request = [{}], result = [{}], error = [{}]", request, result, error);

        String tag = request.getTag();
        if (!requestMap.containsKey(tag)) {
            throw new UnknownRequestException(tag);
        }
        RequestInfo requestInfo = requestMap.get(tag);
        requestInfo.setResult(result);
        requestInfo.setError(error);
        IListener<Result> listener = requestInfo.getListener();
        // If we have listener attached, notify immediately
        // Otherwise, wait, we would notify user when he would re-attach listener
        if (listener != null) {
            notifyListener(listener, requestInfo);
        }
    }

    protected class RequestInfo {
        private final Request request;
        private IListener<Result>  listener;
        private Result result;
        private CommonError error;

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

        @Nullable
        public IListener<Result>  getListener() {
            return listener;
        }

        public void setListener(@Nullable IListener<Result> listener) {
            this.listener = listener;
        }

        @NonNull
        public Request getRequest() {
            return request;
        }

        public CommonError getError() {
            return error;
        }

        public void setError(CommonError error) {
            this.error = error;
        }
    }

}
