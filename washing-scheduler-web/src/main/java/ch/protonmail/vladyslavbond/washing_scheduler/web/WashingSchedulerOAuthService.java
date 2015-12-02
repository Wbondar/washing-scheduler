package ch.protonmail.vladyslavbond.washing_scheduler.web;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;

public enum WashingSchedulerOAuthService implements OAuthService {
	FACEBOOK (FacebookApi.class);
	
	public final ResourceBundle bundle;

	private final OAuthService service;

	private WashingSchedulerOAuthService(Class<? extends Api> api) {
		this.bundle = ResourceBundle.getBundle(this.name());
		this.service = new ServiceBuilder()
		.apiKey(this.getProperty("apiKey"))
		.apiSecret(this.getProperty("apiSecret"))
		.callback(this.getProperty("callback"))
		.provider(api)
		.build();
	}
	
	@Override
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		return service.getAccessToken(requestToken, verifier);
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return service.getAuthorizationUrl(requestToken);
	}

	@Override
	public Token getRequestToken() {
		return service.getRequestToken();
	}

	@Override
	public String getVersion() {
		return service.getVersion();
	}

	@Override
	public void signRequest(Token accessToken, OAuthRequest request) {
		service.signRequest(accessToken, request);
	}

	public final String getProperty(final String key) {
		return bundle.getString(name() + "." + key);
	}
	
	public final <T> T getProperty(final HttpSession session, final String key) {
		return (T)session.getAttribute(name() + "." + key);
	}
	
	public final <T> void setProperty(final HttpSession session, final String key, final T value) {
		session.setAttribute(name() + "." + key, value);
	}
}
