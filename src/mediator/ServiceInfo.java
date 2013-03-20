package mediator;

import java.util.List;

import model.User;
import model.service.Service;
import model.state.OfferState;
import model.state.ServiceState;
import model.state.TransferState;

/**
 * Contains all the info about a service.
 * Info contained:
 * 	- all users that offer / request the service
 *	- offer state
 *	- transfer state
 * TODO maybe make this a class
 */
public interface ServiceInfo {

	Service getService();

	void setServiceState(ServiceState state);
	ServiceState getServiceState(ServiceState state);

	void setOfferState(OfferState state);
	OfferState getOfferState();

	void TransferState(TransferState state);
	TransferState getTransferState();

	List<User> getUsers();
}
