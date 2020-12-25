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
import com.kumuluz.ee.logs.cdi.Log;

import org.apache.commons.codec.binary.Base64;
import si.fri.rso.kb6750.model3dcatalog.lib.ImageMetadata;
import si.fri.rso.kb6750.model3dcatalog.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcatalog.services.beans.ImageMetadataBean;
import si.fri.rso.kb6750.model3dcatalog.services.beans.Model3dMetadataBean;

@Log
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
    @Log
    @GET
    public Response getModel3dMetadata() {

        List<Model3dMetadata> model3dMetadata = model3dMetadataBean.getModel3dMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();
    }
    /*
    @GET
    @Path("/info")
    public Response getModel3dMetadataInfo(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        JSONObject json = new JSONObject();

        json.put("clani", "['kb6750']");
        json.put("opis_projekta", "Aplikacija za nalaganje,shranjevanje in prikazovanje 3d modelov.");
        json.put("mikrostoritve", "['52.142.34.154:8080/v1/models3d','40.88.193.71:8080/v1/parser']");
        // json.put("uri", "This value should get removed soon.");
        json.put("github", "['https://github.com/rsokb2020/rso-3d-model-parser,'https://github.com/rsokb2020/rso-3d-model-catalog']");
        json.put("travis", "['https://github.com/rsokb2020/rso-3d-model-parser/actions','https://github.com/rsokb2020/rso-3d-model-catalog/actions']");
        json.put("dockerhub", "['https://hub.docker.com/repository/docker/klemiba/model-3d-parser'],['https://hub.docker.com/repository/docker/klemiba/model-3d-catalog']");

        return Response.status(Response.Status.OK).entity(json).build();
    }*/
    @Log
    @GET
    @Path("/{model3dMetadataId}")
    public Response getModel3dMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        Model3dMetadata model3dMetadata = model3dMetadataBean.getModel3dMetadata(model3dMetadataId);

        if (model3dMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();
    }
    @Log
    @GET
    @Path("/{model3dMetadataId}/assetBundle")
    public Response getModel3dMetadataAssetBundle(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        Model3dMetadata model3dMetadata = model3dMetadataBean.getModel3dMetadata(model3dMetadataId);

        if (model3dMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(Base64.decodeBase64(model3dMetadata.getAssetBundleBinaryArray())).build();
    }
    @Log
    @POST
    public Response createModel3dMetadata(Model3dMetadata model3dMetadata) {

        if ((model3dMetadata.getTitle() == null || model3dMetadata.getDescription() == null/* || model3dMetadata.getUri() == null*/)) {
            System.out.println(model3dMetadata.getTitle());
            System.out.println(model3dMetadata.getDescription());
            // System.out.println(model3dMetadata.getUri());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            System.out.println(model3dMetadata.toString());
            model3dMetadata = model3dMetadataBean.createModel3dMetadata(model3dMetadata);
        }

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();

    }
    @Log
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
    @Log
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
