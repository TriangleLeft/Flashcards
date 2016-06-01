package com.triangleleft.flashcards.service.common;

import com.triangleleft.flashcards.service.common.error.CommonError;
import com.triangleleft.flashcards.service.common.error.ErrorType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class AbstractProvider implements IProvider {

    private static final Logger logger = LoggerFactory.getLogger(AbstractProvider.class);

    private final Map<String, Call<?>> requestMap = new ConcurrentHashMap<>();

//    @Override
//    public void registerListener(@NonNull Request request, @NonNull IListener<Result> listener) {
//        logger.debug("registerListener() called with: request = [{}], listener = [{}]", request, listener);
//
//        // Check that we re-attaching listener to valid request
//        RequestInfo info = requestMap.get(request.getTag());
//        if (info == null) {
//            throw new UnknownRequestException(request.getTag());
//        } else {
//            // If request was already completed, notify listener immediately
//            if (info.isCompleted()) {
//                notifyListener(listener, info);
//            } else {
//                // Else, store it in request info, do be called later
//                info.setListener(listener);
//            }
//        }
//    }

//    @Override
//    public void unregisterListener(@NonNull Request request, @NonNull IListener<Result> listener) {
//        logger.debug("unregisterListener() called with: request = [{}], listener = [{}]", request, listener);
//
//        String tag = request.getTag();
//        RequestInfo requestInfo = requestMap.get(tag);
//        if (requestInfo == null) {
//            throw new UnknownRequestException(tag);
//        } else {
//            requestInfo.setListener(null);
//        }
//    }

//    @Override
//    public void doRequest(@NonNull Request request, @NonNull IListener<Result> listener) {
//        logger.debug("doRequest() called with: request = [{}], tag = [{}], listener = [{}]", request, request.getTag(),
//                listener);
//
//        // Dissalow duplicate requests
//        String tag = request.getTag();
//        if (requestMap.containsKey(tag)) {
//            throw new DuplicateRequestException();
//        }
//
//        requestMap.put(tag, new RequestInfo(request, listener));
//
//        processRequest(request);
//    }

    protected void addRequest(IProviderRequest request, Call<?> call) {
        requestMap.put(request.getTag(), call);
    }

    private void removeRequest(IProviderRequest request) {
        requestMap.remove(request.getTag());
    }

    public void cancelRequest(IProviderRequest request) {
        Call<?> call = requestMap.get(request.getTag());
        if (call != null) {
            call.cancel();
        }
        removeRequest(request);
    }

//
//    protected void notifyListener(@NonNull IListener<Result> listener, @NonNull RequestInfo info) {
//        logger.debug("notifyListener() called with: listener = [{}], info = [{}]", listener, info);
//
//        Result result = info.getResult();
//        if (result != null) {
//            listener.onResult(result);
//        } else {
//            CommonError error = info.getError();
//            checkNotNull(error, "Got null error for unsuccessful result");
//            listener.onFailure(error);
//        }
//        // Clean up
//        requestMap.remove(info.getRequest().getTag());
//    }
//
//


    //    protected class RequestInfo {
//        private final Request request;
//        private IListener<Result> listener;
//        private Result result;
//        private CommonError error;
//
//        public RequestInfo(@NonNull Request request, @NonNull IListener<Result> listener) {
//            this.setListener(listener);
//            this.request = request;
//        }
//
//        public boolean isCompleted() {
//            return result != null;
//        }
//
//        public Result getResult() {
//            return result;
//        }
//
//        public void setResult(Result result) {
//            this.result = result;
//        }
//
//        @Nullable
//        public IListener<Result> getListener() {
//            return listener;
//        }
//
//        public void setListener(@Nullable IListener<Result> listener) {
//            this.listener = listener;
//        }
//
//        @NonNull
//        public Request getRequest() {
//            return request;
//        }
//
//        public CommonError getError() {
//            return error;
//        }
//
//        public void setError(CommonError error) {
//            this.error = error;
//        }
//    }
//
    protected abstract class AbstractCallback<T> implements Callback<T> {

        private final IProviderRequest request;

        public AbstractCallback(IProviderRequest request) {
            this.request = request;
        }

        protected abstract void onError(CommonError error);

        protected abstract void onResult(T result);

        @Override
        public final void onFailure(Call<T> call, Throwable t) {
            onError(CommonError.fromThrowable(t));
            removeRequest(request);
        }

        @Override
        public final void onResponse(Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
                onResult(response.body());
            } else {
                // Non-200 response code, for now, we don't expect them
                onError(CommonError.create(ErrorType.SERVER, response.message()));
            }
        }
    }

}
