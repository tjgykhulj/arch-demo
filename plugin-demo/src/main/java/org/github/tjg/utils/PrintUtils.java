package org.github.tjg.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class PrintUtils {
    private static AtomicBoolean active;
    private static Thread t;
    private static BlockingQueue<String> msgQueue;

    // 怀疑gradle重复执行时，static没有重置
    public static void initPrintUtils() {
        active = new AtomicBoolean(true);
        msgQueue = new LinkedBlockingQueue<>();
        t = new Thread(() -> {
            while (active.get() || msgQueue.size() > 0){
                try {
                    String msg = msgQueue.poll(1, TimeUnit.SECONDS);
                    if (!StringUtils.isEmpty(msg)){
                        System.out.println(msg);
                    }
                } catch (InterruptedException e) {
                    log.warn("HttpUtils", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static void stopAndWait(){
        active.set(false);
        try {
            int count=0;
            while (t.isAlive() && count<4){
                Thread.sleep(1000);
                count++;
            }
        } catch (InterruptedException e) {
            log.warn("PrintUtils", e);
            Thread.currentThread().interrupt();
        }
    }

    public static void printSuccess(String msg){
        if (active.get()){
            msgQueue.add("🙆[Plugin]> "+msg);
        }
    }

    public static void printError(String msg){
        if (active.get()){
            msgQueue.add("🙅[Plugin]> "+msg);
        }
    }

    public static void print(String msg) {
        if (active.get()){
            msgQueue.add(msg);
        }
    }
}
