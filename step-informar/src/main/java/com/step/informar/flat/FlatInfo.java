package com.step.informar.flat;

import com.step.informar.flat.visitor.FlatInfoVisitor;

/**
 * Created by amishra on 6/21/14.
 */
public interface FlatInfo {
    void accept(FlatInfoVisitor visitor);
}
