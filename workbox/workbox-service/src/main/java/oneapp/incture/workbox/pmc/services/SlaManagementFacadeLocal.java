package oneapp.incture.workbox.pmc.services;

import oneapp.incture.workbox.pmc.dto.SlaListDto;
import oneapp.incture.workbox.pmc.dto.responses.SlaProcessNamesResponse;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

public interface SlaManagementFacadeLocal {

	SlaProcessNamesResponse getAllProcessNames();

	SlaListDto getSlaDetails(String processName);

	ResponseMessage updateSla(SlaListDto dto);

}
