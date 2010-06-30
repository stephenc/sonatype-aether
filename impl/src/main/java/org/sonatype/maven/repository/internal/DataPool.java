package org.sonatype.maven.repository.internal;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.sonatype.maven.repository.Artifact;
import org.sonatype.maven.repository.ArtifactDescriptorRequest;
import org.sonatype.maven.repository.ArtifactDescriptorResult;
import org.sonatype.maven.repository.ArtifactRepository;
import org.sonatype.maven.repository.Dependency;
import org.sonatype.maven.repository.DependencyInfo;
import org.sonatype.maven.repository.DependencyNode;
import org.sonatype.maven.repository.RemoteRepository;
import org.sonatype.maven.repository.RepositoryCache;
import org.sonatype.maven.repository.Version;
import org.sonatype.maven.repository.VersionConstraint;
import org.sonatype.maven.repository.VersionRangeRequest;
import org.sonatype.maven.repository.VersionRangeResult;

/**
 * @author Benjamin Bentmann
 */
class DataPool
{

    private static final String ARTIFACT_POOL = DataPool.class.getName() + "$Artifact";

    private static final String DEPENDENCY_POOL = DataPool.class.getName() + "$Dependency";

    private ObjectPool<Artifact> artifacts;

    private ObjectPool<Dependency> dependencies;

    private Map<Object, Descriptor> descriptors = new WeakHashMap<Object, Descriptor>();

    private Map<Object, Constraint> constraints = new WeakHashMap<Object, Constraint>();

    private Map<Object, List<RemoteRepository>> repositories = new HashMap<Object, List<RemoteRepository>>();

    private Map<Object, DependencyNode> nodes = new HashMap<Object, DependencyNode>();

    private Map<Object, List<RemoteRepository>> repos = new HashMap<Object, List<RemoteRepository>>();

    public DataPool( RepositoryCache cache )
    {
        if ( cache != null )
        {
            artifacts = (ObjectPool<Artifact>) cache.get( ARTIFACT_POOL );
            dependencies = (ObjectPool<Dependency>) cache.get( DEPENDENCY_POOL );
        }

        if ( artifacts == null )
        {
            artifacts = new ObjectPool<Artifact>();
            if ( cache != null )
            {
                cache.put( ARTIFACT_POOL, artifacts );
            }
        }

        if ( dependencies == null )
        {
            dependencies = new ObjectPool<Dependency>();
            if ( cache != null )
            {
                cache.put( DEPENDENCY_POOL, dependencies );
            }
        }
    }

    public Artifact intern( Artifact artifact )
    {
        return artifacts.intern( artifact );
    }

    public Dependency intern( Dependency dependency )
    {
        return dependencies.intern( dependency );
    }

    public List<RemoteRepository> intern( List<RemoteRepository> repositories )
    {
        List<RemoteRepository> interned = this.repositories.get( repositories );
        if ( interned != null )
        {
            return interned;
        }
        this.repositories.put( repositories, repositories );
        return repositories;
    }

    public Object toKey( ArtifactDescriptorRequest request )
    {
        return request.getArtifact();
    }

    public ArtifactDescriptorResult getDescriptor( Object key, ArtifactDescriptorRequest request )
    {
        Descriptor descriptor = descriptors.get( key );
        if ( descriptor != null )
        {
            return descriptor.toResult( request );
        }
        return null;
    }

    public void putDescriptor( Object key, ArtifactDescriptorResult result )
    {
        descriptors.put( key, new Descriptor( result ) );
    }

    public Object toKey( VersionRangeRequest request )
    {
        return new ConstraintKey( request );
    }

    public VersionRangeResult getConstraint( Object key, VersionRangeRequest request )
    {
        Constraint constraint = constraints.get( key );
        if ( constraint != null )
        {
            return constraint.toResult( request );
        }
        return null;
    }

    public void putConstraint( Object key, VersionRangeResult result )
    {
        constraints.put( key, new Constraint( result ) );
    }

    public Object getNodeKey( DependencyInfo info )
    {
        return new InfoKey( info );
    }

    public DependencyNode getNode( Object key )
    {
        return nodes.get( key );
    }

    public void putNode( Object key, DependencyNode node )
    {
        nodes.put( key, node );
    }

    public List<RemoteRepository> getRepositories( Object key )
    {
        return repos.get( key );
    }

    public void putRepositories( Object key, List<RemoteRepository> repositories )
    {
        if ( !this.repos.containsKey( key ) )
        {
            this.repos.put( key, repositories );
        }
    }

    static class InfoKey
    {
        private final DependencyInfo info;

        private final int hashCode;

        public InfoKey( DependencyInfo info )
        {
            this.info = info;

            int hash = info.getDependency().getArtifact().hashCode();
            hash = hash * 31 + info.getDependency().getScope().hashCode();
            hash = hash * 31 + info.getVersionConstraint().hashCode();
            hashCode = hash;
        }

