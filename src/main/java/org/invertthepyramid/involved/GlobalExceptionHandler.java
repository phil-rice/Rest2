package org.invertthepyramid.involved;

import org.invertthepyramid.involved.mdm.MDMServiceException;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.function.Function;

class ResponseDetails {
    int status;
    String body;
    String severity;

    public ResponseDetails(int status, String body, String severity) {
        this.status = status;
        this.body = body;
        this.severity = severity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}

public class GlobalExceptionHandler {

//    <From> PartialFunction<Exception, ResponseDetails> handler(Class<From> from, Function<From, ResponseDetails> fn) {
//        return new ClassPartialFunction<Exception, From, ResponseDetails>(from, fn);
//    }
//
//    PartialFunction<Exception, ResponseDetails> illegalStateExeptionHandler =
//            handler(IllegalStateException.class, e -> new ResponseDetails(500, "was illegal state exception" + e.getMessage(), "Fatal"));
//    PartialFunction<Exception, ResponseDetails> involvedExceptionHandler =
//            handler(InvolvedException.class, e -> new ResponseDetails(500, "was InvolvedException" + e.errorKind + e.errorCode, "Fatal"));
//
//    Function<Exception, ResponseDetails> defaultHandler = (e) -> new ResponseDetails(500, "unknown exception" + e, "Fatal");
//    Function<Exception, ResponseDetails> chain = new ChainOfResponsibility<Exception, ResponseDetails>(
//            defaultHandler,
//            illegalStateExeptionHandler,
//            involvedExceptionHandler);

    public void process(Exception e, ResponseDetails details) {
        if (e instanceof IllegalStateException) {
            details.setBody("was illegal state exception" + ((IllegalStateException) e).getMessage());
            details.setStatus(500);
            details.setSeverity("Fatal");
        }
        if (e instanceof InvolvedException) {
            details.setBody("was InvolvedException" + ((InvolvedException) e).errorKind + ((InvolvedException) e).errorCode);
            details.setStatus(((InvolvedException) e).status);
            details.setSeverity("Fatal");
        }
        if (e instanceof MDMServiceException) {
            details.setBody("was MDMServiceException" + ((MDMServiceException) e).getMessage());
            details.setStatus(((MDMServiceException) e).getErrorId() + 500);
            details.setSeverity("Fatal");
        }
    }

}

interface PartialFunction<From, To> extends Function<From, To> {
    boolean isDefinedAt(From from);
}

class ChainOfResponsibility<From, To> implements Function<From, To> {

    Function<From, To> defaultFn;
    List<PartialFunction<From, To>> chain;

    public ChainOfResponsibility(Function<From, To> defaultFn, PartialFunction<From, To>... chain) {
        this.defaultFn = defaultFn;
        this.chain = Arrays.asList(chain);
    }

    @Override
    public To apply(From from) {
        for (PartialFunction<From, To> pfn : chain)
            if (pfn.isDefinedAt(from))
                return pfn.apply(from);
        return defaultFn.apply(from);
    }
}

class ClassPartialFunction<Main, From, To> implements PartialFunction<Main, To> {
    Class<From> fromClass;
    Function<From, To> fn;

    public ClassPartialFunction(Class<From> fromClass, Function<From, To> fn) {
        this.fromClass = fromClass;
        this.fn = fn;
    }

    @Override
    public boolean isDefinedAt(Main main) {
        return fromClass.isAssignableFrom(main.getClass());
    }

    @Override
    public To apply(Main main) {
        return fn.apply((From) main);
    }
}
