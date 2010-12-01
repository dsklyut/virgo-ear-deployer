package com.dsklyut.virgo.deployer.ear.artifact.descriptor;

import com.dsklyut.virgo.deployer.ear.artifact.EarBridge;
import com.dsklyut.virgo.deployer.ear.artifact.descriptor.internal.EarReaderEntityResolver;
import com.dsklyut.virgo.deployer.ear.artifact.descriptor.internal.EarReaderErrorHandler;
import org.slf4j.LoggerFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple parser of jee application (ear) descriptor.
 * <p/>
 * NOTE: some code stolen from PlanReader
 * <p/>
 * This parser does not parse out any of the extended attributes of the jee application at this time.
 * No ejb refs, jpa, security....
 * <p/>
 * Just simply modules and descriptor info...
 *
 * @author dsklyut
 * @see org.eclipse.virgo.kernel.artifact.plan.PlanReader
 *      <p/>
 */
public final class EarDescriptorReader {


    private static final String DISPLAY_NAME_ELEMENT = "display-name".intern();
    private static final String DESCRIPTION_ELEMENT = "description".intern();

    // version 6 only
    private static final String APPLICATION_NAME_ELEMENT = "application-name".intern();

    private static final String LIBRARY_DIRECTORY_ELEMENT = "library-directory".intern();
    private static final String MODULE_ELEMENT = "module".intern();

    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";


    public EarDescriptor read(InputStream inputStream) {
        return read(new InputStreamReader(inputStream));
    }

    public EarDescriptor read(Reader reader) {
        try {
            Document doc = readDocument(reader);
            Element element = doc.getDocumentElement();
            return parseApplicationElement(element);
        } catch (Exception ex) {
            throw new RuntimeException("Error parsing input application.xml", ex);
        }
    }

    private EarDescriptor parseApplicationElement(Element root) {
        String displayName = DomUtils.getChildElementValueByTagName(root, DISPLAY_NAME_ELEMENT);
        String description = DomUtils.getChildElementValueByTagName(root, DESCRIPTION_ELEMENT);

        // version 6 only
        String applicationName = DomUtils.getChildElementValueByTagName(root, APPLICATION_NAME_ELEMENT);


        String libraryDir = DomUtils.getChildElementValueByTagName(root, LIBRARY_DIRECTORY_ELEMENT);

        List<Module> modules = parseModules(DomUtils.getChildElementsByTagName(root, MODULE_ELEMENT));

        return new EarDescriptor(applicationName, description, displayName, libraryDir, modules);
    }

    private List<Module> parseModules(List<Element> moduleElements) {
        List<Module> result = new ArrayList<Module>();
        for (Element e : moduleElements) {
            result.add(ModuleType.fromXmlElement(e));
        }
        return result;
    }


    private Document readDocument(Reader reader) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = createDocumentBuilderFactory().newDocumentBuilder();
        builder.setEntityResolver(new EarReaderEntityResolver());
        builder.setErrorHandler(new EarReaderErrorHandler(LoggerFactory.getLogger(EarBridge.class)));
        return builder.parse(new InputSource(reader));
    }

    private DocumentBuilderFactory createDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
        return factory;
    }
}
