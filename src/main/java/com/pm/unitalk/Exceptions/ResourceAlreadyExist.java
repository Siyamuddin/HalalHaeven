package com.pm.unitalk.Exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceAlreadyExist extends RuntimeException {
  private String username;
  private Long id;
    public ResourceAlreadyExist(String username,Long id) {
        super(String.format("User %s with user ID: %d already exists with this email.", username, id));
        this.username=username;
        this.id=id;
    }
}
