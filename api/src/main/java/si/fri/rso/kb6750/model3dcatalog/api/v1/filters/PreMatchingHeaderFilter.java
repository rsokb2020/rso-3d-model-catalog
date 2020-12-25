package si.fri.rso.kb6750.model3dcatalog.api.v1.filters;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.kb6750.model3dcatalog.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.UUID;

@Log
@Provider
@ApplicationScoped
@PreMatching
public class PreMatchingHeaderFilter implements ContainerRequestFilter {

    @Inject
    private RestProperties restProperties;

    @Override
    public void filter(ContainerRequestContext ctx)  {

        String idFromHeader = ctx.getHeaderString("request-chain");
        if(idFromHeader == null){
            idFromHeader = UUID.randomUUID().toString();
        }
        restProperties.setRequestChainHeader(idFromHeader);
    }
}