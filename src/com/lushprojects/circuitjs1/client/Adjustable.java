package com.lushprojects.circuitjs1.client;

import java.util.Vector;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.lushprojects.circuitjs1.client.CirSim;

public abstract class Adjustable implements Command{
    CircuitElm elm;
    double value;
    String labelText;
    int flags;
    
    Adjustable sharedAdjustable;
    
    final int FLAG_SHARED = 1;
    int editItem;
    
    Label label;
    boolean settingValue;
    
    Adjustable(CircuitElm ce, int item){
	flags = 0;
	elm = ce;
	editItem = item;

    }
    
    Adjustable(StringTokenizer st, CirSim sim){
	
    }
    
    public boolean createElement(CirSim sim) {
	if (elm == null)
	    return false;
	EditInfo ei = elm.getEditInfo(editItem);
	if (ei == null)
	    return false;
	if (sharedAdjustable != null)
	    return true;
	if (labelText.length() == 0)
	    return false;
	double value = ei.value;
	createElement(sim, value);
	return true;
    }
    
    abstract boolean createElement(CirSim sim, double value);

    public void execute() {
	if (settingValue)
	    return;
	int i;
	CirSim sim = CirSim.theSim;
	for (i = 0; i != sim.adjustables.size(); i++) {
	    Adjustable adj = sim.adjustables.get(i);
	    if (adj == this || adj.sharedAdjustable == this)
		adj.executeElement();
	}
    }
    
    // actually not sure if this needs to be implemented
    // by subclasses, maybe we can move it here
    abstract void executeElement();
    
    void delete(CirSim sim) {
	try {
	    sim.removeWidgetFromVerticalPanel(label);
	    // TODO replace this with display element from not yet existing interface
	    //
	    customDeleter(sim);
	    
	} catch (Exception e) {}
    }

    abstract boolean customDeleter(CirSim sim);
    
    boolean adjustableBeingShared() {
	int i;
	for (i = 0; i != CirSim.theSim.adjustables.size(); i++) {
	    Adjustable adj = CirSim.theSim.adjustables.get(i);
	    if (adj.sharedAdjustable == this)
		return true;
	}
	return false;
    }
    
    
    abstract String dump();
    
    // reorder adjustables so that items with sliders come first in the list, followed by items that reference them.
    // this simplifies the UI code, and also makes it much easier to dump/undump the adjustables list, since we will
    // always be undumping the adjustables with sliders first, then the adjustables that reference them.
    static void reorderAdjustables() {
	Vector<Adjustable> newList = new Vector<Adjustable>();
	Vector<Adjustable> oldList = CirSim.theSim.adjustables;
	int i;
	for (i = 0; i != oldList.size(); i++) {
	    Adjustable adj = oldList.get(i);
	    if (adj.sharedAdjustable == null)
		newList.add(adj);
	}
	for (i = 0; i != oldList.size(); i++) {
	    Adjustable adj = oldList.get(i);
	    if (adj.sharedAdjustable != null)
		newList.add(adj);
	}
	CirSim.theSim.adjustables = newList;
    }

}
