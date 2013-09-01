package com.step.web.builder;

import com.step.core.container.StepContainer;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/23/13
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepContainerBuilder<I> {
    StepContainer build(I input);
}
