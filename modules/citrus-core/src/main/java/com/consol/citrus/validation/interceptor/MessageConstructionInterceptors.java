/*
 * Copyright 2006-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.validation.interceptor;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.variable.dictionary.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * List of global message construction interceptors that modify message payload and message headers. User just has to add
 * interceptor implementation as bean to the Spring application context.
 * @author Christoph Deppisch
 * @since 1.4
 */
public class MessageConstructionInterceptors implements MessageConstructionInterceptor {

    @Autowired(required = false)
    private List<MessageConstructionInterceptor> messageConstructionInterceptors = new ArrayList<MessageConstructionInterceptor>();

    @Override
    public Message<?> interceptMessageConstruction(Message<?> message, String messageType, TestContext context) {
        if (messageConstructionInterceptors.size() > 0) {
            Message<?> intercepted = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders()).build();

            for (MessageConstructionInterceptor messageConstructionInterceptor : messageConstructionInterceptors) {
                if (messageConstructionInterceptor.supportsMessageType(messageType.toString())) {
                    if (messageConstructionInterceptor instanceof DataDictionary &&
                            ((DataDictionary) messageConstructionInterceptor).getScope().equals(DataDictionary.DictionaryScope.EXPLICIT)) {
                        // skip explicit data dictionary to avoid duplicate dictionary usage.
                        continue;
                    }

                    intercepted = messageConstructionInterceptor.interceptMessageConstruction(intercepted, messageType, context);
                }
            }

            return intercepted;
        }

        return message;
    }

    @Override
    public boolean supportsMessageType(String messageType) {
        return true;
    }
}
