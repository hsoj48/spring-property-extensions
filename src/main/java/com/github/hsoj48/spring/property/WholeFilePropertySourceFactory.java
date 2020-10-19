package com.github.hsoj48.spring.property;


import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * {@link PropertySourceFactory} that reads the entire contents of the specified resource into a single
 * property.  The property key is the specified resource name with the file extension removed.
 *
 * In addition, this class supports pattern matching (using {@link PathMatchingResourcePatternResolver})
 * so many resources can be included into a single property source using wildcard placeholders in the resource URI.
 */
public class WholeFilePropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource.getResource().isFile()) {
            return new WholeFilePropertySource(name != null ? name : getNameForResource(resource.getResource()), resource);
        } else {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources;

            if (resource.getResource() instanceof ClassPathResource) {
                resources = resolver.getResources("classpath:" + ((ClassPathResource) resource.getResource()).getPath());
            } else {
                resources = resolver.getResources(resource.getResource().getURI().getPath());
            }

            if (resources.length == 0) {
                throw new FileNotFoundException(resource.getResource().getDescription() + " cannot be opened because it does not exist");
            }

            EncodedResource[] encodedResources = new EncodedResource[resources.length];
            for (int i = 0; i < resources.length; i++) {
                encodedResources[i] = new EncodedResource(resources[i], resource.getEncoding());
            }

            return new MultipleResourceWholeFilePropertySource(name != null ? name : getNameForResource(resource.getResource()), encodedResources);
        }
    }

    private String getNameForResource(Resource resource) {
        String name = resource.getDescription();
        if (!StringUtils.hasText(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }

        return name;
    }

}
