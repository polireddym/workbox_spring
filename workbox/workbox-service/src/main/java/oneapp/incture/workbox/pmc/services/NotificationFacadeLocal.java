package oneapp.incture.workbox.pmc.services;

import oneapp.incture.workbox.pmc.dto.MailRequestDto;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

public interface NotificationFacadeLocal {

	public ResponseMessage sendNotification(MailRequestDto requestDto);

	ResponseMessage sendProcessRemainder();

	ResponseMessage sendTaskRemainder();

}
