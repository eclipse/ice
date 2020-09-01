package org.eclipse.ice.dev.jsonschemaconverter;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
	
	/**
	 * Package name of generated java files.
	 */
	@Parameter(names = {"-p", "--package"}, description = "Package Name of Generated Files")
	private static String packageName = "";

   /**
    * Executes program
    * @param args command line arguments
    */
	public static void main( String[] args )
	{
       JsonSchemaConverter app = new JsonSchemaConverter();
       app.run(args);
	}
   
	/**
	 * Parse arguments and continues execution of program
	 * @param args command line arguments
	 */
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
   /**
    * Converts given json schema file into a json format accepted by org.eclipse.ice.dev.annotations
    * @param is InputStream of original json schema file
    * @param destination directory where output files will written
    * @param filePath string representing path to input file
    * @throws JsonParseException On failure to parse the input json schema file
    * @throws JsonMappingException On failure to map the json schema to Map<String, Object> 
    * @throws IOException On failure to write files
    */
   public static void handleInputJson(InputStream is, Path destination, String filePath) throws JsonParseException, JsonMappingException, IOException {
	   	Map<String, Object> map = mapper.readValue(is, new TypeReference<Map<String,Object>>(){});
	   	List<PojoOutline> jsonArrayOut = new ArrayList<>();
	   	String fileName = filePath.strip().substring(filePath.lastIndexOf('/') + 1, filePath.length() - 5); //get the filename from the path
	   	packageName = packageName.equals("") ? fileName.toLowerCase(): packageName; //if no package name is given, set it to the input filename
	   	
	   	List<Map.Entry<String, Object>> entries = map.entrySet().stream()
	   															.filter(e -> !e.getKey().equals("definitions"))
	   															.collect(Collectors.toList());  //ignore definitions section 
	   	
	   	for (Map.Entry<String, Object> entry : entries.stream()
	   												.filter(e -> e.getValue() instanceof Map)
	   												.collect(Collectors.toList())) {
	   		List<Field> fields = new ArrayList<>();
	   		Map<String, Object> innerNode = (Map<String, Object>) entry.getValue();   			
	   			if (innerNode.keySet().contains("anyOf")) { //default to string if multiple types are allowed
	   				Field n = Field.builder()
	   						.name(getValidName(entry.getKey()))
	   						.type("String")
	   						.docString(String.valueOf(innerNode.get("description")))
	   						.defaultValue("")
	   						.build();
	   				fields.add(n);			   				
	   			} else if ("string".equals(innerNode.get("type"))) {
	   				Field n = Field.builder()
	   						.name(getValidName(entry.getKey()))
	   						.docString(String.valueOf(innerNode.get("description")))
	   						.defaultValue(String.valueOf(innerNode.get("default")))
	   						.build();
				   	n.setType(getTypeAsString(n.getDefaultValue()));
				   	fields.add(n);
	   			} else { //no type property or type='object'
	   				fields = processJsonObject(innerNode, entry.getKey());
	   			} 
	   			PojoOutline po = PojoOutline.builder()
		   				.packageName(packageName)
		   				.element(entry.getKey())
		   				.fields(fields)
		   				.build();
		   		jsonArrayOut.add(po);
	   	}
	   	
	   	List<Field> fields = new ArrayList<>(); //collect all top level string properties into one file
	   	entries.stream().filter(e -> !(e.getValue() instanceof Map)).forEach(e -> {
	   		Field n = Field.builder()
   					.name(getValidName(e.getKey()))
   					.defaultValue(String.valueOf(e.getValue()))
   					.build();
	   		n.setType(getTypeAsString(n.getDefaultValue()));
	   		fields.add(n);
	   	});
	   	PojoOutline po = PojoOutline.builder()
   				.packageName(packageName)
   				.element(fileName + "Properties")
   				.fields(fields)
   				.build();
   		jsonArrayOut.add(po);
   		
	   	writeJson(jsonArrayOut, fileName);  
	   	writeDataElements(jsonArrayOut);
   }
   
   /**
    * Helper function to recursively obtain all of the fields from a map 
    * @param map map to get data fields from
    * @param key the name of key used to access this map from its parent map
    * @return list of Fields representing the data from the input map
    */
   public static List<Field> processJsonObject(Map<String, Object> map, String key) {
	   List<Field> fields = new ArrayList<>();
	   if (map.keySet().contains("default")) {
		   Field n = Field.builder()
						.name(getValidName(key))
						.build();
		   //set doc string to the provided description. if description isn't provided, then you $ref. If $ref is also not provided, then uses 'null'
		   n.setDocString(map.get("description") == null ? String.valueOf(map.get("$ref")):String.valueOf(map.get("description"))); 
		   if (map.get("default") instanceof ArrayList) {
			   n.setDefaultValue(formatArrayListAsString((ArrayList) map.get("default")));
			   n.setType(getTypeAsString(String.valueOf(((ArrayList) map.get("default")).get(0))) + "[]");
		   } else {
			   n.setDefaultValue(String.valueOf(map.get("default")));
			   n.setType(getTypeAsString(n.getDefaultValue()));
		   }
	
		   fields.add(n);
		   return fields;
	   }
	   map.entrySet().stream().forEach(e -> {
		   if (e.getValue() instanceof Map) {
			   fields.addAll(processJsonObject((Map<String, Object>)e.getValue(), e.getKey()));		
		   } else if (!(e.getValue() instanceof ArrayList)) {
			   Field n = Field.builder()
 						.name(getValidName(key))
 						.type("String")
 						.docString(String.valueOf(e.getValue()))
 						.build();
			   fields.add(n);
		   } else {
			   Field n = Field.builder()
  						.name(getValidName(e.getKey()))
  						.defaultValue(String.valueOf(e.getValue()))
  						.build();
			   n.setType(getTypeAsString(n.getDefaultValue()) + "[]");
			   fields.add(n);
		   }   	   
	   });
	   return fields;
   }
   
   /**
    * Write the converted json file, represented as a list of PojoOutline to output destination
    * @param json converted json file represented as a list of PojoOultine
    * @param file the name of the new json file
    */
   public static void writeJson(List<PojoOutline> json, String file) {  
	   try {
		   mapper.writeValue(new File(output + "/" + file + "_" + "result.json"), json);
	   } catch (Exception e) {
		   System.err.println(e.getMessage());
	   }
   }
   
   /**
    * Uses PojoFromJson to write java files based on input json representation
    * @param json list of PojoOutlines to be written as java objects
    */
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
    * Check if input string represents a float/double. taken from https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html#valueOf(java.lang.String)
    * @param input string to check
    * @return boolean representing whether or not input is a float
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
   
   /**
    * Checks if input string represents a boolean
    * @param input string to check
    * @return boolean representing whether or not input is a boolean
    */
   public static boolean isBoolean(String input) {
	   if (input == null) {
		   return false;
	   }
	   return input.trim().equals("false") || input.trim().equals("true");
   }
   
   /**
    * Checks if input string represents an int
    * @param input string to check
    * @return boolean representing whether or not input is an int
    */
   public static boolean isInt(String input) {
	   try {
		   int integer = Integer.parseInt(input);
		   return true;
	   } catch (Exception e) {
		   return false;
	   }
   }
   
   /**
    * gives the string representation of the type of input string. used when input string may represent an int, float, boolean, etc
    * @param input string that may represent a type other than string
    * @return the type of the input as a string
    */
   public static String getTypeAsString(String input) {
	   if (isInt(input)) {
		   return "Integer";
	   } else  if (isFloat(input)) {
		   return "Float";
	   } else if (isBoolean(input)) {
		   return "Boolean";		
	   } 
	   return "String";	
   }
   
   /** 
    * converts input arraylist into a string representation of a java array, eg String[] arr = {str1, str2}
    * @param al arraylist to be converted
    * @return a string representation of the input arraylist
    */
   public static String formatArrayListAsString(ArrayList al) {
	   String output = "{";
	   if (getTypeAsString(String.valueOf(al.get(0))).equals("String")) {
		   for (Object o : al) {
			   output += "\"" + String.valueOf(o) + "\",";
		   }
		   return output.substring(0, output.length() - 1) + "}";
	   }
	   
	   for (Object o : al) {
		   output += String.valueOf(o) + ",";
	   }
	   return output.substring(0, output.length() - 1) + "}";
   }
   
   /**
    * returns a string with a valid name for java variable
    * @param str the input variable name
    * @return a valid java version of the name (starts with letter, $, _)
    */
   public static String getValidName(String str) {
	   if (!Character.isLetter(str.charAt(0)) && str.charAt(0) != '$' && str.charAt(0) != '_') {
		   return '_' + str;
	   }
	   return str;
   }

}
