package org.apache.skywalking.oap.server.core.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrPro
 */
@Setter
@Getter
public class ProfileTaskSegmentSnapshotDataHolder {

    private List<ProfileTaskSegmentSnapshotData> list;

    public List<ProfileTaskSegmentSnapshotRecord> transform() {
        // adapt stack data
//        ArrayList<ProfileTaskSegmentSnapshotRecord> records = new ArrayList<>(this.list.size());
        return this.list.stream().parallel().map(t -> {
            t.doStackAdapt();
            return t;
        }).collect(Collectors.toList());
//        for (ProfileTaskSegmentSnapshotData data : this.list) {
//            data.doStackAdapt();
//            records.add(data);
//        }
//
//        return records;
    }
}
