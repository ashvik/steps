package com.step.core.example;

import com.step.core.container.StepContainer;
import com.step.core.io.StepInput;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/6/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Runner {
    public static void main(String args[]){
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-stepCore-Example.xml");
        StepContainer container = appContext.getBean(StepContainer.class);

        //Adding
        /*Map<String, String> data = new HashMap();
        data.put("id","1");
        data.put("name","name_1");
        data.put("value", "value_1");
        data.put("description", "description_1");

        StepInput input = new StepInput("add", data);
        ExecutionResult<BusinessObject> result = container.submit(input);

        //Fetching
        data = new HashMap();
        data.put("id","1");

        input = new StepInput("fetch", data);
        result = container.submit(input);*/

        Map map = new HashMap();
        //map.put("testNew","test");
        //map.put("breakNew","test");
        StepInput input1 = new StepInput("jumpTestNew", map);
        container.submit(input1);

    }
}
