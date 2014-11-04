%module exodusII
%{
#include "exodusII.h" // The target library to be wrapped.

#include <inttypes.h>
%}

/* Required for using SWIG on Windows. */
%include <windows.i>

/*******************************************************************************
  The below typemaps effectively convert Java String[] into native char** and
  vice versa so that the Java programmer can think of String[] in a C-like way.
  
  When reading these typemaps, keep in mind the following:
	$1 is the char**
	$input is the jobjectArray (String[])
*/

/* This typemap is for passing String[] as parameters to wrapped native methods.
    It should convert the array of jstrings into a char**. */
%typemap(in) char ** (jint size) {
	
    // Local declarations must go here for MSVC (C89) support.
    int i;
    jstring j_string;
    int len;
    int max_len = 32;

    // Allocate the char** to hold the same number of strings as the String[].
    size = (*jenv)->GetArrayLength(jenv, $input);
    $1 = (char**) malloc(sizeof(char*) * size);

    // Copy each String into the char**.
    for (i = 0; i < size; i++) {
    	j_string = (jstring) (*jenv)->GetObjectArrayElement(jenv, $input, i);
    	
    	// If the String is null, just allocate enough space.
    	if (j_string == NULL) {
    		$1[i] = (char*) malloc(sizeof(char) * max_len + 1);
    	} else { // Otherwise, get a native version of the String.
    		const char * c_string = (*jenv)->GetStringUTFChars(jenv, j_string, 0);
    		len = strlen(c_string);
    		
    		// If the length is smaller than the max length, make the char* 
    		// big enough to fit max_len characters. In either case, copy the
    		// UTF string to the char* and set the last char to the 
    		// terminating bit.
    		if (len <= max_len) {
    			$1[i] = (char*) malloc(sizeof(char) * (max_len + 1));
    		} else { // Otherwise, just copy over the whole String.
    			$1[i] = (char*) malloc(sizeof(char) * (len + 1));
    		}
    		// Copy over as much of the String as we can. Set the last char
    		// to the terminating bit.
			strncpy($1[i], c_string, len);
			$1[i][len] = '\0';
			
    	 	// Release the String.
        	(*jenv)->ReleaseStringUTFChars(jenv, j_string, c_string);
        	(*jenv)->DeleteLocalRef(jenv, j_string);
    	}
    } 
}

/* This typemap frees up the char** allocated from the input typemap. */
%typemap(freearg) char ** {
	// Free the char** if it is not null.
	if ($1) {
		int i;
		for (i = 0; i < size$argnum; i++) {
			free($1[i]);
		}
		free($1);
		$1 = NULL;
	}
}


/* This typemap updates the String[] passed to a native function from the char**
    allocated in the input typemap. In other words, String[] are treated like
    native char**, and the array can be read for changes after the call. */ 
%typemap(argout) char ** {

    // Local declarations must go here for MSVC (C89) support.
    int len = 0;
	int i;
	jstring j_string;

	// Get the length of the char** and array.
	while ($1[len]) len++;

	// Copy each of the char* to a String and put it in the String[].
	for (i = 0; i < len && i < size$argnum; i++) {
		j_string = (*jenv)->NewStringUTF(jenv, $1[i]);
		(*jenv)->SetObjectArrayElement(jenv, $input, i, j_string); 
		(*jenv)->DeleteLocalRef(jenv, j_string);
		// Free the native string.
		free($1[i]);
	}
	// Free the char**. Set it to null so it's not freed again by freearg.
	free($1);
	$1 = NULL;
}

/* This typemap allows a C function to return a char** as a Java String[]. */
%typemap(out) char ** {

    // Local declarations must go here for MSVC (C89) support.
    int len=0;
	int i;
	jstring j_string;
    const jclass clazz = (*jenv)->FindClass(jenv, "java/lang/String");

	// Create an appropriately sized String[].
    while ($1[len]) len++;    
    jresult = (*jenv)->NewObjectArray(jenv, len, clazz, NULL);

	// Copy the resulting char** over to the String[].
    for (i=0; i<len; i++) {
      j_string = (*jenv)->NewStringUTF(jenv, result[i]);
      (*jenv)->SetObjectArrayElement(jenv, jresult, i, j_string);
      (*jenv)->DeleteLocalRef(jenv, j_string);
    }
}

/* These 3 typemaps tell SWIG what JNI and Java types to use */
%typemap(jni) char ** "jobjectArray"
%typemap(jtype) char ** "String[]"
%typemap(jstype) char ** "String[]"

/* These 2 typemaps handle the conversion of the jtype to jstype typemap type
   and vice versa */
%typemap(javain) char ** "$javainput"
%typemap(javaout) char ** {
	return $jnicall;
}

/* End of char** and String[] typemaps.
*******************************************************************************/

/* Configure pointer types */
%include cpointer.i
%pointer_functions(int, intPtr)
%pointer_functions(long,longPtr)
%pointer_functions(float,floatPtr)
%pointer_functions(int64_t,int64tPtr)
%pointer_cast(int64_t *, long *,int64tToLongPtr)
%pointer_cast(long *, int64_t *, longToInt64tPtr)
%pointer_cast(int *,void *,intPtrToVoidPtr)
%pointer_cast(int64_t *,void *,int64tPtrToVoidPtr)
%pointer_cast(void *,int *,voidPtrToIntPtr)
%pointer_cast(float *,void *,floatPtrToVoidPtr)
%pointer_cast(void *,float *,voidPtrToFloatPtr)
%pointer_functions(ex_block,ex_blockPtr)

/* Configure array types */
%include carrays.i
%array_functions(int,intPtrArray)
%array_functions(int64_t,int64tPtrArray)
%array_functions(float,floatPtrArray)
%array_functions(ex_block,ex_blockPtrArray)

/* Use this method to get an int64_t pointer from an int value. */
%inline %{
int64_t * getInt64tPtr(int value) {
  int64_t *ptr = (int64_t *) calloc(1, sizeof(int64_t));
  *ptr = (int64_t) value;
  return ptr;
}
%}

/* We need to include the configuration header for the definitions (Windows support). */
%include "exodusII_cfg.h"
/* Parse the header files and generate the wrapper */
%include "exodusII.h"
