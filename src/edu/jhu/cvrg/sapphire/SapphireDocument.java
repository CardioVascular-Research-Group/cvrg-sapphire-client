package edu.jhu.cvrg.sapphire;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class SapphireDocument {
	
	private SapphireHeader header;
	private Document document;

	public SapphireDocument(String xmlString){
		document = buildDOM(xmlString);
		Element rootElement = document.getRootElement();
		if(!rootElement.getName().equals("sapphire")){
			System.out.println("Document is not a sapphire document.");
		}
		
		parseHeader(rootElement);
		
		
		
	}
	
	private void parseHeader(Element rootElement){
		Element headerElement = rootElement.getChild("header");
		header = new SapphireHeader();
		header.namespace = headerElement.getAttributeValue("xmlns");
		header.msgCHN = headerElement.getChild("msgCHN").getAttributeValue("v");
		header.sessionID = headerElement.getChild("sessionID").getAttributeValue("v");

	}
	
	private Document buildDOM(String xmlDocAsString){
		Document doc = null;
	    SAXBuilder builder = new SAXBuilder();
	    Reader stringreader = new StringReader(xmlDocAsString);
	    try {
	    	doc = builder.build(stringreader);
	    } catch(IOException e) {
	    	e.printStackTrace();
	    } catch (JDOMException e) {
			e.printStackTrace();
		}
	    return doc;
	}
	
	private class SapphireHeader{
		public String namespace;
		public String sessionID;
		public String msgCHN;
		public String replytoMsgSQN;
		public String responseID;
		public String creationDateTime;
	}
}
