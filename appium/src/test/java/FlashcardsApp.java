/*
 *
 * =======================================================================
 *
 * Copyright (c) 2014-2015 Domlex Limited. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Domlex Limited.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Domlex Limited.
 *
 * =======================================================================
 *
 */

import io.appium.java_client.AppiumDriver;

public class FlashcardsApp {

    private final AppiumDriver driver;

    public FlashcardsApp(AppiumDriver driver) {
        this.driver = driver;
    }

    public LoginPage loginPage() {
        return new LoginPage(driver);
    }

}
