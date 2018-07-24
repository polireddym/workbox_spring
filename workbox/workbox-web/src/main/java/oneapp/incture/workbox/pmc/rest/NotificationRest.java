package oneapp.incture.workbox.pmc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.pmc.dto.MailRequestDto;
import oneapp.incture.workbox.pmc.services.NotificationFacadeLocal;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/notification", produces = "application/json")
public class NotificationRest {

	@Autowired
	private NotificationFacadeLocal notification;

	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public ResponseMessage sendMail(MailRequestDto requestDto) {
		return notification.sendNotification(requestDto);
	}

}
