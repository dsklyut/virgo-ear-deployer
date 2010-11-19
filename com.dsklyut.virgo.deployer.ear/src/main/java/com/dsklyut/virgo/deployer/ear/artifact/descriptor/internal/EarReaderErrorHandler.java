package com.dsklyut.virgo.deployer.ear.artifact.descriptor.internal;

import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class EarReaderErrorHandler implements ErrorHandler {

    private final Logger logger;

    public EarReaderErrorHandler(Logger logger) {
        this.logger = logger;
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void warning(SAXParseException exception) throws SAXException {
        logger.warn("Ignored XML validation warning", exception);
    }

}
