package com.galacticflake.restime.projectsupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.galacticflake.restime.RestEndpoint;

public class ProjectExporter {

	public static void export(Project project, File file) throws FileNotFoundException, XMLStreamException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();

			// Uncomment if you do not require XML declaration
			// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			Element rootE = document.createElement("resTime");
			Element versionE = document.createElement("version");
			versionE.setTextContent(project.getVersion());
			Element nameE = document.createElement("name");
			nameE.setTextContent(project.getName());
			
			document.appendChild(rootE);
			rootE.appendChild(versionE);
			rootE.appendChild(nameE);
			
			// endpoints
			Element endpointsE = document.createElement("endpoints");
			for (RestEndpoint endpoint : project.getEndpoints()) {
				Element endpointE = document.createElement("endpoint");
				endpointE.setAttribute("name", endpoint.getName());
				endpointE.setAttribute("endpoint", endpoint.getEndpoint());
				endpointE.setAttribute("method", endpoint.getMethod());
				endpointE.setAttribute("content-type", endpoint.getContentType());
				endpointE.setAttribute("content", endpoint.getContent());
				Map<String, String> headers = endpoint.getHeaders();
				if (!headers.isEmpty()) {
					Element headersE = document.createElement("header");
					for (String hname : headers.keySet()) {
						Element headerE = document.createElement("header");
						headerE.setAttribute("name", hname);
						headerE.setAttribute("value", headers.get(hname));
						headersE.appendChild(headerE);
					}
					endpointE.appendChild(headersE);
				}
				endpointsE.appendChild(endpointE);
			}
			rootE.appendChild(endpointsE);
			// Write XML to file
			FileOutputStream outStream = new FileOutputStream(file);
			transformer.transform(new DOMSource(document), new StreamResult(outStream));
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
