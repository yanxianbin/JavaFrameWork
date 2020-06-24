package mode;

import java.util.Properties;

public class UserInfoRequest extends UserInfo {

    private Properties properties;

    private String propertiesText;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPropertiesText() {
        return propertiesText;
    }

    public void setPropertiesText(String propertiesText) {
        this.propertiesText = propertiesText;
    }

    @Override
    public String toString() {
        return "UserInfoRequest{" +
                "properties=" + properties +
                ", propertiesText='" + propertiesText + '\'' +
                '}';
    }
}
