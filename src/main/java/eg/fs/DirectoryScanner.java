package eg.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: Evgin
 * Date: 05.10.12
 * Time: 22:34
 */
public class DirectoryScanner implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private String url;
    private TimeUnit timeUnit;
    private long time;

    private List<Action> onChangeActions = new ArrayList<Action>();

    public DirectoryScanner(String url, TimeUnit timeUnit, long time) {
        this.url = url;
        this.timeUnit = timeUnit;
        this.time = time;
    }

    @Override
    public void run() {
        File directory = new File(url);
        if (!directory.exists())
            throw new RuntimeException(String.format("'%s' don't exists", url));
        if (!directory.isDirectory())
            throw new RuntimeException(String.format("'%s' isn't directory ", url));
        Snapshot prevSnapshot = Snapshot.create(directory);
        while (true) {
            try {
                Snapshot snapshot = Snapshot.create(directory);
                log.trace(prevSnapshot.toString());
                log.trace(snapshot.toString());
                if (snapshot.equals(prevSnapshot)) {
                    log.trace(String.format("'%s' did not change", url));
                } else {
                    log.trace(String.format("'%s' changed", url));
                    execute(onChangeActions);
                }
                prevSnapshot = Snapshot.create(directory);
                timeUnit.sleep(time);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void execute(List<Action> actions) {
        for (Action action : actions) {
            action.execute();
        }
    }

    public void addOnChangeAction(Action action) {
        onChangeActions.add(action);
    }
}
