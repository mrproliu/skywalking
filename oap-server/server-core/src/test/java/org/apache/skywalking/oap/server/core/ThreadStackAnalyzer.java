package org.apache.skywalking.oap.server.core;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.skywalking.apm.network.language.profile.ThreadStack;
import org.apache.skywalking.oap.server.core.profile.ProfileAnalyze;
import org.apache.skywalking.oap.server.core.profile.ProfileTaskSegmentSnapshotRecord;
import org.apache.skywalking.oap.server.core.profile.ThreadStackAnalyze;
import org.apache.skywalking.oap.server.core.profile.ThreadStackCoreInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrPro
 */
public class ThreadStackAnalyzer {

    public static ProfileAnalyze analyze(List<ProfileTaskSegmentSnapshotRecord> records) {
        // transform data
        List<ThreadStackCoreInfo> stacks = records.stream().map(ThreadStackAnalyzer::transformInfo).collect(Collectors.toList());

        // init analyze
        ProfileAnalyze profileAnalyze = new ProfileAnalyze();
        profileAnalyze.getStack().add(initStackAnalyze(stacks.get(0)));

        // each stack and fill stack trees
        for (int i = 1; i < stacks.size(); i++) {
            ThreadStackCoreInfo stackCoreInfo = stacks.get(i);

            // find element analyze tree node
            String currentStackFirstElement = stackCoreInfo.getStackElements().get(0);
            ThreadStackAnalyze currentAnalyzeTree = null;
            for (ThreadStackAnalyze threadStackAnalyze : profileAnalyze.getStack()) {
                if (threadStackAnalyze.matches(currentStackFirstElement)) {
                    currentAnalyzeTree = threadStackAnalyze;
                    break;
                }
            }

            if (currentAnalyzeTree == null) {
                // cannot found tree, add new stack tree
                ThreadStackAnalyze threadStackAnalyze = initStackAnalyze(stackCoreInfo);
                profileAnalyze.getStack().add(threadStackAnalyze);
            } else {
                // increment node data
                increaseSelfAndChildNodes(currentAnalyzeTree, stackCoreInfo);
            }
        }

        // calculate duration and exclude child duration
        for (ThreadStackAnalyze stackAnalyze : profileAnalyze.getStack()) {
            fillDuration(stackAnalyze);
        }

        return profileAnalyze;
    }

    /**
     * calculate appoint {@link ThreadStackAnalyze} node with children nodes duration
     * @param stackAnalyze
     */
    private static void fillDuration(ThreadStackAnalyze stackAnalyze) {
        // each and calculate tree from button node, using post order
        // encase stack overflow when use recursion
        LinkedList<ThreadStackAnalyze> stack = new LinkedList<>();
        stack.push(stackAnalyze);
        ThreadStackAnalyze current;
        boolean hasChildNeedCalc;

        while (!stack.isEmpty()) {
            current = stack.peek();
            hasChildNeedCalc = false;

            // push all need calculate child to stack
            if (!current.isHasCalculatedChildDuration() && !current.getChildes().isEmpty()) {
                for (ThreadStackAnalyze child : current.getChildes()) {
                    hasChildNeedCalc = true;
                    stack.push(child);
                }
                current.setHasCalculatedChildDuration(true);
            }


            // don't need to calculate child
            if (!hasChildNeedCalc) {
                // calculate duration
                current.calculateDuration(true);

                // pop current node
                stack.pop();

                // clean up
                current.setParentPaths(Collections.emptyMap());

                // work for multiple invoke this calculate duration method
                current.setHasCalculatedChildDuration(false);
            }
        }

    }

    /**
     * init new stack tree
     * @param stackCoreInfo
     * @return
     */
    private static ThreadStackAnalyze initStackAnalyze(ThreadStackCoreInfo stackCoreInfo) {

        List<String> stackElements = stackCoreInfo.getStackElements();
        long dumpTime = stackCoreInfo.getDumpTime();
        int stackDepth = stackElements.size();
        int sequence = stackCoreInfo.getSequence();

        // init first root
        String firstCodeSign = stackElements.get(0);
        ThreadStackAnalyze root = ThreadStackAnalyze.createNode(dumpTime, firstCodeSign);
        root.getParentPaths().put(firstCodeSign, root);

        ThreadStackAnalyze parent = root;
        for (int depth = 1; depth < stackDepth; depth++) {
            String codeSign = stackElements.get(depth);

            ThreadStackAnalyze currentNode = ThreadStackAnalyze.createNode(dumpTime, codeSign);
            // put parents
            currentNode.getParentPaths().put(codeSign, currentNode);
            currentNode.getParentPaths().putAll(parent.getParentPaths());

            currentNode.setLastInvokedSequence(sequence);

            // set root child to this
            parent.getChildes().add(currentNode);
            parent = currentNode;
        }

        return root;
    }

