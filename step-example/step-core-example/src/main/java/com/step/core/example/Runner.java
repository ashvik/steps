package com.step.core.example;

import com.step.core.BasicConfiguration;
import com.step.core.StepExecutionContainerProvider;
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
        /*String str = "[CREATE_SPEED_LIMIT_PATH]\n" +
                "       Service does following:<br><br>\n" +
                "\n" +
                "       (1) First prepare Configuration which will guide PathExplorer to explore the path. The configuration used is\n" +
                "           &nbsp;&nbsp; [SPEED_LIMIT_PATH_PARAMS].<br>\n" +
                "       (2) Determine the start link to calculate the speed limit path using plugin [UPDATE_SOURCE_LINK_IN_NON_SPLIT_CASE].\n" +
                "           If link is not split and geotag is near to the exit node within required distance then,\n" +
                "           next link will be the start link otherwise current link will be the start link.<br>\n" +
                "       (3) Calculate the path for speed limit using plugin CODE_PATH.";

        int i = str.indexOf('[');
        int j = str.indexOf(']');

        System.out.println(str.substring(i+1,j));
        System.out.println(str.substring(j+1, str.length()));*/


        HashMap map = new HashMap();
        map.size();
            int h = 0;
            h ^= 7*7;//k.hashCode();

            // This function ensures that hashCodes that differ only by
            // constant multiples at each bit position have a bounded
            // number of collisions (approximately 8 at default load factor).
            h ^= (h >>> 20) ^ (h >>> 12);
            int result =  h ^ (h >>> 7) ^ (h >>> 4);

        System.out.println(result & 15);


        List<? super Set> l = new ArrayList();

        l.add(new HashSet());

        Set s = (Set)l.get(0);
        System.out.println(s);
       /* List objs = new ArrayList();

        boolean b = true;
        objs.add(b);

        System.out.println();*/
        /*Class cls = List.class;
        System.out.println(cls.getName());*/

       BasicConfiguration configuration = new BasicConfiguration();
        configuration.setStepPackages("com.step.core.example.steps","com.step.core.steps","com.step.external.steps");
        configuration.setStepConfigurationFiles("step-example.xml",
                "external-steps-conf.xml",
                "step-request-params.xml");

        DocumentationGenerator.generateDocumentation(configuration, "S/W version");

        //List<String> printInfo = informationService.getStepChainInfoDiagramForRequest("runExternal");

       /* for(String str : printInfo){
            System.out.println(str);
        }*/

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

        //Map map = new HashMap();
        /*List<ResponsiveStep> count = new ArrayList<ResponsiveStep>();
       // count.add(new ExecutePluginRequests());

        Set<ResponseLessStep> countStr = new HashSet<ResponseLessStep>();
        countStr.add(new JumpTestStep2());
        countStr.add(new JumpTestStep3());


        StepInput input1 = new StepInput("jumpTestNew");
        input1.setInput(count);
        input1.setInput(countStr);

        List<Integer> c = input1.getListTypeInput(AbstractResponsiveStep.class);
        System.out.println(c);
        System.out.println(input1.getSetTypeInput(String.class));*/

        //map.put("test1","test");
        //map.put("test","test");
        //map.put("break","test");
        //map.put("breakNew","test");
        //StepInput input1 = new StepInput("jumpTestNew", count);
        //input1.setInput(map);
        /*try {
        container.submit("jumpTest");
        }catch (Exception e){
            System.out.println("-----> " + e.getMessage());
        }*/
       // Class c = (Class)((ParameterizedType)count.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
       // System.out.println(c);

        //System.out.println(Parameter.class.getSimpleName());

       /* String str = "com.navteq.map.coder.plugin.rmob.input.coding.common.FeatureCodingInput";

        String[] token = str.split(" of ");
        System.out.println(token[0]);*/

        ConfigurableApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext-stepCore-Example.xml");
        Map<String, String> data = new HashMap();
        data.put("id","1");
        data.put("name","name_1");
        data.put("value", "value_1");
        data.put("description", "description_1");
        StepInput input1 = new StepInput("fetch");
        input1.setInput(data);
        StepExecutionContainerProvider.INSTANCE.getStepExecutionContainer().submit(input1);
    }
}
