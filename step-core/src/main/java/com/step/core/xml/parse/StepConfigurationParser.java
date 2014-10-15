package com.step.core.xml.parse;

import com.step.core.xml.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/24/13
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepConfigurationParser {
    public StepRequestMapper parse(InputStream in) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(in);
        doc.getDocumentElement().normalize();
        StepRequestMapper root = new StepRequestMapper();

        NodeList genSteps = doc.getElementsByTagName("multiScopedSteps");
        if(genSteps != null ){
            Node node = genSteps.item(0);
            Element ele = (Element)node;
            if(ele != null){
                NodeList multiScopedSteps = ele.getElementsByTagName("step");
                for(int i=0 ; i<multiScopedSteps.getLength() ; i++){
                    Node mss = multiScopedSteps.item(i);
                    Element msele = (Element)mss;

                    if(msele != null){
                        MultiScopedStep multiScopedStep = new MultiScopedStep(msele.getAttribute("name"));
                        NodeList scopes = msele.getElementsByTagName("scope");
                        for(int s=0 ; s<scopes.getLength() ; s++){
                            Node scope = scopes.item(s);
                            Element scopeEle = (Element)scope;

                            if(scopeEle != null){
                                String request = scopeEle.getAttribute("request");
                                String nextStep = scopeEle.getAttribute("nextStep");

                                Scope scp = new Scope(request, nextStep);
                                multiScopedStep.addScope(scp);
                            }
                        }

                        root.addMultiScopedStep(multiScopedStep);
                    }
                }
            }
        }

        NodeList nList = doc.getElementsByTagName("mappedRequest");

        for(int i=0 ; i<nList.getLength() ; i++){
            Node node = nList.item(i);
            MapRequest mr = buildMapRequest(node);

            if(mr != null){
                root.add(mr);
            }
        }

        NodeList genericParameters = doc.getElementsByTagName("parameterConfiguration");
        for(int i=0; i<genericParameters.getLength(); i++){
            Node node = genericParameters.item(i);

            GenericParameterConfiguration genericParameterConfiguration = buildGenericParameters(node);

            if(genericParameterConfiguration != null){
                root.addGenericParameterConfiguration(genericParameterConfiguration);
            }
        }

        return root;
    }

    private MapRequest buildMapRequest(Node node){
        if(node.getNodeType() != Node.ELEMENT_NODE){
            return null;
        }

        MapRequest mr = new MapRequest();
        Element ele = (Element)node;

        String canUseGenericSteps = ele.getAttribute("applyGenericSteps");
        mr.setRequest(ele.getAttribute("request"));
        mr.setRootStep(ele.getAttribute("rootStep"));
        mr.setOnSuccess(ele.getAttribute("onSuccess"));
        mr.setOnFailure(ele.getAttribute("onFailure"));
        mr.setApplyGenericSteps(canUseGenericSteps.isEmpty() ? true : Boolean.valueOf(canUseGenericSteps));

        //populating pre steps....
        NodeList preStepNodes = ele.getElementsByTagName("preSteps");
        populateInterceptorSteps(mr, preStepNodes, true);

        //populating post steps....
        NodeList postStepNodes = ele.getElementsByTagName("postSteps");
        populateInterceptorSteps(mr, postStepNodes, false);

        //populating pluginRequests....
        NodeList pluginRequestsNode = ele.getElementsByTagName("pluginRequest");
        populatePluginRequests(mr, pluginRequestsNode);

        //populating jumpers....
        NodeList jumpers = ele.getElementsByTagName("jumper");
        populateJumpers(mr, jumpers);

        //populating breakers....
        NodeList breakers = ele.getElementsByTagName("breaker");
        populateBreakers(mr, breakers);

        //populating repeaters....
        NodeList repeaters = ele.getElementsByTagName("repeater");
        populateRepeaters(mr, repeaters);

        //populating repeaters....
        NodeList parameters = ele.getElementsByTagName("parameter");
        populateParameter(mr, parameters);

        //populating generic params....
        NodeList genParams = ele.getElementsByTagName("configuration");
        populateGenericParams(mr, genParams);

        //populating contract
        NodeList contract = ele.getElementsByTagName("contract");
        populateContract(mr, contract);

        //populating contract
        NodeList exceptionHandler = ele.getElementsByTagName("exceptionHandler");
        populateExceptionHandler(mr, exceptionHandler);

        return mr;
    }

    private GenericParameterConfiguration buildGenericParameters(Node node){
        if(node.getNodeType() != Node.ELEMENT_NODE){
            return null;
        }
        Element ele = (Element)node;
        GenericParameterConfiguration genericParameterConfiguration = new GenericParameterConfiguration(ele.getAttribute("name"));

        NodeList params = ele.getElementsByTagName("parameter");
        populateGenericParameter(genericParameterConfiguration, params);

        return genericParameterConfiguration;
    }

    private void populateJumpers(MapRequest mr, NodeList nodes) {
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Jumper jumper = new Jumper();
                Element e = (Element)nodes.item(i);
                jumper.setRequest(mr.getRequest());
                jumper.setForStep(e.getAttribute("forStep"));
                jumper.setConditionClass(e.getAttribute("conditionClass"));
                jumper.setOnSuccessJumpTo(e.getAttribute("onSuccessJumpTo"));
                jumper.setOnFailureJumpTo(e.getAttribute("onFailureJumpTo"));

                mr.addJumper(jumper);
            }
        }
    }

    private void populateBreakers(MapRequest mr, NodeList nodes) {
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Breaker breaker = new Breaker();
                Element e = (Element)nodes.item(i);
                breaker.setRequest(mr.getRequest());
                breaker.setForStep(e.getAttribute("forStep"));
                breaker.setConditionClass(e.getAttribute("conditionClass"));

                mr.addBreaker(breaker);
            }
        }
    }

    private void populateRepeaters(MapRequest mr, NodeList nodes) {
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Repeater repeater = new Repeater();
                Element e = (Element)nodes.item(i);
                repeater.setRequest(mr.getRequest());
                repeater.setForStep(e.getAttribute("forStep"));
                repeater.setConditionClass(e.getAttribute("conditionClass"));
                repeater.setRepeatFromStep(e.getAttribute("repeatFromStep"));

                mr.addRepeater(repeater);
            }
        }
    }

    private void populateParameter(MapRequest mr, NodeList nodes) {
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Element e = (Element)nodes.item(i);
                Parameter parameter = new Parameter(e.getAttribute("name"));
                mr.addParameter(parameter);
                NodeList values = e.getElementsByTagName("value");

                for(int j=0 ; j<values.getLength() ; j++){
                    Node val = values.item(j);
                    parameter.addValue(val.getTextContent());
                }
            }
        }
    }

    private void populateGenericParameter(GenericParameterConfiguration mr, NodeList nodes) {
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Element e = (Element)nodes.item(i);
                Parameter parameter = new Parameter(e.getAttribute("name"));
                mr.addParameter(parameter);
                NodeList values = e.getElementsByTagName("value");

                for(int j=0 ; j<values.getLength() ; j++){
                    Node val = values.item(j);
                    parameter.addValue(val.getTextContent());
                }
            }
        }
    }

    private void populateInterceptorSteps(MapRequest mr, NodeList nodes, boolean isPreStep) {
        if(nodes != null && nodes.getLength()>0){
            Node node = nodes.item(0);
            Element ele = (Element)node;
            NodeList steps = ele.getElementsByTagName("step");
            if(steps.getLength() > 0){
                buildSteps(steps, mr, isPreStep);
            }
        }
    }

    private void populatePluginRequests(MapRequest mr, NodeList nodes) {
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Element e = (Element)nodes.item(i);
                Plugins p = new Plugins(e.getAttribute("name"));
                String runAuto = e.getAttribute("runAutomatically");
                if(runAuto != null && !runAuto.isEmpty()){
                    p.setApplyAutomatically(Boolean.valueOf(runAuto));
                }
                mr.addPlugin(p);
            }
        }
    }

    private void populateGenericParams(MapRequest mr, NodeList nodes) {
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Element e = (Element)nodes.item(i);
                mr.addGenericParameter(e.getAttribute("ref"));
            }
        }
    }

    private void populateContract(MapRequest mr, NodeList nodes) {
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Element e = (Element)nodes.item(i);
                Contract contract = new Contract();
                mr.setContract(contract);
                NodeList reqinputs = e.getElementsByTagName("requiredInputs");
                populateRequiredInputs(contract, reqinputs);

                NodeList expectedOutput = e.getElementsByTagName("expectedOutcome");
                if(expectedOutput != null && expectedOutput.getLength()>0){
                    for(int j=0 ; j<expectedOutput.getLength() ; j++){
                        Element element = (Element)expectedOutput.item(j);
                        String out = element.getTextContent().trim();
                        contract.setExpectedOutCome(out);
                    }
                }
            }
        }
    }

    private void populateRequiredInputs(Contract contract, NodeList nodes){
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Element e = (Element)nodes.item(i);
                NodeList reqinputs = e.getElementsByTagName("inputType");

                if(reqinputs != null && reqinputs.getLength()>0){
                    for(int j=0 ; j<reqinputs.getLength() ; j++){
                        Element element = (Element)reqinputs.item(j);
                        String in = element.getTextContent().trim();
                        contract.addInputType(in);
                    }
                }
            }
        }
    }

    private void populateExceptionHandler(MapRequest mr, NodeList nodes){
        if(nodes != null && nodes.getLength()>0){
            for(int i=0 ; i<nodes.getLength() ; i++){
                Element e = (Element)nodes.item(i);
                String val = e.getTextContent().trim();
                mr.setStepExceptionHandler(val);
            }
        }
    }

    private void buildSteps(NodeList list, MapRequest mr, boolean isPreStep){
        for(int i=0 ; i<list.getLength(); i++){
            Node node = list.item(i);
            if(isPreStep){
                mr.addPreSteps(node.getTextContent());
            }else{
                mr.addPostSteps(node.getTextContent());
            }
        }
    }
}
