package oneapp.incture.workbox.pmc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.pmc.dto.CollaborationDto;
import oneapp.incture.workbox.pmc.dto.responses.CollaborationResponseDto;
import oneapp.incture.workbox.pmc.dto.responses.NotificationResponseDto;
import oneapp.incture.workbox.pmc.services.CollaborationFacadeLocal;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/collaboration", produces = "application/json")
public class CollaborationRest {

	@Autowired
	private CollaborationFacadeLocal collaborationFacadeLocal;

	
	public ResponseMessage createCollaboration(@RequestBody CollaborationDto dto) {
		return collaborationFacadeLocal.createCollaboration(dto);
	}

	@RequestMapping(value = "/getMessageDetails", method = RequestMethod.GET)
	public CollaborationResponseDto getMessageDetails(@RequestParam("processId") String processId,
			@RequestParam("taskId") String taskId) {
		return collaborationFacadeLocal.getMessageDetails(processId, taskId);
	}

	@RequestMapping(value = "/getProcessWithTaskMessage", method = RequestMethod.GET)
	public CollaborationResponseDto getProcessLevelWithTaskLevelMessage(@RequestParam("processId") String processId) {
		return collaborationFacadeLocal.getProcessLevelWithTaskLevelMessage(processId);
	}

	@RequestMapping(value = "/getOwnerMessage", method = RequestMethod.GET)
	public CollaborationResponseDto getOwnerMessage() {
		return collaborationFacadeLocal.getMessageUsingOwnerId();
	}

	@RequestMapping(value = "/getNotification", method = RequestMethod.GET)
	public NotificationResponseDto getNotification() {
		return collaborationFacadeLocal.getNotification();
	}

}
