package si.fri.rso.kb6750.model3dcatalog.lib;

import java.time.Instant;

public class Model3DMetadata {
    private Integer modelID;
    private String title;
    private String description;
    private Long numberOfVertices;
    private Long numberOfFaces;
    private Instant created;
    private String uri;

    public Integer getModelId() {
        return modelID;
    }

    public void setModelId(Integer modelID) {
        this.modelID = modelID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNumberOfVertices() {
        return numberOfVertices;
    }

    public void setNumberOfVertices(Long numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    public Long getNumberOfFaces() {
        return numberOfFaces;
    }

    public void setNumberOfFaces(Long numberOfFaces) {
        this.numberOfFaces = numberOfFaces;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
