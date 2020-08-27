package org.eclipse.ice.dev.jsonschemaconverter;

import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.eclipse.ice.dev.pojofromjson.*;
import org.eclipse.ice.dev.annotations.processors.Field;


/**
 * utility to convert json schemas to the json format that the PojoFromJson project accepts
 * Pieces of code was derived from Daniel Bluhm's org.eclipse.ice.dev.PojoFromJson
 * @author gzi
 *
 */
public class JsonSchemaConverter {
	
	/**
	 * Mapper used for deserializing POJO Outline JSON
	 */
	private static ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;

	/**
	 * List of files to operate on.
	 */
	@Parameter(description = "FILE [FILE...]")
	private List<String> jsonFiles = new ArrayList<>();

	/**
	 * Directory to output generated files into.
	 */
	@Parameter(names = {"-o", "--output"}, description = "Output directory")
	private static String output = ".";

   
	public static void main( String[] args )
	{
       JsonSchemaConverter app = new JsonSchemaConverter();
       app.run(args);
	}
   
	public void run(String... args) {
		JCommander jcomm = JCommander.newBuilder()
   			.addObject(this)
   			.build();
   		jcomm.setProgramName("JsonSchemaConverter");
   		jcomm.parse(args);
   		
   		try {
   			if (jsonFiles.size() == 0) {
   				handleInputJson(System.in, Path.of(output), "");
   			}
   			for (String filePath : jsonFiles) {
   				
   				try (FileInputStream inputJson = new FileInputStream(filePath)) {
   					handleInputJson(inputJson, Path.of(output), filePath);
   				}
   			}
   		} catch (Exception ex) {
   			System.err.println(ex.getMessage());
   			System.exit(1);
   		}
   }
   
   public static void handleInputJson(InputStream is, Path destination,String filePath) throws JsonParseException, JsonMappingException, IOException {
	   	Map<String, Object> map = mapper.readValue(is, new TypeReference<Map<String,Object>>(){});
	   	List<PojoOutline> jsonArrayOut = new ArrayList<>();
	   	for (Map.Entry<String, Object> entry : map.entrySet()) {
	   		List<Field> fields = new ArrayList<>();
	   		if (!entry.getKey().equals("definitions")) { //ignore definitions section 
		   		if (entry.getValue() instanceof Map) {
		   			Map<String, Object> innerNode = (Map<String, Object>) entry.getValue();
		   			
			   			if (innerNode.keySet().contains("anyOf")) { //default to string if multiple types are allowed
			   				Field n = Field.builder()
			   						.name(entry.getKey())
			   						.type("String")
			   						.docString(String.valueOf(innerNode.get("description")))
			   						.defaultValue("")
			   						.build();
				   			fields.add(n);
			   				
			   			} else if ("string".equals(innerNode.get("type"))) {
			   				Field n = Field.builder()
			   						.name(entry.getKey())
			   						.docString(String.valueOf(innerNode.get("description")))
			   						.defaultValue(String.valueOf(innerNode.get("default")))
			   						.build();
				   			n.setType(getTypeAsString(n.getDefaultValue()));
				   			fields.add(n);
			   			} else { //no type property or type='object'
			   				fields = processJsonObject(innerNode, entry.getKey());
			   			}   			
		   		} else if (entry.getValue() instanceof String){
		   			Field n = Field.builder()
	   						.name(entry.getKey())
	   						.defaultValue(String.valueOf(entry.getValue()))
	   						.build();
		   			n.setType(getTypeAsString(n.getDefaultValue()));
		   			fields.add(n);
		   		}
		   		PojoOutline po = PojoOutline.builder()
		   				.packageName("testpackage")
		   				.element(entry.getKey())
		   				.fields(fields)
		   				.build();
		   		jsonArrayOut.add(po);
	   		}
	   	}	
	   	writeJson(jsonArrayOut, filePath);  
	   	writeDataElements(jsonArrayOut);
   }
   
   
   public static List<Field> processJsonObject(Map<String, Object> map, String key) {
	   List<Field> fields = new ArrayList<>();
	   if ("object".equals(map.get("type")) && map.get("properties") instanceof Map){
		   for (Map.Entry<String, Object> entry : ((Map<String, Object>) map.get("properties")).entrySet()) {
			   if (((Map<String, Object>)entry.getValue()).get("default") != null)  {
				   Field n = Field.builder()
	   						.name(entry.getKey())
	   						.defaultValue(String.valueOf(((Map<String, Object>)entry.getValue()).get("default")))
	   						.docString(String.valueOf(((Map<String, Object>)entry.getValue()).get("description")))
	   						.build();
				   n.setType(getTypeAsString(n.getDefaultValue()));		 			   
				   fields.add(n);
			   } else {
				   if (entry.getValue() instanceof Map) {				   
					   fields.addAll(processJsonObject((Map<String, Object>)entry.getValue(), entry.getKey()));					   
				   } else {
					   Field n = Field.builder()
		   						.name(entry.getKey())
		   						.defaultValue(String.valueOf(entry.getValue()))
		   						.build();
					   n.setType(getTypeAsString(n.getDefaultValue()));
					   fields.add(n);
				   }  
		   		}
		   }   
	   } else {
		   if (map.keySet().contains("default")) {
			   Field n = Field.builder()
  						.name(key)
  						.defaultValue(String.valueOf((map.get("default"))))
  						.build();
			   n.setType(getTypeAsString(n.getDefaultValue()));
			   fields.add(n);
		   } else {
			   for (Map.Entry<String, Object> entry : map.entrySet()) {
				   if (entry.getValue() instanceof Map) {
					   if (((Map<String, Object>)entry.getValue()).get("default") != null)  {
						   Field n = Field.builder()
			  						.name(entry.getKey())
			  						.defaultValue(String.valueOf(((Map<String, Object>)entry.getValue()).get("default")))
			  						.build();
						   n.setType(getTypeAsString(n.getDefaultValue()));
						   fields.add(n);
					   } else {
						   fields.addAll(processJsonObject((Map<String, Object>)entry.getValue(), entry.getKey()));
					   }
				   } else if(!(entry.getValue() instanceof ArrayList)) {
					   Field n = Field.builder()
		  						.name(key)
		  						.type("String")
		  						.docString(String.valueOf(entry.getValue()))
		  						.build();
					   fields.add(n);
				   }
			   }
		   }
	   }
	   return fields;
   }
   
