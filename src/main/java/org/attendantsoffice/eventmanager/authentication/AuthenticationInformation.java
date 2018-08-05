/*
 * Copyright (c) 2018 by Hotspring Ventures Limited
 * 1 Regent Street (c/o Calder & Co), London SW1Y 4NW
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Hotspring Ventures Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Hotspring Ventures Limited.
 */
package org.attendantsoffice.eventmanager.authentication;

/**
 * Basic information about the possibly authenticated user.
 *
 */
public class AuthenticationInformation {
    private boolean authenticated;
    private String name;

    public static AuthenticationInformation notAuthenticated() {
        AuthenticationInformation information = new AuthenticationInformation();
        return information;
    }

    public static AuthenticationInformation authenticated(String name) {
        AuthenticationInformation information = new AuthenticationInformation();
        information.authenticated = true;
        information.name = name;
        return information;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getName() {
        return name;
    }

}
