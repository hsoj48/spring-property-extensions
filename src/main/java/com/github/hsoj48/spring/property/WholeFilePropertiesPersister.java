package com.github.hsoj48.spring.property;

import org.springframework.util.DefaultPropertiesPersister;

import java.io.*;
import java.util.Properties;

public class WholeFilePropertiesPersister extends DefaultPropertiesPersister {

    private final String key;

    public WholeFilePropertiesPersister(String key) {
        this.key = key;
    }

    @Override
    public void load(Properties props, InputStream is) throws IOException {
        load(props, new InputStreamReader(is));
    }

    @Override
    public void load(Properties props, Reader reader) throws IOException {
        BufferedReader buff = new BufferedReader(reader);
        StringBuilder value = new StringBuilder();
        boolean appendNewline = false;
        String line;

        while ((line = buff.readLine()) != null) {
            if (appendNewline) {
                value.append("\n");
            } else if (value.length() != 0) {
                appendNewline = true;
                value.append("\n");
            }

            value.append(line);
        }

        buff.close();
        props.setProperty(key, value.toString());
    }

}
