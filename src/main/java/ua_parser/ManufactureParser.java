/**
 * Manufaacture parser using a list of manufactures with a set of regex expressions. Will determine the
 * manufacture of the host's OS.
 *
 * @author Salvatore D'Agostino (@iToto)
 */
package ua_parser;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sdagostino on 2015-07-27.
 */
public class ManufactureParser {
    private final Logger LOGGER = Logger.getLogger(PlatformParser.class.getName());

    public ManufactureParser() {
    }

    private static final Multimap<String, Pattern> MANUFACTURER_OS_PATTERN_MAP =
        ImmutableMultimap.<String, Pattern>builder()
                         .put(Constants.APPLE, Pattern.compile("iOS"))
                         .put(Constants.APPLE, Pattern.compile("OS X"))
                         .put(Constants.APPLE, Pattern.compile("Macintosh"))
                         .put(Constants.GOOGLE, Pattern.compile("Android"))
                         .put(Constants.MICROSOFT, Pattern.compile("Windows"))
                         .put(Constants.LINUX, Pattern.compile("Linux"))
                         .build();

    /**
     * Calculate the manufacture of the hosting OS from the User Agent
     *
     * @param uaString The User Agent string
     * @return The manufacture of the host OS
     */
    public Manufacture parse(String uaString) {
        for (Map.Entry<String, Pattern> entry : MANUFACTURER_OS_PATTERN_MAP.entries()) {
            final String key = entry.getKey();
            final Pattern pattern = entry.getValue();
            final Matcher matcher = pattern.matcher(uaString);

            if (matcher.find()) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("Found match in manufacture with key: " + key);
                }
                return new Manufacture(key);
            }
        }

        LOGGER.finest("Could not find a manufacturer, using default manufacture value");
        return new Manufacture(Constants.OTHER);
    }

}
