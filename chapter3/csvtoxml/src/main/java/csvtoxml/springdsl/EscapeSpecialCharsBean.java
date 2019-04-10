package csvtoxml.springdsl;

import org.apache.commons.lang.StringEscapeUtils;

public class EscapeSpecialCharsBean {

	public static String escapeSpecialChars(String body) {
		/*
		 * body = body.replaceAll("&", "&amp;"); body = body.replaceAll("<", "&lt;");
		 * body = body.replaceAll(">", "&gt;"); body = body.replaceAll("'", "&apos;");
		 * body = body.replaceAll("\"", "&quot;");
		 */
		
		body =  StringEscapeUtils.escapeXml(body);

		return body;
	}
}
