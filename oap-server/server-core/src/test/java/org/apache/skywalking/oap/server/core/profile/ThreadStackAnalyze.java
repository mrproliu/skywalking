package org.apache.skywalking.oap.server.core.profile;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author MrPro
 */
@Setter
@Getter
public class ThreadStackAnalyze {

    // stack code signature
    private String codeSign;

    // self include children duration(millisecond)
    private int duration;

    // self exclude children duration(millisecond)
    private int excludeChildDuration;

    // total dump count
    private int count;

    // max circulate invoke count
    // use on recursion
    private int maxCirculateCount;

    // children of this stack code sign
    private List<ThreadStackAnalyze> childes = new LinkedList<>();

    // dump start time
    private long startTime;

    // dump finish time
    private long endTime;

    // current node parents
    // work for recursion method invoke
    private Map<String, ThreadStackAnalyze> parentPaths = new HashMap<>();

    // last invoked sequence, if next invoke this node not continuity, need to calculate duration and reset dump times
    // work for for-each method invoke
    private int lastInvokedSequence;

    // work for for-each nodes to calculate duration
    private boolean hasCalculatedChildDuration = false;

    public static ThreadStackAnalyze createNode(long dumpTime, String codeSign) {
        ThreadStackAnalyze stackAnalyze = new ThreadStackAnalyze();
        stackAnalyze.setCodeSign(codeSign);
        stackAnalyze.setCount(1);
        stackAnalyze.setStartTime(dumpTime);
        stackAnalyze.setEndTime(dumpTime);

        return stackAnalyze;
    }

    public boolean matches(String codeSign) {
        return Objects.equal(this.codeSign, codeSign);
    }

    public void calculateDuration(boolean calcExcludeChildren) {
        this.duration += (endTime - startTime);
        if (calcExcludeChildren) {
            int childUseTime = 0;
            for (ThreadStackAnalyze child : childes) {
                childUseTime += child.getDuration();
            }
            excludeChildDuration = duration - childUseTime;
        }
    }
}