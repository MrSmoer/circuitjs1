package com.lushprojects.circuitjs1.client;

import java.util.Vector;

public class DesignatorManager {

    static boolean isUnused(Vector<CircuitElm> otherElements, String suffix) {
	boolean notUsed = true;
	for (CircuitElm c : otherElements)  {
	    if(c.getDesignatorSuffix() == suffix) {
		notUsed = false;
		break;
	    }
	}
	
	return notUsed;
	
	
    }
    
    static String getNextDesignatorSuffix(Vector<CircuitElm> otherElements) {
	// fill used with all already used numbers
	Vector<Integer> used = new Vector<>();
	for (CircuitElm c : otherElements) {
		StringBuilder b = new StringBuilder();
		String result;
		for (char character : c.getDesignatorSuffix().toCharArray()) {
		    if (Character.isDigit(character)) {
			b.append(character);
		    } else {
			break;
		    }
		}
		result = b.toString();
		if (result.length() > 0  && !used.contains(Integer.parseInt(result))) {
		    used.addElement(Integer.parseInt(result));
		}
	}
	// find an int not in used -> unused
	int i = 1;
	while(true) {
	    if (!used.contains(i)) {
		break;
	    }
	    i++;
	}
	return String.valueOf(i);
    }
}
