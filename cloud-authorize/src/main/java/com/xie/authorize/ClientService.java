package com.xie.authorize;

import com.xie.gateway.api.authorize.Oauth2ClientInfo;

public interface ClientService {

    Oauth2ClientInfo queryClientInfo(String clientId);

}
