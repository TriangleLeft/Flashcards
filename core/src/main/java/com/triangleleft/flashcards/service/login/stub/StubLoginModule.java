package com.triangleleft.flashcards.service.login.stub;

import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.IProviderRequest;
import com.triangleleft.flashcards.service.common.error.CommonError;
import com.triangleleft.flashcards.service.common.error.ErrorType;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.login.ILoginRequest;
import com.triangleleft.flashcards.service.login.ILoginResult;
import com.triangleleft.flashcards.service.login.LoginStatus;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.IPersistentStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionsAreNonnullByDefault
public class StubLoginModule implements ILoginModule {

    private static final Logger logger = LoggerFactory.getLogger(StubLoginModule.class);

    private static final String STUB_LOGIN_KEY = "StubLoginModuleKey";
    private final IPersistentStorage storage;

    public StubLoginModule(IPersistentStorage storage) {
        this.storage = storage;
    }

    @Override
    public void login(ILoginRequest request, IListener<ILoginResult> listener) {
        if ("login".equals(request.getLogin()) && "password".equals(request.getPassword())) {
            storage.put(STUB_LOGIN_KEY, LoginStatus.LOGGED);
            listener.onResult(() -> LoginStatus.LOGGED);
        } else {
            listener.onFailure(new CommonError() {
                @Override
                public ErrorType getType() {
                    return ErrorType.SERVER;
                }

                @Override
                public String getMessage() {
                    return "Failed to login";
                }
            });
        }
    }

    @Override
    public LoginStatus getLoginStatus() {
        return storage.get(STUB_LOGIN_KEY, LoginStatus.class, LoginStatus.NOT_LOGGED);
    }

    @Override
    public void cancelRequest(IProviderRequest request) {
        logger.debug("cancelRequest() called with: request = [{}]", request);
    }
}
