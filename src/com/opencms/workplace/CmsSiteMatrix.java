package com.opencms.workplace;

/*
 * File   : $Source: /alkacon/cvs/opencms/src/com/opencms/workplace/Attic/CmsSiteMatrix.java,v $
 * Date   : $Date: 2000/10/02 14:09:00 $
 *
 * Copyright (C) 2000  The OpenCms Group 
 * 
 * This File is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * For further information about OpenCms, please see the
 * OpenCms Website: http://www.opencms.com
 * 
 * You should have received a copy of the GNU General Public License
 * long with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import com.opencms.file.*;
import com.opencms.core.*;
import com.opencms.util.*;
import com.opencms.template.*;

import java.util.*;

import javax.servlet.http.*;

 /**
 * Template class for displaying a sitematrix
 * Creation date: (09/22/00 13:32:48)
 * @author: Finn Nielsen
 */
public class CmsSiteMatrix extends com.opencms.template.CmsXmlTemplate {
/**
 * Gets the content of a defined section in a given template file and its subtemplates
 * with the given parameters. 
 * 
 * @see getContent(CmsObject cms, String templateFile, String elementName, Hashtable parameters)
 * @param cms CmsObject Object for accessing system resources.
 * @param templateFile Filename of the template file.
 * @param elementName Element name of this template in our parent template.
 * @param parameters Hashtable with all template class parameters.
 * @param templateSelector template section that should be processed.
 */
public byte[] getContent(CmsObject cms, String templateFile, String elementName, Hashtable parameters, String templateSelector) throws CmsException
{
	Hashtable country_map = new Hashtable();
	Hashtable category_map = new Hashtable();
	Hashtable siteinfo = null;
	String country_key = null, country_place = null, category_place = null, lsname = null, csname = null;
	StringBuffer lines = null, nodes = null;
	CmsCategory category = null;
	int country_count = 0;
	CmsXmlTemplateFile xmlTemplateDocument = getOwnTemplateFile(cms, templateFile, elementName, parameters, templateSelector);
	Vector sites = cms.getSiteMatrixInfo();
	Vector categories = cms.getAllCategories();

	// Map categories to rows in the matrix
	lines = new StringBuffer();
	for (int i = 0; i < categories.size(); i++)
	{
		category = (CmsCategory) categories.elementAt(i);
		category_place = "" + i;
		category_map.put(new Integer(category.getId()), category_place);
		xmlTemplateDocument.setData("y", category_place);
		xmlTemplateDocument.setData("name", category.getName());
		lines.append(xmlTemplateDocument.getProcessedDataValue("category"));
	}
	xmlTemplateDocument.setData("categories", lines.toString());

	// Map countries and languages to columns in the matrix and find out the width of the matrix
	/* Reminder of how a site hashtable was created
	Hashtable a = new Hashtable();
	a.put("siteid", new Integer(res.getInt("SITE_ID")));
	a.put("sitename", res.getString("SITE_NAME"));
	a.put("categoryid", new Integer(res.getInt("CATEGORY_ID")));
	a.put("langid", new Integer(res.getInt("LANGUAGE_ID")));
	a.put("countryid", new Integer(res.getInt("COUNTRY_ID")));
	
	shortname = res.getString("LANG_SNAME");
	if (shortname != null) a.put("lang_sname", shortname);
	a.put("lang_name", res.getString("LANG_NAME"));
	
	shortname = res.getString("COUNTRY_SNAME");
	if (shortname != null) a.put("country_sname", shortname);
	a.put("country_name", res.getString("COUNTRY_NAME"));
	siteinfo.addElement(a);
	*/
	lines = new StringBuffer();
	nodes = new StringBuffer();
	country_count = 0;
	for (int i = 0; i < sites.size(); i++)
	{
		siteinfo = (Hashtable) sites.elementAt(i);
		country_key = siteinfo.get("countryid").toString() + "x" + siteinfo.get("langid").toString();
		if (!country_map.containsKey(country_key))
		{
			country_place = "" + country_count++;
			country_map.put(country_key, country_place);
		}
		else
		{
			country_place = (String) country_map.get(country_key);
		}
		category_place = (String) category_map.get(siteinfo.get("categoryid"));

		//
		xmlTemplateDocument.setData("x", country_place);
		xmlTemplateDocument.setData("y", category_place);
		lsname = (String) siteinfo.get("lang_sname");
		if (lsname == null)
			lsname = (String) siteinfo.get("lang_name");
		csname = (String) siteinfo.get("country_sname");
		if (csname == null)
			csname = (String) siteinfo.get("country_name");
		//
		xmlTemplateDocument.setData("shortname", csname + " (" + lsname + ")");
		xmlTemplateDocument.setData("name", (String) siteinfo.get("country_name") + " (" + (String) siteinfo.get("lang_name") + ")");
		lines.append(xmlTemplateDocument.getProcessedDataValue("domain"));

		//
		xmlTemplateDocument.setData("id", ""+((Integer) siteinfo.get("siteid")).intValue());
		xmlTemplateDocument.setData("name", (String) siteinfo.get("sitename"));
		nodes.append(xmlTemplateDocument.getProcessedDataValue("sitenode"));
	}
	xmlTemplateDocument.setData("domains", lines.toString());
	xmlTemplateDocument.setData("sites", nodes.toString());

	//
	return startProcessing(cms, xmlTemplateDocument, elementName, parameters, templateSelector);
}
/**
 * Indicates if the results of this class are cacheable.
 * 
 * @param cms CmsObject Object for accessing system resources
 * @param templateFile Filename of the template file 
 * @param elementName Element name of this template in our parent template.
 * @param parameters Hashtable with all template class parameters.
 * @param templateSelector template section that should be processed.
 * @return <EM>true</EM> if cacheable, <EM>false</EM> otherwise.
 */
public boolean isCacheable(CmsObject cms, String templateFile, String elementName, Hashtable parameters, String templateSelector)
{
	return false;
}
}
