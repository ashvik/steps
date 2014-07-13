package com.step.core.example;

import com.step.core.container.StepExecutionContainer;
import com.step.informar.service.StepInformationService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/6/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Runner {
    public static void main(String args[]) throws Exception {
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-stepCore-Example.xml");
        StepExecutionContainer container = appContext.getBean(StepExecutionContainer.class);
       StepInformationService informationService = appContext.getBean(StepInformationService.class);

        List<String> printInfo = informationService.getStepChainInfoDiagramForRequest("runExternal");

        for(String str : printInfo){
            System.out.println(str);
        }

        //AddingXXXXXXXXXXXXX
        /*Map<String, String> data = new HashMap();
        data.put("id","1");
        data.put("name","name_1");
        data.put("value", "value_1");
        data.put("description", "description_1");

        StepInput input = new StepInput("add", data);
        ExecutionResult<BusinessObject> result = container.submit(input);*/

        //Fetching
        /*data = new HashMap();
        data.put("id","1");

        input = new StepInput("fetch", data);
        result = container.submit(input);*/

        Map map = new HashMap();
        List<Integer> count = new ArrayList<Integer>();
        count.add(1);
        count.add(2);
        count.add(3);
        //map.put("test1","test");
        //map.put("test","test");
        //map.put("break","test");
        //map.put("breakNew","test");
        //StepInput input1 = new StepInput("jumpTestNew", count);
        //input1.setInput(map);
        container.submit("jumpTestNew", count,map);
       // Class c = (Class)((ParameterizedType)count.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
       // System.out.println(c);

    }
}
