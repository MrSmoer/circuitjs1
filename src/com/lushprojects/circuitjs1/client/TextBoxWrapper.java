package com.lushprojects.circuitjs1.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;

public class TextBoxWrapper extends TextBox {
    Command command;
    
    TextBoxWrapper(Command command){
	super();
	this.command=command;
    }
    
}
