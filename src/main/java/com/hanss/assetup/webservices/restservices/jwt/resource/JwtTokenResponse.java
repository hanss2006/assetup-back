package com.hanss.assetup.webservices.restservices.jwt.resource;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public class JwtTokenResponse implements Serializable {

  private static final long serialVersionUID = 8317676219297719109L;

  private final String token;
  private final String[] roles;

  // eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpbjI4bWludXRlcyIsImV4cCI6MTYxNzczNjQxOSwiaWF0IjoxNjE3MTMxNjE5fQ.5efBMia-sR4NFw-T2xVWATXIf-5thH_w7buvMM66GB-CL5uI0yJbG_BRzXZtfWq2adkQx7S42B66ffagTq2cZg
    public JwtTokenResponse(String token, Collection<? extends GrantedAuthority> roles) {
        this.token = token;
        String[] roleArray = new String[roles.size()];

        for (int i = 0; i < roles.size(); i++) {
            roleArray[i] = roles.iterator().next().getAuthority();
        }
        this.roles = roleArray;
    }

    public String getToken() {
        return this.token;
    }
    public String[] getRoles() { return roles; }
}
