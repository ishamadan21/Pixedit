package com.example.recruitment.pixedit;

import android.app.Application;

import com.aviary.android.feather.sdk.IAviaryClientCredentials;


/**
 *
 *Secret       cda0985c-d2e4-4f4f-8606-45f61ed5dc8f

 Client_id      ba66d615e3ac4b31883d0833b3d86de4

 */

/**
 * Created by recruitment on 16/06/15.
 */
public class PixEditApplication extends Application implements IAviaryClientCredentials{

    private static final String CLIENT_ID = "ba66d615e3ac4b31883d0833b3d86de4";
private static final String CLIENT_SECRET="cda0985c-d2e4-4f4f-8606-45f61ed5dc8f";
    @Override
    public String getBillingKey() {
        return "";
    }

    @Override
    public String getClientID() {
        return CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CLIENT_SECRET;
    }
}
