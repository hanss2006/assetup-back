package com.hanss.assetup.webservices.restservices.jwt.resource;

import java.io.Serializable;

public class JwtTokenResponse implements Serializable {

  private static final long serialVersionUID = 8317676219297719109L;

  private final String token;

  // eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpbjI4bWludXRlcyIsImV4cCI6MTYxNzczNjQxOSwiaWF0IjoxNjE3MTMxNjE5fQ.5efBMia-sR4NFw-T2xVWATXIf-5thH_w7buvMM66GB-CL5uI0yJbG_BRzXZtfWq2adkQx7S42B66ffagTq2cZg
    public JwtTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
