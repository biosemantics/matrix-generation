package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.io.Serializable;

public class StructureIdentifier implements Serializable, Comparable<StructureIdentifier> {
	
	private String structureName;
	private String structureConstraint;
	private String structureOntologyId;
	
	public StructureIdentifier(Structure structure) {
		this.structureName = structure.getName();
		this.structureConstraint = structure.getConstraint();
		this.structureOntologyId = structure.getOntologyId();
	}
	
	public StructureIdentifier(String structureName, String structureConstraint, String structureOntologyId) {
		this.structureName = structureName;
		this.structureConstraint = structureConstraint;
		this.structureOntologyId = structureOntologyId;
	}

	public String getStructureName() {
		return structureName;
	}

	public String getStructureConstraint() {
		return structureConstraint;
	}
	
	public String getStructureOntologyId() {
		return structureOntologyId;
	}
	
	public boolean hasStructureOntologyId() {
		return structureOntologyId != null && !structureOntologyId.isEmpty();
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
		result = prime
				* result
				+ ((structureOntologyId == null) ? 0 : structureOntologyId
						.hashCode());
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
		if (structureOntologyId == null) {
			if (other.structureOntologyId != null)
				return false;
		} else if (!structureOntologyId.equals(other.structureOntologyId))
			return false;
		return true;
	}

	public String getDisplayName() {
		return (getStructureConstraintOrEmpty() + " " + getStructureName()).trim();
	}

	public int compareTo(StructureIdentifier structureIdentifier) {
		return (this.getStructureName() + this.getStructureConstraintOrEmpty() + this.structureOntologyId).compareTo(
						structureIdentifier.getStructureName() + structureIdentifier.getStructureConstraintOrEmpty() + structureIdentifier.structureOntologyId);
	}
}