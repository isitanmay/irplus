/**  
   Copyright 2008-2010 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.ir.oai.metadata.provider.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.ur.ir.SimpleDateFormatter;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.institution.DeletedInstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemContentType;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemExtent;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemTitle;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.PublishedDate;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMappingService;
import edu.ur.ir.oai.OaiUtil;
import edu.ur.ir.oai.metadata.provider.ListSetsService;
import edu.ur.ir.oai.metadata.provider.OaiMetadataProvider;
import edu.ur.ir.person.BasicPersonNameFormatter;

/**
 * This allows qualified dublin core metatadata to be output.
 * 
 * @author NathanS
 *
 */
public class DefaultQualifiedDublinCoreMetadataProvider implements OaiMetadataProvider{

	/** eclipse generated id  */
	private static final long serialVersionUID = -8227691687049064056L;

	/** Prefix handled by this provider */
	public static String METADATA_PREFIX = "dcterms";
	
	public static String METADATA_NAMESPACE = "http://urresearch.rochester.edu/OAI/2.0/oai_dc_terms/";
	
	public static String SCHEMA = "http://dublincore.org/schemas/xmls/qdc/2003/04/02/dcterms.xsd";
	
	/** Service for dealing with contributor types */
	private ContributorTypeDublinCoreMappingService contributorTypeDublinCoreMappingService;  
	
	/** Service for dealing with dublin core identifier types */
	private IdentifierTypeDublinCoreMappingService identifierTypeDublinCoreMappingService;

	/** Person name formatter */
	private BasicPersonNameFormatter nameFormatter;
	
	/** namespace for the oai url */
	private String namespaceIdentifier;
	
	/** service to deal with listing set information */
	private ListSetsService listSetsService;

