package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.util.Locale;
import java.util.ResourceBundle;

import org.thymeleaf.Arguments;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.messageresolver.MessageResolution;

class CustomMessageResolver implements IMessageResolver {
	private static final MessageResolution EMPTY_MESSAGE_RESOLUTION = new MessageResolution("[[MISSING]]");
	
	@Override
	public String getName() {
		return "CustomMessageResolver";
	}

	@Override
	public Integer getOrder() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void initialize() {}
	
	/**
	 * @param key has the following format: <resource bundle name>.<value name>
	 * @param messageParameters totally ignored
	 */
	@Override
	public MessageResolution resolveMessage(final Arguments arguments, final String key, final Object[] messageParameters) {
		try
		{
			final Locale locale = arguments.getContext().getLocale();
			final int positionOfSeparator = key.indexOf('.');
			if (positionOfSeparator > 0) 
			{
				final String baseName = key.substring(0, positionOfSeparator);	
				ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, locale);
				final String valueName = key.substring(positionOfSeparator + 1);
				if (valueName != null && !valueName.isEmpty())
				{
					return new MessageResolution(resourceBundle.getString(valueName));	
				}
				return new MessageResolution(baseName);	
			}
			ResourceBundle resourceBundle = ResourceBundle.getBundle(key, locale);
			return new MessageResolution(resourceBundle.getString(key));	
		} catch (Exception e) {
			e.printStackTrace();
			return EMPTY_MESSAGE_RESOLUTION;
		}
	}

}
