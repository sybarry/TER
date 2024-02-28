package pilottageColorSensor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pilottageMQTT.Action;

public class Automate {
  private  String black;
  private  String white;
  private  String red;
  private Set<String> keys;
  private JSONObject jsonPayload;
  private Map<String, Action_Etat> automate;
  Pattern pattern = Pattern.compile("([a-zA-Z]+)\\((\\d+)\\)(-?\\d+)?");
        public  Automate(String payload) throws ParseException {
         
        	JSONParser parser = new JSONParser();
             jsonPayload = (JSONObject) parser.parse(payload);
          // Extraire les valeurs des champs "action" et "nom"
              automate = new HashMap<String, Action_Etat>();
             keys = jsonPayload.keySet();
             BuildAutomate(keys);
                
        }
        
        public void  BuildAutomate(Set<String> keys) {
        	
        	for(String key : keys) {
        		String value = (String) jsonPayload.get(key);
        		Matcher matcher = pattern.matcher(value);
        		Action actionElement = null;
        		   if (matcher.matches()) {
        	            String action = matcher.group(1);
        	            int temps = Integer.parseInt(matcher.group(2));
        	            String etat_destination = matcher.group(3);
                    
        	            
        	         // Si remainingValue est présent, retirez le signe négatif
        	            if (etat_destination != null && etat_destination.startsWith("-")) {
        	                etat_destination = etat_destination.substring(1);
        	            }
        	            
        	            actionElement = Action(action);
        	          
        	            Action_Etat element = new Action_Etat(actionElement, temps, etat_destination);
        	            
        	             automate.put(key, element);
        	              
        	       
        	        } else {
        	            System.out.println("La chaîne ne correspond pas au format attendu.");
        	        }
        	}
        	
        	
        	
        }
        public Action Action(String action) {
        	switch (action) {
			case "g":
				 return Action.FORWARD;
			case "b":
				return Action.BACKWARD;
			case "s":
				return Action.STOP;
			case "l":
				 return Action.LEFT;
			case "r":
				 return Action.RIGHT;

			default:
				return null;
			}
        }
        
        public String getWhite() {
        	return white;
        }
        public String getBlack() {
        	return black;
        }
        public String getRed() {
        	return red;
        }
        
        public Map<String,Action_Etat> getAutomate() {
        	
        	return this.automate;
        }
        
        
        
}
