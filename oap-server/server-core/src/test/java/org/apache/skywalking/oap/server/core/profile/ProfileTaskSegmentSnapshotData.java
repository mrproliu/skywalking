package org.apache.skywalking.oap.server.core.profile;

import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.apm.network.language.profile.ThreadStack;

import java.util.List;

/**
 * Use for deserialize from yml data file
 *
 * @author MrPro
 */
@Setter
@Getter
public class ProfileTaskSegmentSnapshotData extends ProfileTaskSegmentSnapshotRecord {

    private List<String> stackList;

    /**
     * Adapt {@link #stackList} to {@link #setStackBinary(byte[])} ()}
     */
    public void doStackAdapt() {
        ThreadStack.Builder stackBuilder = ThreadStack.newBuilder().addAllCodeSignatures(stackList);
        super.setStackBinary(stackBuilder.build().toByteArray());
    }
}
