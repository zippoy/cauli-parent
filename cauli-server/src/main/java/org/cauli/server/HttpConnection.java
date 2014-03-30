package org.cauli.server;

import java.util.concurrent.Executor;

public interface HttpConnection extends Executor, DataHolder {
    HttpRequest httpRequest();

    HttpConnection close();

    Executor handlerExecutor();
}
