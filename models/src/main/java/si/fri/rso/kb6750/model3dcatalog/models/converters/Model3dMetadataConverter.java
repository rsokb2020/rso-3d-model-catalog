package si.fri.rso.kb6750.model3dcatalog.models.converters;

import si.fri.rso.kb6750.model3dcatalog.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcatalog.models.entities.Model3dMetadataEntity;

public class Model3dMetadataConverter {

    public static Model3dMetadata toDto(Model3dMetadataEntity entity) {

        Model3dMetadata dto = new Model3dMetadata();
        dto.setModelId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        dto.setVertices(entity.getVertices());
        dto.setFaces(entity.getFaces());
        dto.setUri(entity.getUri());

        return dto;

    }

    public static Model3dMetadataEntity toEntity(Model3dMetadata dto) {

        Model3dMetadataEntity entity = new Model3dMetadataEntity();
        entity.setCreated(dto.getCreated());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setVertices(dto.getVertices());
        entity.setFaces(dto.getFaces());
        entity.setUri(dto.getUri());

        return entity;

    }

}
