/**
 * 
 */
package com.heiya.mobileapi.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author Dian Krisnanjaya
 *
 */
public class EmailTemplate {

	private String templateId;
	
	private String template;

	public EmailTemplate(String templateId) {
		this.templateId = templateId;
		try {
			this.template = loadTemplate(this.templateId);
		} 
		catch (Exception e) {
			this.template = "Empty";
		}
	}
	
	
	private String loadTemplate(String templateId) throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(templateId).getFile());
		String content = "Empty";
		try {
			content = new String(Files.readAllBytes(file.toPath()));
		} 
		catch (IOException e) {
			throw new Exception("Could not read template with ID = " + templateId);
		}
		return content;
	}
	
	
	public String getTemplate(Map<String, String> replacements) {
		String cTemplate = this.template;

		//Replace the String 
		for (Map.Entry<String, String> entry : replacements.entrySet()) {
			cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
		}
		return cTemplate;
	}
}
