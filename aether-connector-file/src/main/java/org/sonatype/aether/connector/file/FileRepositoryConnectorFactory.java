package org.sonatype.aether.connector.file;

/*******************************************************************************
 * Copyright (c) 2010-2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * The Apache License v2.0 is available at
 *   http://www.apache.org/licenses/LICENSE-2.0.html
 * You may elect to redistribute this code under either of these licenses.
 *******************************************************************************/

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.spi.connector.RepositoryConnector;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.spi.io.FileProcessor;
import org.sonatype.aether.spi.locator.Service;
import org.sonatype.aether.spi.locator.ServiceLocator;
import org.sonatype.aether.spi.log.Logger;
import org.sonatype.aether.spi.log.NullLogger;
import org.sonatype.aether.transfer.NoRepositoryConnectorException;

/**
 * Factory creating {@link FileRepositoryConnector}s.
 * 
 * @author Benjamin Hanzelmann
 */
@Component( role = RepositoryConnectorFactory.class, hint = "file" )
public class FileRepositoryConnectorFactory
    implements RepositoryConnectorFactory, Service
{

    @Requirement
    private Logger logger = NullLogger.INSTANCE;

    @Requirement
    private FileProcessor fileProcessor;

    private int priority = 1;

    public static final String CFG_PREFIX = "aether.connector.file";

    public FileRepositoryConnectorFactory()
    {
        // enables default constructor
    }

    public FileRepositoryConnectorFactory( Logger logger, FileProcessor fileProcessor )
    {
        setLogger( logger );
        setFileProcessor( fileProcessor );
    }

    public void initService( ServiceLocator locator )
    {
        setLogger( locator.getService( Logger.class ) );
        setFileProcessor( locator.getService( FileProcessor.class ) );
    }

    /**
     * Sets the logger to use for this component.
     * 
     * @param logger The logger to use, may be {@code null} to disable logging.
     * @return This component for chaining, never {@code null}.
     */
    public FileRepositoryConnectorFactory setLogger( Logger logger )
    {
        this.logger = ( logger != null ) ? logger : NullLogger.INSTANCE;
        return this;
    }

    /**
     * Sets the file processor to use for this component.
     * 
     * @param fileProcessor The file processor to use, must not be {@code null}.
     * @return This component for chaining, never {@code null}.
     */
    public FileRepositoryConnectorFactory setFileProcessor( FileProcessor fileProcessor )
    {
        if ( fileProcessor == null )
        {
            throw new IllegalArgumentException( "file processor has not been specified" );
        }
        this.fileProcessor = fileProcessor;
        return this;
    }

    public RepositoryConnector newInstance( RepositorySystemSession session, RemoteRepository repository )
        throws NoRepositoryConnectorException
    {
        if ( "file".equalsIgnoreCase( repository.getProtocol() ) )
        {
            FileRepositoryConnector connector =
                new FileRepositoryConnector( session, repository, fileProcessor, logger );
            return connector;
        }

        throw new NoRepositoryConnectorException( repository );
    }

    public int getPriority()
    {
        return priority;
    }

    /**
     * Sets the priority of this component.
     * 
     * @param priority The priority.
     * @return This component for chaining, never {@code null}.
     */
    public FileRepositoryConnectorFactory setPriority( int priority )
    {
        this.priority = priority;
        return this;
    }

}
