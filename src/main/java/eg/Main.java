package eg;

import eg.fs.Action;
import eg.fs.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * User: Evgin
 * Date: 05.10.12
 * Time: 23:43
 */
public class Main {

    public static void main(String[] args) {
        final String url = args[0];
        DirectoryScanner scanner = new DirectoryScanner(url, TimeUnit.SECONDS, Integer.valueOf(args[1]));
        new File(url + "\\logs").mkdir();
        scanner.addOnChangeAction(new Action() {

            Logger log = LoggerFactory.getLogger(getClass());

            @Override
            public void execute() {
                try {
                    Process process = Runtime.getRuntime().exec("cmd /c cd " + url + " && mvn clean install");
                    InputStream in = process.getInputStream();

                    File file = new File(url + "\\logs\\" + System.currentTimeMillis() + "_build.log");
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, bytesRead);
                    }
                    int exitVal = process.waitFor();
                    fos.write(String.valueOf("exit code: " + exitVal).getBytes());
                    fos.flush();
                    fos.close();
                } catch (Throwable t) {
                    log.error("", t);
                }
            }
        });
        new Thread(scanner).start();
    }

}
