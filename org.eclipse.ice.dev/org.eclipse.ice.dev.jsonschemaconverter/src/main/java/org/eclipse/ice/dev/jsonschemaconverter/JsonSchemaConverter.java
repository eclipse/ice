package org.eclipse.ice.dev.jsonschemaconverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.ice.dev.pojofromjson.*;
import org.eclipse.ice.dev.annotations.processors.Field;


/**
 * Utility to convert JSON schemas to the JSON format that the
 * PojoFromJson project accepts
 * Pieces of code were derived from Daniel Bluhm's
 * org.eclipse.ice.dev.PojoFromJson.
 * @author gzi
 *
 */
public class JsonSchemaConverter {

	/**
	 * Mapper used for deserializing POJO Outline JSON.
	 */
	private static ObjectMapper mapper = new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT);

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
	@Parameter(names = {"-p", "--package"}, description = "Package of output files")
	private static String packageName = "";

	/**
	 * System logger.
	 */
	private static final Logger logger =
						LoggerFactory.getLogger(JsonSchemaConverter.class);


	/**
	 * Default type of JSON property if not specified in a JSON property.
	 */
	private static String typeField = "string";

	/**
	 * Name of JSON property that contains the default value of a parent.
	 */
	private static String defaultField = "default";

	/**
	 * Name of JSON property that contains the description of a parent.
	 */
	private static String descriptionField = "description";

   /**
    * Executes program.
    * @param args command line arguments
    */
	public static void main( String[] args ) {
       JsonSchemaConverter app = new JsonSchemaConverter();
       app.run(args);
	}

	/**
	 * Parse arguments and continues execution of program.
	 * @param args command line arguments
	 */
	public void run(String... args) {
		JCommander jcomm = JCommander.newBuilder()
   			.addObject(this)
   			.build();
   		jcomm.setProgramName("JsonSchemaConverter");
   		jcomm.parse(args);

   		try {
   			if (jsonFiles.isEmpty()) {
   				handleInputJson(System.in, Path.of(output));
   			}
   			for (String filePath : jsonFiles) {

   				try (FileInputStream inputJson = 
   											new FileInputStream(filePath)) {
   					handleInputJson(inputJson, Path.of(filePath));
   				}
   			}
   		} catch (Exception ex) {
   			logger.error(ex.getMessage());
   			System.exit(1);
   		}
   }
   /**
    * Converts given JSON schema file into a JSON format.
    * accepted by org.eclipse.ice.dev.annotations.
    * @param is InputStream of original JSON schema file
    * @param destination directory where output files will written
    * @param filePath string representing path to input file
    * @throws JsonParseException On failure to parse the input JSON schema file
    * @throws JsonMappingException On failure to map the JSON schema to Map<String, Object>
    * @throws IOException On failure to write files
    */
   public static void handleInputJson(InputStream is, Path filePath) throws IOException {
	   	Map<String, Object> map = mapper.readValue(is, new TypeReference<Map<String,Object>>(){});
	   	List<PojoOutline> jsonArrayOut = new ArrayList<>();
	   	String fileName = filePath.getFileName().toString();
	   	fileName = fileName.substring(0, fileName.length() - 5); //remove .json
	   	packageName = packageName.equals("") ? fileName.toLowerCase() : packageName;
	   	Stream<Map.Entry<String, Object>> entries = map.entrySet().stream();

	   	entries.filter(e -> e.getValue() instanceof Map 
	   						&& !e.getKey().equals("definitions"))
	   	.forEach(entry -> {  //ignore definitions section 
	   		List<Field> fields = new ArrayList<>();
	   		Map<String, Object> node = (Map<String, Object>) entry.getValue();
	   		
	   			if (node.keySet().contains("anyOf")) {
	   				Field n = Field.builder()
	   						.name(getValidName(entry.getKey()))
	   						.type(typeField)
	   						.docString(String.valueOf(node.get(descriptionField)))
	   						.defaultValue("")
	   						.build();
	   				fields.add(n);

	   			} else if (typeField.equals(node.get("type"))) {
	   				Field n = Field.builder()
	   						.name(getValidName(entry.getKey()))
	   						.docString(String.valueOf(node.get(descriptionField)))
	   						.defaultValue(String.valueOf(node.get(defaultField)))
	   						.build();
				   	n.setType(getTypeAsString(n.getDefaultValue()));
				   	fields.add(n);
	   			} else { //no type property or type='object'
	   				fields = processJsonObject(node, entry.getKey());
	   			}
	   			
	   			PojoOutline po = PojoOutline.builder()
		   				.packageName(packageName)
		   				.element(entry.getKey().substring(0, 1).toUpperCase() 
		   						+ entry.getKey().substring(1))
		   				.fields(fields)
		   				.build();
		   		jsonArrayOut.add(po);

	   	});
	   	
	   	//collect all top level string properties into one file
	   	List<Field> fields = new ArrayList<>();
	   	map.entrySet().stream()
	   				  .filter(e -> !(e.getValue() instanceof Map)).forEach(e -> {
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

	   	writeJson(jsonArrayOut, filePath, fileName);
	   	writeDataElements(jsonArrayOut, filePath);
   }

   /**
    * Helper function to recursively obtain all of the fields from a map.
    * @param map map to get data fields from
    * @param key the name of key used to access this map from its parent map
    * @return list of Fields representing the data from the input map
    */
   public static List<Field> processJsonObject(Map<String, Object> map, String key) {
	   List<Field> fields = new ArrayList<>();
	   if (map.keySet().contains(defaultField)) {
		   Field n = Field.builder()
						.name(getValidName(key))
						.build();
		   //if no description can be found, then uses 'null'
		   n.setDocString(
				   map.get(descriptionField) == null ? String.valueOf(map.get("$ref"))
						   : String.valueOf(map.get(descriptionField))); 
		   
		   if (map.get(defaultField) instanceof ArrayList) {
			   n.setDefaultValue(formatListAsString((ArrayList) map.get(defaultField)));
			   n.setType(getTypeAsString(String.valueOf(((ArrayList) map.get(defaultField)).get(0))) + "[]");
		   } else {
			   n.setDefaultValue(String.valueOf(map.get(defaultField)));
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
 						.type(typeField)
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
    * Write the converted JSON file, represented as a 
    * list of PojoOutline to output destination.
    * @param json converted JSON file represented as a list of PojoOultine
    * @param file the name of the new JSON file
    */
   public static void writeJson(List<PojoOutline> json, Path filePath, String file) {  
	   try {
		   mapper.writeValue(filePath
				   			 .resolve(filePath.getParent() 
				   					 + "/" + file + "_" + "result.json")
				   			 .toFile(), json);
	   } catch (Exception e) {
		   logger.error(e.getMessage());
	   }
   }

   /**
    * Uses PojoFromJson to write java files based on input JSON representation.
    * @param json list of PojoOutlines to be written as java objects
    */
   public static void writeDataElements(List<PojoOutline> json, Path filePath) {
	   for (PojoOutline j : json) {
		   try {
			   PojoFromJson.createDataElement(j, filePath.getParent());
		   } catch (Exception e) {
			   logger.error(e.getMessage());
		   }
	   }
   }

   /**
    * Check if input string represents a float/double. 
    * Sourced from 
    * https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html#valueOf(java.lang.String).
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
    * Checks if input string represents a boolean.
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
    * Checks if input string represents an int.
    * @param input string to check
    * @return boolean representing whether or not input is an int
    */
   public static boolean isInt(String input) {
	   try {
		   Integer.parseInt(input);
		   return true;
	   } catch (Exception e) {
		   return false;
	   }
   }

   /**
    * Gives the string representation of the type of input string.
    * Used when input string may represent an int, float, boolean, etc.
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
    * Converts input list into a string representation of a java.
    * array, eg String[] arr = {str1, str2}.
    * @param al list to be converted
    * @return a string representation of the input list
    */
   public static String formatListAsString(List<Object> li) {
	   String output = "{";
	   if (getTypeAsString(String.valueOf(li.get(0))).equals("String")) {
		   for (Object o : li) {
			   output += String.valueOf("\"" + o + "\",");
		   }
		   return output.substring(0, output.length() - 1) + "}";
	   }

	   for (Object o : li) {
		   output += String.valueOf(o) + ",";
	   }
	   return output.substring(0, output.length() - 1) + "}";
   }

   /**
    * Returns a string with a valid name for java variable.
    * @param str the input variable name
    * @return a valid java version of the name (starts with letter, $, _)
    */
   public static String getValidName(String str) {
	   if (!Character.isLetter(str.charAt(0))
			   && str.charAt(0) != '$'
			   && str.charAt(0) != '_') {
		   return '_' + str;
	   }
	   return str;
   }

}
