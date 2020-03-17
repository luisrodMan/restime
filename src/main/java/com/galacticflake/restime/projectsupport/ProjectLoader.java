package com.galacticflake.restime.projectsupport;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.galacticflake.restime.RestEndpoint;

public class ProjectLoader {
	
	public static Project load(File file) throws IOException, ProjectParsingException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			String prjVersion = doc.getElementsByTagName("version").item(0).getTextContent().trim();
			Project project = null;
			if (prjVersion.equals("1.0")) {
				project = loadv1_0(doc);
			} else
				throw new ProjectParsingException("project file version not valid!");
			project.setPath(file.getAbsolutePath());
			return project;
		} catch (Exception e) {
			throw new ProjectParsingException("Error on load file project: " + e.getMessage());
		}
	}
	
	public static Project loadv1_0(Document doc) {
		String prjName = doc.getElementsByTagName("name").item(0).getTextContent();
		Project project = new Project(prjName);
		
		// read enpoints
		Element endpointsE = getElement(doc.getElementsByTagName("endpoints"));
		if (endpointsE != null)
		for (Element endpointE : getElements(endpointsE, "endpoint")) {
			String name = endpointE.getAttribute("name");
			String endpointAttr = endpointE.getAttribute("endpoint");
			String method = endpointE.getAttribute("method");
			
			RestEndpoint endpoint = new RestEndpoint(name);
			endpoint.setEndpoint(endpointAttr);
			endpoint.setMethod(method);
			endpoint.setContentType(endpointE.getAttribute("content-type"));
			endpoint.setContent(endpointE.getAttribute("content"));
			
			// headers
			Element headersE = getElement(endpointE.getElementsByTagName("headers"));
			if (headersE != null) {
				for (Element headerE : getElements(headersE, "header")) {
					String hname = headerE.getAttribute("name");
					String hvalue = headerE.getAttribute("value");
					if (hname != null && hvalue != null)
						endpoint.addHeader(hname, hvalue);
				}
			}
			
			project.addEndpoint(endpoint);
		}
		return project;
	}
	
	private static Element getElement(NodeList list) {
		if (list != null && list.getLength() > 0)
			return (Element) list.item(0);
		return null;
	}
	
	private static List<Element> getElements(Element element, String tag) {
		NodeList list = element.getElementsByTagName(tag);
		List<Element> elements = new LinkedList<>();
		if (list != null && list.getLength() > 0) {
			for (int i = 0; i < list.getLength(); i++)
				elements.add((Element) list.item(i));
		}
		return elements;
	}

}