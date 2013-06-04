package com.step.core.xml.parse;

import com.step.core.xml.model.MapRequest;
import com.step.core.xml.model.MultiScopedStep;
import com.step.core.xml.model.Scope;
import com.step.core.xml.model.StepRequestMapper;
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

        NodeList nList = doc.getElementsByTagName("mappedrequest");

        for(int i=0 ; i<nList.getLength() ; i++){
            Node node = nList.item(i);
            MapRequest mr = buildMapRequest(node);

            if(mr != null){
                root.add(mr);
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
        mr.setApplyGenericSteps(canUseGenericSteps.isEmpty() ? true : Boolean.valueOf(canUseGenericSteps));

        //populating pre steps....
        NodeList preStepNodes = ele.getElementsByTagName("preSteps");
        populateInterceptorSteps(mr, preStepNodes, true);

        //populating post steps....
        NodeList postStepNodes = ele.getElementsByTagName("postSteps");
        populateInterceptorSteps(mr, postStepNodes, false);

        return mr;
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