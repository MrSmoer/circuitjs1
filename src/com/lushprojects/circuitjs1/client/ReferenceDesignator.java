package com.lushprojects.circuitjs1.client;

public enum ReferenceDesignator {
    C("C"), // Capacitor
    R("R"), // Resistor
    K("K"), // relay
    D("D"), // Diode
    L("L"), // Inductor
    Q("Q"), // Transistor/chip something
    G("G"); // oscillator?
    
    private final String designator;
    ReferenceDesignator(String designatorString){
	this.designator = designatorString;
    }
    public String getDesignator() {
	return designator;
    }
}
