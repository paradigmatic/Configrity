package org.streum.configrity.conf4j;

import java.util.Properties;
import java.io.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;


public class TestIO {

    private File tmpFile  = null;

    @Before public void setup() throws IOException {
	tmpFile = File.createTempFile( "test", ".conf" );
    }

    @After public void tearDown() {
	tmpFile.delete();
    }

    @Test public void testReadFlatFormat() throws IOException {
	PrintWriter out = new PrintWriter( tmpFile );
	out.println("foo = FOO");
	out.println("bar = 12" );
	out.println("baz = on" );
	out.close();
	Configuration conf = Configuration.load( tmpFile, Formats.flatFormat() );
	assertEquals( conf.get("foo"), "FOO" );
	assertEquals( conf.getInt("bar"), 12 );
	assertEquals( conf.getBoolean("baz"), true );
    }

    @Test public void testReadBlockFormat() throws IOException {
	PrintWriter out = new PrintWriter( tmpFile );
	out.println("block {");
	out.println("  foo = FOO");
	out.println("  bar = 12" );
	out.println("  sub {");
	out.println("    baz = on" );
	out.println("  }");
	out.println("}");
	out.close();
	Configuration conf = Configuration.load( tmpFile, Formats.blockFormat() );
	assertEquals( conf.get("block.foo"), "FOO" );
	assertEquals( conf.getInt("block.bar"), 12 );
	assertEquals( conf.getBoolean("block.sub.baz"), true );
    }
}