package com.github.hsoj48.spring.property;

import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MultipleResourceWholeFilePropertySource extends PropertiesPropertySource {

    public MultipleResourceWholeFilePropertySource(String name, EncodedResource[] resources) throws IOException {
        super(name, loadProperties(resources));
    }

    private static Properties loadProperties(EncodedResource[] resources) throws IOException {
        Properties props = new Properties();
        for (EncodedResource r : resources) {
            fillProperties(props, r, new WholeFilePropertiesPersister(getKey(r)));
        }
        return props;
    }

    private static void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister) throws IOException {
        try (InputStream stream = resource.getInputStream()) {
            persister.load(props, stream);
        }
    }

    private static String getKey(EncodedResource resource) {
        String filename = resource.getResource().getFilename();
        return filename.substring(0, filename.lastIndexOf('.'));
    }

}
