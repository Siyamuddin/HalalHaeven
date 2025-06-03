package com.pm.unitalk.Utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Component
public class APIResponse extends Throwable {
    private String massage;
    private boolean success;
}
