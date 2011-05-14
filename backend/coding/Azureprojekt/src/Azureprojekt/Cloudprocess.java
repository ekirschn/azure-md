package Azureprojekt;

/** benoetigt:
 * appname
 * version
 * upgradepath
 * runningpath
 * 
 * @author Ronny
 *
 */


public class Cloudprocess extends Thread {
	
	String appname;
	Float version;
	String upgradepath;
	String runningpath;
	Boolean StopState = false;
	
	
	// running Method
	public void run() {
		while(!StopState){
			// do what u have to do
		}
		
	}
	  
	
	/**
	 * Getters und Setters
	 * @return
	 */
	public Boolean getStopState() {
		return StopState;
	}

	public void setStopState(Boolean stopState) {
		StopState = stopState;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public Float getVersion() {
		return version;
	}

	public void setVersion(Float version) {
		this.version = version;
	}

	public String getUpgradepath() {
		return upgradepath;
	}

	public void setUpgradepath(String upgradepath) {
		this.upgradepath = upgradepath;
	}

	public String getRunningpath() {
		return runningpath;
	}

	public void setRunningpath(String runningpath) {
		this.runningpath = runningpath;
	}
	
}
