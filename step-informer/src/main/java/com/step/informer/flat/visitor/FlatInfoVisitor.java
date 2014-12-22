package com.step.informer.flat.visitor;

import com.step.informer.flat.*;

/**
 * Created by amishra on 6/21/14.
 */
public interface FlatInfoVisitor {
    void visitStepInfo(StepInfo stepInfo);
    void visitJumpInfo(JumpInfo jumpInfo);
    void visitBreakInfo(BreakInfo breakInfo);
    void visitRepeatInfo(RepeatInfo repeatInfo);
}
