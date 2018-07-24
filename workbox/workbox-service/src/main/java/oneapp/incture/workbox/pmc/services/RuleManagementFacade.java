package oneapp.incture.workbox.pmc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.pmc.dto.RuleManagementDto;
import oneapp.incture.workbox.pmc.dto.responses.RuleManagementResponseDto;
import oneapp.incture.workbox.poadapter.dao.RuleManagementDao;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;
import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * Session Bean implementation class ConfigurationFacade
 */
@Service("RuleManagementFacade")
public class RuleManagementFacade implements RuleManagementFacadeLocal {	
    
	@Autowired
	private RuleManagementDao  dao ;
	
	@Override
	public RuleManagementResponseDto getRules(String processName) {
		RuleManagementResponseDto responseDto = new RuleManagementResponseDto();
		ResponseMessage message = new ResponseMessage();
		List<RuleManagementDto> rules = dao.getRules(processName);
		if(!ServicesUtil.isEmpty(rules)){
			message.setStatusCode("0");
		}else{
			message.setStatusCode("1");
			message.setMessage(PMCConstant.NO_RESULT);
		}
		
		message.setStatus(PMCConstant.STATUS_SUCCESS);
	
		responseDto.setActionDto(dao.getActions());
		responseDto.setTaskList(dao.getTasks(processName));
		responseDto.setMessage(message);
		responseDto.setRuleManagementDtos(rules);
		return responseDto;
	}

	@Override
	public ResponseMessage onSubmit(List<RuleManagementDto> dtoList) {
		return dao.submitRules(dtoList);
	}
	
}
