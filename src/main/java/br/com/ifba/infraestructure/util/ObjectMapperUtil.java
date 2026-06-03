package br.com.ifba.infraestructure.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperUtil {

    private static final ModelMapper MODEL_MAPPER;

    // Colocamos as configurações aqui dentro. Assim, ele só faz isso 1 vez!
    static {
        MODEL_MAPPER = new ModelMapper();
        MODEL_MAPPER.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    public <Input, Output> Output map(final Input object, final Class<Output> clazz) {
        // Agora o método só faz o trabalho de converter, ficando super rápido.
        return MODEL_MAPPER.map(object, clazz);
    }
}