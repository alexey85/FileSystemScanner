package eg.fs.test.eg.fs.test;

import eg.fs.Snapshot;

import java.io.File;

/**
 * User: Evgin
 * Date: 05.10.12
 * Time: 23:15
 */
public class ShapshotTest {

    public static void main(String[] args) {
        String url = "d:\\work\\projects\\FileSystemScanner";
        Snapshot snapshot = Snapshot.create(new File(url));
        System.out.println(snapshot.toString());
    }

}
