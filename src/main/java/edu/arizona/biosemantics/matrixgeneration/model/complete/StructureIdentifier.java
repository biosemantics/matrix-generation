package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.io.Serializable;

public class StructureIdentifier implements Serializable, Comparable<StructureIdentifier> {
	
	private static final long serialVersionUID = 1L;
	
	private String structureName;
	private String structureConstraint;
	private String structureOntologyId;
	
	public StructureIdentifier(Structure structure) {
		this.structureName = structure.getName() == null ? null : structure.getName().trim().isEmpty() ? null : structure.getName().trim();
		this.structureConstraint = structure.getConstraint() == null ? null : structure.getConstraint().trim().isEmpty() ? null : structure.getConstraint().trim();
		this.structureOntologyId = structure.getOntologyId() == null ? null : structure.getOntologyId().trim().isEmpty() ? null : structure.getOntologyId().trim();
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

	/*
	 * Don't use ontologyid for hashing, as there may be duplicate characters created where one may know ontologyid and other not
	 * it is not necessary for duplicates to share having ontoogyid value or not. ontologyid is found anyway only by data name + constraint.
	 * @see java.lang.Object#hashCode()
	 */
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

	public int compareTo(StructureIdentifier structureIdentifier) {
		return (this.getStructureName() + this.getStructureConstraintOrEmpty() + this.structureOntologyId).compareTo(
						structureIdentifier.getStructureName() + structureIdentifier.getStructureConstraintOrEmpty() + structureIdentifier.structureOntologyId);
	}
	
	public String toString() {
		return this.structureName + " (" + this.structureConstraint + ") " + this.structureOntologyId;
	}

}