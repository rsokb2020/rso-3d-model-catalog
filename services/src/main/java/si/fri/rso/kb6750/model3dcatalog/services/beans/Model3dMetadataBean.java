package si.fri.rso.kb6750.model3dcatalog.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.kb6750.model3dcatalog.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcatalog.models.converters.Model3dMetadataConverter;
import si.fri.rso.kb6750.model3dcatalog.models.entities.Model3dMetadataEntity;

@RequestScoped
public class Model3dMetadataBean {
    private Logger log = Logger.getLogger(Model3dMetadataBean.class.getName());

    @Inject
    private EntityManager em;

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

        System.out.println("Models in catalog list: ");
        for (Model3dMetadata model3dMetadata : model3dMetadataList) {
            System.out.println(model3dMetadata);
        }

        return model3dMetadataList;
    }

    public Model3dMetadata getModel3dMetadata(Integer id) {

        Model3dMetadataEntity model3dMetadataEntity = em.find(Model3dMetadataEntity.class, id);

        if (model3dMetadataEntity == null) {
            throw new NotFoundException();
        }

        Model3dMetadata model3dMetadata = Model3dMetadataConverter.toDto(model3dMetadataEntity);

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
