//package edu.arizona.biosemantics.matrixgeneration.transform.complete;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
//import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
//import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
//import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
//import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
//
//public class TaxonomyAncestorInheritanceTransformer implements Transformer {
//
//	@Override
//	public void transform(Matrix matrix) {
//		for(Taxon taxon : matrix.getLeafTaxa()) {
//			propagateToAncestors(taxon);
//		}
//	}
//	
//	private void propagateToAncestors(Taxon taxon) {
//		Taxon parent = taxon.getParent();
//		if(parent != null) {
//			for(Structure structure : taxon.getStructures()) {
//				for(Character character : structure.getCharacters()) {	
//					Value parentValue = determineParentValue(parent, structure, character);	
//					
//					if(!parent.containsStructure(structure.getName()))
//						parent.addStructure(structure.clone());
//					Structure parentStructure = parent.getStructure(structure.getName());
//					if(!parentStructure.containsCharacter(character) && parentValue != null) {
//						parentStructure.setCharacterValue(character, parentValue);
//					}
//				}
//			}
//		}
//
//		//recursively inherit character values
//		propagateToAncestors(taxon.getParent());
//	}
//
//	//if all children of parent contain the structure and have the character set the same
//	private Value determineParentValue(Taxon parent, Structure structure, Character character) {
//		Set<Value> allValues = new HashSet<Value>();
//		for(Taxon child : parent.getChildren()) {
//			if(child.containsStructure(structure.getName())) {
//				Structure childStructure = child.getStructure(structure.getName());
//				if(childStructure.containsCharacter(character)) {
//					Value value = childStructure.getCharacterValue(character);
//					allValues.add(value);
//				} else {
//					return null;
//				}
//			} else {
//				return null;
//			}
//		}
//		
//		// unclear how to deal with value attributes from xml... would probably only want to inherit plain value.. hence go raw
//		//Value value = new Value();
//	}
//
//}
