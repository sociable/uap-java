/**
 * Platform parser using custom list of devices. Will parse and categorize the platform
 * of the UA into either Desktop, Mobile or Tablet.
 *
 * @author Salvatore D'Agostino (@iToto)
 */
package ua_parser;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlatformParser {
    private final Map<String, Pattern> mobileDevicePatters;
    private final Map<String, Pattern> tabletDevicePatters;
    private final Logger LOGGER = Logger.getLogger(PlatformParser.class.getName());

    public PlatformParser(Map<String, String> mobileDevicePatters, Map<String, String> tabletDevicePatters) {
        this.mobileDevicePatters = toPatternMap(mobileDevicePatters);
        this.tabletDevicePatters = toPatternMap(tabletDevicePatters);
    }

    private static Map<String, Pattern> toPatternMap(Map<String, String> stringPatternMap) {
        final ImmutableMap.Builder<String, Pattern> builder = ImmutableMap.<String, Pattern>builder();
        for (Map.Entry<String,String> entry : stringPatternMap.entrySet()) {
            builder.put(entry.getKey(), Pattern.compile(entry.getValue()));
        }
        return builder.build();
    }

    public Platform parse(String uaString) {

        if (isMobile(uaString)) {
            return new Platform(Constants.MOBILE);

        } else if (isTablet(uaString)) {
            return  new Platform(Constants.TABLET);
        }
        else { // if not a mobile or tablet, will assume desktop!
            return new Platform(Constants.DESKTOP);
        }
    }

    private boolean isMobile(String ua) {
        return runPatternsOnString(mobileDevicePatters, ua);
    }

    private boolean isTablet(String ua) {
        return runPatternsOnString(tabletDevicePatters, ua);
    }

    private boolean runPatternsOnString(Map<String, Pattern> patterns, String input) {
        for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
            final Pattern pattern = entry.getValue();
            final Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                // Found a match
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("Found match in device with key: " + entry.getKey());
                }
                return true;
            }
        }
        LOGGER.finest("No match found, using default platform value");
        return false;
    }
}
