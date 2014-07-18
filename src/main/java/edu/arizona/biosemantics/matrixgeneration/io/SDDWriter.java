package edu.arizona.biosemantics.matrixgeneration.io;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.tdwg.rs.ubif._2006.Dataset;
import org.tdwg.rs.ubif._2006.Datasets;
import org.tdwg.rs.ubif._2006.DetailText;
import org.tdwg.rs.ubif._2006.DocumentGenerator;
import org.tdwg.rs.ubif._2006.LabelText;
import org.tdwg.rs.ubif._2006.ObjectFactory;
import org.tdwg.rs.ubif._2006.Representation;
import org.tdwg.rs.ubif._2006.TaxonNameCore;
import org.tdwg.rs.ubif._2006.TaxonNameSet;
import org.tdwg.rs.ubif._2006.TaxonomicRank;
import org.tdwg.rs.ubif._2006.TechnicalMetadata;

import edu.arizona.biosemantics.matrixgeneration.Configuration;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class SDDWriter implements Writer {
	
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
		ObjectFactory objectFactory = new ObjectFactory();
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
		String label = ""; //"The " + taxonRank + " " + taxon.getName();
		labelText.setValue(label);
		DetailText detailText = objectFactory.createDetailText();
		detailText.setValue("");//("Generated from Hong's mark-up of FNA document");
		representation.getRepresentationGroup().add(labelText);
		representation.getRepresentationGroup().add(detailText);
		dataset.setRepresentation(representation);
		
		TaxonNameSet taxonNameSet = objectFactory.createTaxonNameSet();
		
		for(RowHead rowHead : rawMatrix.getRowHeads()) {
			TaxonNameCore taxonNameCore = objectFactory.createTaxonNameCore();
			taxonNameCore.setId(rowHead.getValue());
			TaxonomicRank taxonomicRank = objectFactory.createTaxonomicRank();
			String rank = taxon.getTaxonRank().toString().toLowerCase();
			taxonomicRank.setLiteral(rank);
			Representation rep = objectFactory.createRepresentation();
			LabelText labelText = objectFactory.createLabelText();
			labelText.setValue(rank.concat(" ").concat(taxon.getName()));
			rep.getRepresentationGroup().add(labelText);
			taxonNameCore.setRepresentation(rep);
			taxonNameCore.setRank(taxonomicRank);
			this.taxonNameSet.getTaxonName().add(taxonNameCore);
		}
		
		private edu.arizona.biosemantics.matrixgeneration.sdd.Dataset dataset;
		private edu.arizona.biosemantics.matrixgeneration.sdd.TaxonNameSet taxonNameSet;
		private edu.arizona.biosemantics.matrixgeneration.sdd.ObjectFactory sddFactory;
		
		return datasets;
	}

}
