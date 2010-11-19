/*******************************************************************************
 * Copyright (c) 2008, 2010 VMware Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   VMware Inc. - initial contribution
 *******************************************************************************/

package com.dsklyut.virgo.deployer.ear.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.virgo.util.io.FileCopyUtils;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.osgi.manifest.BundleManifestFactory;


/**
 * Utility methods for working with {@link BundleManifest BundleManifests} and other nested files.
 * <p/>
 * <p/>
 * <p/>
 * <strong>Concurrent Semantics</strong><br />
 * <p/>
 * Thread-safe.
 */
public final class EarUtils {

    public static Reader readFileFromArchive(File archive, String nestedFile, String... archiveSuffixes) throws IOException {
        String fileName = archive.getName();

        Reader reader = null;

        if (archive.isDirectory()) {
            File manifestFile = new File(archive, nestedFile);
            if (manifestFile.exists()) {
                reader = readerFromFile(manifestFile);
            }
        } else {
            for (String suffix : archiveSuffixes) {
                if (fileName.endsWith(suffix)) {
                    reader = readFileFromJar(archive, nestedFile);
                }
            }
        }
        return reader;
    }

    public static Reader readEarApplicationDescriptor(File file) throws IOException {
        return readFileFromArchive(file, "META-INF/application.xml");
    }

    /**
     * Reads the <code>BundleManifest</code> from the supplied <code>file</code>. The <code>File</code> can either
     * be a file, i.e. a jar archive, or a directory. If the file is an archive its manifest will only be read
     * if its name ends with one of the supplied <code>archiveSuffixes</code>
     *
     * @param file            The file from which the manifest is to be read.
     * @param archiveSuffixes The suffixes with which an archive's file name must end
     * @return The <code>BundleManifest</code> from the file or <code>null</code> if one was not found.
     * @throws IOException Thrown if a manifest is detected but the reading of it fails.
     */
    public static BundleManifest readBundleManifest(File file, String... archiveSuffixes) throws IOException {
        final Reader reader = readFileFromArchive(file, JarFile.MANIFEST_NAME, archiveSuffixes);

        if (reader != null) {
            return BundleManifestFactory.createBundleManifest(reader);
        } else {
            return null;
        }
    }

    private static Reader readFileFromJar(File file, String nestedFile) throws IOException {
        JarFile jar = null;
        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry(nestedFile);

            if (entry == null) {
                return null; // not an error -- no manifest means this isn't a bundle
            }
            StringWriter writer = new StringWriter();
            FileCopyUtils.copy(new InputStreamReader(jar.getInputStream(entry)), writer);
            return new StringReader(writer.toString());
        } catch (Exception e) {
            throw new IOException(String.format("Failed to read manifest from file '%s'.", file.getName()), e);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException ioe) {
                    //noinspection ThrowFromFinallyBlock
                    throw new IOException(String.format("Failed to close file '%s'.", file.getName()), ioe);
                }
            }
        }
    }

    private static Reader readerFromFile(File file) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            FileCopyUtils.copy(new InputStreamReader(new FileInputStream(file)), writer);
            return new StringReader(writer.toString());
        } catch (IOException ioe) {
            throw new IOException("Failed to create reader for manifest file.", ioe);
        }
    }
}
