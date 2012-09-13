package com.engineer.sun.hessian;

import com.caucho.hessian.server.HessianServlet;

/**
 * User: luning.sun
 * Date: 12-9-13
 * Time: 上午11:23
 */
public class BasicService extends HessianServlet implements BasicAPI {
    private String _geeeting = "Hello world";

    public void set_geeeting(String geeeting) {
        _geeeting=geeeting;
    }
    @Override
    public String hello() {
        return null;
    }
}
