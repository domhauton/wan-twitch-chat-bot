package twitch.channel.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitch.channel.settings.enums.IChannelSetting;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Dominic Hauton on 27/06/2016.
 *
 * Basic implementation of ChannelSettingDao for local storage and testing.
 */
public class ChannelSettingDAOHashMapImpl implements ChannelSettingDao {
    private final HashMap<String, Object> settingMap;
    private static final Logger s_log = LogManager.getLogger();

    public ChannelSettingDAOHashMapImpl() {
        settingMap = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSetting(String channelName, IChannelSetting<T> channelSetting) throws ChannelSettingDAOException{
        s_log.info("Retrieving setting {} for channel {}", channelSetting, channelName);
        String hashMapKey = generateKey(channelName, channelSetting);
        Optional<Object> mapResultOptional = Optional.ofNullable(settingMap.get(hashMapKey));
        Object mapResult = mapResultOptional.orElseThrow(
                () -> new ChannelSettingDAOException("Key " + hashMapKey + " not found."));
        if( channelSetting.getGenericInterfaceType().isInstance(mapResult) ){
            try {
                return (T) mapResult;
            } catch (ClassCastException e) {
                throw new ChannelSettingDAOException("Database returned object of incorrect type. Type check passed "
                                                     + "but cast failed!");
            }
        } else {
            throw new ChannelSettingDAOException("Database returned object of incorrect type!");
        }
    }

    @Override
    public <T> void setSetting(String channelName, IChannelSetting<T> channelSetting, T value) throws ChannelSettingDAOException{
        String hashMapKey = generateKey(channelName, channelSetting);
        settingMap.put(hashMapKey, value);
    }

    private <T> String generateKey(String channelName, IChannelSetting<T> channelSetting) {
        return String.join("#", channelName, channelSetting.getGenericInterfaceType().getSimpleName(), channelSetting
                .toString());
    }
}