package com.yuan.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public final class XmlUtil {
	public static Document parseText(String xmlText) {
		try {
			return new SAXBuilder().build(new StringReader(xmlText));
		} catch (JDOMException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Document loadDocument(File file) {
		try {
			return new SAXBuilder().build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void saveDocument(File file, Document document) {
		XMLOutputter XMLOut = new XMLOutputter();
		XMLOut.setFormat(Format.getPrettyFormat());
		try {
			XMLOut.output(document, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T xmlToObject(String xml, Class<T> clz) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clz);
		Unmarshaller marshaller = context.createUnmarshaller();
		return (T) marshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
	}

	@SuppressWarnings("unchecked")
	public static <T> T xmlFileToObject(File xmlFile, Class<T> clz) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clz);
		Unmarshaller marshaller = context.createUnmarshaller();
		return (T) marshaller.unmarshal(xmlFile);
	}

	public static String objectToXml(Object obj) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// 写入OutPutStream中
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(obj, baos);
		return baos.toString();
	}

	public static void objectToXmlFile(Object obj, File xmlFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// 写入文件中
		jaxbMarshaller.marshal(obj, xmlFile);
	}
}
