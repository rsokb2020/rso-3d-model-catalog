package si.fri.rso.kb6750.model3dcatalog.config;

import javax.enterprise.context.ApplicationScoped;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ConfigBundle("rest-properties")
@ApplicationScoped
public class RestProperties {

    @ConfigValue(watch = true)
    private Boolean maintenanceMode;
    @ConfigValue(watch = true)
    private Boolean broken;

    public Boolean getMaintenanceMode() {
        return this.maintenanceMode;
    }
    public Boolean getBroken() {
        return this.broken;
    }
    public void setMaintenanceMode(final Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }
    public void setBroken(final Boolean broken) {this.broken = broken;
    }
}
