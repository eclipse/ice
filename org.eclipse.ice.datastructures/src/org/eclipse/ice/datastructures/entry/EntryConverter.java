package org.eclipse.ice.datastructures.entry;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class EntryConverter {

	/**
	 * 
	 * @param entry
	 * @return
	 */
	public static IEntry convert(Entry entry) {
		IEntry convertedEntry = null;
		AllowedValueType type = entry.getValueType();

		if (type == AllowedValueType.Undefined) {
			convertedEntry = new StringEntry();
		} else if (type == AllowedValueType.Discrete) {
			convertedEntry = new DiscreteEntry();
			convertedEntry.setAllowedValues(entry.getAllowedValues());
		} else if (type == AllowedValueType.Continuous) {
			convertedEntry = new ContinuousEntry();
			convertedEntry.setAllowedValues(entry.getAllowedValues());
		} else if (type == AllowedValueType.File) {
			convertedEntry = new FileEntry();
			// set project?
		} else if (type == AllowedValueType.Executable) {
			convertedEntry = new ExecutableEntry();
		}

		convertedEntry.setName(entry.getName());
		convertedEntry.setDescription(entry.getDescription());
		convertedEntry.setComment(entry.getComment());
		convertedEntry.setTag(entry.getTag());
		convertedEntry.setId(entry.getId());
		convertedEntry.setDefaultValue(entry.getDefaultValue());
		convertedEntry.setReady(entry.isReady());
		convertedEntry.setRequired(entry.isRequired());
		convertedEntry.setValue(entry.getValue());

		return convertedEntry;
	}

	public static Entry convert(IEntry entry) {
		Entry convertedEntry = null;
		if (entry instanceof StringEntry) {
			convertedEntry = new Entry() {
				@Override
				public void setup() {
					defaultValue = entry.getDefaultValue();
					value = entry.getValue();
					allowedValueType = AllowedValueType.Undefined;
				}
			};
		} else if (entry instanceof DiscreteEntry) {
			convertedEntry = new Entry() {
				@Override
				public void setup() {
					defaultValue = entry.getDefaultValue();
					value = entry.getValue();
					allowedValueType = AllowedValueType.Discrete;
					allowedValues = (ArrayList<String>) entry.getAllowedValues();
				}
			};
		} else if (entry instanceof ContinuousEntry) {
			convertedEntry = new Entry() {
				@Override
				public void setup() {
					defaultValue = entry.getDefaultValue();
					value = entry.getValue();
					allowedValueType = AllowedValueType.Continuous;
					allowedValues = (ArrayList<String>) entry.getAllowedValues();
				}
			};

		} else if (entry instanceof FileEntry) {
			convertedEntry = new Entry() {
				@Override
				public void setup() {
					defaultValue = entry.getDefaultValue();
					value = entry.getValue();
					allowedValueType = AllowedValueType.File;
					allowedValues = (ArrayList<String>) entry.getAllowedValues();
				}
			};

		} else if (entry instanceof ExecutableEntry) {
			convertedEntry = new Entry() {
				@Override
				public void setup() {
					defaultValue = entry.getDefaultValue();
					value = entry.getValue();
					allowedValueType = AllowedValueType.Executable;
					allowedValues = (ArrayList<String>) entry.getAllowedValues();
				}
			};

		}

		convertedEntry.setName(entry.getName());
		convertedEntry.setDescription(entry.getDescription());
		convertedEntry.setComment(entry.getComment());
		convertedEntry.setTag(entry.getTag());
		convertedEntry.setId(entry.getId());
		convertedEntry.setReady(entry.isReady());
		convertedEntry.setRequired(entry.isRequired());
		convertedEntry.setValue(entry.getValue());

		return convertedEntry;

	}

	public static ArrayList<Entry> convertIEntriesToEntries(ArrayList<IEntry> entries) {
		ArrayList<Entry> convertedEntries = new ArrayList<Entry>();

		for (IEntry e : entries) {
			convertedEntries.add(convert(e));
		}

		return convertedEntries;
	}

	/**
	 * 
	 * @param entries
	 * @return
	 */
	public static ArrayList<IEntry> convertEntriesToIEntries(ArrayList<Entry> entries) {
		ArrayList<IEntry> convertedEntries = new ArrayList<IEntry>();

		for (Entry e : entries) {
			convertedEntries.add(convert(e));
		}

		return convertedEntries;
	}
}
