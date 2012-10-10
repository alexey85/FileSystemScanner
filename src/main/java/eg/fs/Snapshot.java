package eg.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Evgin
 * Date: 05.10.12
 * Time: 22:50
 */
public class Snapshot {

    private List<SnapshotItem> snapshotItems = new ArrayList<SnapshotItem>();

    private static class SnapshotItem {
        private String path;
        private long size;
        private long modified;

        private SnapshotItem() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SnapshotItem)) return false;

            SnapshotItem that = (SnapshotItem) o;

            if (modified != that.modified) return false;
            if (size != that.size) return false;
            if (!path.equals(that.path)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + (int) (size ^ (size >>> 32));
            result = 31 * result + (int) (modified ^ (modified >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "SnapshotItem{" +
                    "path='" + path + '\'' +
                    ", size=" + size +
                    ", modified=" + modified +
                    '}';
        }
    }

    private Snapshot() {}

    public static Snapshot create(File directory) {
        Snapshot snapshot = new Snapshot();
        snapshot.snapshotItems = new ArrayList<SnapshotItem>();
        processDirectory(directory, snapshot.snapshotItems);
        return snapshot;
    }

    private static void processDirectory(File directory, List<SnapshotItem> snapshots) {
        snapshots.add(createSnapshotItem(directory));
        for (File file : directory.listFiles()) {
            if (file.isDirectory())
                processDirectory(file, snapshots);
            else
                snapshots.add(createSnapshotItem(file));
        }
    }

    private static SnapshotItem createSnapshotItem(File file) {
        SnapshotItem snapshotItem = new SnapshotItem();
        snapshotItem.path = file.getAbsolutePath();
        snapshotItem.modified = file.lastModified();
        snapshotItem.size = file.length();
        return snapshotItem;
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "snapshotItems=" + snapshotItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Snapshot)) return false;

        Snapshot snapshot = (Snapshot) o;

        if (snapshotItems != null ? !snapshotItems.equals(snapshot.snapshotItems) : snapshot.snapshotItems != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return snapshotItems != null ? snapshotItems.hashCode() : 0;
    }
}