    /**
     * transform {@link ProfileTaskSegmentSnapshotRecord} to {@link ThreadStackCoreInfo}
     */
    private static ThreadStackCoreInfo transformInfo(ProfileTaskSegmentSnapshotRecord record) {
        ThreadStackCoreInfo info = new ThreadStackCoreInfo();

        info.setDumpTime(record.getDumpTime());
        info.setSequence(record.getSequence());

        try {
            // deserialize stack
            ThreadStack threadStack = ThreadStack.parseFrom(record.getStackBinary());
            info.setStackElements(threadStack.getCodeSignaturesList());

            // clear stack binary
            record.setStackBinary(null);
        } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException("wrong thread stack data");
        }
        return info;
    }

    /**
     * increment stack tree and child nodes
     * @param currentNode
     * @param stackCoreInfo
     */
    private static void increaseSelfAndChildNodes(ThreadStackAnalyze currentNode, ThreadStackCoreInfo stackCoreInfo) {
        // increase self data
        long dumpTime = stackCoreInfo.getDumpTime();
        int sequence = stackCoreInfo.getSequence();
        currentNode.setEndTime(dumpTime);
        currentNode.setCount(currentNode.getCount() + 1);

        HashMap<ThreadStackAnalyze, Integer> recursionCounts = new LinkedHashMap<>();

        ThreadStackAnalyze parent = currentNode;
        for (int childDepth = 1; childDepth < stackCoreInfo.getStackElements().size(); childDepth++) {
            String elementCodeSign = stackCoreInfo.getStackElements().get(childDepth);

            // find current child
            ThreadStackAnalyze childElement = null;
            for (ThreadStackAnalyze child : parent.getChildes()) {
                if (child.matches(elementCodeSign)) {
                    childElement = child;
                    break;
                }
            }

            if (childElement != null) {
                childElement.setCount(childElement.getCount() + 1);

                // judge is invoke continuity
                // work for for-each invoke multiple child method
                // such as for 2 {methodA(); methodB();}
                // when second invoke methodA(), need to reset start time and calculate previous invoke methodA() duration
                if (!(sequence - 1 == childElement.getLastInvokedSequence())) {
                    // calculate self duration and reset dump start time
                    childElement.calculateDuration(false);
                    childElement.setStartTime(dumpTime);
                }

                // change last invoke child
                childElement.setLastInvokedSequence(sequence);

                childElement.setEndTime(dumpTime);
                parent = childElement;
            } else {
                // if not found, try to found it on parent paths
                // work for recursion
                ThreadStackAnalyze sameNode = parent.getParentPaths().get(elementCodeSign);

                if (sameNode != null) {
                    // increment same node recursion count
                    Integer currentIncrementRecursionCount = recursionCounts.getOrDefault(sameNode, 0);
                    currentIncrementRecursionCount++;
                    recursionCounts.put(sameNode, currentIncrementRecursionCount);

                    if (!(sequence - 1 == sameNode.getLastInvokedSequence())) {
                        // calculate self duration and reset dump start time
                        sameNode.calculateDuration(false);
                        sameNode.setStartTime(dumpTime);
                    }

                    // update end time and last invoke sequence
                    sameNode.setEndTime(dumpTime);
                    sameNode.setLastInvokedSequence(sequence);

                    // change parent to recursion node
                    parent = sameNode;
                } else {
                    // if also not found, add child node
                    ThreadStackAnalyze childNode = ThreadStackAnalyze.createNode(dumpTime, elementCodeSign);

                    // clone parent paths
                    if (parent.getChildes().size() > 0) {
                        childNode.setParentPaths(parent.getChildes().get(0).getParentPaths());
                        // include self
                        childNode.getParentPaths().put(elementCodeSign, childNode);
                    }

                    childNode.setLastInvokedSequence(sequence);
                    parent.getChildes().add(childNode);
                    parent = childNode;
                }
            }
        }

        // if has any recursion calculate, check is bigger than old circulate count
        if (!recursionCounts.isEmpty()) {
            for (Map.Entry<ThreadStackAnalyze, Integer> entry : recursionCounts.entrySet()) {
                ThreadStackAnalyze node = entry.getKey();
                Integer count = entry.getValue();
                // upgrade max circulate count
                if (count > node.getMaxCirculateCount()) {
                    node.setMaxCirculateCount(count);
                }
            }
        }
    }

}
