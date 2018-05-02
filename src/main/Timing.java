package main;

import java.util.ArrayList;

public class Timing {
	private String name;
	private long start;
	private long inoperableStart;
	private long inoperableEnd;
	private long end;
	
	/**
	 * Creates a timing
	 * @param name name used for identification
	 * @param start absolute time
	 * @param iStart time relative to start in seconds
	 * @param iEnd time relative to start in seconds
	 * @param end time relative to start in seconds
//	 * @param timings timings relative to start in seconds
	 */
	public Timing(String name, long start, double iStart, double iEnd, double end){//, ArrayList<Double> timings
		this.name = name;
		this.start = start;
		inoperableStart = start + (long)(iStart * Main.MILISECONDS_PER_SECOND);
		inoperableEnd = start + (long)(iEnd * Main.MILISECONDS_PER_SECOND);
		this.end = start + (long)(end * Main.MILISECONDS_PER_SECOND);
		
//		System.out.println("Timing: " + start + ", " + inoperableStart + ", " + inoperableEnd + ", " + this.end);
		
//		Collections.sort(timings);//sort timings by ascending order
//		for (Double timing : timings){
//			this.timings.add(start + (long)(timing * Main.MICROSECONDS_PER_SECOND));
//		}
	}
	
	/**
	 * Gets the state of the timing
	 * @param time absolute time
	 * @return 0 if not within timing
	 *         1 if within but not in inoperable
	 *         2 if inoperable
	 */
	public int getState(long time){
//		assert (start<=inoperableStart && inoperableStart<=inoperableEnd && inoperableEnd<=end);
		if (time < start || end < time){
			return 0;
		} else if (time < inoperableStart || inoperableEnd < time) {
			return 1;
		} else {
			return 2;
		}
	}
	
	/**
	 * Finds if a timing exists in a list of timings
	 * @param name
	 * @param timings
	 * @return
	 */
	static public boolean exists(String name, ArrayList<Timing> timings){
//		System.out.println("exists? " + timings.size());
//		if (!timings.isEmpty()) {
			for (Timing timing : timings) {
				if (timing.name == name) {
					return true;
				}
			}
//		}
		return false;
	}
	
	/**
	 * Checks all timings
	 * @param timings
	 * @return false if any timing blocks
	 */
	static public boolean checkTimings(ArrayList<Timing> timings){
		boolean cleared = true;
//		if (!timings.isEmpty()) {
			for (Timing timing : new ArrayList<>(timings)) {
				int state = timing.getState(Main.sysTime());
//				System.out.println("Timing of " + timing + " = " + state);
				if (state == 0) {
//					System.out.println("Timing " + timing + " removed");
					timings.remove(timing);
				} else if (state == 2) {
//					System.out.println("Timing Blocked");
					cleared = false;
				}
//				if (timings.isEmpty()) {
//					break;
//				}
			}
//		}
//		System.out.println("Cleared: " + cleared);
		return cleared;
	}
	
	
	public String toString() {
		return name;
	}
}
