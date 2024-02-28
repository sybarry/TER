package pilottageColorSensor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Automate {
  private  String black;
  private  String white;
  private  String red;
  
     
        public  Automate(String payload) throws ParseException {
        	JSONParser parser = new JSONParser();
            JSONObject jsonPayload = (JSONObject) parser.parse(payload);
          // Extraire les valeurs des champs "action" et "nom"
          white   = (String) jsonPayload.get("White");
          black = (String) jsonPayload.get("Black");
          red = (String) jsonPayload.get("Red");
        
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
        
        
        
        
}
