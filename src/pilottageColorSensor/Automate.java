package pilottageColorSensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
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
  private Map<String, ArrayList<Action_Etat>> automate;
  //Cet Regex permet de verifier que le fichier JSon respect bien le format de donnée attendu
  Pattern pattern = Pattern.compile("([a-zA-Z]+)\\((\\d+)\\)(?:-(\\d+))?");
        public  Automate(String payload) throws ParseException {
         
        	JSONParser parser = new JSONParser();
             jsonPayload = (JSONObject) parser.parse(payload);
          // Extraire les valeurs des champs "etats" et "action"
              automate = new HashMap<String, ArrayList<Action_Etat>>();
             keys = jsonPayload.keySet();
             //Conduit L'automate en utilisant un HashMap 
             BuildAutomate(keys);
                
        }
        
        
       /* Verifie que le format est correct et puis contruit l'automate comme suit recupere la valeur du fichier JSon en lui  
        * decomposant en action plus Etat, et chaque keys u fichier devient un etat
        * 
        * */
        
        public void BuildAutomate(Set<String> keys) {
            for (String key : keys) {
                Object valueObj = jsonPayload.get(key);

                if (valueObj instanceof JSONArray) {
                    processJSONArray(key, (JSONArray) valueObj);
                } else if (valueObj instanceof String) {
                    processString(key, (String) valueObj);
                }
            }
        }

        private void processJSONArray(String key, JSONArray jsonArray) {
            for (Object obj : jsonArray) {
                if (obj instanceof String) {
                    processString(key, (String) obj);
                }
            }
        }

        private void processString(String key, String value) {
            Matcher matcher = pattern.matcher(value);
            Action actionElement = null;
            
            if (matcher.matches()) {
                String action = matcher.group(1);
                int temps = Integer.parseInt(matcher.group(2));
                String etat_destination = matcher.group(3);

                if (etat_destination != null && etat_destination.startsWith("-")) {
                    etat_destination = etat_destination.substring(1);
                }

                actionElement = Action(action);

                Action_Etat element = new Action_Etat(actionElement, temps, etat_destination);

                if (automate.containsKey(key)) {
                    ArrayList<Action_Etat> list = automate.get(key);
                    list.add(element);
                    automate.put(key, list);
                } else {
                    ArrayList<Action_Etat> list = new ArrayList<>();
                    list.add(element);
                    automate.put(key, list);
                }
            } else {
                System.out.println("La chaîne ne correspond pas au format attendu.");
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
          
        public Map<String,ArrayList<Action_Etat>> getAutomate() {
        	
        	return this.automate;
        }
        
        
        
}
