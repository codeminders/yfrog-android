/**
 * 
 */
package com.codeminders.yfrog.client.response;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * @author idemydenko
 *
 */
class XMLResponseFactory extends ResponseFactory {
	
	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	XMLResponseFactory() {
	}
	
	public UploadResponse buildResponse(InputStream is) throws IOException, UploadResponseFormatException {
		UploadResponse response = new UploadResponse();

		try {
			response = execute(is);
		} catch (IOException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new UploadResponseFormatException("Response format exception", e);
		}
		
		return response;
	}

	private UploadResponse execute(InputStream is)  throws IOException, ParserConfigurationException, SAXException, UploadResponseFormatException {
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document d = builder.parse(is);
		
		return parse(d.getDocumentElement());
	}

	private UploadResponse parse(Node root) throws UploadResponseFormatException {
		if(root == null || !UploadResponse.RESPONSE_NAME.equals(root.getNodeName())) {
			throw new UploadResponseFormatException("Element " + UploadResponse.RESPONSE_NAME + " not found");
		}
		
		String status = getAttributeValue(root, UploadResponse.RESPONSE_STATUS_NAME); 

		return UploadResponse.STATUS_OK.equals(status) ? parseOKBody(root.getChildNodes()) : parseFailBody(root.getChildNodes());
	}

	private UploadResponse parseOKBody(NodeList nodes) throws UploadResponseFormatException {
		UploadResponse response = new UploadResponse();
		response.setStatus(UploadResponse.STATUS_OK);

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (hasName(node, UploadResponse.RESPONSE_MEDIAID_NAME)) {
					response.setMediaId(getElementValue(node));
				} else if (hasName(node, UploadResponse.RESPONSE_MEDIAURL_NAME)) {
					response.setMediaUrl(getElementValue(node));
				} else if (hasName(node, UploadResponse.RESPONSE_STATUSID_NAME)) {
					response.setStatusId(Long.parseLong(getElementValue(node)));
				} else if (hasName(node, UploadResponse.RESPONSE_USERID_NAME)) {
					response.setUserId(Long.parseLong(getElementValue(node)));
				} 
			}
		}

		return response;
	}

	private UploadResponse parseFailBody(NodeList nodes) throws UploadResponseFormatException {
		UploadResponse response = new UploadResponse();
		response.setStatus(UploadResponse.STATUS_FAIL);

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (hasName(node, UploadResponse.RESPONSE_ERROR_NAME)) {
					response.setErrorCode(Integer.parseInt(getAttributeValue(node, UploadResponse.RESPONSE_ERROR_CODE_NAME)));
					response.setErrorMessage(getAttributeValue(node, UploadResponse.RESPONSE_ERROR_MESSAGE_NAME));
				} 
			}
		}

		return response;
	}

	private String getAttributeValue(Node node, String attrName) throws UploadResponseFormatException {
		Node n = node.getAttributes().getNamedItem(attrName);
		
		if (n == null) {
			throw new UploadResponseFormatException("Value for element [" + attrName + "] not found");
		}
		
		return n.getNodeValue();
	}
	
	private String getElementValue(Node node) throws UploadResponseFormatException {
		Node n = node.getChildNodes().item(0);
		
		if (n == null) {
			throw new UploadResponseFormatException("Value for element [" + node.getNodeName() + "] not found");
		}
		
		return n.getNodeValue();		
	}
	
	private boolean hasName(Node node, String name) {
		return name.equals(node.getNodeName());
	}
}
