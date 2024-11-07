package org.itp.enums;

public enum Tables {

	    CUSTOMERS("customers"),
	    READING("reading");

	    private final String name;
	    Tables(String name) {
	        this.name = name;
	    }
	    public String toString() {
	        return name;
	    }
}
