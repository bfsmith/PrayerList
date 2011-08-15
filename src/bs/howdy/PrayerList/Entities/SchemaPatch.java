package bs.howdy.PrayerList.Entities;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SchemaPatch {
	private int _number;
	private String _sql;
	
	public SchemaPatch(int number, String sql) {
		_number = number;
		_sql = sql;
	}
	
	public SchemaPatch(Node xmlNode) {
		Element element = (Element) xmlNode;
        _number = Integer.parseInt(element.getAttribute("number")); 
        _sql = element.getChildNodes().item(0).getNodeValue().trim();
	}
	
	public int getNumber() {
		return _number;
	}
	public String getSql() {
		return _sql;
	}
}
