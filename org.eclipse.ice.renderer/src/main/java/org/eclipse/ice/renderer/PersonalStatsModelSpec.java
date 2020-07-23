package org.eclipse.ice.renderer;

import java.util.List;

import org.eclipse.ice.dev.annotations.DataModel;
import org.eclipse.ice.dev.annotations.Persisted;
import org.eclipse.ice.dev.annotations.Validator;

@DataModel(name = "PersonalStats")
public class PersonalStatsModelSpec {
	
	//@Persisted(collection = "people")
    @Validator(name = "javascriptValidator")
    private List<Person> people;
	
}
