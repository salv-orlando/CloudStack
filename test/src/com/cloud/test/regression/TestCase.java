/**
 * *  Copyright (C) 2011 Citrix Systems, Inc.  All rights reserved
*
 *
 * This software is licensed under the GNU General Public License v3 or later.
 *
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cloud.test.regression;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;


public abstract class TestCase{
	
	public static Logger s_logger = Logger.getLogger(TestCase.class.getName());
	private Connection conn;
	private ArrayList <Document> inputFile = new ArrayList<Document> ();
	private HttpClient client;
	private String testCaseName;
	private HashMap<String, String> param = new HashMap<String, String> ();
	private HashMap<String, String> commands = new HashMap<String, String> ();
	
	public HashMap<String, String> getParam() {
		return param;
	}

	public void setParam(HashMap<String, String> param) {
		this.param = param;
	}
	
	
	public HashMap<String, String> getCommands() {
		return commands;
	}

	public void setCommands() {
		File asyncCommands = null;
		if (param.get("apicommands") == null) {
			s_logger.info("Unable to get the list of commands, exiting");
			System.exit(1);
		} else {
			asyncCommands = new File(param.get("apicommands"));
		}
		try {
			Properties pro = new Properties();
			FileInputStream in = new FileInputStream(asyncCommands);
	        pro.load(in);
	        Enumeration<?> en = pro.propertyNames();
	        while (en.hasMoreElements()) {
	        	String key = (String) en.nextElement();
	        	commands.put(key, pro.getProperty(key));
	        }	
		} catch (Exception ex) {
			s_logger.info("Unable to find the file " + param.get("apicommands") + " due to following exception " + ex);
		}
			
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(String dbPassword) {
		this.conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conn=DriverManager.getConnection("jdbc:mysql://" + param.get("db") + "/cloud", "root", dbPassword);
			if (!this.conn.isValid(0)) {
				s_logger.error("Connection to DB failed to establish");
			}
			
		}catch (Exception ex) {
			s_logger.error(ex);
		}
	}

	public void setInputFile (ArrayList<String> fileNameInput) {
		for (String fileName: fileNameInput) {
			File file = new File(fileName);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document doc = null;
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				doc = builder.parse(file);
				doc.getDocumentElement().normalize();
			} catch (Exception ex) {
				s_logger.error("Unable to load " + fileName + " due to ", ex);
			}
			this.inputFile.add(doc);
		}
	} 

	public ArrayList<Document> getInputFile() {
		return inputFile;
	}
	
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	
	public String getTestCaseName(){
		return this.testCaseName;
	}

	public void setClient() {
		HttpClient client = new HttpClient();
		this.client = client;
	}
	
	public HttpClient getClient() {
		return this.client;
	}
	
	//abstract methods
	public abstract boolean executeTest();

}
