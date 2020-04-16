package org.github.tjg.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoTask {

    @Task("DemoTask")
    public void test() {
        log.info("it's a demo task");
    }
}
