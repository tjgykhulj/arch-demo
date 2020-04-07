package org.github.tjg;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class MyBean {
    @Value("${avalue}")
    private String a;
    @Value("${bvalue}")
    private String b;
}
