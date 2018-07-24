package oneapp.incture.workbox.pmc.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.pmc.dto.ProcessActionDto;
import oneapp.incture.workbox.pmc.wsdlconsumers.ProcessActionConsumer;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * Session Bean implementation class UserManagementFacadeWsdlConsumer
 */
@Service("ProcessActionFacadeWsdlConsumer")
public class ProcessActionFacadeWsdlConsumer implements ProcessActionFacadeWsdlConsumerLocal {
	
	private static final Logger logger=LoggerFactory.getLogger(ProcessActionFacadeWsdlConsumer.class);

	/*@WebServiceRef(name = "ProcessActionFacadeService")
	private ProcessActionFacadeService actionServices;*/
	@Autowired
	private ProcessActionConsumer processActionConsumer ;

	@Override
	public ResponseMessage cancelProcess(ProcessActionDto processList) {
		logger.error("[PMC][services][processAction][cancelProcess] method invoked with input" + processList);
		
		ResponseMessage responseDto = new ResponseMessage();
		List<String> processInstanceList = processList.getProcessInstanceList(); 
		if(!ServicesUtil.isEmpty(processInstanceList)){
			for(String processInstance : processInstanceList){
				logger.error("[PMC][services][processAction][cancelProcess] in loop  with instance " + processInstance);
				if (!ServicesUtil.isEmpty(processInstance)) {
					try {
						logger.error("[PMC][services][processAction][cancelProcess][taskInstanceId] " + processInstance);
						responseDto.setMessage(processActionConsumer.cancelProcess(processInstance));
					} catch (Exception e) {
						responseDto.setMessage("Process cancellation Failed because" + e.getMessage());
					}
				} else {
					responseDto.setMessage("Instance Id required to cancel");
					break;
				}
			} 
		}
		else {
			responseDto.setMessage(" No Instance Id sent to to cancel");
		}
		if(responseDto.getMessage().equals("SUCCESS")){
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");	
		}else{
			responseDto.setStatus("FAILURE");
			responseDto.setStatusCode("1");
		}
		return responseDto;
	}


}
