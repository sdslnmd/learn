package com.engineer.sun.continuation;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SimpleSuspendResumeServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 6112996063962978130L;

    private MyAsyncHandler myAsyncHandler;

    public void init() throws ServletException {

        myAsyncHandler = new MyAsyncHandler() {
            public void register(final MyHandler myHandler) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(10000);
                            myHandler.onMyEvent("complete!");
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block  
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        };

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // if we need to get asynchronous results  
        //Object results = request.getAttribute("results");  
        final PrintWriter writer = response.getWriter();
        final Continuation continuation = ContinuationSupport
                .getContinuation(request);
        //if (results == null) {  
        if (continuation.isInitial()) {

            //request.setAttribute("results","null");  
            sendMyFirstResponse(response);
            // suspend the request  
            continuation.suspend(); // always suspend before registration  

            // register with async service. The code here will depend on the  
            // the service used (see Jetty HttpClient for example)  
            myAsyncHandler.register(new MyHandler() {
                public void onMyEvent(Object result) {
                    continuation.setAttribute("results", result);

                    continuation.resume();
                }
            });
            return; // or continuation.undispatch();  
        }

        if (continuation.isExpired()) {
            sendMyTimeoutResponse(response);
            return;
        }
        //Send the results
        Object results = request.getAttribute("results");
        if (results == null) {
            response.getWriter().write("why reach here??");
            continuation.resume();
            return;
        }
        sendMyResultResponse(response, results);
    }

    private interface MyAsyncHandler {
        public void register(MyHandler myHandler);
    }

    private interface MyHandler {
        public void onMyEvent(Object result);
    }

    private void sendMyFirstResponse(HttpServletResponse response) throws IOException {
        //必须加上这一行，否者flush也没用，为什么？  
        response.setContentType("text/html");
        response.getWriter().write("start");
        response.getWriter().flush();

    }

    private void sendMyResultResponse(HttpServletResponse response,
                                      Object results) throws IOException {
        //response.setContentType("text/html");  
        response.getWriter().write("results:" + results);
        response.getWriter().flush();

    }

    private void sendMyTimeoutResponse(HttpServletResponse response)
            throws IOException {
        response.getWriter().write("timeout");

    }

}  