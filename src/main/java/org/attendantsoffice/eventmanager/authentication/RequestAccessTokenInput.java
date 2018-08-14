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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Request made when asking for a new access token.
 */
public class RequestAccessTokenInput {
    @NotEmpty
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
