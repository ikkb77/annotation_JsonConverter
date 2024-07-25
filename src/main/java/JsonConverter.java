import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JsonConverter {
    public static void main(String[] args) {
        try {
            // Read the original JSON file
            ObjectMapper mapper = new ObjectMapper();
            JsonNode originalJson = mapper.readTree(new File("bb-fortify-annotations.json"));

            // Create the new JSON structure
            ObjectNode newJson = mapper.createObjectNode();
            ArrayNode annotationsArray = mapper.createArrayNode();

            // Populate the new JSON structure
            for (JsonNode node : originalJson) {
                ObjectNode annotation = mapper.createObjectNode();
                annotation.put("path", node.get("path").asText());
                annotation.put("line", node.get("line").asInt());
                annotation.put("message", node.get("summary").asText());
                annotation.put("severity", node.get("severity").asText().equalsIgnoreCase("CRITICAL")?"HIGH":node.get("severity").asText());
                annotation.put("link", node.get("link").asText());
                annotationsArray.add(annotation);
            }

            newJson.set("annotations", annotationsArray);

            // Write the new JSON to a file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("bb-fortify-annotations2.json"), newJson);
            System.out.println("annotation JSON transformation complete. Check output.json for results.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
