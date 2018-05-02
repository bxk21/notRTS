package main.Managers;

import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class InputManager extends JPanel implements KeyListener, MouseListener {
	private HashMap<Integer, Boolean> keysInput = new HashMap<>();
	//TODO: change to ArrayList, with false being absence in list
	public final int MOUSE_LEFT_NUMBER = 10001;
	public final int MOUSE_RIGHT_NUMBER = 10002;
	private final Integer[] keyNames= {KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_SPACE, KeyEvent.VK_CONTROL, KeyEvent.VK_ESCAPE, MOUSE_LEFT_NUMBER, MOUSE_RIGHT_NUMBER};
	Frame frame;
	public InputManager(){
		initList();
	}
	
	/**
	 * Loads (and clears) the input Hashmap
	 * @return
	 */
	public HashMap<Integer, Boolean> loadInput(){
		HashMap<Integer, Boolean> temp = new HashMap<>(keysInput);
//		clearList();
		return temp;
	}
	
	/**
	 * Gets the position of the mouse relative to the window
	 * @return
	 */
	public int[] loadMouse() {
		if(frame==null){
			this.frame = Main.outputManager.getFrame();
		}
		
		int x = (int) (MouseInfo.getPointerInfo().getLocation().getX() - frame.getLocationOnScreen().getX());
		int y = (int) (MouseInfo.getPointerInfo().getLocation().getY() - frame.getLocationOnScreen().getY());
		if (Main.IS_TESTING) {
//			System.out.println("Mouse X: " + x);
//			System.out.println("Mouse Y: " + y);
		}
		return new int[]{x, y};
	}
	
	/**
	 * Clears all inputs
	 * A little safer than initList. Clears items that might not be in keyNames
	 * Shouldn't be necessary, as input keysInput should be the same as output keysInput
	 */
	private void clearList(){
		for(int i : keysInput.keySet()){
			keysInput.put(i,false);
		}
	}
	
	/**
	 * Initializes all inputs
	 * To be used only once at creation
	 */
	private void initList(){
		for(int i : keyNames){
			keysInput.put(i,false);
		}
	}
	
	/**
	 * Mouse Event for menu items
	 * @param event MouseEvent
	 */
	public void mouseClicked(MouseEvent event) {
		//only allow if paused
		if (Main.isPaused() && event.getButton()==MouseEvent.BUTTON1){//Left mouse button
			int x = event.getX();
			int y = event.getY();
			//TODO: Menu Switch
		}
		
	}
	
	/**
	 * Mouse Event for aiming abilities
	 * @param event
	 */
	@Override
	public void mousePressed(MouseEvent event) {
//		System.out.println("Mouse Down");
		if (Main.isPaused()) {
			return;
		}
		switch (event.getButton()){
			case 1:{
//				lmbX = event.getX();
//				lmbY = event.getY();
				keysInput.put(MOUSE_LEFT_NUMBER, true);
				break;
			}
			case 2:{
//				rmbX = event.getX();
//				rmbY = event.getY();
				keysInput.put(MOUSE_RIGHT_NUMBER, true);
				break;
			}
		}
//		inputs.add()
	}
	
	@Override
	public void mouseReleased(MouseEvent event) {
//		System.out.println("Mouse Up");
		if (Main.isPaused()) {
			return;
		}
		switch (event.getButton()){
			case 1:{
//				lmbX = event.getX();
//				lmbY = event.getY();
				keysInput.put(MOUSE_LEFT_NUMBER, false);
				break;
			}
			case 2:{
//				rmbX = event.getX();
//				rmbY = event.getY();
				keysInput.put(MOUSE_RIGHT_NUMBER, false);
				break;
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent event) {
	
	}
	
	@Override
	public void mouseExited(MouseEvent event) {
	
	}
	
	@Override
	public void keyTyped(KeyEvent event) {
	
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
//		keyBuffer.add(e.getKeyChar());
		if(Main.IS_TESTING) {
//			System.out.println("Key Pressed: " + event.getKeyChar() + " with key code: " + event.getKeyCode());
		}
		
		keysInput.put(event.getKeyCode(), true);
	}
	
	/**
	 * keysInput for menu items
	 * @param event
	 */
	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE){
			Main.togglePause();
			updateMenu(Main.menuState);
//			clearList();//TODO: decide whether or not to accept inputs
		}
		
		if (keysInput.get(KeyEvent.VK_CONTROL) && event.getKeyCode() == KeyEvent.VK_S && Main.isPaused()){
			StringSelection stringSelection = new StringSelection(Main.getSeedHex());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
		
		
		
		keysInput.put(event.getKeyCode(), false);
	}
	/**
	 * Update Menu UI
	 * @param menuState
	 */
	private void updateMenu(int menuState) {
		//TODO
	}
}
