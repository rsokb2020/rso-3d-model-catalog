package si.fri.rso.kb6750.model3dcatalog.api.v1.filters;

import si.fri.rso.kb6750.model3dcatalog.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.UUID;

@Provider
@ApplicationScoped
@PreMatching
public class ResponseHeaderFilter implements ContainerResponseFilter {

    @Inject
    private RestProperties restProperties;


    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        containerRequestContext.getHeaders().add("request-chain", restProperties.getRequestChainHeader());
        containerResponseContext.getHeaders().add("request-chain", restProperties.getRequestChainHeader());
    }
}