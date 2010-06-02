package org.apache.maven.repo;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.ArrayList;
import java.util.Collection;

/**
 * A request to install artifacts and their accompanying metadata into the local repository.
 * 
 * @author Benjamin Bentmann
 * @see RepositorySystem#install(RepositorySession, InstallRequest)
 */
public class InstallRequest
{

    private Collection<Artifact> artifacts = new ArrayList<Artifact>();

    private Collection<Metadata> metadata = new ArrayList<Metadata>();

    /**
     * Gets the artifact to install.
     * 
     * @return The artifacts to install, never {@code null}.
     */
    public Collection<Artifact> getArtifacts()
    {
        return artifacts;
    }

    /**
     * Sets the artifacts to install.
     * 
     * @param artifacts The artifacts to install, may be {@code null}.
     * @return This request for chaining, never {@code null}.
     */
    public InstallRequest setArtifacts( Collection<Artifact> artifacts )
    {
        if ( artifacts == null )
        {
            this.artifacts = new ArrayList<Artifact>();
        }
        else
        {
            this.artifacts = artifacts;
        }
        return this;
    }

    /**
     * Adds the specified artifacts for installation.
     * 
     * @param artifact The artifact to add, may be {@code null}.
     * @return This request for chaining, never {@code null}.
     */
    public InstallRequest addArtifact( Artifact artifact )
    {
        if ( artifact != null )
        {
            artifacts.add( artifact );
        }
        return this;
    }

    /**
     * Gets the metadata to install.
     * 
     * @return The metadata to install, never {@code null}.
     */
    public Collection<Metadata> getMetadata()
    {
        return metadata;
    }

    /**
     * Sets the metadata to install.
     * 
     * @param metadata The metadata to install.
     * @return This request for chaining, never {@code null}.
     */
    public InstallRequest setMetadata( Collection<Metadata> metadata )
    {
        if ( metadata == null )
        {
            this.metadata = new ArrayList<Metadata>();
        }
        else
        {
            this.metadata = metadata;
        }
        return this;
    }

    /**
     * Adds the specified metadata for installation.
     * 
     * @param metadata The metadata to add, may be {@code null}.
     * @return This request for chaining, never {@code null}.
     */
    public InstallRequest addMetadata( Metadata metadata )
    {
        if ( metadata != null )
        {
            this.metadata.add( metadata );
        }
        return this;
    }

}