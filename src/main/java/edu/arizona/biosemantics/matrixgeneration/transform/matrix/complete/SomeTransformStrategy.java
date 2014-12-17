package edu.arizona.biosemantics.matrixgeneration.transform.matrix.complete;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;

public class SomeTransformStrategy implements TransformStrategy {

	private Set<String> presentRelation;
	private Set<String> absentRelation;	
	private boolean inheritValues;
	private boolean generateAbsentPresent;
	private boolean inferCharactersFromOntologies;
	
	@Inject
	public SomeTransformStrategy(@Named("InheritValues")boolean inheritValues,
			@Named("GenerateAbsentPresent") boolean generateAbsentPresent, 
			@Named("InferCharactersFromOntologies") boolean inferCharactersFromOntologies, 
			@Named("PresentRelation") Set<String> presentRelation, 
			@Named("AbsentRelation") Set<String> absentRelation) {
		
		this.inheritValues = inheritValues;
		this.generateAbsentPresent = generateAbsentPresent;
		this.inferCharactersFromOntologies = inferCharactersFromOntologies;
		this.presentRelation = presentRelation;
		this.absentRelation = absentRelation;
	}

	@Override
	public void transform(Matrix matrix) {
		if(inheritValues) {
			Transformer inherit = new InheritanceTransformer();
			inherit.transform(matrix);
		}
		if(generateAbsentPresent) {
			Transformer generateAbsentPresent = new AbsentPresentTranformer(presentRelation, absentRelation);
			generateAbsentPresent.transform(matrix);
		}
		
		if(inferCharactersFromOntologies) {
			Transformer ontologyInferenceTransformer = new OntologyInferenceTransformer();
			ontologyInferenceTransformer.transform(matrix);
		}
		
		//Transformer switchUnits = new NormalizeUnitsTransformer(Unit.mm);
		//switchUnits.transform(matrix);
		
		//Transformer splitRangeValues = new SplitRangeValuesTransformer();
		//splitRangeValues.transform(matrix);
		
		//System.out.println("transformed matrix: " + \n " + matrix.toString());
	}

}
