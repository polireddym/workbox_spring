package oneapp.incture.workbox.pmc.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.pmc.poadapter.services.UserDetailsDto;

import oneapp.incture.workbox.pmc.dao.CollaborationDao;
import oneapp.incture.workbox.pmc.dto.CollaborationDto;
import oneapp.incture.workbox.pmc.dto.CollaborationMessagesDto;
import oneapp.incture.workbox.pmc.dto.CollaborationNotificationDto;
import oneapp.incture.workbox.pmc.dto.responses.CollaborationResponseDto;
import oneapp.incture.workbox.pmc.dto.responses.NotificationResponseDto;
import oneapp.incture.workbox.pmc.wsdlconsumers.UMEManagementEngineConsumer;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;
import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

@Service("CollaborationFacade")
public class CollaborationFacade implements CollaborationFacadeLocal {

	
	private static final Logger logger=LoggerFactory.getLogger(CollaborationFacade.class);
	
	/*@WebServiceRef(name = "UMEUserManagementFacadeService")
	private UMEUserManagementFacadeService userServices;*/
    @Autowired
	private UMEManagementEngineConsumer umeConsumer;
	
    @Autowired
    private CollaborationDao dao;
    
	CollaborationResponseDto collaborationMessageDto;
	
	ResponseMessage responseDto;

	public ResponseMessage createCollaboration(CollaborationDto dto) {
		
		responseDto = new ResponseMessage();
		UserDetailsDto getLoggedInUser = umeConsumer.getLoggedInUser();
		
		dto.setCreatedAt(new Date());
		dto.setUserId(getLoggedInUser.getUserId());
		dto.setUserDisplayName(getLoggedInUser.getDisplayName());
		
		if (!ServicesUtil.isEmpty(dto.getMessage()) && !ServicesUtil.isEmpty(dto.getProcessId())) {
			if (dao.createCollaborationDetail(dto).equals("SUCCESS")) {
				responseDto.setMessage("Created Successfully");
				responseDto.setStatus("SUCCESS");
				responseDto.setStatusCode("0");
				return responseDto;
			} 
		} else {
			responseDto.setMessage("Mandatory Fields are Empty");
		}
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		return responseDto;
	}

	public CollaborationResponseDto getMessageDetails( String processId, String taskId) {
		collaborationMessageDto = new CollaborationResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try{
			List<CollaborationMessagesDto>	dto = dao.getMessageDetails(processId,taskId);
			if (!ServicesUtil.isEmpty(dto)) {
				collaborationMessageDto.setResponseDtos(dto);
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		}
		catch(Exception e){
			logger.error("[PMC][CollaborationFacade][getMessageDetails][error]"+e.getMessage());
			responseDto.setMessage("Fetching data failed due to " + e.getMessage());
		}
		collaborationMessageDto.setResponseMessage(responseDto);
		return collaborationMessageDto;
	}

	public CollaborationResponseDto getProcessLevelWithTaskLevelMessage(String processId) {
		collaborationMessageDto = new CollaborationResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try{
			List<CollaborationMessagesDto>	dto = dao.getAllDetailsOfProcessWithTask(processId);
			if (!ServicesUtil.isEmpty(dto)) {
				collaborationMessageDto.setResponseDtos(dto);
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		}
		catch(Exception e){
			logger.error("[PMC][CollaborationFacade][getProcessLevelWithTaskLevelMessage][error]"+e.getMessage());
			responseDto.setMessage("Fetching data failed due to " + e.getMessage());
		}
		collaborationMessageDto.setResponseMessage(responseDto);
		return collaborationMessageDto;
	}

	public CollaborationResponseDto getMessageUsingOwnerId() {
		collaborationMessageDto = new CollaborationResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try{
			UMEManagementEngineConsumer umeConsumer = new UMEManagementEngineConsumer();
			List<CollaborationMessagesDto>	dto = dao.getMessageOfOwner(umeConsumer.getLoggedInUser().getUserId());
			if (!ServicesUtil.isEmpty(dto)) {
				collaborationMessageDto.setResponseDtos(dto);
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		}
		catch(Exception e){
			logger.error("[PMC][CollaborationFacade][getMessageUsingOwnerId][error]"+e.getMessage());
			responseDto.setMessage("Fetching data failed due to " + e.getMessage());
		}
		collaborationMessageDto.setResponseMessage(responseDto);
		return collaborationMessageDto;
	}

	public NotificationResponseDto getNotification() {
		NotificationResponseDto dto = new NotificationResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try{
			UMEManagementEngineConsumer umeConsumer = new UMEManagementEngineConsumer();
			List<CollaborationNotificationDto> dtos =dao.getNotificationForOwner(umeConsumer.getLoggedInUser().getUserId());
			if (!ServicesUtil.isEmpty(dtos)) {
				dto.setResponseDto(dtos);
				responseDto.setMessage("Notifications fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		}
		catch(Exception e){
			logger.error("[PMC][CollaborationFacade][getNotification][error]"+e.getMessage());
			responseDto.setMessage("Fetching notification failed due to " + e.getMessage());
		}
		dto.setResponseMessage(responseDto);
		return dto;

	}
}
