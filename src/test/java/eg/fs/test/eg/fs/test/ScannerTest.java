package eg.fs.test.eg.fs.test;

import eg.fs.DirectoryScanner;

import java.util.concurrent.TimeUnit;

/**
 * User: Evgin
 * Date: 05.10.12
 * Time: 22:32
 */
public class ScannerTest {
    public static void main(String[] args) {
        DirectoryScanner scanner = new DirectoryScanner("d:\\_tmp", TimeUnit.SECONDS, 5);
        new Thread(scanner).start();
    }
}
