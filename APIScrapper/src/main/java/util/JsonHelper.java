package util;
import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.*;

public class JsonHelper {
	
	public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error mapping object (" + data + ") to JSON");
        }
    }

}
