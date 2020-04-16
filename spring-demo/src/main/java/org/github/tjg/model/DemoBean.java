package org.github.tjg.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class DemoBean {
    @Value("${avalue}")
    private String a;
    @Value("${bvalue}")
    private String b;
}
