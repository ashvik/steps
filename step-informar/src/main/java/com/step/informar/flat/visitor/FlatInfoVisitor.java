package com.step.informar.flat.visitor;

import com.step.informar.flat.*;

/**
 * Created by amishra on 6/21/14.
 */
public interface FlatInfoVisitor {
    void visitStepInfo(StepInfo stepInfo);
    void visitJumpInfo(JumpInfo jumpInfo);
    void visitBreakInfo(BreakInfo breakInfo);
    void visitRepeatInfo(RepeatInfo repeatInfo);
}
