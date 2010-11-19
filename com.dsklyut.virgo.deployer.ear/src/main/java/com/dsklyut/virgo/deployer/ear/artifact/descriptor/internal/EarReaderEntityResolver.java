package com.dsklyut.virgo.deployer.ear.artifact.descriptor.internal;

import org.osgi.framework.FrameworkUtil;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: dsklyut
 * Date: 11/18/10
 * Time: 3:04 PM
 */
public class EarReaderEntityResolver implements EntityResolver {

    private static final String ROOT_SCHEMA_LOCATION = "/com/dsklyut/virgo/deployer/ear/xsd/";

    private static final Map<String, String> SYSTEM_ID_TO_LOCATION_MAP = new HashMap<String, String>();

    static {
        // javaee 5
        SYSTEM_ID_TO_LOCATION_MAP.put("http://java.sun.com/xml/ns/javaee/application_5.xsd",
                                      ROOT_SCHEMA_LOCATION + "application_5.xsd");
        SYSTEM_ID_TO_LOCATION_MAP.put("http://java.sun.com/xml/ns/javaee/javaee_5.xsd",
                                      ROOT_SCHEMA_LOCATION + "javaee_5.xsd");
        SYSTEM_ID_TO_LOCATION_MAP.put("http://java.sun.com/xml/ns/javaee/javaee_web_services_client_1_2.xsd",
                                      ROOT_SCHEMA_LOCATION + "javaee_web_services_client_1_2.xsd");

        // javaee 6 - just in case...
        SYSTEM_ID_TO_LOCATION_MAP.put("http://java.sun.com/xml/ns/javaee/application_6.xsd",
                                      ROOT_SCHEMA_LOCATION + "application_6.xsd");
        SYSTEM_ID_TO_LOCATION_MAP.put("http://java.sun.com/xml/ns/javaee/javaee_6.xsd",
                                      ROOT_SCHEMA_LOCATION + "javaee_6.xsd");
        SYSTEM_ID_TO_LOCATION_MAP.put("http://java.sun.com/xml/ns/javaee/javaee_web_services_client_1_3.xsd",
                                      ROOT_SCHEMA_LOCATION + "javaee_web_services_client_1_3.xsd");

        // this is common between those two.
        SYSTEM_ID_TO_LOCATION_MAP.put("http://www.w3.org/2001/xml.xsd",
                                      ROOT_SCHEMA_LOCATION + "xml.xsd");
        SYSTEM_ID_TO_LOCATION_MAP.put("http://www.w3.org/2001/XMLSchema.dtd",
                                      ROOT_SCHEMA_LOCATION + "XMLSchema.dtd");
        SYSTEM_ID_TO_LOCATION_MAP.put("http://www.w3.org/2001/datatypes.dtd",
                                      ROOT_SCHEMA_LOCATION + "datatypes.dtd");
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

        InputSource result = null;

        ClassLoader classLoader = this.getClass().getClassLoader();
        String location = SYSTEM_ID_TO_LOCATION_MAP.get(systemId);

        if (location != null) {
            InputStream xsdResource = new ClassPathResource(location, classLoader).getInputStream();
            if (xsdResource != null) {
                InputSource source = new InputSource(xsdResource);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                result = source;
            }
        }

        if (result == null) {
            throw new SAXException(String.format("%s:%s could not be loaded from bundle %s",
                                                 publicId,
                                                 systemId,
                                                 FrameworkUtil.getBundle(getClass())));
        }
        return result;
    }

}
