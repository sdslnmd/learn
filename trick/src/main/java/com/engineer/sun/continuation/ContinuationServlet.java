package com.engineer.sun.continuation;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: luning.sun
 * Date: 12-10-19
 * Time: 下午12:43
 */
public class ContinuationServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //得到applition对象
        ServletContext applition = getServletConfig().getServletContext();
        //获取continuation
        Continuation continuation = ContinuationSupport.getContinuation(req);
        //设定超时时间、可以不设置。默认为30秒
        continuation.setTimeout(10);
        //阻塞
        continuation.suspend();
        //如果applition中有msg这个字符。开放阻塞
        if (applition.getAttribute("msg").toString() != null) {
            continuation.complete();
            resp.getWriter().print(
                    applition.getAttribute("msg").toString()
            );
        }
    }
}
