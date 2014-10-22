package edu.arizona.biosemantics.matrixgeneration.model;

import java.io.Serializable;

public class StructureIdentifier implements Serializable {
	
	private String structureName;
	private String structureConstraint;
	
	public StructureIdentifier(String structureName, String structureConstraint) {
		this.structureName = structureName;
		this.structureConstraint = structureConstraint;
	}

	public String getStructureName() {
		return structureName;
	}

	public String getStructureConstraint() {
		return structureConstraint;
	}
	
	public String getStructureConstraintOrEmpty() {
		if(getStructureConstraint() == null)
			return "";
		return getStructureConstraint();
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((structureConstraint == null) ? 0 : structureConstraint
						.hashCode());
		result = prime * result
				+ ((structureName == null) ? 0 : structureName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StructureIdentifier other = (StructureIdentifier) obj;
		if (structureConstraint == null) {
			if (other.structureConstraint != null)
				return false;
		} else if (!structureConstraint.equals(other.structureConstraint))
			return false;
		if (structureName == null) {
			if (other.structureName != null)
				return false;
		} else if (!structureName.equals(other.structureName))
			return false;
		return true;
	}

	public String getDisplayName() {
		return (getStructureConstraintOrEmpty() + " " + getStructureName()).trim();
	}
}