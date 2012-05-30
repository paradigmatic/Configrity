package org.streum.configrity.conf4j;

import java.util.Properties;

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
	conf = conf.set("pi", 3.14 );
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

    @Test public void testGetDefault() {
	assertEquals( conf.get("foo", "1" ), "FOO" );
	assertEquals( conf.get("hoo", "2" ), "2" );
	conf = conf.set("hoo", "HOO" );
	assertEquals( conf.get("hoo", "2" ), "HOO" );
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

    @Test public void testGetInt() {
	assertEquals( conf.getInt("bar"), 12 );
	assertEquals( conf.getInt("hello",0), 0 );
    }

    @Test public void testGetDouble() {
	assertEquals( conf.getDouble("pi"), 3.14, 1e-8 );	
	assertEquals( conf.getDouble("hello",0.0), 0.0, 1e-8 );
    }

    @Test public void testGetBoolean() {
	assertTrue( conf.getBoolean("baz") );	
	assertFalse( conf.getBoolean("hello",false) );
    }

    @Test public void testToProperties() {
	final Properties props = conf.toProperties();
	assertEquals( props.getProperty("foo" ) , "FOO" );		     
	assertEquals( props.getProperty("bar" ) , "12" );		     
	assertEquals( props.getProperty("baz" ) , "true" );		     
	assertEquals( props.getProperty("pi" ) , "3.14" );		     
    }

    @Test public void testFromProperties() {
	final Properties props = conf.toProperties();
	props.setProperty( "hello", "world" );
	props.setProperty( "value", "333" );
	conf = Configuration.fromProperties( props );
	assertEquals( conf.get("hello"), "world" );	
	assertEquals( conf.getInt("value"), 333 );	
    }

}

