package org.bot0ff.kafka;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
public class UpdateProducerImpl implements UpdateProducer{

    @Override
    public void produce(String kafkaQueue, Update update) {
        log.debug(update.getMessage().getText());
    }
}
