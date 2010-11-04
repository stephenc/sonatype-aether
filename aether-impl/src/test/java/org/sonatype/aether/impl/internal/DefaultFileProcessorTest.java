package org.sonatype.aether.impl.internal;

/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonatype.aether.test.util.TestFileUtils;

/**
 * @author Benjamin Hanzelmann
 */
public class DefaultFileProcessorTest
{

    private File targetDir;

    private DefaultFileProcessor fileProcessor;

    @Before
    public void setup()
    {
        targetDir = new File( "target/test-FileProcessor" );
        fileProcessor = new DefaultFileProcessor();
    }

    @After
    public void teardown()
        throws Exception
    {
        TestFileUtils.delete( targetDir );
        fileProcessor = null;
    }

    @Test
    public void testCopy()
        throws IOException
    {
        File file = TestFileUtils.createTempFile( "testCopy\nasdf" );
        File target = new File( targetDir, "testCopy.txt" );

        fileProcessor.copy( file, target, null );

        TestFileUtils.assertContent( "testCopy\nasdf".getBytes( "UTF-8" ), file );

        file.delete();
    }

    @Test
    public void testOverwrite()
        throws IOException
    {
        File file = TestFileUtils.createTempFile( "testCopy\nasdf" );

        for ( int i = 0; i < 5; i++ )
        {
            File target = new File( targetDir, "testCopy.txt" );
            fileProcessor.copy( file, target, null );
            TestFileUtils.assertContent( "testCopy\nasdf".getBytes( "UTF-8" ), file );
        }

        file.delete();
    }

    @Test
    public void testCopyEmptyFile()
        throws IOException
    {
        File file = TestFileUtils.createTempFile( "" );
        File target = new File( "target/testCopyEmptyFile" );
        target.delete();
        fileProcessor.copy( file, target, null );
        assertTrue( "empty file was not copied", target.exists() && target.length() == 0 );
        target.delete();
    }

}
