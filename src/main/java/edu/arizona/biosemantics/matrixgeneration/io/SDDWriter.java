package edu.arizona.biosemantics.matrixgeneration.io;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.tdwg.rs.ubif._2006.Dataset;
import org.tdwg.rs.ubif._2006.Datasets;
import org.tdwg.rs.ubif._2006.DetailText;
import org.tdwg.rs.ubif._2006.DocumentGenerator;
import org.tdwg.rs.ubif._2006.LabelText;
import org.tdwg.rs.ubif._2006.ObjectFactory;
import org.tdwg.rs.ubif._2006.Representation;
import org.tdwg.rs.ubif._2006.TaxonHierarchyCore;
import org.tdwg.rs.ubif._2006.TaxonHierarchyNode;
import org.tdwg.rs.ubif._2006.TaxonHierarchyNodeRef;
import org.tdwg.rs.ubif._2006.TaxonHierarchyNodeSeq;
import org.tdwg.rs.ubif._2006.TaxonHierarchySet;
import org.tdwg.rs.ubif._2006.TaxonNameCore;
import org.tdwg.rs.ubif._2006.TaxonNameRef;
import org.tdwg.rs.ubif._2006.TaxonNameSet;
import org.tdwg.rs.ubif._2006.TaxonomicRank;
import org.tdwg.rs.ubif._2006.TechnicalMetadata;

import edu.arizona.biosemantics.matrixgeneration.Configuration;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;
import edu.arizona.biosemantics.matrixgeneration.sdd.CharacterTree;
import edu.arizona.biosemantics.matrixgeneration.sdd.TaxonomicScopeSet;
import edu.arizona.biosemantics.matrixgeneration.util.ConversionUtil;

public class SDDWriter implements Writer {
	
	private Marshaller marshaller;
	private File file;
	private ObjectFactory objectFactory = new ObjectFactory();
	private final String NODE_PREFIX = "th_node_";

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
		
		Dataset dataset = objectFactory.createDataset();
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
		
		TaxonNameSet taxonNameSet = objectFactory.createTaxonNameSet();
		dataset.setTaxonNames(taxonNameSet);

		TaxonHierarchySet taxonHierarchySet = objectFactory.createTaxonHierarchySet();
		dataset.setTaxonHierarchies(taxonHierarchySet);
		TaxonHierarchyCore taxonHierarchyCore = objectFactory.createTaxonHierarchyCore();
		TaxonHierarchyNodeSeq taxonHierarchyNodeSeq = objectFactory.createTaxonHierarchyNodeSeq();
		taxonHierarchyCore.setNodes(taxonHierarchyNodeSeq);
		taxonHierarchySet.getTaxonHierarchy().add(taxonHierarchyCore);
		addRepToTaxonHierarchyCore(taxonHierarchyCore);
		
		for(RowHead rowHead : rawMatrix.getRootRowHeads()) {
			processTaxonHierarchy(rowHead, null, dataset);
		}
		
		return datasets;
	}
	
	private void processTaxonHierarchy(RowHead rowHead, RowHead parent, Dataset dataset) {
		addTaxonNameToSet(rowHead, dataset.getTaxonNames());
		addTaxonHierarchyToDataset(rowHead, parent, dataset, 
				dataset.getTaxonHierarchies().getTaxonHierarchy().get(0).getNodes());
		for(RowHead child : rowHead.getChildren()) {
			processTaxonHierarchy(child, rowHead, dataset);
		}
	}

	protected void addTaxonNameToSet(RowHead rowHead, TaxonNameSet taxonNameSet) {
		TaxonNameCore taxonNameCore = objectFactory.createTaxonNameCore();
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
		
		addCharacterTreeToSet(taxonNameCore);
	}
	
	private void addCharacterTreeToSet(TaxonNameCore taxonName) {
		if(tempTrees.containsKey(taxonName.getId()))
			charTreeSet.getCharacterTree().add(tempTrees.get(taxonName.getId()));
		else {
			CharacterTree characterTree = sddFactory.createCharacterTree();
			Representation ctRep = ConversionUtil.makeRep(CT_REP_LABEL);
			characterTree.setRepresentation(ctRep);
			TaxonomicScopeSet taxonomicScopeSet = sddFactory.createTaxonomicScopeSet();
			TaxonNameRef taxonNameRef = sddFactory.createTaxonNameRef();
			taxonNameRef.setRef(taxonName.getId());
			taxonomicScopeSet.getTaxonName().add(taxonNameRef);
			characterTree.setScope(taxonomicScopeSet);
			charTreeSet.getCharacterTree().add(characterTree);
		}
	}
	
	protected void addTaxonHierarchyToDataset(RowHead rowHead, RowHead parent, Dataset dataset, 
			TaxonHierarchyNodeSeq taxonHierarchyNodeSeq) {
		TaxonNameCore taxonName = lookupTaxonNameCore(rowHead, dataset);
		TaxonHierarchyNode taxonNode = objectFactory.createTaxonHierarchyNode();
		taxonNode.setId(NODE_PREFIX + rowHead.getValue());
		TaxonNameRef ref = objectFactory.createTaxonNameRef();
		ref.setRef(taxonName.getId());
		taxonNode.setTaxonName(ref);
		
		if(parent != null) {
			TaxonHierarchyNodeRef parentNodeRef = objectFactory.createTaxonHierarchyNodeRef();
			parentNodeRef.setRef(NODE_PREFIX + parent.getValue());
			taxonNode.setParent(parentNodeRef);
		}
		taxonHierarchyNodeSeq.getNode().add(taxonNode);
	}

	/**
	 * Just adds a simple rep to the TaxonHierarchy core.  Called once, from
	 * Constructor.
	 */
	private void addRepToTaxonHierarchyCore(TaxonHierarchyCore taxonHierarchyCore) {
		Representation rep = objectFactory.createRepresentation();
		LabelText labelText = objectFactory.createLabelText();
		labelText.setValue("Taxon hierarchy defined by this collection of descriptions.");
		rep.getRepresentationGroup().add(labelText);
		taxonHierarchyCore.setRepresentation(rep);
		taxonHierarchyCore.setTaxonHierarchyType(
				new QName("http://rs.tdwg.org/UBIF/2006/", "PhylogeneticTaxonomy"));
	}

	/**
	 * Lookup a Taxon's TaxonNameCore in the SDD Dataset.
	 * @param taxon
	 * @return
	 */
	private TaxonNameCore lookupTaxonNameCore(RowHead rowHead, Dataset dataset) {
		List<TaxonNameCore> coreNames = dataset.getTaxonNames().getTaxonName();
		for(TaxonNameCore name : coreNames) {
			if(rowHead.getValue().equals(name.getId()))
				return name;
		}
		return null;
	}

}
