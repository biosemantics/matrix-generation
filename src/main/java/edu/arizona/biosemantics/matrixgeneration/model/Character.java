package edu.arizona.biosemantics.matrixgeneration.model;

public class Character implements Comparable<Character> {

	public static class StructureIdentifier {
		
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
	}
	
	private String name;
	private StructureIdentifier structureIdentifier;
	
	public Character(String name, String structureName, String structureConstraint) {
		this.name = name;
		this.structureIdentifier = new StructureIdentifier(structureName, structureConstraint);
	}

	public String getName() {
		return name;
	}	
	
	public String getStructureName() {
		return structureIdentifier.getStructureName();
	}
	
	public String getStructureConstraint() {
		return structureIdentifier.getStructureConstraint();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((structureIdentifier == null) ? 0 : structureIdentifier
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
		Character other = (Character) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (structureIdentifier == null) {
			if (other.structureIdentifier != null)
				return false;
		} else if (!structureIdentifier.equals(other.structureIdentifier))
			return false;
		return true;
	}

	@Override
	public int compareTo(Character character) {
		if(getStructureName().equals(character.getStructureName())) {
			if(this.getStructureConstraintOrEmpty().equals(character.getStructureConstraintOrEmpty()))
				return name.compareTo(character.getName());
			return getStructureConstraintOrEmpty().compareTo(character.getStructureConstraintOrEmpty());
		}
		return getStructureName().compareTo(character.getStructureName());
	}

	public String getStructureConstraintOrEmpty() {
		if(structureIdentifier.getStructureConstraint() == null)
			return "";
		return structureIdentifier.getStructureConstraint();
	}	
	
}
