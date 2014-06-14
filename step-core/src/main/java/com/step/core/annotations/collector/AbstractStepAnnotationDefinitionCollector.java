package com.step.core.annotations.collector;

import com.step.core.utils.AnnotatedDefinitionCollector;
import com.step.core.utils.PackageScanner;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/18/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStepAnnotationDefinitionCollector implements AnnotatedDefinitionCollector {

    protected Class[] findClassesInPackage(String pack){
        Class[] bds = null;
        if(pack != null){
            try {
                bds = PackageScanner.getClassesInPackage(pack);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bds;
    }
}
