package org.sonatype.aether.util;

/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sonatype.aether.SessionData;

/**
 * A simple session data storage backed by a thread-safe map.
 * 
 * @author Benjamin Bentmann
 */
public class DefaultSessionData
    implements SessionData
{

    private final Map<Object, Object> data;

    public DefaultSessionData()
    {
        data = new ConcurrentHashMap<Object, Object>();
    }

    public void set( Object key, Object value )
    {
        if ( key == null )
        {
            throw new IllegalArgumentException( "key must not be null" );
        }

        if ( value != null )
        {
            data.put( key, value );
        }
        else
        {
            data.remove( key );
        }
    }

    public Object get( Object key )
    {
        if ( key == null )
        {
            throw new IllegalArgumentException( "key must not be null" );
        }

        return data.get( key );
    }

}
