package edu.arizona.biosemantics.matrixgeneration.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.tdwg.rs.ubif._2006.*;

import edu.arizona.biosemantics.matrixgeneration.Configuration;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Character.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.RankData;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class SDDWriter implements Writer {

	private ObjectFactory objectFactory = new ObjectFactory();
	private MetadataCreator metdataCreator = new MetadataCreator();
	private DatasetHandler datasetHandler = new DatasetHandler();
	private TaxonNameHandler taxonNameHandler = new TaxonNameHandler();
	private TaxonHierarchyHandler taxonHierarchyHandler = new TaxonHierarchyHandler();
	private DescripitiveConceptHandler descriptiveConceptHandler = new DescripitiveConceptHandler();
	private ModifierHandler modifierHandler = new ModifierHandler();
	private CharacterSetHandler characterSetHandler = new CharacterSetHandler();
	private CharacterTreeHandler characterTreeHandler = new CharacterTreeHandler();
	private CodeDescriptionHandler codeDescriptionHandler = new CodeDescriptionHandler();
	private final String NODE_PREFIX = "th_node_";
	private	ValueTypeDeterminer valueTypeDeterminer = new ValueTypeDeterminer();
	

	
	/** StateFactory code before **/
	private static class ValueTypeDeterminer {	
		public enum ValueType {
			SINGLETON_NUMERIC, SINGLETON_CATEGORICAL, RANGE_CATEGORICAL, RANGE_NUMERIC, EMPTY
		}
		public boolean isSingletonNumeric(Value value) {
			return determine(value).equals(ValueType.SINGLETON_NUMERIC);
		}
		public boolean isSingletonCategorical(Value value) {
			return determine(value).equals(ValueType.SINGLETON_CATEGORICAL);
		}
		public boolean isRangeCategorical(Value value) {
			return determine(value).equals(ValueType.RANGE_CATEGORICAL);
		}
		public boolean isRangeNumeric(Value value) {
			return determine(value).equals(ValueType.RANGE_NUMERIC);
		}
		public boolean isEmpty(Value value) {
			return determine(value).equals(ValueType.EMPTY);
		}
		private ValueType determine(Value value) {
			if(value.getValue() == null) {
				if(value.getFromUnit() != null && value.getToUnit() != null) {
					Double from = null, to = null;
					try {
						from = Double.parseDouble(value.getFrom());
						to = Double.parseDouble(value.getTo());
					}
					catch(NumberFormatException e) {
						System.out.println("Bad number:" + e.getMessage() + " in value: " + value.toString());
						from = -1.0;
						to = -1.0;
					}
					return ValueType.RANGE_NUMERIC;
				}
				else {
					//Let's first check if it's a count, by trying to parse it as an integer.
					Double from = null, to = null;
					try {
						from = Double.parseDouble(value.getFrom());
						if (value.getTo()!=null)
							to = Double.parseDouble(value.getTo());
						else 
							to = 9999.00;
						return ValueType.RANGE_NUMERIC;
					}
					catch (NumberFormatException e) {
						String sto;
						if (value.getTo()!=null)
							sto = value.getTo();
						else 
							sto = "9999.00";
						return ValueType.RANGE_CATEGORICAL;
					}
				}
			}
			else {
				if(value.getUnit() != null) {
					Double v = null;
					try {
						v = Double.parseDouble(value.getValue());
					}
					catch(NumberFormatException e) {
						System.out.println("Bad number:" + e.getMessage() + " in value: " + value.toString());
						v = -1.0;
					}
					return ValueType.SINGLETON_CATEGORICAL;
				} else {
					//Need to check here for counts, too
					Double v = null;
					try {
						v = Double.parseDouble(value.getValue());
						return ValueType.SINGLETON_NUMERIC;
					}
					catch (NumberFormatException e) {
						if(value.getValue().equals("/"))
							return ValueType.EMPTY;
						else
							return ValueType.SINGLETON_CATEGORICAL;
					}
				}
			}
		}
		public boolean isSingleton(Value value) {
			ValueType result = determine(value);
			return result.equals(ValueType.SINGLETON_CATEGORICAL) || result.equals(ValueType.SINGLETON_NUMERIC);
		}
		public boolean isRange(Value value) {
			ValueType result = determine(value);
			return result.equals(ValueType.RANGE_CATEGORICAL) || result.equals(ValueType.RANGE_NUMERIC);
		}
		
	}
	
	private class TaxonCharacterStateTriple {
		
		public String taxonName;
		public AbstractCharacterDefinition character;
		public CharacterLocalStateDef state;

		public TaxonCharacterStateTriple(String taxonName, AbstractCharacterDefinition character, CharacterLocalStateDef state) {
			this.taxonName = taxonName;
			this.character = character;
			this.state = state;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((character == null) ? 0 : character.hashCode());
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			result = prime * result
					+ ((taxonName == null) ? 0 : taxonName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TaxonCharacterStateTriple)) {
				return false;
			}
			TaxonCharacterStateTriple other = (TaxonCharacterStateTriple) obj;
			if (character == null) {
				if (other.character != null) {
					return false;
				}
			} else if (!character.equals(other.character)) {
				return false;
			}
			if (state == null) {
				if (other.state != null) {
					return false;
				}
			} else if (!state.equals(other.state)) {
				return false;
			}
			if (taxonName == null) {
				if (other.taxonName != null) {
					return false;
				}
			} else if (!taxonName.equals(other.taxonName)) {
				return false;
			}
			return true;
		}

	}
	
	public class TaxonCharacterStructureTriple {
		public RowHead rowHead;
		public AbstractCharacterDefinition character;
		public StructureIdentifier structureIdentifier;
		public TaxonCharacterStructureTriple(RowHead rowHead,
				AbstractCharacterDefinition character,
				StructureIdentifier structureIdentifier) {
			this.rowHead = rowHead;
			this.character = character;
			this.structureIdentifier = structureIdentifier;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((character == null) ? 0 : character.hashCode());
			result = prime * result
					+ ((structureIdentifier == null) ? 0 : structureIdentifier.hashCode());
			result = prime * result
					+ ((rowHead == null) ? 0 : rowHead.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TaxonCharacterStructureTriple)) {
				return false;
			}
			TaxonCharacterStructureTriple other = (TaxonCharacterStructureTriple) obj;
			if (character == null) {
				if (other.character != null) {
					return false;
				}
			} else if (!character.equals(other.character)) {
				return false;
			}
			if (structureIdentifier == null) {
				if (other.structureIdentifier != null) {
					return false;
				}
			} else if (!structureIdentifier.equals(other.structureIdentifier)) {
				return false;
			}
			if (rowHead == null) {
				if (other.rowHead != null) {
					return false;
				}
			} else if (!rowHead.equals(other.rowHead)) {
				return false;
			}
			return true;
		}
	}
	
	public class TaxonConceptStructureTriple {
		public RowHead rowHead;
		public DescriptiveConcept descriptiveConcept;
		public StructureIdentifier structureIdentifier;
		public TaxonConceptStructureTriple(RowHead rowHead, 
				DescriptiveConcept dc, StructureIdentifier structureIdentifier) {
			this.rowHead = rowHead;
			this.descriptiveConcept = dc;
			this.structureIdentifier = structureIdentifier;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((descriptiveConcept == null) ? 0 : descriptiveConcept
							.hashCode());
			result = prime * result
					+ ((structureIdentifier == null) ? 0 : structureIdentifier.hashCode());
			result = prime * result
					+ ((rowHead == null) ? 0 : rowHead.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TaxonConceptStructureTriple)) {
				return false;
			}
			TaxonConceptStructureTriple other = (TaxonConceptStructureTriple) obj;
			if (descriptiveConcept == null) {
				if (other.descriptiveConcept != null) {
					return false;
				}
			} else if (!descriptiveConcept.equals(other.descriptiveConcept)) {
				return false;
			}
			if (structureIdentifier == null) {
				if (other.structureIdentifier != null) {
					return false;
				}
			} else if (!structureIdentifier.equals(other.structureIdentifier)) {
				return false;
			}
			if (rowHead == null) {
				if (other.rowHead != null) {
					return false;
				}
			} else if (!rowHead.equals(other.rowHead)) {
				return false;
			}
			return true;
		}
	}
	
	private class MetadataCreator {
		public void create(Datasets datasets) throws DatatypeConfigurationException, IOException {
			TechnicalMetadata metadata = objectFactory.createTechnicalMetadata();
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
	        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance(); 
	        XMLGregorianCalendar xMLGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			
			metadata.setCreated(xMLGregorianCalendar);
			DocumentGenerator documentGenerator = objectFactory.createDocumentGenerator();
			documentGenerator.setName("edu.arizona.biosemantics.matrixgeneration");
			documentGenerator.setVersion(Configuration.getInstance().getProjectVersion());
			
			metadata.setGenerator(documentGenerator);
			datasets.setTechnicalMetadata(metadata);
		}
	}
	
	private class DatasetHandler {
		private Dataset dataset;
		public void create(RawMatrix rawMatrix) {
			dataset = objectFactory.createDataset();
			dataset.setLang("en-us");
			
			Representation representation = objectFactory.createRepresentation();
			LabelText labelText = objectFactory.createLabelText();
			String label = "";
			for(RowHead rowHead : rawMatrix.getRootRowHeads()) {
				label += rowHead.getValue() + ";";
			}
			labelText.setValue(label);
			DetailText detailText = objectFactory.createDetailText();
			detailText.setValue("");//("Generated from Hong's mark-up of FNA document");
			representation.getRepresentationGroup().add(labelText);
			representation.getRepresentationGroup().add(detailText);
			dataset.setRepresentation(representation);
						
			for(RowHead rowHead : rawMatrix.getRootRowHeads()) {
				//possibly want to keep the rowheads in a list, see original datasethandler and its getTaxa method
				taxonNameHandler.processRowHead(rowHead);
				characterSetHandler.processRowHead(rowHead, rawMatrix);
				characterTreeHandler.processRowHead(rowHead);
				descriptiveConceptHandler.processRowHead(rowHead, rawMatrix);
			}
			dataset.setTaxonNames(taxonNameHandler.getTaxonNames());
			dataset.setTaxonHierarchies(taxonHierarchyHandler.getTaxonHierarchySet());
			dataset.getTaxonNames().getTaxonName();
			dataset.setCharacterTrees(characterTreeHandler.getCharacterTreeSet());
			dataset.setCharacters(characterSetHandler.getCharacterSet());
			dataset.setDescriptiveConcepts(descriptiveConceptHandler.getDescriptiveConceptsSet());
		}
		public Dataset getDataset() {
			return dataset;
		}
	}
	
	private class TaxonNameHandler {
		private TaxonNameSet taxonNameSet = objectFactory.createTaxonNameSet();
		private Map<RowHead, TaxonNameCore> rowHeadTaxonNameCoreMap = new HashMap<RowHead, TaxonNameCore>();
		
		public void processRowHead(RowHead rowHead) {
			TaxonNameCore taxonNameCore = objectFactory.createTaxonNameCore();
			rowHeadTaxonNameCoreMap.put(rowHead, taxonNameCore);
			taxonNameCore.setId(rowHead.getValue());
			TaxonomicRank taxonomicRank = objectFactory.createTaxonomicRank();
			String rank = rowHead.getSource().getTaxonName().getRankData().getLast().getRank().toString();
			taxonomicRank.setLiteral(rank);
			Representation rep = objectFactory.createRepresentation();
			LabelText labelText = objectFactory.createLabelText();
			labelText.setValue(rowHead.getValue());
			rep.getRepresentationGroup().add(labelText);
			taxonNameCore.setRepresentation(rep);
			taxonNameCore.setRank(taxonomicRank);
			taxonNameSet.getTaxonName().add(taxonNameCore);
			
			taxonHierarchyHandler.processRowHead(rowHead);
			characterTreeHandler.processTaxonName(taxonNameCore);
			codeDescriptionHandler.processTaxonName(taxonNameCore);
		}

		public TaxonNameSet getTaxonNames() {
			return taxonNameSet;
		}

		public TaxonNameCore getTaxonNameCore(RowHead rowHead) {
			return rowHeadTaxonNameCoreMap.get(rowHead);
		}
	}
	
	private class TaxonHierarchyHandler {
		private TaxonHierarchyCore taxonHierarchyCore = objectFactory.createTaxonHierarchyCore();
		private TaxonHierarchySet taxonHierarchySet = objectFactory.createTaxonHierarchySet();
		private TaxonHierarchyNodeSeq taxonHierarchyNodeSeq = objectFactory.createTaxonHierarchyNodeSeq();
		private Representation rep = objectFactory.createRepresentation();
		
		public TaxonHierarchyHandler() {
			LabelText labelText = objectFactory.createLabelText();
			labelText.setValue("Taxon hierarchy defined by this collection of descriptions.");
			rep.getRepresentationGroup().add(labelText);
			taxonHierarchyCore.setRepresentation(rep);
			taxonHierarchyCore.setTaxonHierarchyType(new QName("http://rs.tdwg.org/UBIF/2006/", "PhylogeneticTaxonomy"));
			taxonHierarchyCore.setNodes(taxonHierarchyNodeSeq);
			taxonHierarchySet.getTaxonHierarchy().add(taxonHierarchyCore);
		}
		
		public void processRowHead(RowHead rowHead) {
			TaxonNameCore taxonName = lookupTaxonNameCore(rowHead);
			TaxonHierarchyNode taxonNode = objectFactory.createTaxonHierarchyNode();
			taxonNode.setId(NODE_PREFIX + rowHead.getValue());
			TaxonNameRef ref = objectFactory.createTaxonNameRef();
			ref.setRef(taxonName.getId());
			taxonNode.setTaxonName(ref);
			
			if(rowHead.getParent() != null) {
				TaxonHierarchyNodeRef parentNodeRef = objectFactory.createTaxonHierarchyNodeRef();
				parentNodeRef.setRef(NODE_PREFIX + rowHead.getParent().getValue());
				taxonNode.setParent(parentNodeRef);
			}
			taxonHierarchyNodeSeq.getNode().add(taxonNode);
		}
				
		private TaxonNameCore lookupTaxonNameCore(RowHead rowHead) {
			return taxonNameHandler.getTaxonNameCore(rowHead);
			/*List<TaxonNameCore> coreNames = dataset.getTaxonNames().getTaxonName();
			for(TaxonNameCore name : coreNames) {
				if(rowHead.getValue().equals(name.getId()))
					return name;
			}
			return null;*/
		}
		
		public TaxonHierarchySet getTaxonHierarchySet() {
			return taxonHierarchySet;
		}
	}
	
	private class DescripitiveConceptHandler {
		private DescriptiveConceptSet dcSet = objectFactory.createDescriptiveConceptSet();
		private Map<String, DescriptiveConcept> dcsToAdd = new HashMap<String, DescriptiveConcept>();
		
		public static final String DC_PREFIX = "dc_";
		public static final String GLOBAL_STATE_ID = "dc_global_states";
		
		public DescripitiveConceptHandler() {
			DescriptiveConcept dcGlobalStates = objectFactory.createDescriptiveConcept();
			dcGlobalStates.setId(GLOBAL_STATE_ID);
			Representation rep = makeRep("Descriptive concept describing states used globally.");
			dcGlobalStates.setRepresentation(rep);
			ConceptStateSeq conceptStates = objectFactory.createConceptStateSeq();
			dcGlobalStates.setConceptStates(conceptStates);
			this.dcsToAdd.put(GLOBAL_STATE_ID, dcGlobalStates);
		}
		
		public void processRowHead(RowHead rowHead, RawMatrix rawMatrix) {
			List<ColumnHead> columnHeads = rawMatrix.getColumnHeads();
			Set<StructureIdentifier> createdStructures = new HashSet<StructureIdentifier>();
			for(ColumnHead columnHead : columnHeads) {
				if(columnHead.hasCharacterSource()) {
					StructureIdentifier structureIdentifier = columnHead.getSource().getStructureIdentifier();
					if(!createdStructures.contains(structureIdentifier)) {
						createdStructures.add(structureIdentifier);
						DescriptiveConcept dc = objectFactory.createDescriptiveConcept();
						String structureName = (structureIdentifier.getStructureConstraintOrEmpty() + " " + 
								structureIdentifier.getStructureName()).trim();
						dc.setId(DC_PREFIX.concat(structureName));
						Representation rep = makeRep(structureName);
						dc.setRepresentation(rep);
						dcsToAdd.put(dc.getId(), dc);
						modifierHandler.processColumnHead(columnHead, rawMatrix);
						characterTreeHandler.processTaxonConceptStructureTriple(new TaxonConceptStructureTriple(rowHead, dc, structureIdentifier));
					}
				}
			}
		}

		public void create() {
			this.dcSet.getDescriptiveConcept().addAll(dcsToAdd.values());
		}
		
		public DescriptiveConceptSet getDescriptiveConceptsSet() {
			return dcSet;
		}

		public void processModifiers(DescriptiveConcept dcModifiers) {
			//Update the modifier DC (which will be added to set later).
			dcsToAdd.put(dcModifiers.getId(), dcModifiers);
		}
		
		public void processAbstractRef(AbstractRef abstractRef) {
			addToGlobalStates(abstractRef);
		}
	
		private void addToGlobalStates(AbstractRef ref) {
			ConceptStateDef conceptStateDef = objectFactory.createConceptStateDef();
			conceptStateDef.setId(ref.getRef());
			Representation rep = makeRep(ref.getRef().replace(CharacterSetHandler.STATE_ID_PREFIX, ""));
			conceptStateDef.setRepresentation(rep);
			List<ConceptStateDef> currentConceptStates = dcsToAdd.get(GLOBAL_STATE_ID).getConceptStates().getStateDefinition();
			if(!currentConceptStates.contains(conceptStateDef))
				currentConceptStates.add(conceptStateDef);
			characterSetHandler.processConceptStateDef(conceptStateDef);
		}
	}
	
	private class ModifierHandler {
		private static final String MODIFIERS_ID = "modifiers";
		private static final String LABEL_TEXT = "Descriptive Concept for holding modifiers.";
		private static final String ID_PREFIX = "mod_";
		private DescriptiveConcept dcModifiers = objectFactory.createDescriptiveConcept();
		private Map<String, ModifierDef> seenModifiers = new HashMap<String, ModifierDef>();
		/** Maps matrix taxon-character-state "triples" to modifier defs. */
		private Map<TaxonCharacterStateTriple, ModifierDef> matrixModifiers = 
				new HashMap<TaxonCharacterStateTriple, ModifierDef>();
		
		public ModifierHandler() {
			ModifierSeq modifierSeq = objectFactory.createModifierSeq();
			dcModifiers.setModifiers(modifierSeq);
			Representation repModifiers = objectFactory.createRepresentation();
			LabelText labelTextModifiers = objectFactory.createLabelText();
			dcModifiers.setId(MODIFIERS_ID);
			labelTextModifiers.setValue(LABEL_TEXT);
			repModifiers.getRepresentationGroup().add(labelTextModifiers);
			dcModifiers.setRepresentation(repModifiers);
		}
		
		public void processColumnHead(ColumnHead columnHead, RawMatrix rawMatrix) {
			//loop over each character name in the structure's char->state map
			int index = rawMatrix.getColumnHeads().indexOf(columnHead);
			for(List<CellValue> values : rawMatrix.getCellValues().values()) {
				CellValue cellValue = values.get(index);
				String modifier = cellValue.getSource().getModifier();
				if(modifier != null) {
					ModifierDef modifierDef;
					if(!seenModifiers.containsKey(modifier)) {
						modifierDef = objectFactory.createModifierDef();
						modifierDef.setId(ID_PREFIX.concat(
								modifier.replace(" ", "_").
								replace(";", "")));
						Representation rep = makeRep(modifier);
						modifierDef.setRepresentation(rep);
						//add to modifier sequence
						dcModifiers.getModifiers().getModifier().add(modifierDef);
						seenModifiers.put(modifier, modifierDef);
						descriptiveConceptHandler.processModifiers(dcModifiers);
					}
				}
			}
		}
		public void processPossibleModifier(Map<TaxonCharacterStateTriple, CellValue> possibleModifier) {
			CellValue cellValue = possibleModifier.values().iterator().next();
			if(cellValue.getSource().getModifier() != null) {
				TaxonCharacterStateTriple triple = possibleModifier.keySet().iterator().next();
				ModifierDef modDef = seenModifiers.get(cellValue.getSource().getModifier());
				matrixModifiers.put(triple, modDef);
			}
		}
		
		public Map<String, ModifierDef> getSeenModifiers() {
			return seenModifiers;
		}
		public Map<TaxonCharacterStateTriple, ModifierDef> getMatrixModifiers() {
			return matrixModifiers;
		}
	}
	
	private class CharacterSetHandler {
		
		/**
		 * This is used as a kind of "dummy" local state definition for QuantitativeCharacters.
		 * Primary use is for help in maintaining map (in SDDConverter) 
		 * from LocalStateDefs to ModifierDefs (which was originally created for use with 
		 * Categorical Characters only).
		 * @author Alex
		 *
		 */
		private class QuantitativeStateDef extends CharacterLocalStateDef {

			private AbstractCharacterDefinition character;
			/**
			 * 
			 */
			public QuantitativeStateDef(String id, AbstractCharacterDefinition character) {
				this.setId(id);
				this.character = character;
			}
			/**
			 * @return the character
			 */
			public AbstractCharacterDefinition getCharacter() {
				return character;
			}
			/* (non-Javadoc)
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				StringBuilder builder = new StringBuilder();
				builder.append("QuantitativeStateDef [");
				if (character != null)
					builder.append("character=").append(character).append(", ");
				if (id != null)
					builder.append("id=").append(id);
				builder.append("]");
				return builder.toString();
			}

		}
		
		public static final String STATE_ID_PREFIX = "state_";
		
		/** Taxon-by-character matrix. */
		private Map<String, Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>>> matrix = 
				new HashMap<String, Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>>>();
		/** SDD CharacterSet that gets attached to main Dataset. */
		private CharacterSet characterSet = objectFactory.createCharacterSet();
		/** Keeps track of state references to be used for later characters. */
		private Map<String, AbstractRef> stateRefs = new TreeMap<String, AbstractRef>();
		/** Keeps track of global states from Descriptive Concept. */
		private Map<String, ConceptStateRef> globalStates = new HashMap<String, ConceptStateRef>();
		
		public void create() {
			//make this a TreeMap to get characters listed alphabetically.
			Map<String, AbstractCharacterDefinition> charsToAdd =
					new TreeMap<String, AbstractCharacterDefinition>();
			
			for(String taxon : matrix.keySet()) {
				Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>> charStateMap = matrix.get(taxon);
				for(AbstractCharacterDefinition charDef : charStateMap.keySet()) {
					if(!charsToAdd.containsKey(charDef.getId()))
							charsToAdd.put(charDef.getId(), charDef);
					else {	//merge categorical characters
						AbstractCharacterDefinition character = 
								charsToAdd.get(charDef.getId());
						if(character instanceof CategoricalCharacter) {
							CategoricalCharacter tempChar = (CategoricalCharacter) charDef;
							mergeWithoutDuplicates((CategoricalCharacter) character, tempChar);
						}
						else if(character instanceof QuantitativeCharacter) {
							QuantitativeCharacter tempChar = (QuantitativeCharacter) charDef;
							mergeWithoutDuplicates((QuantitativeCharacter) character, tempChar);
						}
					}
					//now resolve global states of Categorical Characters
					AbstractCharacterDefinition character =
							charsToAdd.get(charDef.getId());
					if(character instanceof CategoricalCharacter)
						resolveGlobalStates((CategoricalCharacter)character);
				}
			}
			characterSet.getCategoricalCharacterOrQuantitativeCharacterOrTextCharacter().
				addAll(charsToAdd.values());
		}
		
		public void processConceptStateDef(ConceptStateDef globalStateDef) {
			//getting passed back a global state from DCHandler
			//add it to the global states mapping
			ConceptStateRef globalStateRef = objectFactory.createConceptStateRef();
			globalStateRef.setRef(globalStateDef.getId());
			globalStates.put(globalStateDef.getId(), globalStateRef);
		}

		public void processRowHead(RowHead rowHead, RawMatrix rawMatrix) {
			//add this new taxon's name to the matrix
			matrix.put(rowHead.getValue(), 	new HashMap<AbstractCharacterDefinition, Set<CharacterLocalStateDef>>());
			
			//loop over each structure in this ITaxon
			for(int i=0; i<rawMatrix.getColumnCount(); i++) {
				ColumnHead columnHead = rawMatrix.getColumnHeads().get(i);
				if(columnHead.hasCharacterSource()) {
					CellValue cellValue = rawMatrix.getCellValues().get(rowHead).get(i);
					Value value = cellValue.getSource();
					
					//loop over char->state mappings in this structure
					//initialize char and state for matrix
					AbstractCharacterDefinition character = null;
					CharacterLocalStateDef localStateDef = null;
					//resolve full character name
					String fullCharName = resolveFullCharacterName(columnHead);
					Representation charRep = makeRep(fullCharName);
					///get each associated state (and it's value)
					
					switch(valueTypeDeterminer.determine(value)) {
					case SINGLETON_NUMERIC:
						if(character == null)
							character = makeSingleQuantitativeCharState(localStateDef,
									fullCharName, cellValue);
						break;
					case SINGLETON_CATEGORICAL:
						if(character == null)
							character = makeSingleCategoricalCharacter(fullCharName);
						localStateDef = makeSingleCategoricalState(cellValue.getValue());
						attachStateCategorical((CategoricalCharacter) character, 
								localStateDef, cellValue);
						break;
					case RANGE_NUMERIC:
						if(character == null)
							character = makeRangeQuantitativeCharState(localStateDef,
									fullCharName, cellValue);
						break;
					case RANGE_CATEGORICAL:
						character = objectFactory.createCategoricalCharacter();
						character.setId(fullCharName);
						((CategoricalCharacter)character).setStates(
								objectFactory.createCharacterStateSeq());
						break;
					default:
						character = objectFactory.createQuantitativeCharacter();
						character.setId(fullCharName);
						break;	
					}
					
					//we use the same char rep in either case (for all states of that char)
					character.setRepresentation(charRep);
					Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>> charStateDescMap = 
							matrix.get(rowHead.getValue());
					if(!charStateDescMap.containsKey(character))
						charStateDescMap.put(character, new HashSet<CharacterLocalStateDef>());
					charStateDescMap.get(character).add(localStateDef);
					
					TaxonCharacterStateTriple triple = 
							new TaxonCharacterStateTriple(rowHead.getValue(), character, localStateDef);
					Map<TaxonCharacterStateTriple, CellValue> possibleModifier =
							new HashMap<TaxonCharacterStateTriple, CellValue>();
					possibleModifier.put(triple, cellValue);
					modifierHandler.processPossibleModifier(possibleModifier);
					characterTreeHandler.processTaxonCharacterStructureTripe(new TaxonCharacterStructureTriple(rowHead, character, columnHead.getSource().getStructureIdentifier()));
				}
			}
		}
		
		/**
		 * Makes a single quantitative character state definition.
		 * @param character
		 * @param localStateDef Will get initialized by the time function returns.
		 * @param fullCharName
		 * @param state
		 */
		@SuppressWarnings("rawtypes")
		private QuantitativeCharacter makeSingleQuantitativeCharState(
				CharacterLocalStateDef localStateDef, String fullCharName, CellValue cellValue) {
			QuantitativeCharacter character = objectFactory.createQuantitativeCharacter();
			character.setId(fullCharName);
			putMappingAndRangeOnQuantitativeCharacter(character, cellValue);
			localStateDef = new QuantitativeStateDef(character.getId(), character);
			return character;
		}
		
		/**
		 * 
		 * @param character The CategoricalCharacter to which a state will be attached.
		 * @param localStateDef The local State definition (might end up being a State Reference).
		 * @param stateName The name of the state.
		 */
		private void attachStateCategorical(CategoricalCharacter character,
				CharacterLocalStateDef localStateDef, CellValue cellValue) {
			//We first have to check if there's another character that uses this
			//same state.  If we remove this character from consideration, is there 
			//another one that still maps to the state in question?  If so,
			//we need this state globally.
			boolean flaggedGlobal = false;
			for(Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>> taxonMap 
					: this.matrix.values()) {
				for(AbstractCharacterDefinition presentChar : taxonMap.keySet()) {
					Set<CharacterLocalStateDef> stateSet = taxonMap.get(presentChar);
					if(!flaggedGlobal && stateSet.contains(localStateDef) &&
							!presentChar.equals(character)) {
						//then we need this state globally
						AbstractRef stateRef = stateRefs.get(cellValue.getValue());
						List<Object> currentStates = 
								character.getStates().getStateDefinitionOrStateReference();
						if(!currentStates.contains(stateRef))
							currentStates.add(stateRef);
						flaggedGlobal = true;
						descriptiveConceptHandler.processAbstractRef(stateRef);	//publish to DCHandler
						break;
					}
				}
			}
			List<Object> currentStates = 
					character.getStates().getStateDefinitionOrStateReference();
			if(!flaggedGlobal && !stateRefs.containsKey(cellValue.getValue())) {
				//haven't seen this before, stays as a local state def.
				if(!currentStates.contains(localStateDef))
					currentStates.add(localStateDef);
				//make a ref for this in case we see it later
				AbstractRef stateRef = objectFactory.createConceptStateRef();
				stateRef.setRef(localStateDef.getId());
				stateRefs.put(cellValue.getValue(), stateRef);
			}
			else if(!flaggedGlobal){
				if(!currentStates.contains(stateRefs.get(cellValue.getValue())))
					currentStates.add(stateRefs.get(cellValue.getValue()));
			}
		}
		
		/**
		 * Makes a ranged quantitative character state definition.
		 * @param localStateDef
		 * @param fullCharName
		 * @param state
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		private AbstractCharacterDefinition makeRangeQuantitativeCharState(
				CharacterLocalStateDef localStateDef, String fullCharName,
				CellValue cellValue) {
			QuantitativeCharacter character = objectFactory.createQuantitativeCharacter();
			character.setId(fullCharName);
			putMappingAndRangeOnQuantitativeCharacter(character, cellValue);
			localStateDef = new QuantitativeStateDef(character.getId(), character);
			return character;
		}
		
		/**
		 * Adds a mapping set (mapping and value range) to a QuantitativeCharacter.
		 * @param character
		 * @param state
		 */
		private void putMappingAndRangeOnQuantitativeCharacter(
				QuantitativeCharacter character, CellValue cellValue) {
			Value value = cellValue.getSource();
			QuantitativeCharMappingSet mappingSet = objectFactory.createQuantitativeCharMappingSet();
			
			String from = value.getFrom();
			if(from == null || from.isEmpty())
				from = value.getFromInclusive();
			if(from == null || from.isEmpty())
				from = value.getValue();
			String to = value.getTo();
			if(to == null || to.isEmpty())
				to = value.getToInclusive();
			if(to == null || to.isEmpty())
				to = value.getValue();
			
			if (valueTypeDeterminer.isSingleton(cellValue.getSource())) {
				QuantitativeCharMapping mapping = objectFactory.createQuantitativeCharMapping();
				ValueRangeWithClass range = objectFactory.createValueRangeWithClass();
				range.setLower(Double.valueOf(from));
				range.setUpper(Double.valueOf(to));
				mapping.setFrom(range);
				if (!mappingSet.getMapping().contains(mapping))
					mappingSet.getMapping().add(mapping);
				QuantitativeCharacter.MeasurementUnit measurementUnit = new QuantitativeCharacter.MeasurementUnit();
				LabelText unitLabelText = objectFactory.createLabelText();
				unitLabelText.setValue(value.getFromUnit());
				if(value.getFromUnit() == null || value.getFromUnit().isEmpty())
					unitLabelText.setValue("counted occurences");
				unitLabelText.setRole(new QName("http://rs.tdwg.org/UBIF/2006/", "Abbrev"));
				measurementUnit.getLabel().add(unitLabelText);
				((QuantitativeCharacter) character).setMappings(mappingSet);
				((QuantitativeCharacter) character)
						.setMeasurementUnit(measurementUnit);
			}
			else if (valueTypeDeterminer.isRange(cellValue.getSource())) {
				QuantitativeCharMapping mapping = objectFactory.createQuantitativeCharMapping();
				ValueRangeWithClass range = objectFactory.createValueRangeWithClass();
				range.setLower(Double.valueOf(from));
				range.setUpper(Double.valueOf(to));
				mapping.setFrom(range);
				if (!mappingSet.getMapping().contains(mapping))
					mappingSet.getMapping().add(mapping);
				QuantitativeCharacter.MeasurementUnit measurementUnit = new QuantitativeCharacter.MeasurementUnit();
				LabelText unitLabelText = objectFactory.createLabelText();
				unitLabelText.setValue(value.getFromUnit());
				if(value.getFromUnit() == null || value.getFromUnit().isEmpty())
					unitLabelText.setValue("counted occurences");
				unitLabelText.setRole(new QName("http://rs.tdwg.org/UBIF/2006/", "Abbrev"));
				measurementUnit.getLabel().add(unitLabelText);
				((QuantitativeCharacter) character).setMappings(mappingSet);
				((QuantitativeCharacter) character)
						.setMeasurementUnit(measurementUnit);
			}
			
		}
		
		/**
		 * Makes a single categorical character.
		 * @param fullCharName
		 * @return sdd CategoricalCharacter for addition to CharacterSet.
		 */
		private CategoricalCharacter makeSingleCategoricalCharacter(String fullCharName) {
			CategoricalCharacter character = objectFactory.createCategoricalCharacter();
			character.setId(fullCharName);
			//need to start with an empty state seq in this categorical char
			CharacterStateSeq stateSeq = objectFactory.createCharacterStateSeq();
			character.setStates(stateSeq);
			return character;
		}
		
		/**
		 * Makes a single local state definition for a categorical character.
		 * @param stateValue
		 * @return
		 */
		private CharacterLocalStateDef makeSingleCategoricalState(Object stateValue) {
			Representation stateRep = makeRep(stateValue.toString());
			String stateName = stateValue.toString();
			CharacterLocalStateDef localStateDef = objectFactory.createCharacterLocalStateDef();
			localStateDef.setId(STATE_ID_PREFIX.concat(stateName));
			localStateDef.setRepresentation(stateRep);
			return localStateDef;
		}
		
		/**
		 * Merges a temp character's states into a "keeper" character's states,
		 * disallowing duplicate refs or duplicate local state defs (task of removing
		 * defs from state set when appropriate state reference exists belongs to
		 * different function).
		 * @param keeper
		 * @param temp
		 */
		private void mergeWithoutDuplicates(CategoricalCharacter keeper, CategoricalCharacter temp) {
			List<Object> statesToKeep = keeper.getStates().getStateDefinitionOrStateReference();
			List<Object> tempStates = temp.getStates().getStateDefinitionOrStateReference();
			for(Object tempState : tempStates) {
				if(!statesToKeep.contains(tempState))
					statesToKeep.add(tempState);
			}
		}
		
		/**
		 * Merges a temp quant. character's state into the keeper quant. characters.
		 * Does this by expanding lower and upper range as far as necessary for
		 * both characters.
		 * @param keeper
		 * @param temp
		 */
		private void mergeWithoutDuplicates(QuantitativeCharacter keeper, QuantitativeCharacter temp) {
			List<QuantitativeCharMapping> keeperMappings = keeper.getMappings().getMapping();
			List<QuantitativeCharMapping> tempMappings = temp.getMappings().getMapping();
			Double min = Double.POSITIVE_INFINITY;
			Double max = Double.NEGATIVE_INFINITY;
			for(QuantitativeCharMapping mapping : keeperMappings) {
				if(mapping.getFrom().getLower() < min)
					min = mapping.getFrom().getLower();
				if(mapping.getFrom().getUpper() > max)
					max = mapping.getFrom().getUpper();
			}
			for(QuantitativeCharMapping mapping : tempMappings) {
				if(mapping.getFrom().getLower() < min)
					min = mapping.getFrom().getLower();
				if(mapping.getFrom().getUpper() > max)
					max = mapping.getFrom().getUpper();
			}
			keeperMappings.get(0).getFrom().setLower(min);
			keeperMappings.get(0).getFrom().setUpper(max);
		}
		
		private void resolveGlobalStates(CategoricalCharacter character) {
			List<Object> states = character.getStates().
					getStateDefinitionOrStateReference();
			Set<Object> statesToKeep = new HashSet<Object>();
			for(Object state : states) {
				if(state instanceof CharacterLocalStateDef) {
					CharacterLocalStateDef stateDef = (CharacterLocalStateDef) state;
					if(globalStates.containsKey(stateDef.getId()))
						statesToKeep.add(globalStates.get(stateDef.getId()));
					else
						statesToKeep.add(stateDef);
				}
				else if(state instanceof ConceptStateRef) {
					statesToKeep.add(state);
				}
			}
			states.clear();
			states.addAll(statesToKeep);
		}

		public Map<String, Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>>> getMatrix() {
			return matrix;
		}

		public CharacterSet getCharacterSet() {
			return characterSet;
		}
	}
	
	private class CharacterTreeHandler {
		private Map<String, CharacterTree> tempTrees = new HashMap<String, CharacterTree>();
		private Map<DescriptiveConcept, CharTreeNode> dcToCtNode = new HashMap<DescriptiveConcept, CharTreeNode>();
		private CharacterTreeSet charTreeSet = objectFactory.createCharacterTreeSet();
		private static final String DC_NODE_PREFIX = "dc_node_";
		private static final String CT_REP_LABEL = "Morphological Character Tree";
		
		public void processTaxonName(TaxonNameCore taxonName) {
			if(tempTrees.containsKey(taxonName.getId()))
				charTreeSet.getCharacterTree().add(tempTrees.get(taxonName.getId()));
			else {
				CharacterTree characterTree = objectFactory.createCharacterTree();
				Representation ctRep = makeRep(CT_REP_LABEL);
				characterTree.setRepresentation(ctRep);
				TaxonomicScopeSet taxonomicScopeSet = objectFactory.createTaxonomicScopeSet();
				TaxonNameRef taxonNameRef = objectFactory.createTaxonNameRef();
				taxonNameRef.setRef(taxonName.getId());
				taxonomicScopeSet.getTaxonName().add(taxonNameRef);
				characterTree.setScope(taxonomicScopeSet);
				charTreeSet.getCharacterTree().add(characterTree);
			}
		}
		public void processTaxonConceptStructureTriple(TaxonConceptStructureTriple triple) {
			addConceptNodesToCharacterTree(triple.rowHead, triple.descriptiveConcept, triple.structureIdentifier);
		}
		
		public void processRowHead(RowHead rowHead) {
			//DatasetHandler handler = (DatasetHandler) o;
			//TreeNode<ITaxon> taxonNode = (TreeNode<ITaxon>) arg;
			datasetHandler.getDataset().setCharacterTrees(charTreeSet);
		}
		
		public void processTaxonCharacterStructureTripe(TaxonCharacterStructureTriple triple) {
			addCharacterNodesToCharacterTree(triple.rowHead, triple.character, triple.structureIdentifier);
		}
		
		/**
		 * Adds character nodes (as references pointing to the appropriate descriptive
		 * concept) to a character tree.
		 * @param taxonNode Contains taxon corresponding to a character tree.
		 * @param character Add nodes based on this character.
		 * @param structureNode "Guess" the descriptive concept based on the structure name.
		 */
		private void addCharacterNodesToCharacterTree(RowHead rowHead, 
				AbstractCharacterDefinition character, StructureIdentifier structureIdentifier) {
			CharacterTree characterTree = findCharacterTree(rowHead);
			DescriptiveConcept dcTemp = objectFactory.createDescriptiveConcept();
			dcTemp.setId(descriptiveConceptHandler.DC_PREFIX.concat((structureIdentifier.getStructureConstraintOrEmpty() + " " + 
					structureIdentifier.getStructureName()).trim()));
			Object dcNode = resolveDescriptiveConceptNodeForCharacter(dcTemp);
			String dcNodeId = resolveDescriptiveConceptNodeId(dcNode);
			//make a CharNode for the tree, and point to the descriptive
			//concept node as the parent.
			CharTreeCharacter ctCharNode = objectFactory.createCharTreeCharacter();
			CharacterRef charRef = objectFactory.createCharacterRef();
			charRef.setRef(character.getId());
			ctCharNode.setCharacter(charRef);
			CharTreeNodeRef ctNodeRef = objectFactory.createCharTreeNodeRef();
			ctNodeRef.setRef(dcNodeId);
			ctCharNode.setParent(ctNodeRef);
			if(characterTree.getNodes() == null) {
				CharTreeNodeSeq ctNodeSeq = objectFactory.createCharTreeNodeSeq();
				characterTree.setNodes(ctNodeSeq);
			}
			if(!characterTree.getNodes().getNodeOrCharNode().contains(ctCharNode))
				characterTree.getNodes().getNodeOrCharNode().add(ctCharNode);
		}
		
		/**
		 * This method first adds a Node to a character tree that points to the descriptive concept object.  It then adds some character nodes 
		 * as references that point to the descriptive concept.
		 * @param taxonNode
		 * @param dc
		 * @param structureNode
		 */
		private void addConceptNodesToCharacterTree(RowHead rowHead, DescriptiveConcept dc, StructureIdentifier structureIdentifier) {
			CharacterTree characterTree = findCharacterTree(rowHead);
			if(characterTree.getNodes() == null) {
				CharTreeNodeSeq ctNodeSeq = objectFactory.createCharTreeNodeSeq();
				characterTree.setNodes(ctNodeSeq);
			}
			//make a new DC node for the tree, set it's reference to the id of the actual Descriptive Concept, set
			//the ID to be the id of the DC object with 'dc_node' in front, and point to the parent of the DC object.
			Object dcNode = resolveDescriptiveConceptNode(dc);
			if(structureIdentifier.getParent() != null) {
				StructureIdentifier parentStructure = structureIdentifier.getParent();
				CharTreeNodeRef ctNodeRef = objectFactory.createCharTreeNodeRef();
				ctNodeRef.setRef(DC_NODE_PREFIX + descriptiveConceptHandler.DC_PREFIX + 
						(parentStructure.getStructureConstraintOrEmpty() + " " + parentStructure.getStructureName()).trim());
				if(dcNode instanceof CharTreeNode)
					((CharTreeNode) dcNode).setParent(ctNodeRef);
			}
			if(dcNode instanceof CharTreeAbstractNode)
				characterTree.getNodes().getNodeOrCharNode().add((CharTreeAbstractNode) dcNode);
		}
		
		/**
		 * Resolves a DescriptiveConcept to either a CharTreeNodeRef or a
		 * CharTreeNode.
		 * @param dc
		 * @return
		 */
		private Object resolveDescriptiveConceptNode(DescriptiveConcept dc) {
			Object dcNode = null;
			DescriptiveConceptRef dcRef = objectFactory.createDescriptiveConceptRef();
			dcRef.setRef(dc.getId());
			if(this.dcToCtNode.containsKey(dc)) {
				dcNode = objectFactory.createCharTreeNodeRef();
				((CharTreeNodeRef)dcNode).setRef(this.dcToCtNode.get(dc).getId());
			}
			else {
				dcNode = objectFactory.createCharTreeNode();
				((CharTreeNode) dcNode).setDescriptiveConcept(dcRef);
				((CharTreeNode) dcNode).setId(DC_NODE_PREFIX+dc.getId());
				this.dcToCtNode.put(dc, (CharTreeNode) dcNode);		
			}
			return dcNode;
		}
		
		/**
		 * Resolves a DescriptiveConcept to either a CharTreeNodeRef or a CharTreeNode
		 * without adding to dctToCtNode mapping.
		 * @param dc
		 * @return
		 */
		private Object resolveDescriptiveConceptNodeForCharacter(DescriptiveConcept dc) {
			Object dcNode = null;
			DescriptiveConceptRef dcRef = objectFactory.createDescriptiveConceptRef();
			dcRef.setRef(dc.getId());
			if(this.dcToCtNode.containsKey(dc)) {
				dcNode = objectFactory.createCharTreeNodeRef();
				((CharTreeNodeRef)dcNode).setRef(this.dcToCtNode.get(dc).getId());
			}
			else {
				dcNode = objectFactory.createCharTreeNode();
				((CharTreeNode) dcNode).setDescriptiveConcept(dcRef);
				((CharTreeNode) dcNode).setId(DC_NODE_PREFIX+dc.getId());
			}
			return dcNode;
		}
		
		
		
		/**
		 * Returns the id of a descriptive concept from either a CharTreeNodeRef
		 * of CharTreeNode.
		 * @param dcNode
		 * @return
		 */
		private String resolveDescriptiveConceptNodeId(Object dcNode) {
			if(dcNode instanceof CharTreeNodeRef) 
				return ((CharTreeNodeRef) dcNode).getRef();
			else
				return ((CharTreeNode) dcNode).getId();
		}
		
		/**
		 * Finds the right character tree to use when adding descriptive concept
		 * or character nodes.
		 * @param taxonNode
		 * @return
		 */
		private CharacterTree findCharacterTree(RowHead rowHead) {
			CharacterTree characterTree = null;
			//first, check to see if we have a character tree for this taxon in the set.
			for(CharacterTree tree : this.charTreeSet.getCharacterTree()) {
				if(tree.getScope().getTaxonName().get(0).getRef().equals(rowHead.getValue()))
					characterTree = tree;
			}
			if(characterTree == null && !tempTrees.containsKey(rowHead.getValue())) {
				//then we don't have a real tree yet, make a temp one.
				addTempCharacterTree(rowHead.getValue());
				characterTree = tempTrees.get(rowHead.getValue());
			}
			else if(characterTree == null)
				characterTree = tempTrees.get(rowHead.getValue());
			if(characterTree.getNodes() == null) {
				CharTreeNodeSeq ctNodeSeq = objectFactory.createCharTreeNodeSeq();
				characterTree.setNodes(ctNodeSeq);
			}
			return characterTree;
		}
		
		/**
		 * Adds a temp character tree to the temp set.
		 * @param taxonName
		 */
		private void addTempCharacterTree(String taxonName) {
			CharacterTree characterTree = objectFactory.createCharacterTree();
			Representation ctRep = makeRep(CT_REP_LABEL);
			characterTree.setRepresentation(ctRep);
			TaxonomicScopeSet taxonomicScopeSet = objectFactory.createTaxonomicScopeSet();
			TaxonNameRef taxonNameRef = objectFactory.createTaxonNameRef();
			taxonNameRef.setRef(taxonName);
			taxonomicScopeSet.getTaxonName().add(taxonNameRef);
			characterTree.setScope(taxonomicScopeSet);
			tempTrees.put(taxonName, characterTree);
		}
		
		public CharacterTreeSet getCharacterTreeSet() {
			return charTreeSet;
		}
	}
	
	private class CodeDescriptionHandler {
		
		private final String DESC_REP_PREFIX = "Coded description for ";
		private final String UBIF_QNAME_URI = "http://rs.tdwg.org/UBIF/2006/";
		private final String CATEGORICAL_QNAME_LOCAL = "Categorical";
		private final String QUANTITATIVE_QNAME_LOCAL = "Quantitative";
		private final String LOW_QNAME_LOCAL = "ObserverEstLower";
		private final String HIGH_QNAME_LOCAL = "ObserverEstUpper";
		
		private CodedDescriptionSet codedDescriptionSet = objectFactory.createCodedDescriptionSet();
		private Map<String, CodedDescription> taxonDescriptions = new HashMap<String, CodedDescription>();
		
		public void processTaxonName(TaxonNameCore taxonNameCore) {
			CodedDescription description = objectFactory.createCodedDescription();
			Representation descRep = makeRep(DESC_REP_PREFIX+taxonNameCore.getId());
			description.setRepresentation(descRep);
			//indicate the taxon scope.
			TaxonNameRef taxonNameRef = objectFactory.createTaxonNameRef();
			taxonNameRef.setRef(taxonNameCore.getId());
			description.setScope(objectFactory.createDescriptionScopeSet());
			description.getScope().getTaxonName().add(taxonNameRef);
			//initialize summary data set
			description.setSummaryData(objectFactory.createSummaryDataSet());
			codedDescriptionSet.getCodedDescription().add(description);
			datasetHandler.getDataset().setCodedDescriptions(codedDescriptionSet);
			this.taxonDescriptions.put(taxonNameCore.getId(), description);
		}
		
		public void addSummaryDataToCodedDescriptions(
				Map<String, Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>>> matrix,
				Map<TaxonCharacterStateTriple, ModifierDef> matrixModifiers){
			//loop over each taxon in the matrix
			for(String taxonName : matrix.keySet()) {
				//create JAXBElement set to hold final data elements
				Set<JAXBElement> dataElementSet = 
						new TreeSet<JAXBElement>(new Comparator<JAXBElement>() {
							@Override
							public int compare(JAXBElement arg0, JAXBElement arg1) {
								return ((AbstractCharSummaryData)arg0.getValue()).getRef().compareTo(
										((AbstractCharSummaryData)arg1.getValue()).getRef());
							}
						});
				Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>> charStateMap =
						matrix.get(taxonName);
				//and loop over each character
				for(AbstractCharacterDefinition character : charStateMap.keySet()) {
					//could be either categorical...
					if(character instanceof CategoricalCharacter) {
						CatSummaryData summaryData = objectFactory.createCatSummaryData();
						summaryData.setRef(character.getId());
						//and loop over states
						for(CharacterLocalStateDef state : charStateMap.get(character)) {
							TaxonCharacterStateTriple triple = 
									new TaxonCharacterStateTriple(taxonName, character, state);
							//check for null states...(?)
							if(state != null) {
								StateData stateData = objectFactory.createStateData();
								stateData.setRef(state.getId());
								//add in any modifiers
								ModifierDef modDef = matrixModifiers.get(triple);
								if(modDef != null) {
									ModifierRefWithData modRef = 
											objectFactory.createModifierRefWithData();
									modRef.setRef(modDef.getId());
									stateData.getModifier().add(modRef);
								}
								summaryData.getState().add(stateData);
							}
						}
						//create final data element (JAXBElement) for each character
						JAXBElement<CatSummaryData> dataElement =
								new JAXBElement<CatSummaryData>(
										new QName(UBIF_QNAME_URI, CATEGORICAL_QNAME_LOCAL),
										CatSummaryData.class,
										CatSummaryData.class,
										(CatSummaryData) summaryData);
						dataElementSet.add(dataElement);
					}
					//...or quantitative
					if(character instanceof QuantitativeCharacter) {
						QuantSummaryData summaryData = objectFactory.createQuantSummaryData();
						summaryData.setRef(character.getId());
						UnivarSimpleStatMeasureData low = objectFactory.createUnivarSimpleStatMeasureData();
						low.setType(new QName("http://rs.tdwg.org/UBIF/2006/", LOW_QNAME_LOCAL));
						low.setValue(((QuantitativeCharacter)character).getMappings().getMapping().get(0).getFrom().getLower());
						summaryData.getMeasureOrPMeasure().add(low);
						UnivarSimpleStatMeasureData high = objectFactory.createUnivarSimpleStatMeasureData();
						high.setType(new QName("http://rs.tdwg.org/UBIF/2006/", HIGH_QNAME_LOCAL));
						high.setValue(((QuantitativeCharacter)character).getMappings().getMapping().get(0).getFrom().getUpper());
						summaryData.getMeasureOrPMeasure().add(high);
						for(CharacterLocalStateDef state : charStateMap.get(character)) {
							TaxonCharacterStateTriple triple = 
									new TaxonCharacterStateTriple(taxonName, character, state);
							//again, check for null states (?)
							if(state != null) {
								//add in any modifiers
								ModifierDef modDef = matrixModifiers.get(triple);
								if(modDef != null) {
									ModifierRefWithData modRef = objectFactory.createModifierRefWithData();
									modRef.setRef(modDef.getId());
									summaryData.getModifier().add(modRef);
								}
							}
						}
						//create final data element (JAXBElement) for each character
						JAXBElement<QuantSummaryData> dataElement =
								new JAXBElement<QuantSummaryData>(
										new QName(UBIF_QNAME_URI, QUANTITATIVE_QNAME_LOCAL),
										QuantSummaryData.class,
										QuantSummaryData.class,
										(QuantSummaryData) summaryData);
						dataElementSet.add(dataElement);
					}
				}
				CodedDescription description = taxonDescriptions.get(taxonName);
				for(JAXBElement element : dataElementSet)
					description.getSummaryData().getCategoricalOrQuantitativeOrSequence().add(element);
			}	//end taxon name
		}
	}
	
	
	private Marshaller marshaller;
	private File file;

	public SDDWriter(File file) throws JAXBException {
		this.file = file;
		Map<String, Object> properties = new HashMap<String, Object>(1);
		JAXBContext jaxbContext = JAXBContextFactory.createContext(new Class[] {Datasets.class}, properties);
		this.marshaller = jaxbContext.createMarshaller(); 
	}
	
	@Override
	public void write(RawMatrix rawMatrix) throws Exception {
		Datasets datasets = createDatasets(rawMatrix);
		marshaller.marshal(datasets, file);			
	}

	private Datasets createDatasets(RawMatrix rawMatrix) throws Exception {	
		Datasets datasets = objectFactory.createDatasets();
		metdataCreator.create(datasets);
		datasetHandler.create(rawMatrix);
		descriptiveConceptHandler.create();
		characterSetHandler.create();
		codeDescriptionHandler.addSummaryDataToCodedDescriptions(characterSetHandler.getMatrix(), 
				modifierHandler.getMatrixModifiers());		
		datasets.getDataset().add(datasetHandler.getDataset());
		return datasets;
	}
	
	private Representation makeRep(String value) {
		ObjectFactory objectFactory = new ObjectFactory();
		Representation rep = objectFactory.createRepresentation();
		LabelText labelText = objectFactory.createLabelText();
		labelText.setValue(value);
		rep.getRepresentationGroup().add(labelText);
		return rep;
	}
	
	public String resolveFullCharacterName(ColumnHead columnHead) {
		Character character = columnHead.getSource();
		StructureIdentifier structureIdentifier = character.getStructureIdentifier();
		String structName = (character.getStructureConstraintOrEmpty() + " " + character.getStructureName()).trim();
		String charName = structName + "_" + character.getName();
		StructureIdentifier parentStructureIdentifier = structureIdentifier.getParent();
		while(parentStructureIdentifier != null) {
			structName = (parentStructureIdentifier.getStructureConstraintOrEmpty() + " " + character.getStructureName()).trim();
			if(!structName.equals("whole_organism"))
				charName = structName + "_" + charName;
			parentStructureIdentifier = parentStructureIdentifier.getParent();
		}
		return charName;
	}

}
