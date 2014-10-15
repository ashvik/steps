package com.step.core.collector.impl;

import com.step.core.Configuration;
import com.step.core.PluginRequest;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.collector.StepCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.parameter.GenericRequestParameterProvider;
import com.step.core.parameter.ParameterNameValueHolder;
import com.step.core.parameter.RequestParameterContainer;
import com.step.core.parameter.impl.BasicRequestParameterContainer;
import com.step.core.xml.model.*;
import com.step.core.xml.parse.StepConfigurationParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class XmlStepCollector implements StepCollector {
    private GenericRequestParameterProvider genericRequestParameterProvider;

    public XmlStepCollector(GenericRequestParameterProvider genericRequestParameterProvider){
        this.genericRequestParameterProvider = genericRequestParameterProvider;
    }

    @Override
    public List<StepDefinitionHolder> collect(Configuration conf) {
        List<StepDefinitionHolder> definitions = new ArrayList<StepDefinitionHolder>();
        String[] xmls = conf.getStepConfigurationFiles();

        for(String xml : xmls){
            definitions.addAll(makeFromConfigurationFile(xml));
        }
        return definitions;
    }

    private List<StepDefinitionHolder> makeFromConfigurationFile(String xmfFile){
        List<StepDefinitionHolder> definitions = new ArrayList<StepDefinitionHolder>();
        List<Jumper> allJumpers = new ArrayList<Jumper>();
        List<Breaker> allBreakers = new ArrayList<Breaker>();
        List<Repeater> allRepeaters = new ArrayList<Repeater>();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmfFile);
        StepConfigurationParser parser = new StepConfigurationParser();

        try{
            StepRequestMapper mapper = parser.parse(in);

            for(GenericParameterConfiguration genericParameterConfiguration : mapper.getGenericParameterConfigurations()){
                String name = genericParameterConfiguration.getName();
                for(Parameter parameter : genericParameterConfiguration.getParameters()){
                    genericRequestParameterProvider.addRequestParameterNameValue(name, new ParameterNameValueHolder(parameter.getName(), parameter.getValues()));
                }
            }

            for(MultiScopedStep mss : mapper.getMultiScopedSteps()){
                StepDefinitionHolder holder = new StepDefinitionHolder(mss.getName());
                MappedRequestDetailsHolder requestDetailsHolder = new MappedRequestDetailsHolder();
                holder.setMappedRequestDetailsHolder(requestDetailsHolder);
                for(Scope scope : mss.getScopes()){
                    holder.addScope(scope.getRequest(), scope.getNextStep());
                }
                definitions.add(holder);
            }
            for(MapRequest mr : mapper.getMapRequests()){
                StepDefinitionHolder holder = new StepDefinitionHolder(mr.getRootStep());
                MappedRequestDetailsHolder requestDetailsHolder = new MappedRequestDetailsHolder();
                holder.setMappedRequestDetailsHolder(requestDetailsHolder);
                List<Parameter> parameters = mr.getParameters();
                Contract contract = mr.getContract();

                if(!parameters.isEmpty()){
                    RequestParameterContainer requestParameterContainer = new BasicRequestParameterContainer();
                    for(Parameter parameter : parameters){
                        requestParameterContainer.addRequestParameter(parameter.getName(), parameter.getValues());
                    }

                    requestDetailsHolder.setRequestParameterContainer(requestParameterContainer);
                }

                requestDetailsHolder.setStepExceptionHandler(mr.getStepExceptionHandler());
                requestDetailsHolder.setMappedRequest(mr.getRequest());
                requestDetailsHolder.setCanApplyGenericSteps(mr.isApplyGenericSteps());
                requestDetailsHolder.setPreSteps(mr.getPreSteps());
                requestDetailsHolder.setPostSteps(mr.getPostSteps());
                requestDetailsHolder.setOnSuccess(mr.getOnSuccess());
                requestDetailsHolder.setOnFailure(mr.getOnFailure());
                requestDetailsHolder.addGenericParameter(mr.getGenericParameters());

                if(contract != null){
                    requestDetailsHolder.setExpectedOutCome(contract.getExpectedOutCome());
                    requestDetailsHolder.setInputTypes(contract.getInputTypes());
                }

                if(!mr.getPlugins().isEmpty()){
                    List<PluginRequest> pluginRequests = new ArrayList<PluginRequest>();
                    for(Plugins plugins : mr.getPlugins()){
                        PluginRequest pluginRequest = new PluginRequest(plugins.getPlugin(), plugins.isApplyAutomatically());
                        pluginRequests.add(pluginRequest);
                    }

                    requestDetailsHolder.addPluginRequests(mr.getRequest(), pluginRequests);
                }

                definitions.add(holder);

                allJumpers.addAll(mr.getJumpers());
                allBreakers.addAll(mr.getBreaker());
                allRepeaters.addAll(mr.getRepeaters());
            }

            processJumpers(allJumpers, definitions);
            processBreakers(allBreakers, definitions);
            processRepeaters(allRepeaters, definitions);
        }catch(Exception e){
            e.printStackTrace();
        }

        return definitions;
    }

    private void processJumpers(List<Jumper> allJumpers, List<StepDefinitionHolder> definitions) throws ClassNotFoundException {
        Map<String, StepDefinitionHolder> localCache = new HashMap<String, StepDefinitionHolder>();
        for(Jumper jumper : allJumpers){
            StepDefinitionHolder h = localCache.get(jumper.getForStep());

            if(h == null){
                h = new StepDefinitionHolder(jumper.getForStep());
                MappedRequestDetailsHolder requestDetailsHolder = new MappedRequestDetailsHolder();
                h.setMappedRequestDetailsHolder(requestDetailsHolder);
                definitions.add(h);
                localCache.put(jumper.getForStep(), h);
            }
            JumpDetails details = new JumpDetails();
            details.setConditionClass(Class.forName(jumper.getConditionClass()));
            details.setOnSuccessJumpStep(jumper.getOnSuccessJumpTo());
            details.setOnFailureJumpStep(jumper.getOnFailureJumpTo());
            h.addJumpDetails(jumper.getRequest(), details);
        }
    }

    private void processBreakers(List<Breaker> allBreakers, List<StepDefinitionHolder> definitions) throws ClassNotFoundException {
        Map<String, StepDefinitionHolder> localCache = new HashMap<String, StepDefinitionHolder>();
        for(Breaker breaker : allBreakers){
            StepDefinitionHolder h = localCache.get(breaker.getForStep());

            if(h == null){
                h = new StepDefinitionHolder(breaker.getForStep());
                MappedRequestDetailsHolder requestDetailsHolder = new MappedRequestDetailsHolder();
                h.setMappedRequestDetailsHolder(requestDetailsHolder);
                definitions.add(h);
                localCache.put(breaker.getForStep(), h);
            }

            BreakDetails details = new BreakDetails();
            details.setConditionClass(Class.forName(breaker.getConditionClass()));
            h.addBreakDetails(breaker.getRequest(), details);

        }
    }

    private void processRepeaters(List<Repeater> allRepeaters, List<StepDefinitionHolder> definitions) throws ClassNotFoundException {
        for(Repeater repeater : allRepeaters){
            StepDefinitionHolder h = new StepDefinitionHolder(repeater.getForStep());
            MappedRequestDetailsHolder requestDetailsHolder = new MappedRequestDetailsHolder();
            h.setMappedRequestDetailsHolder(requestDetailsHolder);
            RepeatDetails details = new RepeatDetails();
            details.setConditionClass(Class.forName(repeater.getConditionClass()));
            details.setRepeatFromStep(repeater.getRepeatFromStep());
            h.addRepeatDetails(repeater.getRequest(), details);
            definitions.add(h);
        }
    }
}
