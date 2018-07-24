package oneapp.incture.workbox.pmc.services;

import oneapp.incture.workbox.pmc.dto.CollaborationDto;
import oneapp.incture.workbox.pmc.dto.responses.CollaborationResponseDto;
import oneapp.incture.workbox.pmc.dto.responses.NotificationResponseDto;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

public interface CollaborationFacadeLocal {

	public ResponseMessage createCollaboration(CollaborationDto dto);

	public CollaborationResponseDto getMessageDetails(String processId, String taskId);

	public CollaborationResponseDto getProcessLevelWithTaskLevelMessage(String processId);

	public CollaborationResponseDto getMessageUsingOwnerId();
	
	public NotificationResponseDto getNotification();
}
