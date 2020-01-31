package org.apache.skywalking.oap.server.core.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author MrPro
 */
@Setter
@Getter
public class ProfileAnalyze {

    // thread stack dump analyze tree
    private List<ThreadStackAnalyze> stack = new LinkedList<>();

}
