package pilottageColorSensor;

import pilottageMQTT.Action;

public class Action_Etat {
	
	private Action action;
	private int temps;
	private String etat_destination;
	
	   public Action_Etat(Action action,int temps,String etat) {
		    this.action = action;
		    this.temps = temps;
		    this.etat_destination = etat;
		   
	   }

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public int getTemps() {
		return temps;
	}

	public void setTemps(int temps) {
		this.temps = temps;
	}

	public String getEtat_Destination() {
		return etat_destination;
	}

	public void setEtat(String etat) {
		this.etat_destination = etat;
	}
	   
	   

}
