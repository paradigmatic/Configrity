package org.streum.configrity.conf4j;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestConfiguration {

    private Configuration conf = null;

    @Before public void setup() {
	conf = Configuration.empty();
	conf = conf.set("foo", "FOO");
	conf = conf.set("bar", 12 );
	conf = conf.set("baz", true );
    }

    @Test public void testContains() {
	assertEquals( conf.contains("foo"), true );
	assertEquals( conf.contains("bar"), true );
	assertEquals( conf.contains("baz"), true );
	assertEquals( conf.contains("hello"), false );
	conf = conf.set("hello", "" );
	assertEquals( conf.contains("hello"), true );
    }

    @Test public void testGet() {
	assertEquals( conf.get("foo"), "FOO" );
	assertEquals( conf.get("bar"), "12" );
	assertEquals( conf.get("baz"), "true" );
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetNotFound() {
	assertEquals( conf.get("hello"), "" );
    }

    @Test public void testSet() {
	assertEquals( conf.contains("hello"), false );
	conf = conf.set("hello","world");
	assertEquals( conf.get("hello"), "world" );
    }

    @Test public void testSetAgain() {
	assertEquals( conf.get("foo"), "FOO" );
	conf = conf.set("foo","hello");
	assertEquals( conf.get("foo"), "hello" );
    }
	
}

