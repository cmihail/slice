package gui;

import java.util.List;
import java.util.Map;

import model.User;
import model.service.Offer;
import model.service.Service;
import model.state.OfferState;
import model.state.ServiceState;

import com.sun.jdi.connect.spi.TransportService;

public interface GUI {

	void drawErrorPage(String errorMessage);
	void drawMainPage(User user, Map<Service, List<User>> mapServiceUsers);

	void changeServiceState(Service service, ServiceState serviceState);
	void changeOfferState(Service service, OfferState state);
	void changeTransferState(Service service, TransportService state);
	void setServiceOffer(Service service, User user, Offer offer);
}
