package com.github.hsoj48.spring.property;

import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WholeFilePropertySource extends PropertiesPropertySource {

    public WholeFilePropertySource(String name, EncodedResource resource) throws IOException {
        super(name, loadProperties(resource));
    }

    private static Properties loadProperties(EncodedResource resource) throws IOException {
        Properties props = new Properties();
        fillProperties(props, resource, new WholeFilePropertiesPersister(getKey(resource)));
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
