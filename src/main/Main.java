package main;

import main.Managers.GameManager;
import main.Managers.InputManager;
import main.Managers.OutputManager;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
	public static boolean IS_TESTING = false;

	private static final int TICKRATE = 60;
	public static final int MICROSECONDS_PER_SECOND = 1000000;
	public static final int MILISECONDS_PER_SECOND = 1000;
	public static final int dTime = MICROSECONDS_PER_SECOND/ TICKRATE; // 1,000,000 / 60 = 16666.6 = 16667 repeating
	private static final TimeUnit timeUnit = TimeUnit.MICROSECONDS;
	private static final int LOCATION_RECHECK_TIME = 1; // seconds between rechecks of location of moving target
	public static final int LOCATION_RECHECK_TIMER = TICKRATE * LOCATION_RECHECK_TIME; // ticks between rechecks of location of moving target
	private static final int ATTACK_RECHECK_TIME = 1; // seconds between rechecks of location of moving target
	public static final int ATTACK_RECHECK_TIMER = TICKRATE * ATTACK_RECHECK_TIME; // ticks between rechecks of location of moving target

	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;
	public static final int SCALE = 64;
	private static long seed;
	private static String seedHex;
	public static int difficulty = 2;
	
	
	private static ScheduledExecutorService executorService;
	public static OutputManager outputManager;
	public static GameManager gameManager;
	public static InputManager inputManager;
	public static int menuState = 1; // 0: playing, 1: Main pause menu
	private static long sysTime = System.currentTimeMillis();
	
	public static void main(String[] args) {
		boolean seeded = false;
		for (int i = 0; i < args.length; i++){
			if (args[i].equalsIgnoreCase("--test")){
				IS_TESTING = true;
			}
			if (args[i].equalsIgnoreCase("-d") && i + 1 < args.length){
				difficulty = Integer.valueOf(args[i+1]);
				System.out.println("Difficulty: " + difficulty);
				difficulty = difficulty * difficulty;
			}
			if (args[i].equalsIgnoreCase("-s") && i + 1 < args.length){
				System.out.println("Reading Seed: " + args[i+1]);
				seed = Long.parseUnsignedLong(args[i+1], 16);
				seedHex = Long.toHexString(seed);
				System.out.println("Read as: " + Long.toHexString(seed));
				seeded = true;
			}
		}
		if (!seeded){
			Random random = new Random();
			seed = random.nextLong();
			seedHex = Long.toHexString(seed);
			System.out.println("SEED: " + seedHex + ", " + seed);
		}
		
		
		
		gameManager = new GameManager();
		inputManager = new InputManager();
		outputManager = new OutputManager(WIDTH, HEIGHT);
		
		setMenu(0);
		
	}
	private static void gameTick() {
		sysTime = System.currentTimeMillis();
		GameManager.tick(dTime);
		outputManager.display();
	}
	
	public static boolean isPaused(){
		return menuState!=0;
	}
	
	public static void setMenu(int newState) {
		
		if (menuState == 0 && newState == 1){//unpaused to paused
			if (IS_TESTING){
				System.out.println("Paused");
			}
			executorService.shutdown();
			outputManager.display();
//			executorService.awaitTermination(dTime, timeUnit);
		} else if (menuState == 1 && newState == 0){//paused to unpaused
			if (IS_TESTING){
				System.out.println("Unpaused");
			}
			executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.scheduleAtFixedRate(Main::gameTick, 0, dTime, timeUnit);
		}
		menuState=newState;
	}
	
	public static void togglePause() {
		if (menuState==0){
			setMenu(1);
		} else {//any other menuState
			setMenu(0);
		}
	}
	
	/**
	 * Gets System "time" synchronized every step.
	 * @return
	 */
	public static long sysTime() {
//		System.out.println("Time: " + sysTime);
		return sysTime;
	}
	
	public static long getSeed() {
		return seed;
	}
	
	public static String getSeedHex(){
		return seedHex;
	}
}
