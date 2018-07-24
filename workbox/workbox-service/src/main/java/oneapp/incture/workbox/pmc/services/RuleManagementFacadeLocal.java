package oneapp.incture.workbox.pmc.services;

import java.util.List;

import oneapp.incture.workbox.pmc.dto.RuleManagementDto;
import oneapp.incture.workbox.pmc.dto.responses.RuleManagementResponseDto;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

public interface RuleManagementFacadeLocal {

	RuleManagementResponseDto getRules(String processName);

	ResponseMessage onSubmit(List<RuleManagementDto> dtoList);

}
