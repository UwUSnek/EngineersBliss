package com.snek.engineersbliss.network.login;

import java.util.concurrent.CompletableFuture;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.utils.Txt;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;




public class ServerModVersionCheck {
    public static final Identifier CHANNEL = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "version_handshake");
    private ServerModVersionCheck() {}


    public static void register() {
        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf(getVersion());
            sender.sendPacket(CHANNEL, buf);
        });

        ServerLoginNetworking.registerGlobalReceiver(CHANNEL, (server, handler, understood, buffer, synchronizer, responseSender) -> {
            if(understood) {
                String clientVersion = buffer.readUtf(); // must read synchronously, buf isn't valid later
                String serverVersion = getVersion();

                synchronizer.waitFor(CompletableFuture.runAsync(() -> {
                    final int versionCheckResult = compareVersions(serverVersion, clientVersion);
                    if(versionCheckResult == 0xBAD) {
                        handler.disconnect(new Txt(EngineerSBliss.MOD_NAME + ": Couldn't parse mod version.").get());
                    }
                    else if(versionCheckResult != 0) {
                        handler.disconnect(new Txt(String.format(
                            "%s: version mismatch.%nThis server is using version %s, but you are on %s.%nPlease %s your mod to version %s.",
                            EngineerSBliss.MOD_NAME,
                            serverVersion, clientVersion,
                            versionCheckResult > 0 ? "upgrade" : "downgrade", serverVersion
                        )).get());
                    }
                    else {
                        EngineerSBliss.LOGGER.info("{} connected with version {}", handler.getUserName(), clientVersion);
                    }
                }, server));
            }
        });
    }




    /**
     * Compares the client and server versions.
     * @param serverVersion The version of the mod on the server. Expected format: <major>.<minor>.<patch>
     * @param clientVersion The version of the mod on the client. Expected format: <major>.<minor>.<patch>
     * @return 1 if the server version is newer, -1 if it is older, 0 if they are equal.
     *      This doesn't check the patch version.
     *      Returns 0xBAD if the version cannot be parsed.
     */
    private static int compareVersions(String serverVersion, String clientVersion) {
        final String[] serverStrings = serverVersion.split("\\.");
        final String[] clientStrings = clientVersion.split("\\.");
        if(serverStrings.length < 2 || clientStrings.length < 2) {
            EngineerSBliss.LOGGER.error("Could not parse mod versions. Server: {}, Client: {}", serverVersion, clientVersion, new Throwable());
            return 0xBAD;
        }

        try {
            final int serverMajor = Integer.parseInt(serverStrings[0]);
            final int clientMajor = Integer.parseInt(clientStrings[0]);
            final int serverMinor = Integer.parseInt(serverStrings[1]);
            final int clientMinor = Integer.parseInt(clientStrings[1]);
            if(serverMajor > clientMajor) return +1;
            if(serverMajor < clientMajor) return -1;
            if(serverMinor > clientMinor) return +1;
            if(serverMinor < clientMinor) return -1;
            return 0;
        }
        catch(NumberFormatException e) {
            EngineerSBliss.LOGGER.error("Could not parse mod versions. Server: {}, Client: {}", serverVersion, clientVersion, e);
            return 0xBAD;
        }
    }




    private static String getVersion() {
        return FabricLoader.getInstance().getModContainer(EngineerSBliss.MOD_ID)
            .map(c -> c.getMetadata().getVersion().getFriendlyString())
            .orElse("0.0.0")
        ;
    }
}