	/**
	 * Get the xml output for the item
	 *  
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getMetadata(edu.ur.ir.institution.InstitutionalItemVersion)
	 */
	public void addXml(Element record, InstitutionalItemVersion institutionalItemVersion) {

		 Document doc = record.getOwnerDocument();
		 // create the header
         createHeader(doc, record, institutionalItemVersion);
         createMetadata(doc, record, institutionalItemVersion);		
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getMetadataPrefixSupport()
	 */
	public String getMetadataPrefix() {
		return METADATA_PREFIX;
	}

	/**
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#supportsPrefix(java.lang.String)
	 */
	public boolean supports(String metadataPrefix) {
		return METADATA_PREFIX.equalsIgnoreCase(metadataPrefix);
	}
	
	/**
	 * Create the header for the item.
	 * 
	 * @param doc
	 * @param institutionalItemVersion
	 */
	private void createHeader(Document doc, Element record, InstitutionalItemVersion institutionalItemVersion)
	{
		 // create the header element of the record 
		 Element header = doc.createElement("header");
		 record.appendChild(header);
		 
		 // identifier element
		 Element identifier = doc.createElement("identifier");
		 Text data = doc.createTextNode("oai:" + namespaceIdentifier + ":" + institutionalItemVersion.getId().toString());
		 identifier.appendChild(data);
		 header.appendChild(identifier);
		 
		 // datestamp element
		 Element datestamp = doc.createElement("datestamp");
		 Date d = institutionalItemVersion.getDateLastModified();
		 if( d == null )
		 {
			 d = institutionalItemVersion.getDateOfDeposit();
		 }
		 String zuluDateTime = OaiUtil.getZuluTime(d);
		 
		 data = doc.createTextNode(zuluDateTime);
		 datestamp.appendChild(data);
		 header.appendChild(datestamp);
		 
		 InstitutionalCollection collection = institutionalItemVersion.getVersionedInstitutionalItem().getInstitutionalItem().getInstitutionalCollection();
		 Element setSpec = doc.createElement("setSpec");
 		 data = doc.createTextNode(listSetsService.getSetSpec(collection));
	     setSpec.appendChild(data);
	     header.appendChild(setSpec);

	}
	
	/**
	 * Create the metadata section for oai.
	 * 
	 * @param doc  - xml document root
	 * @param institutionalItemVersion - institutional item version to write
	 */
	private void createMetadata(Document doc, Element record, InstitutionalItemVersion institutionalItemVersion)
	{
		 // create the header element of the record 
		 Element metadata = doc.createElement("metadata");
		 record.appendChild(metadata);
		 
		 Element oaiDc = doc.createElement("oai_dc:dc");

		 oaiDc.setAttribute("xmlns:oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");
		 oaiDc.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		 oaiDc.setAttribute("xmlns:dcterms", "http://purl.org/dc/terms");
		 oaiDc.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");

		 metadata.appendChild(oaiDc);
		 
		 GenericItem item = institutionalItemVersion.getItem();
		 addTitle(doc, oaiDc, item);
		 addDateAvailable(doc, oaiDc, item);
		 addHandle(doc, oaiDc, institutionalItemVersion);
		 
		 if( item.isPubliclyViewable() && !item.isEmbargoed() && !institutionalItemVersion.isWithdrawn())
		 {
		     addAlternativeTitles(doc, oaiDc, item);
		     addType(doc, oaiDc, item);
		     addContributors(doc, oaiDc, item);
		     addDescription(doc, oaiDc, item);
		     addAbstract(doc, oaiDc, item);
		     addIdentifiers(doc, oaiDc, item);
		     addLanguage(doc, oaiDc, item);	 
		     addSubjects(doc, oaiDc, item);
		     addPublisher(doc, oaiDc, item);
		     addRights(doc, oaiDc, item);
		     addAvailable(doc, oaiDc, item);
		     addCitation(doc, oaiDc, item);
		     addDateAccepted(doc, oaiDc, institutionalItemVersion);
		     addDateIssued(doc, oaiDc, item);
		     addDateModified(doc, oaiDc, institutionalItemVersion);
		     addExtents(doc, oaiDc, item);
		 }
		 
	}
	
	public ContributorTypeDublinCoreMappingService getContributorTypeDublinCoreMappingService() {
		return contributorTypeDublinCoreMappingService;
	}

	public void setContributorTypeDublinCoreMappingService(
			ContributorTypeDublinCoreMappingService contributorTypeDublinCoreMappingService) {
		this.contributorTypeDublinCoreMappingService = contributorTypeDublinCoreMappingService;
	}
	
	public BasicPersonNameFormatter getNameFormatter() {
		return nameFormatter;
	}

	public void setNameFormatter(BasicPersonNameFormatter nameFormatter) {
		this.nameFormatter = nameFormatter;
	}
	
	public String getNamespaceIdentifier() {
		return namespaceIdentifier;
	}

	public void setNamespaceIdentifier(String namespaceIdentifier) {
		this.namespaceIdentifier = namespaceIdentifier;
	}
	
	/**
	 * Add the title 
	 * 
	 * @param doc
	 * @param oaiDc
	 */
	private void addTitle(Document doc, Element oaiDc, GenericItem item)
	{
		 Element title = doc.createElement("dcterms:title");
		 Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars((item.getFullName())));
		 title.appendChild(data);
		 oaiDc.appendChild(title);
		 	 
		 
	}
	
