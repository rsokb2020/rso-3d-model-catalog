package si.fri.rso.kb6750.model3dcatalog.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.json.JSONObject;
import si.fri.rso.kb6750.model3dcatalog.config.RestProperties;
import si.fri.rso.kb6750.model3dcatalog.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcatalog.models.converters.Model3dMetadataConverter;
import si.fri.rso.kb6750.model3dcatalog.models.entities.Model3dMetadataEntity;

@RequestScoped
public class Model3dMetadataBean {
    private Logger log = Logger.getLogger(Model3dMetadataBean.class.getName());

    @Inject
    private EntityManager em;
    @Inject
    private RestProperties restProperties;

    public List<Model3dMetadata> getModel3dMetadata() {

        TypedQuery<Model3dMetadataEntity> query = em.createNamedQuery(
                "Model3DMetadataEntity.getAll", Model3dMetadataEntity.class);

        List<Model3dMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(Model3dMetadataConverter::toDto).collect(Collectors.toList());

    }

    public List<Model3dMetadata> getModel3dMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        List<Model3dMetadata> model3dMetadataList = JPAUtils.queryEntities(em, Model3dMetadataEntity.class, queryParameters).stream()
                .map(Model3dMetadataConverter::toDto).collect(Collectors.toList());

        return model3dMetadataList;
    }

    public Model3dMetadata getModel3dMetadata(Integer id) {

        Model3dMetadataEntity model3dMetadataEntity = em.find(Model3dMetadataEntity.class, id);

        if (model3dMetadataEntity == null) {
            throw new NotFoundException();
        }

        Model3dMetadata model3dMetadata = Model3dMetadataConverter.toDto(model3dMetadataEntity);

        if(model3dMetadata.getNormals() == null || model3dMetadata.getVertices() == null){
            model3dMetadata = parseModel3dMetadata(model3dMetadata);
        }

        return model3dMetadata;
    }

    @Timeout(value = 4, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "parseModelFallback")
    public Model3dMetadata parseModel3dMetadata(Model3dMetadata model3dMetadata){
        try {
            String url = restProperties.getParserServiceIp();
            System.out.println("This is the url: " + url);
            URL obj = new URL(url);

            JSONObject json = new JSONObject();

            json.put("title", model3dMetadata.getTitle());
            json.put("description", model3dMetadata.getDescription());
            String binaryArraydataStr = model3dMetadata.getBinary();
            String binaryArraydataAssetStr = model3dMetadata.getAssetBundleBinaryArray();
            json.put("binaryArrayString", binaryArraydataStr);
            json.put("assetBundleBinaryArray", binaryArraydataAssetStr);

            HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setRequestProperty("request-chain", restProperties.getRequestChainHeader());
            postConnection.setDoOutput(true);

            OutputStream os = postConnection.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();
            os.close();
            int responseCode = postConnection.getResponseCode();
            System.out.println("POST Response Code :  " + responseCode);
            System.out.println("POST Response Message : " + postConnection.getResponseMessage());

            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK ) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in .readLine()) != null) {
                    response.append(inputLine);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                // System.out.println("NUmber of vertices: " + response.toString());
                model3dMetadata.setVertices((long)Integer.parseInt(jsonResponse.get("numberOfVertices").toString()));
                model3dMetadata.setNormals((long)Integer.parseInt(jsonResponse.get("numberOfFaces").toString()));

                in.close();

                // System.out.println(response.toString());
            } else {
                System.out.println("POST NOT WORKED");
            }


        } catch (Exception ex) {
            System.out.println(ex);
            return model3dMetadata;
        }
        return model3dMetadata;
    }

    // Return existing model without parsing for extra info (we're asuming the parser does not work)
    public Model3dMetadata parseModelFallback(Model3dMetadata model3dMetadata){
        return model3dMetadata;
    }

    public Model3dMetadata createModel3dMetadata(Model3dMetadata model3dMetadata) {

        Model3dMetadataEntity model3dMetadataEntity = Model3dMetadataConverter.toEntity(model3dMetadata);

        try {
            beginTx();
            em.persist(model3dMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (model3dMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return Model3dMetadataConverter.toDto(model3dMetadataEntity);
    }

    public Model3dMetadata putModel3dMetadata(Integer id, Model3dMetadata model3dMetadata) {

        Model3dMetadataEntity c = em.find(Model3dMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        Model3dMetadataEntity updatedModel3dMetadataEntity = Model3dMetadataConverter.toEntity(model3dMetadata);

        try {
            beginTx();
            updatedModel3dMetadataEntity.setId(c.getId());
            updatedModel3dMetadataEntity = em.merge(updatedModel3dMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return Model3dMetadataConverter.toDto(updatedModel3dMetadataEntity);
    }

    public boolean deleteModel3dMetadata(Integer id) {

        Model3dMetadataEntity model3dMetadata = em.find(Model3dMetadataEntity.class, id);

        if (model3dMetadata != null) {
            try {
                beginTx();
                em.remove(model3dMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
