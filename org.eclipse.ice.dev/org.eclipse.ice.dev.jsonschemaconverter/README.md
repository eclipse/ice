# JSON Schema Converter

This README's purpose is to be a guide for the JsonSchemaConverter utility by explaining it's purpose, usage, and expected output. This utility takes a JSON file
following the format of Json Schema (https://json-schema.org/), and saves a JSON file containing the data from the schema that follows the JSON format accepted by org.eclipse.ice.dev.PojoFromJson. This utility can also automatically invoke PojoFromJson's conversion methods to go ahead and produce Java files representing the original JSON schema file.

## Building and Executing

Most of the project dependencies are located in maven central. The two that are not are the following: 
`org.eclipse.ice.dev.annotations` and `org.eclipse.ice.dev.pojofromjson`. This packages need to be installed in your local m2 repository. If these packages are installed then this package can be built like any normal maven package:

```
$ mvn clean install
```
or 

```
$ mvn clean verify
```

In order to run the utility, use:
```
$ mvn exec:java -Dexec.args="-o /path/to/desired/output/directory /path/to/json/schema/file.json"
```

Required parameters are `-o` or `--output` which is the path to the desired output directory and also the path to the schema file to be converted. Two optional parameters are also avaliable, `-w` or `--write` and `-p` or `--package`. `-w` is the a boolean that specifies whetehr or not to automatically write Java files using org.eclipse.ice.dev.pojofromjson, with the default being false (and not writing the Java files). `-p` is the package name that should be used for the generated Java files. By default, generated Java files will use the name of the original schema file as the package name. 

## Json Schema Files and Expected Output

As defined on https://json-schema.org/, a JSON schema file takes the form of something like:
```
{
    "$schema": "http://json-schema.org/draft-07/schema#",
    "$id": "http://json-schema.org/draft-07/schema#",
    "title": "Core schema meta-schema",
    "definitions": {
        "schemaArray": {
            "type": "array",
            "minItems": 1,
            "items": { "$ref": "#" }
        },
        "nonNegativeInteger": {
            "type": "integer",
            "minimum": 0
        }
    },
    "properties": {
        "$id": {
            "type": "string",
            "format": "uri-reference"
        },
        "title": {
            "type": "string"
        },
        "description": {
            "type": "string"
        },
        "configuration": {
      		"type": "object",
      		"properties": {

        		"useBoolean": {"default": false},
        		"defaultInt": {"default": 5},
        		"defaultFloat": {"default": 5.0},
        		"defaultIntArray": {"default": [5, 65]},
        		"defaultArray": {"default": ["str1", "str2"]}
      		}
   		}
    }
}
```
The format acceped by org.eclipse.ice.dev.pojofromjson takes the form  of: 
```
{
	"package": "testpackage",
	"element": "TestElement",
	"fields": [{
		"name": "test",
		"type": "String"
	}]
}
```

Taking the first example and converting it would give you (omitting some fields for sake of brevity):
```
[ {
  "element" : "Properties",
  "implementation" : "PropertiesImplementation",
  "fields" : [ {
    "name" : "$id",
    "type" : "String",
    "defaultValue" : null,
  }, {
    "name" : "$id",
    "type" : "String",
    "defaultValue" : null,
    "docString" : "uri-reference",
  }, {
    "name" : "title",
    "type" : "String",
    "defaultValue" : null,
  }, {
    "name" : "description",
    "type" : "String",
    "defaultValue" : null,
  }
  .
  .
  .
  ,{
    "name" : "useBoolean",
    "type" : "Boolean",
    "defaultValue" : "false",
    "docString" : "null",
  }, {
    "name" : "defaultInt",
    "type" : "Integer",
    "defaultValue" : "5",
    "docString" : "null",
  }, {
    "name" : "defaultFloat",
    "type" : "Float",
    "defaultValue" : "5.0",
    "docString" : "null",
  }, {
    "name" : "defaultIntArray",
    "type" : "Integer[]",
    "defaultValue" : "{5,65}",
    "docString" : "null",
  }, {
    "name" : "defaultArray",
    "type" : "String[]",
    "defaultValue" : "{\"str1\",\"str2\"}",
    "docString" : "null",
  } ],
  "package" : "testschema"
}, {
  "element" : "testschemaProperties",
  "implementation" : "testschemaPropertiesImplementation",
  "fields" : [ {
    "name" : "$schema",
    "type" : "String",
    "defaultValue" : "http://json-schema.org/draft-07/schema#",
    "docString" : null,
  }, {
    "name" : "$id",
    "type" : "String",
    "defaultValue" : "http://json-schema.org/draft-07/schema#",
    "docString" : null,
  }, {
    "name" : "title",
    "type" : "String",
    "defaultValue" : "Core schema meta-schema",
    "docString" : null,
  }],
  "package" : "testschema"
} ]`

```



