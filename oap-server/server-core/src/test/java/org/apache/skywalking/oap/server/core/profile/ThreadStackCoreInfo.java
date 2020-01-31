package org.apache.skywalking.oap.server.core.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author MrPro
 */
@Setter
@Getter
public class ThreadStackCoreInfo {

    private long dumpTime;
    private int sequence;
    private List<String> stackElements;

}
