package com.triangleleft.flashcards.service.error;

import android.support.annotation.Nullable;

/**
 * Error indicating that there were problems with login.
 */
public class LoginError extends CommonError {

    private final String loginError;
    private final String passwordError;

    private LoginError(@Nullable String message, @Nullable String loginError, @Nullable String passwordError) {
        super(message);
        this.loginError = loginError;
        this.passwordError = passwordError;
        if (message == null && loginError == null && passwordError == null) {
            throw new IllegalArgumentException("At least one field should be non-null");
        }
    }

    /**
     * Get login field error text.
     *
     * @return login field error text
     */
    @Nullable
    public String getLoginError() {
        return loginError;
    }

    /**
     * Get password field error text.
     *
     * @return password field error text.
     */
    @Nullable
    public String getPasswordError() {
        return passwordError;
    }

    /**
     * Helper builder class.
     * At least one of the messages should be non-null;
     */
    public static class Builder {
        private String message;
        private String loginError;
        private String passwordError;

        /**
         * Set error message.
         *
         * @param message general error message
         * @return builder object
         */
        public Builder setMessage(@Nullable String message) {
            this.message = message;
            return this;
        }

        /**
         * Set login error message
         *
         * @param loginError login error message
         * @return builder object
         */
        public Builder setLoginErrorMessage(@Nullable String loginError) {
            this.loginError = loginError;
            return this;
        }

        /**
         * Set password error message.
         *
         * @param passwordError password error message
         * @return builder object
         */
        public Builder setPasswordErrorMessage(@Nullable String passwordError) {
            this.passwordError = passwordError;
            return this;
        }

        /**
         * Build LoginError object.
         *
         * @return LogniError object.
         */
        public LoginError build() {
            return new LoginError(message, loginError, passwordError);
        }


    }
}