	/**
	 * Add alternative titles information
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addAlternativeTitles(Document doc, Element oaiDc, GenericItem item)
	{
		for(ItemTitle subTitle : item.getSubTitles())
		 {
			 Element alternative = doc.createElement("dcterms:alternative");
			 Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(subTitle.getFullTitle()));
			 alternative.appendChild(data);
			 oaiDc.appendChild(alternative);
		 }	 
	}
	
	/**
	 * Add the type information
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addType(Document doc, Element oaiDc, GenericItem item)
	{
		for(ItemContentType itemContentType : item.getItemContentTypes())
		 {
			 Element secondaryType = doc.createElement("dc:type");
			 Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(itemContentType.getContentType().getName()));
			 secondaryType.appendChild(data);
			 oaiDc.appendChild(secondaryType);
		 }		 
	}
	
	/**
	 * Add contributor information.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addContributors(Document doc, Element oaiDc, GenericItem item)
	{
		 for(ItemContributor itemContributor : item.getContributors())
		 {
			 if(itemContributor.getContributor() == null )
			 {
				 throw new IllegalStateException("contributor null");
			 }
			 if( itemContributor.getContributor().getContributorType() == null )
			 {
				 throw new IllegalStateException("contributor Type null");
			 }
			
			 
			 ContributorTypeDublinCoreMapping dcMapping = contributorTypeDublinCoreMappingService.get(itemContributor.getContributor().getContributorType().getId());
			 Element creator = null;
			 if( dcMapping != null )
			 {
	
					 creator = doc.createElement("dcterms:" + dcMapping.getDublinCoreTerm().getName());
			 }
			 else
			 {
				 creator  = doc.createElement("dcterms:creator");
			 }
			 Text data = doc.createTextNode( OaiUtil.removeInvalidXmlChars(nameFormatter.getNameFormatted(itemContributor.getContributor().getPersonName(), true)) );
			 creator.appendChild(data);
			 oaiDc.appendChild(creator);
		 }
	}
	
	/**
	 * Add the description information 
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDescription(Document doc, Element oaiDc, GenericItem item)
	{
		 if( item.getDescription() != null && !item.getDescription().equalsIgnoreCase(""))
		 {
			 Element description = doc.createElement("dcterms:description");
		     Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(item.getDescription()));
		     description.appendChild(data);
		     oaiDc.appendChild(description);
		 }
	}

	/**
	 * Add the abstract information.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addAbstract(Document doc, Element oaiDc, GenericItem item)
	{
		 if( item.getItemAbstract() != null && !item.getItemAbstract().equalsIgnoreCase(""))
		 {
			 Element itemAbstract = doc.createElement("dcterms:abstract");
		     Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(item.getItemAbstract()));
		     itemAbstract.appendChild(data);
		     oaiDc.appendChild(itemAbstract);
		 }
	}
	
	/**
	 * Add identifier information.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addIdentifiers(Document doc, Element oaiDc, GenericItem item)
	{
		for(ItemIdentifier itemIdentifier : item.getItemIdentifiers())
		{
			IdentifierTypeDublinCoreMapping dcMapping = identifierTypeDublinCoreMappingService.get(itemIdentifier.getIdentifierType().getId());
			Element identifier = null;
			if( dcMapping != null )
			{
				
				identifier = doc.createElement("dcterms:" + dcMapping.getDublinCoreTerm().getName());
				if( dcMapping.getDublinCoreEncodingScheme() != null )
				{
					identifier.setAttribute("xsi:type", "dcterms:" + dcMapping.getDublinCoreEncodingScheme().getName());
				}
			}
			else
			{
			    identifier = doc.createElement("dcterms:identifier");
			}
			Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(itemIdentifier.getValue()));
			identifier.appendChild(data);
			oaiDc.appendChild(identifier);
		}
	}
	
	/**
	 * Add the language type 
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addLanguage(Document doc, Element oaiDc, GenericItem item)
	{
	    LanguageType languageType = item.getLanguageType();
	    if( languageType != null )
	    {
	        if( languageType.getIso639_2() != null && !languageType.getIso639_2().equals(""))
	        {
	            Element language = doc.createElement("dcterms:language");
	            Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(languageType.getIso639_2()));
	            language.appendChild(data);
	            oaiDc.appendChild(language);
	        }
	        else if( languageType.getIso639_1() != null && !languageType.getIso639_1().equals(""))
	        {
	            Element language = doc.createElement("dcterms:language");
	            Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(languageType.getIso639_1()));
	            language.appendChild(data);
	            oaiDc.appendChild(language);
	        }
	        else if( languageType.getName()!= null && !languageType.getName().equals(""))
	        {
	            Element language = doc.createElement("dcterms:language");
	            Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(languageType.getName()));
	            language.appendChild(data);
	            oaiDc.appendChild(language);
	        }
	    }
	}
	
	/**
	 * Add subjects
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addSubjects(Document doc, Element oaiDc, GenericItem item)
	{
	    String itemSubjects = item.getItemKeywords();
	    if( itemSubjects != null )
	    {
	        StringTokenizer tokenizer = new StringTokenizer(itemSubjects, ";");
	        while( tokenizer.hasMoreElements())
	        {
	    	    String value = tokenizer.nextToken();
	    	    if( value != null && !value.equals(""))
	    	    {
	    	        Element subject = doc.createElement("dcterms:subject");
	    	        Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(value));
	    	        subject.appendChild(data);
	    	        oaiDc.appendChild(subject);
	    	    }
	        }
	    }
		
	}
	
	/**
	 * Add publisher to the document.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addPublisher(Document doc, Element oaiDc, GenericItem item)
	{
	    ExternalPublishedItem externalPublishedItem = item.getExternalPublishedItem();
	    if( externalPublishedItem != null )
	    {
	    	Publisher pub = externalPublishedItem.getPublisher();
	    	if( pub != null )
	    	{
	    		 Element publisher = doc.createElement("dcterms:publisher");
		    	 Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(pub.getName()));
		    	 publisher.appendChild(data);
		    	 oaiDc.appendChild(publisher);
	    	}
	    }
	}
	
	/**
	 * Add rights statement.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addRights(Document doc, Element oaiDc, GenericItem item)
	{
		CopyrightStatement copyrightStatement = item.getCopyrightStatement();
		if( copyrightStatement != null )
		{
			Element rightsElement = doc.createElement("dcterms:rights");
			Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(copyrightStatement.getText()));
	    	rightsElement.appendChild(data);
	    	oaiDc.appendChild(rightsElement);
		}
	}
	
	/**
	 * Add the date this publication was made available.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addAvailable(Document doc, Element oaiDc, GenericItem item)
	{
		if( item.getReleaseDate() != null )
		{
		    Date d = item.getReleaseDate();
		    Element availableElement = doc.createElement("dcterms:available");
		    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
			Text data = doc.createTextNode(dateFormat.format(d));
	    	availableElement.appendChild(data);
	    	oaiDc.appendChild(availableElement);
		}
	}
	
	/**
	 * Add the citation
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addCitation(Document doc, Element oaiDc, GenericItem item)
	{
		ExternalPublishedItem externalPublishedItem = item.getExternalPublishedItem();
		if( externalPublishedItem != null )
		{
			if( externalPublishedItem.getCitation() != null )
			{
		        Element citationElement = doc.createElement("dcterms:bibliographicCitation");
			    Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(externalPublishedItem.getCitation()));
			    citationElement.appendChild(data);
	    	    oaiDc.appendChild(citationElement);
			}
		}
	}
	
	/**
	 * Add the date accepted
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDateAccepted(Document doc, Element oaiDc, InstitutionalItemVersion item)
	{
		if( item.getDateOfDeposit() != null )
		{
		    Date d = item.getDateOfDeposit();
		    Element acceptedElement = doc.createElement("dcterms:dateAccepted");
		    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
			Text data = doc.createTextNode(dateFormat.format(d));
	    	acceptedElement.appendChild(data);
	    	oaiDc.appendChild(acceptedElement);
		}
	}
	
	/**
	 * Add the date issued
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDateIssued(Document doc, Element oaiDc, GenericItem item)
	{
		ExternalPublishedItem externalPublishedItem = item.getExternalPublishedItem();
		if( externalPublishedItem != null )
		{
			PublishedDate publishedDate = externalPublishedItem.getPublishedDate();
			if( publishedDate != null )
			{
		        Element citationElement = doc.createElement("dcterms:issued");
		        Text data = doc.createTextNode(SimpleDateFormatter.getDate(publishedDate));
		        citationElement.appendChild(data);
    	        oaiDc.appendChild(citationElement);
			}
		}
	}
	
	/**
	 * Add the date modified
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDateModified(Document doc, Element oaiDc, InstitutionalItemVersion item)
	{
		if( item.getDateLastModified() != null )
		{
		    Date d = item.getDateLastModified();
		    Element modifiedElement = doc.createElement("dcterms:modified");
		    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
			Text data = doc.createTextNode(dateFormat.format(d));
	    	modifiedElement.appendChild(data);
	    	oaiDc.appendChild(modifiedElement);
		}
	}
	
	/**
	 * Add the extents
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addExtents(Document doc, Element oaiDc, GenericItem item)
	{
		for( ItemExtent ie : item.getItemExtents())
		{
			Element extentElement = doc.createElement("dcterms:extent");
		    Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(ie.getExtentType().getName() + ":" + ie.getValue()));
		    extentElement.appendChild(data);
    	    oaiDc.appendChild(extentElement);
		}
	}
	
	/**
	 * Add handle url.
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addHandle(Document doc, Element oaiDc, InstitutionalItemVersion item)
	{
		if( item.getHandleInfo() != null )
		{
			HandleInfo handle = item.getHandleInfo();
		    Element identifier = doc.createElement("dcterms:identifier");
		    identifier.setAttribute("xsi:type", "dcterms:URI");
		    Text data = doc.createTextNode(handle.getNameAuthority().getAuthorityBaseUrl() + handle.getNameAuthority().getNamingAuthority() + "/" + handle.getLocalName());
		    identifier.appendChild(data);
		    oaiDc.appendChild(identifier);
		}
	}
	
	/**
	 * Add date available
	 * 
	 * @param doc
	 * @param oaiDc
	 * @param item
	 */
	private void addDateAvailable(Document doc, Element oaiDc, GenericItem item)
	{
		
		if( item.getReleaseDate() != null )
		{
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    Element dateAvailable = doc.createElement("dcterms:available");
		    Text data = doc.createTextNode(df.format(item.getReleaseDate()));
		    dateAvailable.appendChild(data);
		    oaiDc.appendChild(dateAvailable);
		}
	}
	
