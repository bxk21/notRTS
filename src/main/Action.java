package main;

public abstract class Action {
	protected String name; // name of action
	protected Entity actor; // entity doing action
	protected Location actee; // location (or entity) acted upon
	protected Action preAction; // action to be done before doing this
//	protected Action defaultAction; // action to be done after doing this if there is no other action in the queue
	protected int timeTotal;
	protected int timeLeft = Integer.MAX_VALUE;
	private boolean started = false;
	
	/**
	 * Initiates the actions, setting the timer.
	 */
	public Action(Entity actor, Location actee) {
		this.actor = actor;
		this.actee = actee;
		
	}
	public Action(Entity actor, Location actee, int timeTotal) {
		this(actor, actee);
		this.timeTotal = timeTotal;
		timeLeft = timeTotal;
	}
	
	
	
	/**
	 * The Core Loop for an action
	 * @param time time passed since last actions
	 * @return whether or not the action is finished
	 */
	final public boolean tick(int time) {
		if (!condition()) {
			if(Main.IS_TESTING) {
				System.out.println("Condition Failed for Action: " + this);
			}
			return true;
		}
		
		// PreAction
		if (!prerequisite()){
			if(preAction != null) {
				preAction.tick(time);
				return false;//TODO return tick(time remaining)
			} else {
				System.err.println("ERROR! Prerequisite failed without preAction");
				System.exit(1);
			}
		}
		if (Main.IS_TESTING){
//			System.out.println(this);
		}
		
		if (!started){
			onStart();
			started = true;
		}
		//Action
		if (act(time)){
			System.out.println("Action Finished " + this);
			onEnd();
			return true;
		}
		
		//update time
		timeLeft -= time;
		actor.updateSprite(timeLeft);
		
		if (timeLeft<=0){
			System.out.println("Action Timed out " + this);
			onEnd();
			return true;
		}
		
		return false;
	}
	
	protected abstract void onStart();
	
	protected abstract void onEnd();
	
	/**
	 * Do Action
	 * @param time
	 */
	protected abstract boolean act(int time);

//	public boolean isCompleted() {
//		return (timeLeft <= 0);
//	}
	
	/**
	 * Condition to do actions.
	 * If fails, Action cancels (finishes immediately)
	 * @return
	 */
	protected abstract boolean condition();
	
	/**
	 * Prerequisite to do actions.
	 * If fails, does preAction instead
	 * @return
	 */
	protected abstract boolean prerequisite();
	
	protected abstract Action defaultAction();
	
	@Override
	public String toString() {
		return actor + ": " + name + " " + actee;
	}
}
