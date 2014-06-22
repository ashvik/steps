package com.step.web.servlet;

import com.step.core.Attributes;
import com.step.core.container.StepExecutionContainer;
import com.step.core.exceptions.StepContainerExecutionException;
import com.step.core.io.StepInput;
import com.step.web.WebExecutionResult;
import com.step.web.builder.StepContainerBuilder;
import com.step.web.builder.impl.StepWebContainerBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/23/13
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepFrameworkServlet extends HttpServlet {
    private StepExecutionContainer stepExecutionContainer;

    public void init(ServletConfig config){
        try{
            super.init(config);
            ServletContext servletContext = getServletContext();
            StepContainerBuilder<ServletContext> builder = new StepWebContainerBuilder();
            stepExecutionContainer = builder.build(servletContext);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String path = req.getPathInfo();
        String request = path.substring(1, path.length());
        StepInput input = new StepInput(request, makeAttributes(req));

        WebExecutionResult result = null;
        try {
            result = (WebExecutionResult) stepExecutionContainer.submit(input);
        } catch (Exception e) {
            throw new StepContainerExecutionException(e.getMessage());
        }

        req.setAttribute("result", result.getResultObject());
        Attributes attrs = result.getAttributes();
        for(String name : attrs.getAttributeName()){
            req.getSession(false).setAttribute(name, attrs.getAttribute(name));
        }

        RequestDispatcher view = req.getRequestDispatcher(result.getResource());
        view.forward(req, resp);
    }

    private Attributes makeAttributes(HttpServletRequest req){
        Map<String, String[]> params = req.getParameterMap();
        HttpSession session =  req.getSession(false);
        Enumeration<String> names = session.getAttributeNames();
        Attributes attrs = new Attributes();

        for(String key : params.keySet()){
            attrs.addAttribute(key, params.get(key));
        }

        while(names.hasMoreElements()){
            String name = names.nextElement();
            attrs.addAttribute(name, session.getAttribute(name));
        }

        return attrs;
    }
}
