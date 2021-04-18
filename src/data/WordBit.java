package data;

import java.util.ArrayList;

class WordBit {

	// TODO comments / documentation
	private ArrayList<String> prefixes;
	private ArrayList<String> suffixes;

	protected WordBit(String prefix, String suffix) {
		prefixes = new ArrayList<String>();
		prefixes.add(prefix);
		suffixes = new ArrayList<String>();
		suffixes.add(suffix);
	}

	protected void addBit(String prefix, String suffix) {
		prefixes.add(prefix);
		suffixes.add(suffix);
	}

	protected ArrayList<String> getPrefixes() {
		return this.prefixes;
	}

	protected ArrayList<String> getSuffixes() {
		return this.suffixes;
	}
}
