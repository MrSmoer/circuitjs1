package com.lushprojects.circuitjs1.client;

import java.text.ParseException;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBox;
import com.lushprojects.circuitjs1.client.util.Locale;
import com.lushprojects.circuitjs1.client.EditDialog;

// TODO do we need to implement command in subclasses?
public class AdjustableTextbox extends Adjustable implements Command{
    
    TextBoxWrapper textbox;
    AdjustableTextbox(CircuitElm ce, int item) {
	super(ce, item);
    }

    //undump
    AdjustableTextbox(StringTokenizer st, CirSim sim) {
	super(st, sim);
	int e = Integer.parseInt(st.nextToken());
	try {
	    String ei = st.nextToken();
	    if (ei.startsWith("F")) {
		flags = Integer.parseInt(ei.substring(1));
		st.nextToken();
	    }
	    
	} catch (Exception ex) {}
	
	try {
	    elm = sim.getElm(e);
	} catch (Exception ex) {}
    }

    @Override
    boolean createElement(CirSim sim, double value) {
	// TODO Auto-generated method stub
	sim.addWidgetToVerticalPanel(label = new Label(Locale.LS(labelText)));
	label.addStyleName("topSpace");
	String boxcontent = elm.getUnitText(value, "");
	sim.addWidgetToVerticalPanel(textbox = new TextBoxWrapper(this));
	AdjustableTextbox tbx = this;
	// TODO maybe set the correct handlers
	textbox.addKeyPressHandler( new KeyPressHandler () {
	    
	    @Override
	    public void onKeyPress(KeyPressEvent e) {
		Object src = e.getSource();
		//boolean change = false;
		//boolean applied = false;
		//elm.setEditValue(editItem, ei);
		tbx.execute();
		
	    }
	});
	textbox.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent e){
			Object src = e.getSource();
			//boolean change = false;
			//boolean applied = false;
			//elm.setEditValue(editItem, ei);
			tbx.execute();
		}
	});
	textbox.setText(boxcontent);
	return false;
    }

    @Override
    void executeElement() {
	// TODO Auto-generated method stub
	// TODO was macht das?
	elm.sim.analyzeFlag = true;
	EditInfo ei = elm.getEditInfo(editItem);
	try {
	    ei.value = EditDialog.parseUnits(textbox.getText());
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    ei.value = 1;
	}
	elm.setEditValue(editItem, ei);
	elm.sim.repaint();

    }

    @Override
    boolean customDeleter(CirSim sim) {
	sim.removeWidgetFromVerticalPanel(textbox);
	return true;
    }

    @Override
    String dump() {
	// TODO Auto-generated method stub
	return null;
    }

}
