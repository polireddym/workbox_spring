package oneapp.incture.workbox.pmc.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.pmc.dto.ProcessActionDto;
import oneapp.incture.workbox.pmc.services.ProcessActionFacadeWsdlConsumerLocal;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/processAction", produces = "application/json")
public class ProcessActionRest {

	private static final Logger logger = LoggerFactory.getLogger(ProcessActionRest.class);

	@Autowired
	private ProcessActionFacadeWsdlConsumerLocal processAction;

	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ResponseMessage cancel(@RequestBody ProcessActionDto dto) {
		logger.error("[PMC][ProcessAction][Rest][cancel] method invoked");
		return processAction.cancelProcess(dto);
	}

}
