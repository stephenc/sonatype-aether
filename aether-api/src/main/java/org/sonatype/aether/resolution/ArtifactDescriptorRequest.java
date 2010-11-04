package org.sonatype.aether.resolution;

/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;

/**
 * A request to read an artifact descriptor.
 * 
 * @author Benjamin Bentmann
 * @see RepositorySystem#readArtifactDescriptor(RepositorySystemSession, ArtifactDescriptorRequest)
 */
public class ArtifactDescriptorRequest
{

    private Artifact artifact;

    private List<RemoteRepository> repositories = Collections.emptyList();

    private String context = "";

    /**
     * Creates an uninitialized request.
     */
    public ArtifactDescriptorRequest()
    {
        // enables default constructor
    }

    /**
     * Creates a request with the specified properties.
     * 
     * @param artifact The artifact whose descriptor should be read, may be {@code null}.
     * @param repositories The repositories to resolve the descriptor from, may be {@code null}.
     * @param context The context in which this request is made, may be {@code null}.
     */
    public ArtifactDescriptorRequest( Artifact artifact, List<RemoteRepository> repositories, String context )
    {
        setArtifact( artifact );
        setRepositories( repositories );
        setRequestContext( context );
    }

    /**
     * Gets the artifact whose descriptor shall be read.
     * 
     * @return The artifact or {@code null} if not set.
     */
    public Artifact getArtifact()
    {
        return artifact;
    }

    /**
     * Sets the artifact whose descriptor shall be read.
     * 
     * @param artifact The artifact, may be {@code null}.
     * @return This request for chaining, never {@code null}.
     */
    public ArtifactDescriptorRequest setArtifact( Artifact artifact )
    {
        this.artifact = artifact;
        return this;
    }

    /**
     * Gets the repositories to resolve the descriptor from.
     * 
     * @return The repositories, never {@code null}.
     */
    public List<RemoteRepository> getRepositories()
    {
        return repositories;
    }

    /**
     * Sets the repositories to resolve the descriptor from.
     * 
     * @param repositories The repositories, may be {@code null}.
     * @return This request for chaining, never {@code null}.
     */
    public ArtifactDescriptorRequest setRepositories( List<RemoteRepository> repositories )
    {
        if ( repositories == null )
        {
            this.repositories = Collections.emptyList();
        }
        else
        {
            this.repositories = repositories;
        }
        return this;
    }

    /**
     * Adds the specified repository for the resolution of the artifact descriptor.
     * 
     * @param repository The repository to add, may be {@code null}.
     * @return This request for chaining, never {@code null}.
     */
    public ArtifactDescriptorRequest addRepository( RemoteRepository repository )
    {
        if ( repository != null )
        {
            if ( this.repositories.isEmpty() )
            {
                this.repositories = new ArrayList<RemoteRepository>();
            }
            this.repositories.add( repository );
        }
        return this;
    }

    /**
     * Gets the context in which this request is made.
     * 
     * @return The context, never {@code null}.
     */
    public String getRequestContext()
    {
        return context;
    }

    /**
     * Sets the context in which this request is made.
     * 
     * @param context The context, may be {@code null}.
     * @return This request for chaining, never {@code null}.
     */
    public ArtifactDescriptorRequest setRequestContext( String context )
    {
        this.context = ( context != null ) ? context : "";
        return this;
    }

    @Override
    public String toString()
    {
        return getArtifact() + " < " + getRepositories();
    }

}
