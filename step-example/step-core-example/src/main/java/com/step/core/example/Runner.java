package com.step.core.example;

import com.step.core.Configuration;
import com.step.core.StepExecutionContainerProvider;
import com.step.core.example.steps.IParameter;
import com.step.core.example.steps.Parameter;
import com.step.core.io.StepInput;
import com.step.informer.DocumentationGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/6/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Runner {
    public static void main(String args[]) throws Exception {
        ConfigurableApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext-stepCore-Example.xml");

        runSteps();
        //generateDocumentation(context.getBean(Configuration.class));

    }

    private static void runSteps() throws Exception {
        Map<String, String> data = new HashMap();
        List<IParameter> parameters = new ArrayList<IParameter>();
        Set<String> strings = new HashSet<String>();
        data.put("id","1");
        data.put("name","name_1");
        data.put("value", "value_1");
        data.put("description", "description_1");
        parameters.add(new Parameter());
        strings.add("Ashish");
        StepInput input1 = new StepInput("add");
        input1.setInput(data);
        input1.setInput(parameters);
        input1.setInput(strings);
        StepExecutionContainerProvider.INSTANCE.getStepExecutionContainer().submit(input1);
    }

    private static void generateDocumentation(Configuration configuration){
        DocumentationGenerator.generateDocumentation(configuration, "version");
    }
}
