package virnet.management.information.service;

import java.util.Map;

public interface InformationQuery {
	public Map<String, Object> query(String user, int page, String select);
}

