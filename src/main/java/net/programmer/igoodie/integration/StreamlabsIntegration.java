package net.programmer.igoodie.integration;

import io.socket.client.Socket;
import net.programmer.igoodie.event.IncomingEvent;
import net.programmer.igoodie.event.KnownEventArgs;
import net.programmer.igoodie.event.KnownEventNames;
import net.programmer.igoodie.integration.base.SIOEventPublisher;
import net.programmer.igoodie.util.JSONUtils;
import net.programmer.igoodie.util.ObjUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StreamlabsIntegration extends SIOEventPublisher {

    public StreamlabsIntegration() {
        super("https://sockets.streamlabs.com");
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
        this.socket.on("event", args -> onEvent(socket, args));
    }

    @Override
    protected void onConnect(Socket socket, Object... args) {}

    @Override
    protected void onDisconnect(Socket socket, Object... args) {}

    protected void onEvent(Socket socket, Object... args) {
        JSONObject eventData = (JSONObject) args[0];
        pushRawEvent(eventData);

        String eventFor = JSONUtils.extractFrom(eventData, "for", String.class, () -> "").replace("_account", "");
        String eventType = JSONUtils.extractFrom(eventData, "type", String.class, () -> "");
        String eventName = getEventName(eventFor, eventType);

        if (eventName == null) {
            pushUnknownEvent(eventData);
            return;
        }

        JSONArray messages = extractMessages(eventData);

        JSONUtils.forEach(messages, message -> {
            IncomingEvent event = new IncomingEvent(eventName);
            KnownEventArgs.SOURCE.write(event.getEventArgs(), eventFor);
            KnownEventArgs.ACTOR.write(event.getEventArgs(), ObjUtils.firstNonNull(
                    () -> JSONUtils.extractFrom(message, "name", String.class),
                    () -> JSONUtils.extractFrom(message, "from", String.class)
            ));
            KnownEventArgs.MESSAGE.write(event.getEventArgs(), JSONUtils.extractFrom(message, "message", String.class));
            KnownEventArgs.AMOUNT.write(event.getEventArgs(), JSONUtils.extractNumberFrom(message, "amount").map(Number::doubleValue).orElse(null));
            KnownEventArgs.CURRENCY.write(event.getEventArgs(), JSONUtils.extractFrom(message, "currency", String.class));
            KnownEventArgs.MONTHS.write(event.getEventArgs(), JSONUtils.extractFrom(message, "months", Integer.class));
            KnownEventArgs.RAIDER_COUNT.write(event.getEventArgs(), JSONUtils.extractFrom(message, "raiders", Integer.class));
            KnownEventArgs.VIEWER_COUNT.write(event.getEventArgs(), JSONUtils.extractFrom(message, "viewers", Integer.class));
            KnownEventArgs.SUBSCRIPTION_TIER.write(event.getEventArgs(), getSubscriptionTier(JSONUtils.extractFrom(message, "sub_plan", String.class)));
            KnownEventArgs.GIFTER_ID.write(event.getEventArgs(), JSONUtils.extractFrom(message, "gifter_twitch_id", String.class));
            KnownEventArgs.GIFTER.write(event.getEventArgs(), JSONUtils.extractFrom(message, "gifter", String.class));
            KnownEventArgs.IS_GIFTED.write(event.getEventArgs(),
                    KnownEventArgs.GIFTER_ID.read(event.getEventArgs()).isPresent()
                            || KnownEventArgs.GIFTER.read(event.getEventArgs()).isPresent() ? true : null);
            KnownEventArgs.TITLE.write(event.getEventArgs(), ObjUtils.firstNonNull(
                    () -> JSONUtils.extractFrom(message, "redemption_name", String.class),
                    () -> JSONUtils.extractFrom(message, "product", String.class),
                    () -> JSONUtils.extractFrom(message, "title", String.class)
            ));
            pushEvent(event);
        });
    }

    /* ------------------------------------- */

    private Integer getSubscriptionTier(String tierName) {
        if (tierName == null) return null;

        if (tierName.equalsIgnoreCase("Prime"))
            return 0; // tier = 0 stands for Prime

        if (tierName.equalsIgnoreCase("1000"))
            return 1;
        if (tierName.equalsIgnoreCase("2000"))
            return 2;
        if (tierName.equalsIgnoreCase("3000"))
            return 3;

        return null;
    }

    private String getEventName(String eventFor, String eventType) {
        if (eventFor.equals("") && eventType.equals("donation"))
            return KnownEventNames.DONATION;

        if (eventFor.equals("streamlabs")) {
            if (eventType.equals("donation"))
                return KnownEventNames.DONATION;
            if (eventType.equals("redemption"))
                return KnownEventNames.LOYALTY_POINT_REDEMPTION;
            if (eventType.equals("loyalty_store_redemption"))
                return KnownEventNames.LOYALTY_POINT_REDEMPTION;
            if (eventType.equals("merch"))
                return KnownEventNames.STREAMLABS_MERCH;
        }

        if (eventFor.equals("twitch")) {
            if (eventType.equals("donation"))
                return KnownEventNames.DONATION;
            if (eventType.equals("tip"))
                return KnownEventNames.DONATION;
            if (eventType.equals("twitchcharitydonation"))
                return KnownEventNames.DONATION;
            if (eventType.equals("redemption"))
                return KnownEventNames.LOYALTY_POINT_REDEMPTION;
            if (eventType.equals("loyalty_store_redemption"))
                return KnownEventNames.LOYALTY_POINT_REDEMPTION;
            if (eventType.equals("follow"))
                return KnownEventNames.TWITCH_FOLLOW;
            if (eventType.equals("subscription"))
                return KnownEventNames.TWITCH_SUBSCRIPTION;
            if (eventType.equals("subscriber"))
                return KnownEventNames.TWITCH_SUBSCRIPTION;
            if (eventType.equals("resub"))
                return KnownEventNames.TWITCH_SUBSCRIPTION;
            if (eventType.equals("subMysteryGift"))
                return KnownEventNames.TWITCH_SUBSCRIPTION_GIFT;
            if (eventType.equals("communityGiftPurchase"))
                return KnownEventNames.TWITCH_SUBSCRIPTION_GIFT;
            if (eventType.equals("host"))
                return KnownEventNames.TWITCH_HOST;
            if (eventType.equals("raid"))
                return KnownEventNames.TWITCH_RAID;
            if (eventType.equals("bits"))
                return KnownEventNames.TWITCH_BITS;
            if (eventType.equals("cheer"))
                return KnownEventNames.TWITCH_BITS;
        }

        if (eventFor.equals("justgiving") && eventType.equals("justgivingdonation"))
            return KnownEventNames.JUSTGIVING_DONATION;

        if (eventFor.equals("tiltify") && eventType.equals("tiltifydonation"))
            return KnownEventNames.JUSTGIVING_DONATION;

        if (eventFor.equals("extralife") && eventType.equals("eldonation"))
            return KnownEventNames.EXTRALIFE_DONATION;

        if (eventFor.equals("patreon") && eventType.equals("pledge"))
            return KnownEventNames.PATREON_PLEDGE;

        if (eventFor.equals("treatstream") && eventType.equals("treat"))
            return KnownEventNames.TREATSTREAM_TREAT;

        return null;
    }

    private JSONArray extractMessages(JSONObject eventData) {
        try {
            Object messageField = eventData.get("message");

            if (messageField instanceof JSONArray)
                return (JSONArray) messageField;

            else if (messageField instanceof JSONObject)
                return new JSONArray().put(messageField);

            return null;

        } catch (JSONException e) {
            return null;
        }
    }

}
