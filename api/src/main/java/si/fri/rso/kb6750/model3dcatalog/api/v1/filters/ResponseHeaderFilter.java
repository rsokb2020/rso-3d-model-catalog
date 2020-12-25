package si.fri.rso.kb6750.model3dcatalog.api.v1.filters;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.kb6750.model3dcatalog.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.UUID;

@Log
@Provider
@ApplicationScoped
public class ResponseHeaderFilter implements ContainerResponseFilter {

    @Inject
    private RestProperties restProperties;

    @Context
    HttpHeaders httpHeaders;

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        System.out.println("Request header: " + containerRequestContext.getHeaders().toString());
        System.out.println("Request header: " + containerResponseContext.getHeaders().toString());
        System.out.println("Request header: " + containerRequestContext.getHeaderString("request-chain"));
        System.out.println("Request header: " + containerResponseContext.getHeaders().toString());
    }
}