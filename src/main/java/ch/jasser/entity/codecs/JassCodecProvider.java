package ch.jasser.entity.codecs;

import ch.jasser.entity.GameType;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class JassCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == GameType.class) {
            return (Codec<T>) new GameTypeCodec();
        }
        return null;
    }

}