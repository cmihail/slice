package mediator;

import webserviceclient.WebServiceClient;

/**
 * Defines Mediator interface used to communicate with {@link WebServiceClient}.
 *
 * @author cmihail, radu-tutueanu
 */
public interface MediatorWebServiceClient {

	/**
	 * Sends a web service error message to the mediator.
	 * @param errorMessage the error
	 */
	void webServiceError(String errorMessage);
}
