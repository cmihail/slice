package gui;

import java.util.List;
import java.util.Map;

import model.Offer;
import model.Service;
import model.User;
import model.state.OfferState;
import model.state.ServiceState;

import com.sun.jdi.connect.spi.TransportService;

public interface GUI {

	void drawLoginPage();
	void drawMainPage(User user, Map<Service, List<User>> serviceUsers);

	void changeServiceState(Service service, ServiceState serviceState);
	void changeOfferState(Service service, OfferState state);
	void changeTransferState(Service service, TransportService state);
	void setServiceOffer(Service service, User user, Offer offer);
}