        @Override
        public boolean equals( Object obj )
        {
            if ( obj == this )
            {
                return true;
            }
            else if ( obj == null || !getClass().equals( obj.getClass() ) )
            {
                return false;
            }

            InfoKey that = (InfoKey) obj;
            return eq( info.getDependency().getArtifact(), that.info.getDependency().getArtifact() )
                && info.getDependency().getScope().equals( that.info.getDependency().getScope() )
                && info.getDependency().isOptional() == that.info.getDependency().isOptional()
                && info.getDependency().getExclusions().equals( that.info.getDependency().getExclusions() )
                && eq( info.getVersionConstraint(), that.info.getVersionConstraint() )
                && eq( info.getPremanagedScope(), that.info.getPremanagedScope() )
                && eq( info.getPremanagedVersion(), that.info.getPremanagedVersion() )
                && info.getRelocations().equals( that.info.getRelocations() );
        }

        private static <T> boolean eq( T s1, T s2 )
        {
            return s1 != null ? s1.equals( s2 ) : s2 == null;
        }

        @Override
        public int hashCode()
        {
            return hashCode;
        }

    }

    static class Descriptor
    {

        final Artifact artifact;

        final Map<String, Object> properties;

        final List<Artifact> relocations;

        final List<RemoteRepository> repositories;

        final List<Dependency> dependencies;

        final List<Dependency> managedDependencies;

        public Descriptor( ArtifactDescriptorResult result )
        {
            artifact = result.getArtifact();
            properties = result.getProperties();
            relocations = result.getRelocations();
            dependencies = result.getDependencies();
            managedDependencies = result.getManagedDependencies();
            repositories = clone( result.getRepositories() );
        }

        public ArtifactDescriptorResult toResult( ArtifactDescriptorRequest request )
        {
            ArtifactDescriptorResult result = new ArtifactDescriptorResult( request );
            result.setArtifact( artifact );
            result.setProperties( properties );
            result.setRelocations( relocations );
            result.setDependencies( dependencies );
            result.setManagedDependencies( dependencies );
            result.setRepositories( clone( repositories ) );
            return result;
        }

        private static List<RemoteRepository> clone( List<RemoteRepository> repositories )
        {
            List<RemoteRepository> clones = new ArrayList<RemoteRepository>( repositories.size() );
            for ( RemoteRepository repository : repositories )
            {
                RemoteRepository clone = new RemoteRepository( repository );
                clone.setMirroredRepositories( new ArrayList<RemoteRepository>( repository.getMirroredRepositories() ) );
                clones.add( clone );
            }
            return clones;
        }

    }

    static class Constraint
    {

        final Map<Version, ArtifactRepository> repositories;

        final VersionConstraint versionConstraint;

        public Constraint( VersionRangeResult result )
        {
            versionConstraint = result.getVersionConstraint();
            repositories = new LinkedHashMap<Version, ArtifactRepository>();
            for ( Version version : result.getVersions() )
            {
                repositories.put( version, result.getRepository( version ) );
            }
        }

        public VersionRangeResult toResult( VersionRangeRequest request )
        {
            VersionRangeResult result = new VersionRangeResult( request );
            for ( Map.Entry<Version, ArtifactRepository> entry : repositories.entrySet() )
            {
                result.addVersion( entry.getKey() );
                result.setRepository( entry.getKey(), entry.getValue() );
            }
            result.setVersionConstraint( versionConstraint );
            return result;
        }

    }

    static class ConstraintKey
    {

        private final Artifact artifact;

        private final List<RemoteRepository> repositories;

        private final int hashCode;

        public ConstraintKey( VersionRangeRequest request )
        {
            artifact = request.getArtifact();
            repositories = request.getRepositories();
            hashCode = artifact.hashCode();
        }

        @Override
        public boolean equals( Object obj )
        {
            if ( obj == this )
            {
                return true;
            }
            else if ( !( obj instanceof ConstraintKey ) )
            {
                return false;
            }
            ConstraintKey that = (ConstraintKey) obj;
            return artifact.equals( that.artifact ) && equals( repositories, that.repositories );
        }

        private static boolean equals( Collection<RemoteRepository> repos1, Collection<RemoteRepository> repos2 )
        {
            if ( repos1.size() != repos2.size() )
            {
                return false;
            }
            for ( Iterator<RemoteRepository> it1 = repos1.iterator(), it2 = repos2.iterator(); it1.hasNext(); )
            {
                RemoteRepository repo1 = it1.next();
                RemoteRepository repo2 = it2.next();
                if ( repo1.isRepositoryManager() != repo2.isRepositoryManager() )
                {
                    return false;
                }
                if ( repo1.isRepositoryManager() )
                {
                    if ( !equals( repo1.getMirroredRepositories(), repo2.getMirroredRepositories() ) )
                    {
                        return false;
                    }
                }
                else if ( !repo1.getUrl().equals( repo2.getUrl() ) )
                {
                    return false;
                }
                else if ( repo1.getPolicy( true ).isEnabled() != repo2.getPolicy( true ).isEnabled() )
                {
                    return false;
                }
                else if ( repo1.getPolicy( false ).isEnabled() != repo2.getPolicy( false ).isEnabled() )
                {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            return hashCode;
        }

    }

}