	public ListSetsService getListSetsService() {
		return listSetsService;
	}

	public void setListSetsService(ListSetsService listSetsService) {
		this.listSetsService = listSetsService;
	}

	/**
	 * Get the namespace for the provider
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getNamespace()
	 */
	public String getMetadataNamespace() {
		return METADATA_NAMESPACE;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataProvider#getSchema()
	 */
	public String getSchema() {
		return SCHEMA;
	}
	
	public void addXml(Element record, DeletedInstitutionalItemVersion institutionalItemVersion)
	{
		 Document doc = record.getOwnerDocument();
		 // create the header
         createHeader(doc, record, institutionalItemVersion);
	}
	
	/**
	 * Create the header for the deleted item.
	 * 
	 * @param doc
	 * @param deletedInstitutionalItemVersion
	 */
	private void createHeader(Document doc, Element record, DeletedInstitutionalItemVersion institutionalItemVersion)
	{
		 // create the header element of the record 
		 Element header = doc.createElement("header");
		 record.appendChild(header);
		 
		 // identifier element
		 Element identifier = doc.createElement("identifier");
		 header.setAttribute("status", "deleted");
		 Text data = doc.createTextNode("oai:" + namespaceIdentifier + ":" + institutionalItemVersion.getInstitutionalItemVersionId().toString());
		 identifier.appendChild(data);
		 header.appendChild(identifier);
		 
		 // datestamp element
		 Element datestamp = doc.createElement("datestamp");
		 Date d = institutionalItemVersion.getDeletedInstitutionalItem().getDeletedDate();
		 String zuluDateTime = OaiUtil.getZuluTime(d);
		 
		 data = doc.createTextNode(zuluDateTime);
		 datestamp.appendChild(data);
		 header.appendChild(datestamp);
		 
		 Long collectionId = institutionalItemVersion.getDeletedInstitutionalItem().getInstitutionalCollectionId();
		 if( collectionId != null )
		 {
		     String setSpecStr = listSetsService.getSetSpec(collectionId);
		     if( setSpecStr != null )
		     {
		         Element setSpec = doc.createElement("setSpec");
		         data = doc.createTextNode(setSpecStr);
		         setSpec.appendChild(data);
		         header.appendChild(setSpec);
		     }
		 }

	}
	
	public IdentifierTypeDublinCoreMappingService getIdentifierTypeDublinCoreMappingService() {
		return identifierTypeDublinCoreMappingService;
	}

	public void setIdentifierTypeDublinCoreMappingService(
			IdentifierTypeDublinCoreMappingService identifierTypeDublinCoreMappingService) {
		this.identifierTypeDublinCoreMappingService = identifierTypeDublinCoreMappingService;
	}
	


}
