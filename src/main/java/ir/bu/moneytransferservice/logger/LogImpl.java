package ir.bu.moneytransferservice.logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LogImpl implements Log {
    private static Log INSTANCE = null;
    private final Map<String, Integer> freq = new ConcurrentHashMap<>();
    private final AtomicInteger counterForLogs = new AtomicInteger(1);
    @Value("log.txt")
    String fileLogName = "log.txt";

    LogImpl() throws IOException {
        File logFile = new File(fileLogName);
        logFile.createNewFile();
    }

    public static Log getInstance() throws IOException {
        if (INSTANCE == null) {
            synchronized (Log.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LogImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void log(Object o, String msg) {
        freq.put(o.toString(), counterForLogs.getAndIncrement());
        String textLine = " # ID=" + freq.get(o.toString()) + " [ " + LocalDateTime.now() + " ] " +
                " === " + o + "  ===  " + msg;
        try (FileOutputStream fos = new FileOutputStream(fileLogName, true)) {
            byte[] bytes = (textLine + "\n").getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
