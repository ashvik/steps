package com.step.informer.flat;

import com.step.informer.flat.visitor.FlatInfoVisitor;

/**
 * Created by amishra on 6/21/14.
 */
public interface FlatInfo {
    void accept(FlatInfoVisitor visitor);
}
