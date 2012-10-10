package eg;

import eg.fs.Action;
import eg.fs.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * User: Evgin
 * Date: 05.10.12
 * Time: 23:43
 */
public class Main {

    public static void main(String[] args) {
        final String url = args[0];
        final File targetDir = new File(url);
        final File logDir = new File(targetDir, "logs");
        logDir.mkdirs();
        DirectoryScanner scanner = new DirectoryScanner(url, TimeUnit.SECONDS, Integer.valueOf(args[1]));
        scanner.addOnChangeAction(new Action() {

            Logger log = LoggerFactory.getLogger(getClass());

            @Override
            public void execute() {
                try {

                    File logFile = new File(logDir, System.currentTimeMillis() + "_build.log");
                    log.info("starting new process, log file :" + logFile.getName());
                    Process process = new ProcessBuilder("mvn", "clean", "package")
                            .directory(targetDir)
                            .redirectOutput(logFile)
                            .redirectError(logFile)
                            .start();
                    int exitVal = process.waitFor();
                    log.info("exit code: " + exitVal);
                } catch (Throwable t) {
                    log.error("", t);
                }
            }
        });
        new Thread(scanner).start();
    }

}
