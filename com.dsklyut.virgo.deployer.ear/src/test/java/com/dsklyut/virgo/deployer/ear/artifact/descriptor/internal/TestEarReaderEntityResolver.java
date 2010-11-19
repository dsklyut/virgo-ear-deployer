package com.dsklyut.virgo.deployer.ear.artifact.descriptor.internal;

import org.eclipse.virgo.kernel.artifact.plan.PlanBridge;
import org.eclipse.virgo.kernel.artifact.plan.internal.PlanReaderErrorHandler;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * User: dsklyut
 * Date: 11/18/10
 * Time: 3:17 PM
 */
public class TestEarReaderEntityResolver {
    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

    @Test
    public void testResolverWithJee5Schema() {
        ClassPathResource resource = new ClassPathResource("/com/dsklyut/virgo/deployer/ear/artifact/descriptor/internal/TestEarReaderEntityResolver-application5.xml");
        runTest(resource);
    }

    @Test
    public void testResolverWithJee6Schema() {
        ClassPathResource resource = new ClassPathResource("/com/dsklyut/virgo/deployer/ear/artifact/descriptor/internal/TestEarReaderEntityResolver-application6.xml");
        runTest(resource);
    }

    private void runTest(Resource resource) {
        assertTrue(resource.isReadable());

        Document doc = null;
        try {
            doc = readDocument(resource.getInputStream());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        assertNotNull(doc);
    }

    private Document readDocument(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = createDocumentBuilderFactory().newDocumentBuilder();
        builder.setEntityResolver(new EarReaderEntityResolver());
        builder.setErrorHandler(new PlanReaderErrorHandler(LoggerFactory.getLogger(PlanBridge.class)));
        return builder.parse(inputStream);
    }

    private DocumentBuilderFactory createDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
        return factory;
    }

}