   public static void writeJson(List<PojoOutline> json, String filePath) {
	   String file = filePath.strip().substring(filePath.lastIndexOf('/') + 1, filePath.length() - 5);
	   try {
		   mapper.writeValue(new File(output + "/" + file + "_" + "result.json"), json);
	   } catch (Exception e) {
		   System.err.println(e.getMessage());
	   }
   }
   
   public static void writeDataElements(List<PojoOutline> json) {
	   for (PojoOutline j : json) {
		   try {
			   PojoFromJson.createDataElement(j, Path.of(output));
		   } catch (Exception e) {
			   System.err.println(e.getMessage());
		   }
	   }
   }
   
   /**
    * check if input string represents a float/double. taken from https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html#valueOf(java.lang.String)
    * @param input string
    * @return if input string is a float
    */
   public static boolean isFloat(String input) {
	   final String Digits     = "(\\p{Digit}+)";
	   final String HexDigits  = "(\\p{XDigit}+)";
	   // an exponent is 'e' or 'E' followed by an optionally
	   // signed decimal integer.
	   final String Exp        = "[eE][+-]?"+Digits;
	   final String fpRegex    =
	       ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
	        "[+-]?(" + // Optional sign character
	        "NaN|" +           // "NaN" string
	        "Infinity|" +      // "Infinity" string

	        // A decimal floating-point string representing a finite positive
	        // number without a leading sign has at most five basic pieces:
	        // Digits . Digits ExponentPart FloatTypeSuffix
	        //
	        // Since this method allows integer-only strings as input
	        // in addition to strings of floating-point literals, the
	        // two sub-patterns below are simplifications of the grammar
	        // productions from section 3.10.2 of
	        // The Java™ Language Specification.

	        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
	        "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

	        // . Digits ExponentPart_opt FloatTypeSuffix_opt
	        "(\\.("+Digits+")("+Exp+")?)|"+

	        // Hexadecimal strings
	        "((" +
	         // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
	         "(0[xX]" + HexDigits + "(\\.)?)|" +

	         // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
	         "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

	         ")[pP][+-]?" + Digits + "))" +
	        "[fFdD]?))" +
	        "[\\x00-\\x20]*");// Optional trailing "whitespace"

	   return Pattern.matches(fpRegex, input);
   }
   
   public static boolean isBoolean(String input) {
	   return input.trim().equals("false") || input.trim().equals("true");
   }
   
   public static String getTypeAsString(String input) {
	   if (isFloat(input)) {
		   return "Float";
	   } else if (isBoolean(input)) {
		   return "Boolean";		
	   }
	   return "String";	
   }

}
