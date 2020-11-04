package si.fri.rso.kb6750.model3dcatalog.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

import si.fri.rso.kb6750.model3dcatalog.lib.ImageMetadata;
import si.fri.rso.kb6750.model3dcatalog.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcatalog.services.beans.ImageMetadataBean;
import si.fri.rso.kb6750.model3dcatalog.services.beans.Model3dMetadataBean;

@ApplicationScoped
@Path("/models3d")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class Model3dMetadataResource {
    private Logger log = Logger.getLogger(Model3dMetadataResource.class.getName());

    @Inject
    private Model3dMetadataBean model3dMetadataBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getModel3dMetadata() {

        List<Model3dMetadata> model3dMetadata = model3dMetadataBean.getModel3dMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();
    }

    @GET
    @Path("/{model3dMetadataId}")
    public Response getModel3dMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        Model3dMetadata model3dMetadata = model3dMetadataBean.getModel3dMetadata(model3dMetadataId);

        if (model3dMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();
    }

    @POST
    public Response createModel3dMetadata(Model3dMetadata model3dMetadata) {

        if ((model3dMetadata.getTitle() == null || model3dMetadata.getDescription() == null || model3dMetadata.getUri() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            model3dMetadata = model3dMetadataBean.createModel3dMetadata(model3dMetadata);
        }

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();

    }

    @PUT
    @Path("{model3dMetadataId}")
    public Response putModel3dMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId,
                                       Model3dMetadata model3dMetadata) {

        model3dMetadata = model3dMetadataBean.putModel3dMetadata(model3dMetadataId, model3dMetadata);

        if (model3dMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{model3dMetadataId}")
    public Response deleteModel3dMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        boolean deleted = model3dMetadataBean.deleteModel3dMetadata(model3dMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
