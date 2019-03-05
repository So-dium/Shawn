package virnet.management.information.service;

import java.util.Map;

public interface FacilitiesInformationQuery {
	public Map<String, Object> Facilitiesquery(String user, int page, String select, String physicsMachinesName);
}

