package main.Actions;

import main.Action;

//TODO: Remove?

public class ActionNone extends Action {

	public ActionNone() {
		super(null,null);
		name = "None";
		timeTotal = 0;//No Time
	}
	
	protected void onStart() {
	
	}
	
	protected void onEnd() {
	
	}
	
	protected boolean act(int time) {
		return false;
	}
	
	protected boolean condition() {
		return true;
	}
	
	protected boolean prerequisite() {
		return true;
	}
	
	protected Action defaultAction() {
		return null;
	}
}
