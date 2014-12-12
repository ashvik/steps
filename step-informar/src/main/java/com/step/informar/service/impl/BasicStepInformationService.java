package com.step.informar.service.impl;

import com.step.core.Configuration;
import com.step.core.chain.StepChain;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.repository.StepRepository;
import com.step.informar.flat.*;
import com.step.informar.service.StepInformationService;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.*;

/**
 * Created by amishra on 6/21/14.
 */
public class BasicStepInformationService implements StepInformationService {
    private StepRepository stepRepository;
    private Map<String, StepChainInfo> stepChainInfoMap = new HashMap<String, StepChainInfo>();

    @Override
    public void setStepRepository(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public Set<String> fetchAllConfiguredRequests() {
        TreeSet<String> requests = new TreeSet<String>();
        requests.addAll(stepRepository.getAllRequestsByName());
        return Collections.unmodifiableSet(requests);
    }

    @Override
    public Set<String> fetchAllConfiguredSteps() {
        TreeSet<String> steps = new TreeSet<String>();
        steps.addAll(stepRepository.getAllStepsByName());
        return Collections.unmodifiableSet(steps);
    }

    @Override
    public StepChainInfo getStepChainInfoForRequest(String request) {
        StepChainInfo stepChainInfo = stepChainInfoMap.get(request);

        if(stepChainInfo == null){
            StepChain chain = stepRepository.getStepExecutionChainForRequest(request);
            stepChainInfo = new StepChainInfo();
            stepChainInfo.prepareStepChainInfo(chain);
            stepChainInfoMap.put(request, stepChainInfo);
        }
        return stepChainInfo;
    }

    @Override
    public List<String> getStepChainInfoDiagramForRequest(String request) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public StepInfo getStepDetails(String name) {
        StepDefinitionHolder holder = stepRepository.getStepByName(name);
        StepInfo info = new StepInfo();
        info.setStepName(holder.getName());
        info.setDescription("-DESCRIPTION-");
        return info;
    }

    @Override
    public void generateDocumentation(String versionString,Configuration configuration) {
        XmlCommentParser xmlCommentParser = new XmlCommentParser();
        Map<String, String> serviceToCommentsMap = xmlCommentParser.parseComments(configuration.getStepConfigurationFiles());
        String defaultService = null;
        String menu = prepareMenu();
        writeFile(menu, "menu", true);

        Map<String,String> contents = prepareContent(serviceToCommentsMap);
        for(String service : contents.keySet()){
            if(defaultService == null)defaultService = service;
            writeFile(contents.get(service), service, true);
        }

        String header = prepareHeader(versionString);
        writeFile(header, "header", true);

        String framedContents = prepareFramedContent(defaultService);
        writeFile(framedContents, "index", true);

        String cssContent = getTemplate("templates/bootstrap.min.css");
        writeFile(cssContent, "bootstrap.min.css", false);
    }

    private void writeFile(String content, String name, boolean isHtml){
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("doc/"+name+(isHtml ? ".html" :"")), "utf-8"));
            writer.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {writer.close();} catch (Exception ex) {ex.printStackTrace();}
        }
    }

    private String prepareMenu(){
        Set<String> services = fetchAllConfiguredRequests();
        String menuTemplate = getTemplate("templates/menu.template");
        StringBuilder sb = new StringBuilder();

        for(String service : services){
            sb.append("<li><a href=\""+service+".html\" target=\"content\"><font size=\"1\" color=\"\">"+service+"</font></a></li>\n");
        }

        menuTemplate = menuTemplate.replaceAll("\\$SERVICES\\$", sb.toString());

        return menuTemplate;
    }

    private Map<String, String> prepareContent(Map<String, String> serviceToCommentsMap){
        Map<String,String> serviceContent = new HashMap();
        Set<String> services = fetchAllConfiguredRequests();
        String contentTemplate = getTemplate("templates/content.template");

        for(String service : services){
            int stepNo = 1;
            int inputs = 1;
            int plugins = 1;
            StepChainInfo stepChainInfo = getStepChainInfoForRequest(service);
            List<StepInfo> steps = stepChainInfo.getDefaultExecutionChain();
            String content = contentTemplate.replaceAll("\\$REQUEST\\$", service);
            String description = serviceToCommentsMap.get(service);

            StringBuilder builder = new StringBuilder();
            for(int i=0 ; i<steps.size() ; i++){
                StepInfo stepInfo = steps.get(i);
                String name = stepInfo.getStepName()+(stepInfo.getInterceptorType() != null ? " <span class=\"label label-info\">("+stepInfo.getInterceptorType()+")" : "");
                builder.append("<code>("+stepNo+")&nbsp;"+name+"</code></span><br>\n");
                stepNo++;
            }
            content = content.replaceAll("\\$STEPS\\$", builder.toString());

            content = content.replaceAll("\\$DESCRIPTION\\$",description == null ? "NA" : description);

            builder = new StringBuilder();
            for(String input : stepChainInfo.getInputs()){
                builder.append("<span class=\"label label-important\">"+input+"</span>&nbsp;\n");
                inputs++;
            }
            content = content.replaceAll("\\$INPUTS\\$", builder.toString());

            content = content.replaceAll("\\$OUTPUT\\$", stepChainInfo.getExpectedOutCome()==null?"NA" : "<span class=\"label label-success\">"+stepChainInfo.getExpectedOutCome()+"</span>");

            builder = new StringBuilder();
            if(stepChainInfo.getPlugIns().isEmpty()){
                builder.append("NA");
            }else{
                for(String plugin : stepChainInfo.getPlugIns()){
                    builder.append("<a href=\""+plugin+".html\">"+"<span class=\"label label-info\">"+plugin+"</span></a>&nbsp;\n");
                    plugins++;
                }
            }
            content = content.replaceAll("\\$PLUGINS\\$", builder.toString());

            serviceContent.put(service, content);
        }

        return serviceContent;
    }

    private String prepareHeader(String projectVersion){
        String headerTemplate = getTemplate("templates/header.template");
        headerTemplate = headerTemplate.replaceAll("\\$SOFTWARE_VERSION\\$", projectVersion);
        return headerTemplate;
    }

    private String prepareFramedContent(String defaultContent){
        String indexTemplate = getTemplate("templates/index.template");
        indexTemplate = indexTemplate.replaceAll("\\$CONTENT\\$", defaultContent);
        return indexTemplate;
    }

    private String getTemplate(String path){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(BasicStepInformationService.class.getClassLoader().getResourceAsStream(path)));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private class XmlCommentParser implements LexicalHandler {
        private Map<String, String> comments = new HashMap<String, String>();
        public void startDTD(String name, String publicId, String systemId)
                throws SAXException {}
        public void endDTD() throws SAXException {}
        public void startEntity(String name) throws SAXException {}
        public void endEntity(String name) throws SAXException {}
        public void startCDATA() throws SAXException {}
        public void endCDATA() throws SAXException {}

        public void comment (char[] text, int start, int length)
                throws SAXException {

            String comment = new String(text, start, length);
            int starting = comment.indexOf('[')+1;
            int ending = comment.indexOf(']');

            if(starting > -1 && ending > -1){
                String service = comment.substring(starting, ending);
                comment = comment.substring(ending+1, comment.length());

                this.comments.put(service, comment);
            }
        }

        public Map<String, String> parseComments(String[] args) {

            // set up the parser
            XMLReader parser;
            try {
                parser = XMLReaderFactory.createXMLReader();
            }
            catch (SAXException e) {
                try {
                    parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
                }
                catch (SAXException e2) {
                    System.err.println("Error: could not locate a parser.");
                    return null;
                }
            }

            // turn on comment handling
            try {
                parser.setProperty("http://xml.org/sax/properties/lexical-handler",
                        this);
            }
            catch (SAXNotRecognizedException e) {
                System.err.println(
                        "Installed XML parser does not provide lexical events...");
                return null;
            }
            catch (SAXNotSupportedException e) {
                System.err.println(
                        "Cannot turn on comment processing here");
                return null;
            }

            if (args.length == 0) {
                System.out.println("Usage: java SAXCommentReader URL1 URL2...");
            }

            // start parsing...
            for (int i = 0; i < args.length; i++) {

                try {
                    InputStream stream = XmlCommentParser.class.getClassLoader().getResourceAsStream(args[i]);
                    parser.parse(new InputSource(stream));
                }
                catch (SAXParseException e) { // well-formedness error
                    System.out.println(args[i] + " is not well formed.");
                    System.out.println(e.getMessage()
                            + " at line " + e.getLineNumber()
                            + ", column " + e.getColumnNumber());
                }
                catch (SAXException e) { // some other kind of error
                    System.out.println(e.getMessage());
                }
                catch (IOException e) {
                    System.out.println("Could not check " + args[i]
                            + " because of the IOException " + e);
                }

            }

            return this.comments;

        }
    }
}
