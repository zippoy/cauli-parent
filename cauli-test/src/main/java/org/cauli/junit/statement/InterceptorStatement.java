package org.cauli.junit.statement;

import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import org.cauli.exception.NotFoundNamedMethodException;
import org.cauli.exception.TestFailedError;
import org.cauli.junit.ExcuteScheduler;
import org.cauli.junit.FrameworkMethodWithParameters;
import org.cauli.junit.MethodManager;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class InterceptorStatement extends Statement {
	private Logger logger = LoggerFactory.getLogger(InterceptorStatement.class);
	protected final FrameworkMethodWithParameters testMethod;
    protected Object target;
    private int level;
    private List<InterceptorStatement> dependencyStatement= Lists.newArrayList();
    protected int retryTimes=0;
	public InterceptorStatement(FrameworkMethodWithParameters methodWithParameters, Object target) {
		this.testMethod=methodWithParameters;
		this.target=target;
        String[] depNames = methodWithParameters.getDependencyMethodName();
        if(depNames!=null&&depNames.length>0){
            for(String depName:depNames){
                if(StringUtil.isNotEmpty(depName)){
                    List<FrameworkMethodWithParameters> methods = MethodManager.get(depName);
                    if(methods!=null){
                        for(FrameworkMethodWithParameters method:methods){
                            if(method!=null){
                                this.dependencyStatement.add(new InterceptorStatement(method,target));
                            }
                        }
                    }else{
                        logger.warn("没有找到依赖的Method->name：{}",depName);
                    }
                }
            }
        }


        this.level=methodWithParameters.getLevel();
        this.interceptors.add(new AnnotationInterceptor());
	}
    private List<Interceptor> interceptors = new ArrayList<Interceptor>();

    @Override
	public void evaluate() throws Throwable {
            runRetry();
	}


    public FrameworkMethod getTestMethod() {
        return testMethod;
    }

    public Object getTarget() {
        return target;
    }

    protected void runRetry(){
        for(Interceptor interceptor:interceptors){
            interceptor.interceptorBeforeRetryTimeConfig(this);
        }
        for(int i=0;i<=retryTimes;i++){
            try{
                for(Interceptor interceptor:interceptors){
                    interceptor.interceptorBefore(this);
                }
                if(this.dependencyStatement==null){
                    testMethod.invokeExplosively(target);
                }else{
                    for(final InterceptorStatement statement:this.dependencyStatement){
                        statement.evaluate();
                    }
                        testMethod.invokeExplosively(target);


                }

                for(Interceptor interceptor:interceptors){
                    interceptor.interceptorAfter(this);
                }
                break;
            }catch(Throwable e){
                e.printStackTrace();
                for(Interceptor interceptor:interceptors){
                    interceptor.interceptorAfterForce(this);
                }
                logger.error("用例执行失败了,异常信息->" + e.getMessage());
                if(i==retryTimes){
                    throw new TestFailedError("["+this.testMethod.getName()+"]用例执行失败了！",e);
                }else{
                    logger.info("用例执行失败，重新执行失败的方法-->"+testMethod.getName());
                }
            }
        }
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public void addInterceptor(Interceptor interceptor){
        interceptors.add(interceptor);
    }


    public List<InterceptorStatement> getDependencyStatement() {
        return dependencyStatement;
    }

    public void setDependencyStatement(List<InterceptorStatement> dependencyStatement) {
        this.dependencyStatement = dependencyStatement;
    }
}
