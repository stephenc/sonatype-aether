package org.sonatype.aether.transfer;

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

import org.sonatype.aether.RepositoryException;
import org.sonatype.aether.metadata.Metadata;
import org.sonatype.aether.repository.RemoteRepository;

/**
 * @author Benjamin Bentmann
 */
public class MetadataTransferException
    extends RepositoryException
{

    private final Metadata metadata;

    private final RemoteRepository repository;

    static String getString( String prefix, RemoteRepository repository )
    {
        if ( repository == null )
        {
            return "";
        }
        else
        {
            return prefix + repository.getId() + " (" + repository.getUrl() + ")";
        }
    }

    public MetadataTransferException( Metadata metadata, RemoteRepository repository, String message )
    {
        super( message );

        this.metadata = metadata;
        this.repository = repository;
    }

    public MetadataTransferException( Metadata metadata, RemoteRepository repository, Throwable cause )
    {
        super( "Could not transfer metadata " + metadata + getString( " from/to ", repository )
            + getMessage( ": ", cause ), cause );

        this.metadata = metadata;
        this.repository = repository;
    }

    public MetadataTransferException( Metadata metadata, RemoteRepository repository, String message, Throwable cause )
    {
        super( message, cause );

        this.metadata = metadata;
        this.repository = repository;
    }

    public Metadata getMetadata()
    {
        return metadata;
    }

    public RemoteRepository getRepository()
    {
        return repository;
    }

}
