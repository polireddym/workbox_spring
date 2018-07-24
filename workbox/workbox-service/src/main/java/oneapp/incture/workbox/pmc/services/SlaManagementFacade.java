package oneapp.incture.workbox.pmc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.pmc.dto.SlaListDto;
import oneapp.incture.workbox.pmc.dto.responses.SlaProcessNamesResponse;
import oneapp.incture.workbox.pmc.wsdlconsumers.UMEManagementEngineConsumer;
import oneapp.incture.workbox.poadapter.dao.SlaManagementDao;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

/**
 * Session Bean implementation class ConfigurationFacade
 */
@Service("SlaManagementFacade")
public class SlaManagementFacade implements SlaManagementFacadeLocal {

	
	/*@EJB
	UserManagementFacadeWsdlConsumerLocal ume;*/
	
	@Autowired
	private UMEManagementEngineConsumer umeConsumer;
	
	@Autowired
	private SlaManagementDao dao;

	@Override
	public SlaProcessNamesResponse getAllProcessNames() {
		SlaProcessNamesResponse response = new SlaProcessNamesResponse();
		ResponseMessage responseMessage = new ResponseMessage();
//		response.setSlaProcessNames(new SlaManagementDao(em.getEntityManager())
		response.setSlaProcessNames(dao.getSlaProcessList(umeConsumer.getLoggedInUser().getUserId()));
		responseMessage.setMessage("Sla Processes List Fetched Successfully");
		responseMessage.setStatus("SUCCESS");
		responseMessage.setStatusCode("1");
		response.setResponseMessage(responseMessage);
		return response;
	}

	@Override
	public SlaListDto getSlaDetails(String processName) {
		SlaListDto  slaList = dao.getDetailSla(processName);
		return slaList;
	}

	@Override
	public ResponseMessage updateSla(SlaListDto dto) {
		return dao.updateSla(dto);
	}
	
}
