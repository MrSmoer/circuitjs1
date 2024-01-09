package com.lushprojects.circuitjs1.client;

import java.util.Vector;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.lushprojects.circuitjs1.client.util.Locale;


// values with sliders
public class AdjustableScroll extends Adjustable implements Command {
    double minValue, maxValue;
    //int flags;
    
    // TODO remove this
    //String sliderText;
    
    // null if this Adjustable has its own slider, non-null if it's sharing another one.
    //Adjustable sharedAdjustable;
    
    
    // index of value in getEditInfo() list that this slider controls
    
    
    
    Scrollbar slider;
    
    
    AdjustableScroll(CircuitElm ce, int item) {
	super(ce, item);
	minValue = 1;
	maxValue = 1000;

	
	EditInfo ei = ce.getEditInfo(editItem);
        if (ei != null && ei.maxVal > 0) {
            minValue = ei.minVal;
            maxValue = ei.maxVal;
        }
    }

    // undump
    AdjustableScroll(StringTokenizer st, CirSim sim) {
	super(st, sim);
	int e = Integer.parseInt(st.nextToken());
	if (e == -1)
	    return;
	try {
	    String ei = st.nextToken();

	    // forgot to dump a "flags" field in the initial code, so we have to do this to support backward compatibility
	    if (ei.startsWith("F")) {
		flags = Integer.parseInt(ei.substring(1));
		ei = st.nextToken();
	    }
	    
	    editItem = Integer.parseInt(ei);
	    minValue = Double.parseDouble(st.nextToken());
	    maxValue = Double.parseDouble(st.nextToken());
	    if ((flags & FLAG_SHARED) != 0) {
		int ano = Integer.parseInt(st.nextToken());
		sharedAdjustable = ano == -1 ? null : sim.adjustables.get(ano);
	    }
	    labelText = CustomLogicModel.unescape(st.nextToken());
	} catch (Exception ex) {}
	try {
	    elm = sim.getElm(e);
	} catch (Exception ex) {}
    }
    

    @Override
    public boolean createElement(CirSim sim, double value) {
        sim.addWidgetToVerticalPanel(label = new Label(Locale.LS(labelText)));
        label.addStyleName("topSpace");
        int intValue = (int) ((value-minValue)*100/(maxValue-minValue));
        sim.addWidgetToVerticalPanel(slider = new Scrollbar(Scrollbar.HORIZONTAL, intValue, 1, 0, 101, this, elm));
        return true;
    }

    
    // TODO what is this doin here?
    void setSliderValue(double value) {
	
	if (sharedAdjustable != null && sharedAdjustable instanceof AdjustableScroll) {
	    AdjustableScroll sharedSlider = (AdjustableScroll)sharedAdjustable;
	    sharedSlider.setSliderValue(value);
	    return;
	}
        int intValue = (int) ((value-minValue)*100/(maxValue-minValue));
        settingValue = true; // don't recursively set value again in execute()
        slider.setValue(intValue);
        settingValue = false;
    }
    
    
    @Override
    void executeElement() {
	elm.sim.analyzeFlag = true;
	EditInfo ei = elm.getEditInfo(editItem);
	ei.value = getSliderValue();
	elm.setEditValue(editItem, ei);
	elm.sim.repaint();
    }
    
    double getSliderValue() {
	double val;
        if(sharedAdjustable != null && sharedAdjustable instanceof AdjustableScroll) {
            AdjustableScroll adj = (AdjustableScroll) sharedAdjustable;
            val = adj == null ? slider.getValue() : adj.slider.getValue();
            
        }else {
            
            val = slider.getValue();
        }
        return minValue + (maxValue-minValue)*val/100;
	// TODO this is wrong.
    }
    
    @Override
    boolean customDeleter(CirSim sim){
	sim.removeWidgetFromVerticalPanel(slider);
	return true;
    }
    
    void setMouseElm(CircuitElm e) {
	if (slider != null)
	    slider.draw();
    }
    
    @Override
    String dump() {
	int ano = -1;
	if (sharedAdjustable != null)
	    ano = CirSim.theSim.adjustables.indexOf(sharedAdjustable);
	
	return elm.sim.locateElm(elm) + " F1 " + editItem + " " + minValue + " " + maxValue + " " + ano + " " +
			CustomLogicModel.escape(labelText);
    }
    

}
