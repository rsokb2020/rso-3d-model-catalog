package si.fri.rso.kb6750.model3dcatalog.models.converters;

import si.fri.rso.kb6750.model3dcatalog.lib.ImageMetadata;
import si.fri.rso.kb6750.model3dcatalog.lib.Model3DMetadata;
import si.fri.rso.kb6750.model3dcatalog.models.entities.ImageMetadataEntity;
import si.fri.rso.kb6750.model3dcatalog.models.entities.Model3DMetadataEntity;

public class Model3dMetadataConverter {

    public static Model3DMetadata toDto(Model3DMetadataEntity entity) {

        Model3DMetadata dto = new Model3DMetadata();
        dto.setModelId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        dto.setNumberOfVertices(entity.getVertices());
        dto.setNumberOfFaces(entity.getFaces());
        dto.setUri(entity.getUri());

        return dto;

    }

    public static Model3DMetadataEntity toEntity(Model3DMetadata dto) {

        Model3DMetadataEntity entity = new Model3DMetadataEntity();
        entity.setCreated(dto.getCreated());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setVertices(dto.getNumberOfVertices());
        entity.setFaces(dto.getNumberOfFaces());
        entity.setUri(dto.getUri());

        return entity;

    }

}
