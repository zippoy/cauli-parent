package org.cauli.server.test;

import org.cauli.server.CauliServer;
import org.cauli.server.Configuration;
import org.cauli.server.router.Router;

/**
 * Created by tianqing.wang on 2014/9/2
 */
public class MyServer extends CauliServer{

    @Override
    public void configRoute(Router router) {

    }

    @Override
    public void configServer(Configuration configuration) {
        configuration.setPort(4444);
        configuration.setStaticFile("public");

    }



    public static void main(String[] args) throws Exception {
        new MyServer().start();
    }
}