package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import com.google.gwt.user.client.rpc.IsSerializable;
public class ColumnHead implements Serializable, IsSerializable, Comparable<ColumnHead> {
	
	private static final long serialVersionUID = 1L;

	private List<String> generationProvenance = new LinkedList<String>();
	private String value;
	private Character source;
	
	public ColumnHead(String value, Character source, Object generationProvenance) {
		this.value = value;
		this.source = source;
		this.addGenerationProvenance(generationProvenance);
	}
	
	public String getValue() {
		return value;
	}

	public Character getSource() {
		return source;
	}
	
	public boolean hasCharacterSource() {
		return source != null;
	}

	@Override
	public int compareTo(ColumnHead c) {
		return value.compareTo(c.value);
	}
	
	public void addGenerationProvenance(Object generationProvenance) {
		if(generationProvenance instanceof String)
			this.generationProvenance.add((String)generationProvenance);
		else
			this.generationProvenance.add(generationProvenance.getClass().getSimpleName());
	}
	
	public List<String> getGenerationProvenance() {
		return generationProvenance;
	}
	

}
