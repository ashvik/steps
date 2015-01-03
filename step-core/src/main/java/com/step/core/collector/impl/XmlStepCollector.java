package com.step.core.collector.impl;

import com.step.core.Configuration;
import com.step.core.PluginRequest;
import com.step.core.alias.RequestAliasProvider;
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
import com.step.core.provider.MappedRequestDetailsProvider;
import com.step.core.xml.model.*;
import com.step.core.xml.parse.StepConfigurationParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class XmlStepCollector implements StepCollector {
    private GenericRequestParameterProvider genericRequestParameterProvider;
    private RequestAliasProvider requestAliasProvider;
    private MappedRequestDetailsProvider mappedRequestDetailsProvider;

    public XmlStepCollector(GenericRequestParameterProvider genericRequestParameterProvider, RequestAliasProvider requestAliasProvider,
                            MappedRequestDetailsProvider mappedRequestDetailsProvider){
        this.genericRequestParameterProvider = genericRequestParameterProvider;
        this.requestAliasProvider = requestAliasProvider;
        this.mappedRequestDetailsProvider = mappedRequestDetailsProvider;
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

            for(Alias alias : mapper.getRequestAliases()){
                requestAliasProvider.collectRequestAlias(alias.getName(), alias.getRequest());
            }

            for(MultiScopedStep mss : mapper.getMultiScopedSteps()){
                StepDefinitionHolder holder = new StepDefinitionHolder(mss.getName());
                for(Scope scope : mss.getScopes()){
                    holder.addScope(scope.getRequest(), scope.getNextStep());
                }
                definitions.add(holder);
            }
            for(MapRequest mr : mapper.getMapRequests()){
                MappedRequestDetailsHolder requestDetailsHolder = new MappedRequestDetailsHolder();
                requestDetailsHolder.setRootStep(mr.getRootStep());
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
                    for(Plugins plugins : mr.getPlugins()){
                        PluginRequest pluginRequest = new PluginRequest(plugins.getStep(), plugins.getType(), plugins.getPlugins());
                        requestDetailsHolder.addPluginEvent(pluginRequest);
                    }
                }

                mappedRequestDetailsProvider.addMappedRequestDetails(mr.getRequest(), requestDetailsHolder);

                allJumpers.addAll(mr.getJumpers());
                allBreakers.addAll(mr.getBreaker());
                allRepeaters.addAll(mr.getRepeaters());
            }

            processJumpers(allJumpers);
            processBreakers(allBreakers);
            processRepeaters(allRepeaters);
        }catch(Exception e){
            e.printStackTrace();
        }

        return definitions;
    }

    private void processJumpers(List<Jumper> allJumpers) throws ClassNotFoundException {
        for(Jumper jumper : allJumpers){
            JumpDetails details = new JumpDetails();
            details.setConditionClass(Class.forName(jumper.getConditionClass()));
            details.setOnSuccessJumpStep(jumper.getOnSuccessJumpTo());
            details.setOnFailureJumpStep(jumper.getOnFailureJumpTo());
            mappedRequestDetailsProvider.getMappedRequestDetails(jumper.getRequest()).addJumpExecutionDecisionEvent(details, jumper.getForStep());
        }
    }

    private void processBreakers(List<Breaker> allBreakers) throws ClassNotFoundException {
        for(Breaker breaker : allBreakers){
            BreakDetails details = new BreakDetails();
            details.setConditionClass(Class.forName(breaker.getConditionClass()));
            mappedRequestDetailsProvider.getMappedRequestDetails(breaker.getRequest()).addBreakExecutionDecisionEvent(details, breaker.getForStep());
        }
    }

    private void processRepeaters(List<Repeater> allRepeaters) throws ClassNotFoundException {
        for(Repeater repeater : allRepeaters){
            RepeatDetails details = new RepeatDetails();
            details.setConditionClass(Class.forName(repeater.getConditionClass()));
            details.setRepeatFromStep(repeater.getRepeatFromStep());
            mappedRequestDetailsProvider.getMappedRequestDetails(repeater.getRequest()).addRepeatExecutionDecisionEvent(details, repeater.getForStep());
        }
    }
}
